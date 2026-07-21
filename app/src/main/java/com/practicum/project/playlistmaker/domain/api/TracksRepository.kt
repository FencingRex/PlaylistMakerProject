package com.practicum.project.playlistmaker.domain.api

import com.practicum.project.playlistmaker.domain.models.Track

interface TracksRepository {
    fun searchTracks(expression: String): List<Track>
    fun saveTrack(tracksList: List<Track>)
    fun addTrackToHistory(track: Track)
    fun clearHistory()
    fun getTrackFromHistory(): ArrayList<Track>
    fun isNotEmpty(): Boolean
    fun saveTrackToPref(tracks: List<Track>)
}