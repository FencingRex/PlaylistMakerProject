package com.practicum.project.playlistmaker.data

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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
            return (response as TrackSearchResponse).result.map {
                Track(it.trackId,
                    it.trackName,
                    it.artistName,
                    it.trackTimeMillis,
                    it.artworkUrl100,
                    it.collectionName,
                    it.releaseDate,
                    it.primaryGenreName,
                    it.country,
                    it.previewUrl)
            }
        }else {
            return emptyList()
        }
    }

    override fun saveTrack(tracksList: ArrayList<Track>) {
        val dtoTracks = tracksList.map{ it.toDto()}
        trackHistoryRepository.saveTrackToPref(ArrayList(dtoTracks))
    }
    fun addTrackToHistory(track: Track){
        val dtoTrack = track.toDto()
            //getTrackFromHistory()
//        history.removeIf { it.trackId == track.trackId }
//        history.add(0,track)
//
//        if (history.size > LIMIT_QTY){
//            history.removeAt(history.lastIndex)
//        }
//        saveTrackToPref(history)

    }

//    fun getTrackFromHistory(): MutableList<Track>{
//        val value = sharedPreferences.getString(SEARCH_HISTORY_KEY,null) ?: return mutableListOf()
//        val type  = object : TypeToken<MutableList<Track>>() {}.type
//        val result: MutableList<Track> = Gson().fromJson(value, type)
//        return  result
//    }

    fun clearHistory(){
        trackHistoryRepository.clearHistory()

    }
    fun Track.toDto(): TrackDTO =TrackDTO(trackId, trackName, artistName, trackTimeMillis, artworkUrl100,  collectionName, releaseDate, primaryGenreName, country, previewUrl
    )
    companion object{
        const val SEARCH_HISTORY_PREF = "historyPreferences"
        const val SEARCH_HISTORY_KEY = "searchHistoryKey"
        const val LIMIT_QTY = 10
    }
}