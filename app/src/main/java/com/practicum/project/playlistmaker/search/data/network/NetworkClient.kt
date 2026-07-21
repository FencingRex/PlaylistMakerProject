package com.practicum.project.playlistmaker.search.data.network

import com.practicum.project.playlistmaker.search.data.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response
}