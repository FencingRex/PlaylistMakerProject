package com.practicum.project.playlistmaker.search.ui

import android.app.Application
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.project.playlistmaker.creator.Creator
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import com.practicum.project.playlistmaker.search.data.TrackHistoryRepositoryImpl.Companion.SEARCH_HISTORY_KEY

class SearchViewModel(application: Application): AndroidViewModel(application) {
    private val handler = Handler(Looper.getMainLooper())
    private val tracksInteractor = Creator.provideTrackInteractor()
    private val sharedPreferences = SharedPreferences.OnSharedPreferenceChangeListener{
        _, key ->
        if (key == SEARCH_HISTORY_KEY){
            getHistory()
        }
    }

    fun getHistory(){
        tracksInteractor.getTrackFromHistory()
    }
    fun removeCallback(){
      //  handler.removeCallbacks()
    }
    companion object{
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SearchViewModel(this[APPLICATION_KEY] as Application)
            }
        }
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}