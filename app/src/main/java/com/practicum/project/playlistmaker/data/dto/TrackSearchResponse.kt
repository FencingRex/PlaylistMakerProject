package com.practicum.project.playlistmaker.data.dto

class TrackSearchResponse(val searchType: String,
                          val expression: String,
                          val result: MutableList<TrackDTO>): Response() {
}