package com.practicum.project.playlistmaker

import android.app.Application
import com.practicum.project.playlistmaker.data.TracksRepositoryImpl
import com.practicum.project.playlistmaker.data.network.RetrofitNetworkClient
import com.practicum.project.playlistmaker.domain.api.TrackHistoryRepository
import com.practicum.project.playlistmaker.domain.api.TracksInteractor
import com.practicum.project.playlistmaker.domain.api.TracksRepository
import com.practicum.project.playlistmaker.domain.impl.TracksInteractorImpl
import com.practicum.project.playlistmaker.data.App.Companion.SAVED_THEME_STATE
import com.practicum.project.playlistmaker.data.TrackHistoryRepositoryImpl.Companion.SEARCH_HISTORY_PREF
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import com.practicum.project.playlistmaker.data.TrackHistoryRepositoryImpl

object Creator {
    private lateinit var application: Application
    fun setApp(application: Application){
        Creator.application = application
    }
    private fun getTracksRepository(): TracksRepository{
        return TracksRepositoryImpl(RetrofitNetworkClient(),
            TrackHistoryRepositoryImpl(application.getSharedPreferences(SEARCH_HISTORY_PREF, MODE_PRIVATE)))
        }

    fun provideTrackInteractor(): TracksInteractor{
        return TracksInteractorImpl(getTracksRepository())
    }
}