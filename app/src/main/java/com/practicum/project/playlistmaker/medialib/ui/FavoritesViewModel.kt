package com.practicum.project.playlistmaker.medialib.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.project.playlistmaker.medialib.domain.FavoritesInteractor
import com.practicum.project.playlistmaker.medialib.model.FavoritesState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val favoritesInteractor: FavoritesInteractor
) : ViewModel() {
    private var favoriteJob: Job? = null
    private val favoritesLiveData = MutableLiveData<FavoritesState>()
    val favoritesState: LiveData<FavoritesState> = favoritesLiveData

    init{
        getFavorites()
    }
    private fun  getFavorites(){
        favoriteJob?.cancel()
        favoriteJob = viewModelScope.launch { favoritesInteractor.getFavorites().collect { tracks ->
            if (tracks.isNotEmpty()){
                favoritesLiveData.postValue(FavoritesState.Content(tracks))
            } else favoritesLiveData.postValue(FavoritesState.Empty)
        } }

    }
}