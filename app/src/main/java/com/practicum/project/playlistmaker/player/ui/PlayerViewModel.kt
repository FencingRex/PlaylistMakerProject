package com.practicum.project.playlistmaker.player.ui

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.project.playlistmaker.creator.Creator
import com.practicum.project.playlistmaker.player.domain.PlayerInteractor
import com.practicum.project.playlistmaker.player.model.PlayerState

class PlayerViewModel(
    sampleUrl: String,
    private val playerInteractor: PlayerInteractor
    ): ViewModel() {
    private val playerStateLiveData = MutableLiveData<PlayerState>()
    private val currentPositionLiveData = MutableLiveData<String>()
    private var mainThreadHandler = Handler(Looper.getMainLooper())
    init {
        preparePlayer(sampleUrl)
    }
    fun playbackControl(){
        playerInteractor.playbackControl(
            {
                mainThreadHandler?.post(startTimer())
                playerStateLiveData.postValue(PlayerState.STATE_PLAYING)
            },
            {
                mainThreadHandler?.removeCallbacks(startTimer())
                playerStateLiveData.postValue(PlayerState.STATE_PAUSED)
            }
        )
    }
    fun preparePlayer(sampleUrl: String){
        playerInteractor.preparePlayer(
            url = sampleUrl,
            onPrepared = { },
            onCompletion = {
                mainThreadHandler?.removeCallbacks { startTimer() }
            })
        playerStateLiveData.postValue(PlayerState.STATE_PREPARED)
    }
    fun getState(): LiveData<PlayerState> = playerStateLiveData
    fun getCurrentPosition(): LiveData<String> = currentPositionLiveData
    private fun startTimer() = object : Runnable {
        override fun run() {
            if (playerInteractor.isPlaying()){
                playerStateLiveData.postValue(PlayerState.STATE_PLAYING)
                currentPositionLiveData.postValue(playerInteractor.getCurrentPosition())
                mainThreadHandler?.postDelayed(this,UPDATE_TIME_INTERVAL)
            }
        }
    }
    fun pausePlayer(){
        playerInteractor.pausePlayer()
    }
    fun releasePlayer(){
        mainThreadHandler?.removeCallbacks(startTimer())
        playerInteractor.releasePlayer()
    }
    companion object{
        const val UPDATE_TIME_INTERVAL: Long = 500L
        fun getViewModelFactory(sampleUrl: String): ViewModelProvider.Factory = viewModelFactory{
            initializer{
                PlayerViewModel(
                    sampleUrl,
                    Creator.providePlayerInteractor())
            }
        }
    }
}