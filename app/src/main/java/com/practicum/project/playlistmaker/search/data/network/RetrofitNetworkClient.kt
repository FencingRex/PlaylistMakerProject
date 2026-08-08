package com.practicum.project.playlistmaker.search.data.network

import android.content.Context
import com.practicum.project.playlistmaker.search.data.dto.Response
import com.practicum.project.playlistmaker.search.data.dto.TrackSearchRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class RetrofitNetworkClient(
    private val searchAPI: SearchAPI,
    private val context: Context
): NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        if (!isConnected()){
            return Response().apply { resultCode = -1 }
        }
        if (dto !is TrackSearchRequest) {
            return Response().apply { resultCode = 400 }
        }
        return withContext(Dispatchers.IO){
            try {
                val resp = searchAPI.search(dto.expression)
                resp.apply { resultCode = 200 }
            } catch (e: Throwable){
                Response().apply { resultCode = 500 }
            }
        }
    }
    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        val capabilities = connectivityManager.getNetworkCapabilities(
            connectivityManager.activeNetwork
        )

        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}