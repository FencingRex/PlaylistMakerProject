package com.practicum.project.playlistmaker.medialib.data.db

import android.content.Context
import android.net.Uri
import android.os.Environment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.project.playlistmaker.medialib.data.converters.PlaylistDbConverter
import com.practicum.project.playlistmaker.medialib.data.converters.PlaylistTrackConverter
import com.practicum.project.playlistmaker.medialib.data.db.entity.PlaylistEntity
import com.practicum.project.playlistmaker.medialib.domain.PlaylistRepository
import com.practicum.project.playlistmaker.medialib.model.Playlist
import com.practicum.project.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import okio.IOException
import com.practicum.project.playlistmaker.medialib.data.converters.TrackDbConverter
import com.practicum.project.playlistmaker.medialib.model.AddTrackResult
import com.practicum.project.playlistmaker.playlist.data.PlaylistWithTracks
import java.io.File
import java.io.FileOutputStream
import kotlin.collections.map

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val converter: PlaylistDbConverter,
    private val context: Context
): PlaylistRepository {
    override suspend fun addPlaylist(playlist: Playlist) {
        appDatabase.playlistsDao().addPlaylist(converter.toEntity(playlist))
    }

    override suspend fun getAllPlaylists(): Flow<List<Playlist>> {
        return appDatabase.playlistsDao().getAllPlaylists().map { playlist ->
            convertFromEntity(playlist)
        }.distinctUntilChanged()
    }

    override suspend fun getPlaylistById(playlistId: Long): Flow<List<Playlist>> {
        return appDatabase.playlistsDao().selectPlaylistById(playlistId)
            .map { playlistEntity -> convertFromEntity(playlistEntity) }
            .distinctUntilChanged()
    }

    override suspend fun getTracksFromPlaylist(playlistId: Long): Flow<PlaylistWithTracks?>{
        return appDatabase.playlistsDao().getPlaylistWithTracks(playlistId)
    }

    override suspend fun checkIsTrackInPlaylist(playlistId: Long, trackId: Int): Boolean {
        val inPlaylistCount = appDatabase.playlistsDao().checkTrackInPlaylist(playlistId,trackId)
        if (inPlaylistCount == 0) {
            return true
        } else return false
    }

    override suspend fun addTrackToPlaylist(playlistId: Long, track: Track): AddTrackResult {
       return withContext(Dispatchers.IO){
           try {
               val playlistEntity = appDatabase.playlistsDao().getPlaylistById(playlistId)
                   ?: return@withContext AddTrackResult.PlaylistNotFound

               val exists =
                   appDatabase.playlistsDao().checkTrackInPlaylist(playlistId, track.trackId) > 0
               if (exists) {
                   return@withContext AddTrackResult.AlreadyExists(playlistEntity.name)
               }

               val playlistTrack = PlaylistTrackConverter.fromDomain(track, playlistId)
               appDatabase.playlistsDao().insertPlaylistTrack(playlistTrack)

               val playlistSize = appDatabase.playlistsDao().getTracksCount(playlistId)

               val updatedPlaylist = playlistEntity.copy(
                   tracksQty = playlistSize
               )
               appDatabase.playlistsDao().updatePlaylist(updatedPlaylist)

               return@withContext AddTrackResult.Success(playlistEntity.name)
           } catch (e: Exception) {
               e.printStackTrace()
               return@withContext AddTrackResult.Error
           }
       }
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        appDatabase.playlistsDao().updatePlaylist(converter.toEntity(playlist))
    }

    override suspend fun deletePlaylist(playlistId: Long) {
        appDatabase.playlistsDao().deletePlaylistWithTracks(playlistId)
    }

    override suspend fun deleteTrack(playlistId: Long, trackId: Int) {
        appDatabase.playlistsDao().deleteTrackFromPlaylist(playlistId, trackId)
    }

    private fun convertFromEntity(playlist: List<PlaylistEntity>): List<Playlist>{
        return playlist.map {playlist -> converter.fromEntity(playlist)}
    }

    override suspend fun saveCover(uri: Uri): String? = withContext(Dispatchers.IO){
        try {
            val filePath = File(context.getExternalFilesDir((Environment.DIRECTORY_PICTURES)), "playlistCover")
            if (!filePath.exists()) filePath.mkdirs()

            val fileName = "cover_${System.currentTimeMillis()}.jpg"
            val file = File(filePath, fileName)

            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                FileOutputStream(file).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            } ?: throw IOException("Не удалось открыть InputStream для URI: $uri")

            if (!file.exists()||file.length() == 0L){
                throw IOException("Файл не создался или пустой")
            }
            file.absolutePath

        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}