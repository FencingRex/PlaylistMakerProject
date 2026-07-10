package com.practicum.project.playlistmaker.domain.api

import com.practicum.project.playlistmaker.data.dto.TrackDTO
import com.practicum.project.playlistmaker.domain.models.Track

interface TrackHistoryRepository {
    fun saveTrackToPref(tracks: ArrayList<TrackDTO>)
    fun addTrackToHistory(track: TrackDTO)
    fun getTrackFromHistory(): ArrayList<TrackDTO>
    fun clearHistory()

}
