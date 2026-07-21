package com.practicum.project.playlistmaker.search.data

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.project.playlistmaker.search.data.dto.TrackDTO
import com.practicum.project.playlistmaker.search.domain.models.Track
import com.practicum.project.playlistmaker.search.domain.TrackHistoryRepository

class TrackHistoryRepositoryImpl(private val sharedPreferences: SharedPreferences):
    TrackHistoryRepository {
    override fun addTrackToHistory(track: Track){
        val dto = track.toDto()
        val history = getTrackDtoListFromHistory()
        history.removeIf { it.trackId == track.trackId }
        history.add(0,dto)

        if (history.size > LIMIT_QTY){
            history.removeAt(history.lastIndex)
        }
        saveTrackDtoList(history)
    }
     override fun saveTrackToPref(trackList: List<Track>){
        val dtoList = trackList.map { it.toDto() }
         saveTrackDtoList(ArrayList(dtoList))
    }

    override fun getTrackFromHistory(): List<Track>{
        val dtoList = getTrackDtoListFromHistory()
        return dtoList.map { it.toDomain() }
    }

    override fun clearHistory(){
        sharedPreferences.edit()
            .clear()
            .apply()

    }

    override fun isNotEmpty(): Boolean {
        return getTrackFromHistory().isNotEmpty()
    }
    private fun getTrackDtoListFromHistory(): ArrayList<TrackDTO>{
        val value = sharedPreferences.getString(SEARCH_HISTORY_KEY,null) ?: return ArrayList()
        val type  = object : TypeToken<ArrayList<TrackDTO>>() {}.type
        val result: ArrayList<TrackDTO> = Gson().fromJson(value, type)
        return  result
    }
    private fun saveTrackDtoList(trackDtoList: ArrayList<TrackDTO>) {
        val json = Gson().toJson(trackDtoList)
        sharedPreferences.edit()
            .putString(SEARCH_HISTORY_KEY, json)
            .apply()
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
    companion object{
        const val SEARCH_HISTORY_PREF = "historyPreferences"
        const val SEARCH_HISTORY_KEY = "searchHistoryKey"
        const val LIMIT_QTY = 10
    }
}