package com.practicum.project.playlistmaker.search.data.network

import android.util.Log
import com.practicum.project.playlistmaker.search.data.dto.Response
import com.practicum.project.playlistmaker.search.data.dto.TrackSearchRequest
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
           // Log.d("URL","${dto.expression}")
            val resp = iTunesSearch.search(dto.expression).execute()
            val body = resp.body() ?: Response()

            return body.apply { resultCode = resp.code() }
        } else {
            return Response().apply { resultCode = 400 }
        }

    }
}