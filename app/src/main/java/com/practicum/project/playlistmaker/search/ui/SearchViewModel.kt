package com.practicum.project.playlistmaker.search.ui

import android.app.Application
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.project.playlistmaker.creator.Creator
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import com.practicum.project.playlistmaker.search.data.TrackHistoryRepositoryImpl.Companion.SEARCH_HISTORY_KEY
import com.practicum.project.playlistmaker.search.domain.TracksInteractor
import com.practicum.project.playlistmaker.search.domain.models.Track
import com.practicum.project.playlistmaker.search.model.HistoryState
import com.practicum.project.playlistmaker.search.model.RequestState

class SearchViewModel(application: Application): AndroidViewModel(application) {
    private var searchRequest: String = ""
    private val handler = Handler(Looper.getMainLooper())
    private val tracksInteractor = Creator.provideTrackInteractor()
    private val searchRunnable = Runnable {searchTrack(searchRequest)}
    private val sharedPreferences = SharedPreferences.OnSharedPreferenceChangeListener{
        _, key ->
        if (key == SEARCH_HISTORY_KEY){
            getHistory()
        }
    }
    private var isClickAllowed = true
    private val requestStateLiveData = MutableLiveData<RequestState>()
    fun getRequestState(): LiveData<RequestState> = requestStateLiveData

    private val historyStateLiveData = MutableLiveData<HistoryState>()
    fun getHistoryState(): LiveData<HistoryState> = historyStateLiveData

    init {
        getHistory()
    }
    private fun searchTrack(searchValue: String){
        if(searchValue.isNotEmpty()) {
            requestStateLiveData.postValue(RequestState.Loading)
            val consumer = object : TracksInteractor.TracksConsumer {
                override fun consume(foundTracks: List<Track>) {
                    if (foundTracks.isNotEmpty()) {
                        requestStateLiveData.postValue(RequestState.Success)
                    } else {
                        requestStateLiveData.postValue(RequestState.NotFound)
                    }
                }
                override fun onFailure() {
                    requestStateLiveData.postValue(RequestState.NotConnected)
                }
            }
            tracksInteractor.searchTracks(searchValue, consumer)
        }
    }
    private fun addTrackToHistory(track: Track) {
        tracksInteractor.addTrackToHistory(track)
    }
    fun getHistory(): ArrayList<Track>{
        val historyTrackList = tracksInteractor.getTrackFromHistory()
        historyStateLiveData.postValue(
            if (historyTrackList.isEmpty()) HistoryState.EmptyHistory else
                HistoryState.HistoryContent(historyTrackList)
        )
        return historyTrackList
    }
    fun removeCallback(){
        handler.removeCallbacks(searchRunnable)
    }
    fun clearHistory() {
        tracksInteractor.clearHistory()
        getHistory()
    }
    fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    fun clickDebounce(track: Track): Boolean{
        val currentClick = isClickAllowed
        if (isClickAllowed){
            isClickAllowed = false
            addTrackToHistory(track)
            handler.postDelayed({isClickAllowed = true},
                CLICK_DEBOUNCE_DELAY
            )
        }
        return currentClick
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