package com.practicum.project.playlistmaker.data

import com.practicum.project.playlistmaker.data.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response
}