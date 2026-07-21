package com.practicum.project.playlistmaker

import android.app.Application
import com.practicum.project.playlistmaker.data.TracksRepositoryImpl
import com.practicum.project.playlistmaker.data.network.RetrofitNetworkClient
import com.practicum.project.playlistmaker.domain.api.TracksInteractor
import com.practicum.project.playlistmaker.domain.api.TracksRepository
import com.practicum.project.playlistmaker.domain.impl.TracksInteractorImpl
import com.practicum.project.playlistmaker.data.TrackHistoryRepositoryImpl.Companion.SEARCH_HISTORY_PREF
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import com.practicum.project.playlistmaker.data.PlayerRepositoryImpl
import com.practicum.project.playlistmaker.data.SettingsRepositoryImpl
import com.practicum.project.playlistmaker.data.SettingsRepositoryImpl.Companion.SAVED_THEME_STATE
import com.practicum.project.playlistmaker.data.TrackHistoryRepositoryImpl
import com.practicum.project.playlistmaker.domain.api.PlayerInteractor
import com.practicum.project.playlistmaker.domain.api.PlayerRepository
import com.practicum.project.playlistmaker.domain.api.SettingsInteractor
import com.practicum.project.playlistmaker.domain.api.SettingsRepository
import com.practicum.project.playlistmaker.domain.impl.PlayerInteractorImpl
import com.practicum.project.playlistmaker.domain.impl.SettingsInteractorImpl

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
    private fun getSettingsRepository(): SettingsRepository{
        return SettingsRepositoryImpl(application.getSharedPreferences(SAVED_THEME_STATE,MODE_PRIVATE))
    }
    fun provideSettingsInteractor(): SettingsInteractor{
        return SettingsInteractorImpl(getSettingsRepository())

    }
    private fun getPlayerRepository(): PlayerRepository{
        return PlayerRepositoryImpl()
    }
    fun providePlayerInteractor(): PlayerInteractor{
        return PlayerInteractorImpl(getPlayerRepository())
    }
}
