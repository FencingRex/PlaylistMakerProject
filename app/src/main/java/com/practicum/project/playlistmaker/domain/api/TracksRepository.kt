package com.practicum.project.playlistmaker.domain.api

import com.practicum.project.playlistmaker.data.dto.TrackDTO
import com.practicum.project.playlistmaker.domain.models.Track

interface TracksRepository {
    fun searchTracks(expression: String): List<Track>
    fun saveTrack(tracksList: ArrayList<Track>)
    fun addTrackToHistory(track: Track)
    fun clearHistory()
    fun getTrackFromHistory(): ArrayList<Track>
    fun isNotEmpty(): Boolean
    fun saveTrackToPref(tracks: ArrayList<TrackDTO>)
}