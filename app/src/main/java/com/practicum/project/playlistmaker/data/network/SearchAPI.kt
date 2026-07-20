package com.practicum.project.playlistmaker.data.network

import com.practicum.project.playlistmaker.data.dto.TrackSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchAPI {
    @GET("/search?entity=song")
    fun search(@Query("expression") expression: String):
            Call<TrackSearchResponse>
}