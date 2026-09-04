package com.practicum.project.playlistmaker.medialib.domain

import android.net.Uri
import com.practicum.project.playlistmaker.medialib.data.db.entity.TrackEntity
import com.practicum.project.playlistmaker.medialib.model.AddTrackResult
import com.practicum.project.playlistmaker.medialib.model.Playlist
import com.practicum.project.playlistmaker.playlist.data.PlaylistWithTracks
import com.practicum.project.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(private val playlistRepository: PlaylistRepository): PlaylistInteractor {
    override suspend fun addPlaylist(playlist: Playlist) {
        playlistRepository.addPlaylist(playlist)
    }

    override suspend fun addTrackToPlaylist(playlistId: Long,track: Track): AddTrackResult {
       return playlistRepository.addTrackToPlaylist(playlistId, track)
    }
    override suspend fun deletePlaylist(playlistId: Long) {
        playlistRepository.deletePlaylist(playlistId)
    }

    override suspend fun deleteTrack(playlistId: Long, trackId: Int) {
        playlistRepository.deleteTrack(playlistId,trackId)
    }
    override suspend fun getAllPlaylists(): Flow<List<Playlist>> {
        return playlistRepository.getAllPlaylists()
    }

    override suspend fun getPlaylistById(playlistId: Long): Flow<List<Playlist>> {
        return playlistRepository.getPlaylistById(playlistId)
    }

    override suspend fun getTracksFromPlaylist(playlistId: Long): Flow<PlaylistWithTracks?> {
        return playlistRepository.getTracksFromPlaylist(playlistId)
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        playlistRepository.updatePlaylist(playlist)
    }

    override suspend fun checkIsTrackNotInPlaylist(playlistId: Long, trackId: Int): Boolean {
        return playlistRepository.checkIsTrackNotInPlaylist(playlistId, trackId)
    }
    override suspend fun saveCover(uri: Uri): String? {
       return playlistRepository.saveCover(uri)
    }
}