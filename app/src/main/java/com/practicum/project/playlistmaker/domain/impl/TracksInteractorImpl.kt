package com.practicum.project.playlistmaker.domain.impl

import com.practicum.project.playlistmaker.domain.api.TrackHistoryRepository
import com.practicum.project.playlistmaker.domain.api.TracksInteractor
import com.practicum.project.playlistmaker.domain.api.TracksRepository
import com.practicum.project.playlistmaker.domain.models.Track
import java.util.concurrent.Executors

class TracksInteractorImpl(private val repository: TracksRepository,private val historyRepository: TrackHistoryRepository): TracksInteractor {
    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(expression: String, consumer: TracksInteractor.TracksConsumer) {
        executor.execute {
            consumer.consume(repository.searchTracks(expression))
        }
    }

    override fun saveTrackToHistory(trackList: ArrayList<Track>) {
        TODO("Not yet implemented")
    }
    override fun addTrackToHistory(track: Track){
        historyRepository.addTrackToHistory(track)
    }

    override fun clearHistory() {
        historyRepository.clearHistory()
    }

    override fun getTrackFromHistory(): ArrayList<Track> {
        return historyRepository.getTrackFromHistory()

    }

    override fun isNotEmpty() {
        historyRepository.isNotEmpty()
    }

    override fun saveTrackToPref(trackList: ArrayList<Track>) {
        TODO("Not yet implemented")
    }

}