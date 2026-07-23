package com.practicum.project.playlistmaker.search.model

import com.practicum.project.playlistmaker.search.domain.models.Track

interface HistoryState {
    data object EmptyHistory : HistoryState

    data class HistoryContent(val history: List<Track>) : HistoryState
}