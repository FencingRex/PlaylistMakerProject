package com.practicum.project.playlistmaker.search.data

import com.practicum.project.playlistmaker.R
import com.practicum.project.playlistmaker.utils.Resource
import com.practicum.project.playlistmaker.search.data.network.NetworkClient
import com.practicum.project.playlistmaker.search.data.dto.TrackDTO
import com.practicum.project.playlistmaker.search.data.dto.TrackSearchRequest
import com.practicum.project.playlistmaker.search.data.dto.TrackSearchResponse
import com.practicum.project.playlistmaker.search.domain.models.Track
import com.practicum.project.playlistmaker.search.domain.TrackHistoryRepository
import com.practicum.project.playlistmaker.search.domain.TracksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    private val trackHistoryRepository: TrackHistoryRepository
) : TracksRepository {
    override fun searchTracks(expression: String):  Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        when (response.resultCode) {
            -1 ->{
                emit(Resource.Error(NO_INTERNET_CONNECTION))
            }
            200 -> {
                with(response as TrackSearchResponse) {
                    val data = results.map {
                        it.toDomain()
                    }
                    emit(Resource.Success(data))
                }
            }
            else -> {
                emit(Resource.Error("$ERROR:${response.resultCode}"))
            }
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
    fun Track.toDto(): TrackDTO = TrackDTO(
        trackId,
        trackName,
        artistName,
        trackTimeMillis,
        artworkUrl100,
        collectionName,
        releaseDate,
        primaryGenreName,
        country,
        previewUrl
    )
    fun TrackDTO.toDomain(): Track = Track(
        trackId,
        trackName,
        artistName,
        trackTimeMillis,
        artworkUrl100,
        collectionName,
        releaseDate,
        primaryGenreName,
        country,
        previewUrl
    )
    companion object {
        const val NO_INTERNET_CONNECTION = "No internet connection"
        const val ERROR = "Error"
    }
}