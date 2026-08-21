package com.practicum.project.playlistmaker.medialib.model

sealed class PlaylistState {
    data object Empty: PlaylistState()
    data class Content(val playlist: List<Playlist>): PlaylistState()
}