package com.practicum.project.playlistmaker.search.model

import com.practicum.project.playlistmaker.search.domain.models.Track

sealed interface RequestState {
    data object Empty: RequestState
    data object Success: RequestState
    data object NotConnected: RequestState
    data object NotFound: RequestState
    data object Loading: RequestState
}