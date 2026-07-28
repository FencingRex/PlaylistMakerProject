package com.practicum.project.playlistmaker.player.ui

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.project.playlistmaker.player.domain.PlayerInteractor
import com.practicum.project.playlistmaker.player.model.PlayerState
import com.practicum.project.playlistmaker.player.model.PlayerUiState

class PlayerViewModel(
    sampleUrl: String,
    private val playerInteractor: PlayerInteractor
    ): ViewModel() {
    private val playerStateLiveData = MutableLiveData(PlayerUiState())
    val playerUIState: LiveData<PlayerUiState> = playerStateLiveData
    private var mainThreadHandler = Handler(Looper.getMainLooper())
    private var timerRunnable: Runnable? = null

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
        if (timerRunnable != null) return
        val runnable = object : Runnable {
            override fun run() {
                if (playerInteractor.isPlaying()) {
                    val position = playerInteractor.getCurrentPosition()
                    playerStateLiveData.postValue(
                        PlayerUiState(PlayerState.STATE_PLAYING, position)
                    )
                    mainThreadHandler.postDelayed(this, UPDATE_TIME_INTERVAL)
                } else {
                    stopTimer()
                }
            }
        }
        timerRunnable = runnable
        mainThreadHandler.post(runnable)
    }
    private fun stopTimer(){
        timerRunnable?.let {
            mainThreadHandler.removeCallbacks(it)
            timerRunnable = null
        }
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
        const val UPDATE_TIME_INTERVAL: Long = 500L
    }
}