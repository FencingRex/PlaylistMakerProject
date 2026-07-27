package com.practicum.project.playlistmaker.player.domain

class PlayerInteractorImpl (private  val repository: PlayerRepository): PlayerInteractor{
    override fun preparePlayer(url: String, onPrepared: () -> Unit, onCompletion: () -> Unit) {
        repository.preparePlayer(url, onPrepared, onCompletion)
    }

    override fun startPlayer(onStarted: () -> Unit) {
        repository.startPlayer (onStarted)
    }

    override fun pausePlayer(onPaused: () -> Unit) {
        repository.pausePlayer (onPaused)
    }

    override fun playbackControl(onStarted: () -> Unit, onPaused: () -> Unit) {
        repository.playbackControl(onStarted, onPaused)
    }

    override fun releasePlayer() {
        repository.releasePlayer()
    }

    override fun getCurrentPosition(): String {
        return formatTime(repository.getCurrentPosition().toLong())
    }
    override fun formatTime(milliseconds: Long): String {
        val seconds = milliseconds / 1000
        return String.format("%02d:%02d", seconds / 60, seconds % 60)
    }
    override fun isPlaying(): Boolean {
       return repository.isPlaying()
    }
}