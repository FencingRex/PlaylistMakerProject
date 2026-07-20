package com.practicum.project.playlistmaker.data.dto

class TrackSearchResponse(
    var resultCount: Int,
    val results: List<TrackDTO>
): Response() {}