package com.practicum.project.playlistmaker.search.domain


import com.practicum.project.playlistmaker.search.domain.models.Track
import com.practicum.project.playlistmaker.utils.Resource
import kotlinx.coroutines.flow.Flow

interface TracksRepository {
    fun searchTracks(expression: String): Flow<Resource<List<Track>>>
    fun saveTrack(tracksList: List<Track>)
    fun addTrackToHistory(track: Track)
    fun clearHistory()
    fun getTrackFromHistory(): ArrayList<Track>
    fun isNotEmpty(): Boolean
    fun saveTrackToPref(tracks: List<Track>)
}