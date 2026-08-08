package com.practicum.project.playlistmaker.search.domain

import com.practicum.project.playlistmaker.search.domain.models.Track
import com.practicum.project.playlistmaker.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class TracksInteractorImpl(private val repository: TracksRepository): TracksInteractor {
    override fun searchTracks(expression: String): Flow<Pair<List<Track>?, String?>> {
        return repository.searchTracks(expression).map { result ->
            when(result){
                is Resource.Success ->{
                    Pair(result.data, null)
                }
                is Resource.Error ->{
                    Pair(null,result.message)
                }
            }
        }
    }

    override fun saveTrackToHistory(trackList: ArrayList<Track>) {
        repository.saveTrack(trackList)
    }
    override fun addTrackToHistory(track: Track){
        repository.addTrackToHistory(track)
    }

    override fun clearHistory() {
        repository.clearHistory()
    }

    override fun getTrackFromHistory(): ArrayList<Track> {
        return repository.getTrackFromHistory()

    }

    override fun isNotEmpty(): Boolean {
       return repository.isNotEmpty()
    }

    override fun saveTrackToPref(trackList: ArrayList<Track>) {
        repository.saveTrackToPref(trackList)
    }

}