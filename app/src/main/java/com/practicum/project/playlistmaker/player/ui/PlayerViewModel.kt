package com.practicum.project.playlistmaker.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.project.playlistmaker.player.domain.PlayerInteractor
import com.practicum.project.playlistmaker.player.model.PlayerState
import com.practicum.project.playlistmaker.player.model.PlayerUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class PlayerViewModel(
    sampleUrl: String,
    private val playerInteractor: PlayerInteractor
    ): ViewModel() {
    private val playerStateLiveData = MutableLiveData(PlayerUiState())
    val playerUIState: LiveData<PlayerUiState> = playerStateLiveData

    private var timerJob: Job? = null
    init {
        preparePlayer(sampleUrl)
    }
    fun preparePlayer(sampleUrl: String){
        playerInteractor.preparePlayer(
            url = sampleUrl,
            onPrepared = { },
            onCompletion = {
                stopTimer()
                playerStateLiveData.postValue(PlayerUiState(PlayerState.STATE_PREPARED,"00:00"))
            })
        playerStateLiveData.postValue(PlayerUiState(PlayerState.STATE_PREPARED, "00:00"))
    }
    fun playbackControl(){
        playerInteractor.playbackControl(
            {
                startTimer()
                val currentPos = playerStateLiveData.value?.currentPosition ?: "00:00"
                playerStateLiveData.postValue(PlayerUiState(PlayerState.STATE_PLAYING, currentPos))
            },
            {
                stopTimer()
                val currentPos = playerStateLiveData.value?.currentPosition ?: "00:00"
                playerStateLiveData.postValue(PlayerUiState(PlayerState.STATE_PAUSED, currentPos))
            }
        )
    }
    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (playerInteractor.isPlaying()){
                playerStateLiveData.postValue(PlayerUiState(PlayerState.STATE_PLAYING,playerInteractor.getCurrentPosition()))
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
        playerStateLiveData.postValue(PlayerUiState(PlayerState.STATE_PAUSED, currentPos))
    }
    fun releasePlayer(){
        stopTimer()
        playerInteractor.releasePlayer()
        playerStateLiveData.postValue(PlayerUiState())
    }
    companion object{
        const val UPDATE_TIME_INTERVAL: Long = 300L
    }
}