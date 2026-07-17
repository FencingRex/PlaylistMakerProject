package com.practicum.project.playlistmaker.data.dto

import com.practicum.project.playlistmaker.domain.models.Track

class TrackSearchResponse(   var resultCount: Int,
                             val results: MutableList<Track>): Response() {
}