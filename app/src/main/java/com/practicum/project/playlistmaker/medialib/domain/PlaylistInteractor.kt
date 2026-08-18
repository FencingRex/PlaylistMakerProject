package com.practicum.project.playlistmaker.medialib.domain

import android.net.Uri
import com.practicum.project.playlistmaker.medialib.model.AddTrackResult
import com.practicum.project.playlistmaker.medialib.model.Playlist
import com.practicum.project.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    suspend fun addPlaylist(playlist: Playlist)
    suspend fun addTrackToPlaylist(trackId: Int, playlistId: Long) : AddTrackResult
    suspend fun getAllPlaylists(): Flow<List<Playlist>>
    suspend fun updatePlaylist(playlist: Playlist)
    suspend fun deletePlaylist(playlist: Playlist)
    suspend fun saveCover(uri: Uri): String?
}