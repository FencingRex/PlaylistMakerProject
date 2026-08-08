package com.practicum.project.playlistmaker.search.ui

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.practicum.project.playlistmaker.search.domain.TracksInteractor
import com.practicum.project.playlistmaker.search.domain.models.Track
import com.practicum.project.playlistmaker.search.model.RequestState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(private val tracksInteractor: TracksInteractor): ViewModel() {
    private var searchRequest: String = ""
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable {searchTrack(searchRequest)}
    private var isClickAllowed = true
    private val requestStateLiveData = MutableLiveData<RequestState>()
    private var searchJob: Job? =null
    val requestState: LiveData<RequestState> = requestStateLiveData

    init {
        getHistory()
    }
    fun searchTrack(searchValue: String){
        if(searchValue.isEmpty()) {
            requestStateLiveData.postValue(RequestState.Empty)
            return
        }
        requestStateLiveData.postValue(RequestState.Loading)

        viewModelScope.launch {
            tracksInteractor.searchTracks(searchValue)
                .collect { pair ->
                    processResult(pair.first,pair.second)
                }
        }
    }
    private fun processResult(foundTracks: List<Track>?,errorMessage: String?){
        val tracks = mutableListOf<Track>()
        if (foundTracks != null){
            tracks.addAll(foundTracks)
        }
        when{
            errorMessage != null ->{
                requestStateLiveData.postValue(RequestState.NotConnected)
            }
            tracks.isEmpty() -> {
                requestStateLiveData.postValue(RequestState.NotFound)
            }
            else ->{
                requestStateLiveData.postValue(RequestState.Success(tracks))
            }
        }
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
    }
    fun searchDebounce(searchRequest:String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            searchTrack(searchRequest)
        }
    }
    fun clickDebounce(track: Track): Boolean{
        val currentClick = isClickAllowed
        if (isClickAllowed){
            isClickAllowed = false
            addTrackToHistory(track)
            viewModelScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return currentClick
    }
    companion object{
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}