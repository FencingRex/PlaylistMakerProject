package com.practicum.project.playlistmaker.search.model

import com.practicum.project.playlistmaker.search.domain.models.Track

sealed class RequestState {
    object Loading : RequestState()
    data class Success(val tracks: List<Track>) : RequestState()
    object NotFound : RequestState()
    object NotConnected : RequestState()
    object Empty : RequestState()
}
