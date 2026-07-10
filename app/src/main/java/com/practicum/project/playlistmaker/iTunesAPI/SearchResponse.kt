package com.practicum.project.playlistmaker.iTunesAPI

import com.practicum.project.playlistmaker.data.dto.Response
import com.practicum.project.playlistmaker.domain.models.Track

data class SearchResponse(
    var resultCount: Int,
    val results: MutableList<Track>
) : Response()
