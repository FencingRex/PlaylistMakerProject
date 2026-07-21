package com.practicum.project.playlistmaker.domain.api


import com.practicum.project.playlistmaker.domain.models.Track

interface TrackHistoryRepository {
    fun saveTrackToPref(tracks: List<Track>)
    fun addTrackToHistory(track: Track)
    fun getTrackFromHistory(): List<Track>
    fun clearHistory()
    fun isNotEmpty(): Boolean
}
