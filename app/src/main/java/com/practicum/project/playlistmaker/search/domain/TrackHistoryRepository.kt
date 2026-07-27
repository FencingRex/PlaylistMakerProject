package com.practicum.project.playlistmaker.search.domain

import com.practicum.project.playlistmaker.search.domain.models.Track

interface TrackHistoryRepository {
    fun saveTrackToPref(tracks: List<Track>)
    fun addTrackToHistory(track: Track)
    fun getTrackFromHistory(): List<Track>
    fun clearHistory()
    fun isNotEmpty(): Boolean
}