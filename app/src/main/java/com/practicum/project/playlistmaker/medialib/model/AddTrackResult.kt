package com.practicum.project.playlistmaker.medialib.model

sealed class AddTrackResult {
    data class Success(val playlistName: String) : AddTrackResult()
    data class AlreadyExists(val playlistName: String) : AddTrackResult()
    object PlaylistNotFound : AddTrackResult()
    object Error : AddTrackResult()
}