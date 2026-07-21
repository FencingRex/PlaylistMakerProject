package com.practicum.project.playlistmaker.domain.api

import com.practicum.project.playlistmaker.domain.models.Track

interface TracksInteractor {
    fun searchTracks(expression: String, consumer: TracksConsumer)
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