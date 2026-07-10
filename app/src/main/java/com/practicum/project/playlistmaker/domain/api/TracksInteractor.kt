package com.practicum.project.playlistmaker.domain.api

import com.practicum.project.playlistmaker.domain.models.Track

interface TracksInteractor {
    fun searchTracks(expression: String, consumer: TracksConsumer)

    fun saveTrackToHistory(trackList: ArrayList<Track>)
    fun addTrackToHistory(track: Track)
    fun getTrackFromHistory(): ArrayList<Track>
    fun saveTrackToPref(trackList: ArrayList<Track>)
    fun clearHistory()
    fun isNotEmpty()

    interface TracksConsumer{
        fun consume(foundTracks: List<Track>)
    }
}