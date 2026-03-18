package com.practicum.project.playlistmaker.iTunesAPI

import com.practicum.project.playlistmaker.Track

data class SearchResponse(
    val resultCount: Int,
    val results:List<Track>
)
