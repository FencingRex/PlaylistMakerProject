package com.practicum.project.playlistmaker.data.network

import com.practicum.project.playlistmaker.data.NetworkClient
import com.practicum.project.playlistmaker.data.dto.Response
import com.practicum.project.playlistmaker.data.dto.TrackSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
const val iTunesBaseUrl: String = "https://itunes.apple.com"
class RetrofitNetworkClient: NetworkClient {
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesSearch = retrofit.create(SearchAPI::class.java)

    override fun doRequest(dto: Any): Response {
        if (dto is TrackSearchRequest) {
            val resp = iTunesSearch.search(dto.expression).execute()
            val body = resp.body() ?: Response()

            return body.apply { resultCode = resp.code() }
        } else {
            return Response().apply { resultCode = 400 }
        }

    }
}