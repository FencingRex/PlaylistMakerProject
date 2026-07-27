package com.practicum.project.playlistmaker.search.ui

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.practicum.project.playlistmaker.search.domain.TracksInteractor
import com.practicum.project.playlistmaker.search.domain.models.Track
import com.practicum.project.playlistmaker.search.model.RequestState

class SearchViewModel(private val tracksInteractor: TracksInteractor): ViewModel() {
    private var searchRequest: String = ""
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable {searchTrack(searchRequest)}

    private var isClickAllowed = true
    private val requestStateLiveData = MutableLiveData<RequestState>()
    val requestState: LiveData<RequestState> = requestStateLiveData

    init {
        getHistory()
    }
    fun searchTrack(searchValue: String){
        if(searchValue.isEmpty()) {
            requestStateLiveData.postValue(RequestState.Empty)
            return
        }
        requestStateLiveData.postValue(RequestState.Loading )
        tracksInteractor.searchTracks(searchValue,object : TracksInteractor.TracksConsumer {
            override fun consume(foundTracks: List<Track>) {
                requestStateLiveData.postValue(
                    if (foundTracks.isNotEmpty()) RequestState.Success(foundTracks)
                    else RequestState.NotFound
                )
            }
            override fun onFailure() {
                requestStateLiveData.postValue(RequestState.NotConnected)
            }
        })
    }
    private fun addTrackToHistory(track: Track) {
        tracksInteractor.addTrackToHistory(track)
    }
    fun getHistory(): ArrayList<Track>{
        val historyTrackList = tracksInteractor.getTrackFromHistory()
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
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}