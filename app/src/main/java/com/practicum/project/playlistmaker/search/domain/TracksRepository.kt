package com.practicum.project.playlistmaker.search.domain

import com.practicum.project.playlistmaker.search.domain.models.Track

interface TracksRepository {
    fun searchTracks(expression: String): List<Track>
    fun saveTrack(tracksList: List<Track>)
    fun addTrackToHistory(track: Track)
    fun clearHistory()
    fun getTrackFromHistory(): ArrayList<Track>
    fun isNotEmpty(): Boolean
    fun saveTrackToPref(tracks: List<Track>)
}