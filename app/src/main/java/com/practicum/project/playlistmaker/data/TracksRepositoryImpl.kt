package com.practicum.project.playlistmaker.data

import com.practicum.project.playlistmaker.data.dto.TrackDTO
import com.practicum.project.playlistmaker.data.dto.TrackSearchRequest
import com.practicum.project.playlistmaker.data.dto.TrackSearchResponse
import com.practicum.project.playlistmaker.domain.api.TrackHistoryRepository
import com.practicum.project.playlistmaker.domain.api.TracksRepository
import com.practicum.project.playlistmaker.domain.models.Track

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    private val trackHistoryRepository: TrackHistoryRepository) : TracksRepository {
    override fun searchTracks(expression: String): List<Track> {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        if (response.resultCode == 200) {
            return (response as TrackSearchResponse).results.map{ dto ->
                Track(dto.trackId ,
                    dto.trackName,
                    dto.artistName,
                    dto.trackTimeMillis,
                    dto.artworkUrl100,
                    dto.collectionName,
                    dto.releaseDate,
                    dto.primaryGenreName,
                    dto.country,
                    dto.previewUrl)
            }
        }else {
            return emptyList()
        }
    }

    override fun saveTrack(tracksList: List<Track>) {
        trackHistoryRepository.saveTrackToPref(tracksList)
    }
    override fun addTrackToHistory(track: Track){
        trackHistoryRepository.addTrackToHistory(track)
    }

    override fun getTrackFromHistory(): ArrayList<Track> {
        val dtoTracks = trackHistoryRepository.getTrackFromHistory()
        return ArrayList(dtoTracks)
    }
    override fun clearHistory(){
        trackHistoryRepository.clearHistory()

    }

    override fun isNotEmpty(): Boolean {
        return trackHistoryRepository.getTrackFromHistory().isNotEmpty()
    }

    override fun saveTrackToPref(tracksList: List<Track>) {
        tracksList.map { it.toDto() }
        trackHistoryRepository.saveTrackToPref(tracksList)
    }
    fun Track.toDto(): TrackDTO =TrackDTO(trackId, trackName, artistName, trackTimeMillis, artworkUrl100,  collectionName, releaseDate, primaryGenreName, country, previewUrl
    )
    fun TrackDTO.toDomain(): Track = Track(
        trackId, trackName, artistName, trackTimeMillis, artworkUrl100,  collectionName, releaseDate, primaryGenreName, country, previewUrl
    )
    companion object{
        const val SEARCH_HISTORY_PREF = "historyPreferences"
        const val SEARCH_HISTORY_KEY = "searchHistoryKey"
        const val LIMIT_QTY = 10
    }
}
