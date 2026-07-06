package com.practicum.project.playlistmaker.domain.api

import com.practicum.project.playlistmaker.Track

interface TracksRepository {
    fun searchTracks(expression: String): MutableList<Track>
}