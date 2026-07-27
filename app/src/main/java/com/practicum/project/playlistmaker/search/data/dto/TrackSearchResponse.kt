package com.practicum.project.playlistmaker.search.data.dto

class TrackSearchResponse(
    var resultCount: Int,
    val results: List<TrackDTO>
): Response() {}