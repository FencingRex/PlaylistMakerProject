package com.practicum.project.playlistmaker.playlist.model

import com.practicum.project.playlistmaker.search.domain.models.Track

data class PlaylistEditState(
    val image: String,
    val name: String,
    val description: String,
    val tracksCount: Int,
    val totalDurationTracks: String,
    val tracks: List<Track>
)