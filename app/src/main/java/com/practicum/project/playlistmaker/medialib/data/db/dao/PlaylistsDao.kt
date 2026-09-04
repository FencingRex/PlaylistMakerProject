package com.practicum.project.playlistmaker.medialib.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.practicum.project.playlistmaker.medialib.data.db.entity.PlaylistEntity
import com.practicum.project.playlistmaker.medialib.data.db.entity.PlaylistTracks
import com.practicum.project.playlistmaker.medialib.data.db.entity.TrackEntity
import com.practicum.project.playlistmaker.playlist.data.PlaylistWithTracks
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistsDao {
    @Insert(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPlaylist(playlist: PlaylistEntity)
    @Delete
    suspend fun deletePlaylist(playlist: PlaylistEntity)
    @Query("select * from playlist_table order by timestamp desc")
    fun getAllPlaylists(): Flow<List<PlaylistEntity>>
    @Query("SELECT * FROM playlist_table WHERE id = :playlistId")
    fun selectPlaylistById(playlistId: Long): Flow<List<PlaylistEntity>>
    @Query("SELECT * FROM playlist_table WHERE id = :playlistId")
    suspend fun getPlaylistById(playlistId: Long): PlaylistEntity?
    @Update
    suspend fun updatePlaylist(playlist: PlaylistEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)
    @Query("SELECT COUNT(*) FROM playlist_tracks WHERE playlistId = :playlistId AND trackId = :trackId")
    suspend fun checkTrackInPlaylist(playlistId: Long, trackId: Int): Int
    @Query("SELECT COUNT(*) FROM playlist_tracks WHERE playlistId = :playlistId")
    fun getTracksCount(playlistId: Long): Int
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylistTrack(playlistTrack: PlaylistTracks)
    @Delete
    suspend fun deletePlaylistTrack(playlistTrack: PlaylistTracks)
    @Query("DELETE FROM playlist_tracks WHERE playlistId = :playlistId AND trackId = :trackId")
    suspend fun deleteTrackFromPlaylist(playlistId: Long, trackId: Int)
    @Query("SELECT * FROM playlist_tracks WHERE playlistId = :playlistId ORDER BY id ASC")
    suspend fun getTracksForPlaylist(playlistId: Long): PlaylistTracks?
    @Transaction
    @Query("SELECT * FROM playlist_table WHERE id = :playlistId")
    fun getPlaylistWithTracks(playlistId: Long): Flow<PlaylistWithTracks?>
    @Query("DELETE FROM playlist_tracks WHERE playlistId = :playlistId")
    suspend fun deleteTracksFromPlaylist(playlistId: Long)

    @Query("DELETE FROM playlist_table WHERE id = :playlistId")
    suspend fun deletePlaylist(playlistId: Long)

    @Transaction
    suspend fun deletePlaylistWithTracks(playlistId: Long) {
        deleteTracksFromPlaylist(playlistId)
        deletePlaylist(playlistId)
    }
}
