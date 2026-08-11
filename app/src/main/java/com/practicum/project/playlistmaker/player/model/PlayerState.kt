package com.practicum.project.playlistmaker.player.model

enum class PlayerState {
    STATE_DEFAULT,
    STATE_PREPARED,
    STATE_PLAYING,
    STATE_PAUSED
}
data class PlayerUiState(
    val status: PlayerState = PlayerState.STATE_DEFAULT,
    val currentPosition: String = "00:00",
    val isFavorite: Boolean = false

)