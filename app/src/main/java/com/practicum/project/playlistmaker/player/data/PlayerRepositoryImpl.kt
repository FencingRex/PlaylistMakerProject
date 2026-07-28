package com.practicum.project.playlistmaker.player.data

import android.media.MediaPlayer
import com.practicum.project.playlistmaker.player.domain.PlayerRepository
import com.practicum.project.playlistmaker.player.model.PlayerState

class PlayerRepositoryImpl(private val mediaPlayer: MediaPlayer): PlayerRepository {
    private var playerState = PlayerState.STATE_DEFAULT

    override fun preparePlayer(url: String, onPrepared: () -> Unit, onCompletion: () -> Unit) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = PlayerState.STATE_PREPARED
            onPrepared()
        }
        mediaPlayer.setOnCompletionListener {
            playerState = PlayerState.STATE_PREPARED
            onCompletion()
        }

    }

    override fun startPlayer(onStarted: () -> Unit) {
        mediaPlayer.start()
        playerState = PlayerState.STATE_PLAYING
        onStarted()
    }

    override fun pausePlayer(onPaused: () -> Unit) {
        mediaPlayer.pause()
        playerState = PlayerState.STATE_PAUSED
        onPaused()
    }

    override fun playbackControl(onStarted: () -> Unit, onPaused: () -> Unit) {
        when (playerState){
            PlayerState.STATE_PLAYING ->{
                pausePlayer (onPaused)
            }
            PlayerState.STATE_PREPARED,PlayerState.STATE_PAUSED -> {
                startPlayer (onStarted)
            }
            else -> {}
        }
    }

    override fun releasePlayer() {
        mediaPlayer.release()
    }

    override fun isPlaying(): Boolean {
        return (playerState == PlayerState.STATE_PLAYING)
    }

    override fun getCurrentPosition(): Int {
        return  mediaPlayer.currentPosition
    }
    override fun formatTime(milliseconds: Long): String
    {
        val seconds = milliseconds / 1000
        return String.format("%02d:%02d", seconds / 60, seconds % 60)
    }
}