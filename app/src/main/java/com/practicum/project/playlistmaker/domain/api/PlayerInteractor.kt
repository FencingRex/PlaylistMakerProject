package com.practicum.project.playlistmaker.domain.api

interface PlayerInteractor {
    fun preparePlayer(
        url: String,
        onPrepared: () -> Unit,
        onCompletion: () -> Unit
    )

    fun startPlayer(onStarted: () ->Unit)
    fun pausePlayer(onPaused: () -> Unit = {} )
    fun playbackControl(
        onStarted: () -> Unit = {},
        onPaused: () -> Unit = {}
    )

    fun releasePlayer()
    fun getCurrentPosition(): Int
    fun formatTime(milliseconds: Long): String
    fun isPlaying(): Boolean

}