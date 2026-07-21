package com.practicum.project.playlistmaker.search.domain

import com.practicum.project.playlistmaker.search.domain.models.Track
import java.util.concurrent.Executors

class TracksInteractorImpl(private val repository: TracksRepository): TracksInteractor {
    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(expression: String, consumer: TracksInteractor.TracksConsumer) {
        executor.execute {
            try {
                consumer.consume(repository.searchTracks(expression))
            } catch (e: Exception){
                consumer.onFailure()
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