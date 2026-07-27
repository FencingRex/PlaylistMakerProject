package com.practicum.project.playlistmaker.player.domain

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
    fun getCurrentPosition(): String
    fun formatTime(milliseconds: Long): String
    fun isPlaying(): Boolean

}