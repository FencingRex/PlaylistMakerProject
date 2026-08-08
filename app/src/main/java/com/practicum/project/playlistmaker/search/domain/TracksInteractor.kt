package com.practicum.project.playlistmaker.search.domain

import com.practicum.project.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TracksInteractor {
    fun searchTracks(expression: String): Flow<Pair<List<Track>?, String?>>
    fun saveTrackToHistory(trackList: ArrayList<Track>)
    fun addTrackToHistory(track: Track)
    fun getTrackFromHistory(): ArrayList<Track>
    fun clearHistory()
    fun isNotEmpty(): Boolean
    fun saveTrackToPref(trackList: ArrayList<Track>)
    interface TracksConsumer{
        fun consume(foundTracks: List<Track>)
        fun onFailure()
    }
}