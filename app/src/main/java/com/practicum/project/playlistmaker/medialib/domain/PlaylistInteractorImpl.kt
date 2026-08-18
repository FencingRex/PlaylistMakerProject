package com.practicum.project.playlistmaker.medialib.domain

import android.net.Uri
import com.practicum.project.playlistmaker.medialib.model.AddTrackResult
import com.practicum.project.playlistmaker.medialib.model.Playlist
import com.practicum.project.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(private val playlistRepository: PlaylistRepository): PlaylistInteractor {
    override suspend fun addPlaylist(playlist: Playlist) {
        playlistRepository.addPlaylist(playlist)
    }

    override suspend fun addTrackToPlaylist(trackId: Int, playlistId: Long) : AddTrackResult{
       return playlistRepository.addTrackToPlaylist(trackId,playlistId)
    }
    override suspend fun deletePlaylist(playlist: Playlist) {
        playlistRepository.deletePlaylist(playlist)
    }

    override suspend fun getAllPlaylists(): Flow<List<Playlist>> {
        return playlistRepository.getAllPlaylists()
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        playlistRepository.updatePlaylist(playlist)
    }

    override suspend fun saveCover(uri: Uri): String? {
       return playlistRepository.saveCover(uri)
    }
}