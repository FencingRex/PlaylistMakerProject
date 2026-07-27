package com.practicum.project.playlistmaker.search.data.network

import android.content.Context
import com.practicum.project.playlistmaker.search.data.dto.Response
import com.practicum.project.playlistmaker.search.data.dto.TrackSearchRequest
class RetrofitNetworkClient(
    private val searchAPI: SearchAPI,
    private val context: Context
): NetworkClient {

    override fun doRequest(dto: Any): Response {
        if (dto is TrackSearchRequest) {
            val resp = searchAPI.search(dto.expression).execute()
            val body = resp.body() ?: Response()

            return body.apply { resultCode = resp.code() }
        } else {
            return Response().apply { resultCode = 400 }
        }

    }
}