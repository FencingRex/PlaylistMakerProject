package com.practicum.project.playlistmaker.data

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.project.playlistmaker.data.dto.TrackDTO
import com.practicum.project.playlistmaker.domain.api.TrackHistoryRepository
import com.practicum.project.playlistmaker.domain.models.Track

class TrackHistoryRepositoryImpl(private val sharedPreferences: SharedPreferences): TrackHistoryRepository {
    override fun addTrackToHistory(track: TrackDTO){
        val history = getTrackFromHistory()
        history.removeIf { it.trackId == track.trackId }
        history.add(0,track)

        if (history.size > LIMIT_QTY){
            history.removeAt(history.lastIndex)
        }
        saveTrackToPref(history)

    }
     override fun saveTrackToPref(trackList: ArrayList<TrackDTO>){
        sharedPreferences.edit()
            .putString(SEARCH_HISTORY_KEY, Gson().toJson(trackList))
            .apply()

    }

    override fun getTrackFromHistory(): ArrayList<TrackDTO>{
        val value = sharedPreferences.getString(SEARCH_HISTORY_KEY,null) ?: return ArrayList()
        val type  = object : TypeToken<MutableList<Track>>() {}.type
        val result: ArrayList<TrackDTO> = Gson().fromJson(value, type)
        return  result
    }

    override fun clearHistory(){
        sharedPreferences.edit()
            .clear()
            .apply()

    }
    companion object{
        const val SEARCH_HISTORY_PREF = "historyPreferences"
        const val SEARCH_HISTORY_KEY = "searchHistoryKey"
        const val LIMIT_QTY = 10
    }
}
