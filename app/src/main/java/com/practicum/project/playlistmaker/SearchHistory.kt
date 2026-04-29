package com.practicum.project.playlistmaker

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistory(private val sharedPreferences: SharedPreferences) {
    fun addTrackToHistory(track: Track){
        val history = getTrackFromHistory()
        history.removeIf { it.trackId == track.trackId }
        history.add(0,track)

        if (history.size > LIMIT_QTY){
            history.removeAt(history.lastIndex)
        }
        saveTrackToPref(history)

    }
    fun saveTrackToPref(trackList: MutableList<Track>){
        sharedPreferences.edit()
            .putString(SEARCH_HISTORY_KEY, Gson().toJson(trackList))
            .apply()

    }

    fun getTrackFromHistory(): MutableList<Track>{
        val value = sharedPreferences.getString(SEARCH_HISTORY_KEY,null) ?: return mutableListOf()
        val type  = object : TypeToken<MutableList<Track>>() {}.type
        val result: MutableList<Track> = Gson().fromJson(value, type)
        return  result
    }

    fun clearHistory(){
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
