package com.practicum.project.playlistmaker.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.project.playlistmaker.medialib.domain.FavoritesInteractor
import com.practicum.project.playlistmaker.player.domain.PlayerInteractor
import com.practicum.project.playlistmaker.player.model.PlayerState
import com.practicum.project.playlistmaker.player.model.PlayerUiState
import com.practicum.project.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class PlayerViewModel(
    sampleUrl: String,
    trackId: Int,
    private val playerInteractor: PlayerInteractor,
    private val favoritesInteractor: FavoritesInteractor
    ): ViewModel() {
    private val playerStateLiveData = MutableLiveData(PlayerUiState())
    val playerUIState: LiveData<PlayerUiState> = playerStateLiveData

    private var timerJob: Job? = null
    private var favoriteJob: Job? = null

    init {
        preparePlayer(sampleUrl)
        checkFavoriteStatus(trackId)
    }
    fun preparePlayer(sampleUrl: String){
        playerInteractor.preparePlayer(
            url = sampleUrl,
            onPrepared = { },
            onCompletion = {
                stopTimer()
                playerStateLiveData.postValue(
                    playerStateLiveData.value?.copy(
                        status = PlayerState.STATE_PREPARED,
                        currentPosition = "00:00"
                    ) ?: PlayerUiState(PlayerState.STATE_PREPARED,"00:00"))
            })
        playerStateLiveData.postValue(
            playerStateLiveData.value?.copy(
                status = PlayerState.STATE_PREPARED,
                currentPosition = "00:00"
            ) ?: PlayerUiState(PlayerState.STATE_PREPARED, "00:00"))
    }
    fun playbackControl(){
        playerInteractor.playbackControl(
            {
                startTimer()
                val currentPos = playerStateLiveData.value?.currentPosition ?: "00:00"
                playerStateLiveData.postValue(
                    playerStateLiveData.value?.copy(
                        status = PlayerState.STATE_PLAYING,
                        currentPosition = currentPos
                    ) ?: PlayerUiState(PlayerState.STATE_PLAYING, currentPos))
            },
            {
                stopTimer()
                val currentPos = playerStateLiveData.value?.currentPosition ?: "00:00"
                playerStateLiveData.postValue(
                    playerStateLiveData.value?.copy(
                        status = PlayerState.STATE_PAUSED,
                        currentPosition = currentPos
                    ) ?: PlayerUiState(PlayerState.STATE_PAUSED, currentPos))
            }
        )
    }
    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (playerInteractor.isPlaying()){
                playerStateLiveData.postValue(
                    playerStateLiveData.value?.copy(
                        status = PlayerState.STATE_PLAYING,
                        currentPosition = playerInteractor.getCurrentPosition()
                    ) ?:PlayerUiState(PlayerState.STATE_PLAYING,playerInteractor.getCurrentPosition()))
                delay(UPDATE_TIME_INTERVAL)
            }
        }
    }
    private fun stopTimer(){
        timerJob?.cancel()
    }
    fun pausePlayer(){
        playerInteractor.pausePlayer()
        val currentPos = playerStateLiveData.value?.currentPosition ?: "00:00"
        playerStateLiveData.postValue(
            playerStateLiveData.value?.copy(
                status = PlayerState.STATE_PAUSED,
                currentPosition = currentPos
            ) ?: PlayerUiState(PlayerState.STATE_PAUSED, currentPos))
    }
    fun releasePlayer(){
        stopTimer()
        playerInteractor.releasePlayer()
        playerStateLiveData.postValue(PlayerUiState())
    }
    private fun checkFavoriteStatus(trackId: Int) {
        viewModelScope.launch {
            val isFavorite = favoritesInteractor.isFavorite(trackId)
            playerStateLiveData.value = playerStateLiveData.value?.copy(isFavorite = isFavorite)
        }
    }
    fun onFavoriteClicked(track: Track?){
        track ?: return
        val currentState = playerStateLiveData.value ?: return
        val newFavorite = !currentState.isFavorite
        track.isFavorite = newFavorite
        favoriteJob?.cancel()
        favoriteJob = viewModelScope.launch {
            if (newFavorite){
                favoritesInteractor.addToFavorites(track)
            } else {
                favoritesInteractor.deleteFromFavorites(track)
            }
            playerStateLiveData.postValue(currentState.copy(isFavorite = newFavorite))
        }
    }
    companion object{
        const val UPDATE_TIME_INTERVAL: Long = 300L
    }
}