package com.practicum.project.playlistmaker.medialib.domain

import android.net.Uri
import com.practicum.project.playlistmaker.medialib.data.db.entity.TrackEntity
import com.practicum.project.playlistmaker.medialib.model.AddTrackResult
import com.practicum.project.playlistmaker.medialib.model.Playlist
import com.practicum.project.playlistmaker.playlist.data.PlaylistWithTracks
import com.practicum.project.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    suspend fun addPlaylist(playlist: Playlist)
    suspend fun addTrackToPlaylist(playlistId: Long, track: Track): AddTrackResult
    suspend fun getAllPlaylists(): Flow<List<Playlist>>
    suspend fun getPlaylistById(playlistId: Long): Flow<List<Playlist>>
    suspend fun getTracksFromPlaylist(playlistId: Long): Flow<PlaylistWithTracks?>
    suspend fun updatePlaylist(playlist: Playlist)
    suspend fun deletePlaylist(playlistId: Long)
    suspend fun deleteTrack(playlistId: Long, trackId: Int)
    suspend fun saveCover(uri: Uri): String?
    suspend fun checkIsTrackInPlaylist(playlistId: Long, trackId: Int): Boolean
}