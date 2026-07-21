package com.practicum.project.playlistmaker.player.data

import android.media.MediaPlayer
import com.practicum.project.playlistmaker.player.domain.PlayerRepository

class PlayerRepositoryImpl: PlayerRepository {
    private var mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT

    override fun preparePlayer(url: String, onPrepared: () -> Unit, onCompletion: () -> Unit) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = STATE_PREPARED
            onPrepared()
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
            onCompletion()
        }

    }

    override fun startPlayer(onStarted: () -> Unit) {
        mediaPlayer.start()
        playerState = STATE_PLAYING
        onStarted()
    }

    override fun pausePlayer(onPaused: () -> Unit) {
        mediaPlayer.pause()
        playerState = STATE_PAUSED
        onPaused()
    }

    override fun playbackControl(onStarted: () -> Unit, onPaused: () -> Unit) {
        when (playerState){
            STATE_PLAYING ->{
                pausePlayer (onPaused)
            }
            STATE_PREPARED,STATE_PAUSED -> {
                startPlayer (onStarted)
            }
            else -> {}
        }
    }

    override fun releasePlayer() {
        mediaPlayer.release()
    }

    override fun isPlaying(): Boolean {
        return (playerState == STATE_PLAYING)
    }

    override fun getCurrentPosition(): Int {
        return  mediaPlayer.currentPosition
    }
    override fun formatTime(milliseconds: Long): String
    {
        val seconds = milliseconds / 1000
        return String.format("%02d:%02d", seconds / 60, seconds % 60)
    }
    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val UPDATE_TIME_INTERVAL = 500L
    }

}