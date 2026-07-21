package com.practicum.project.playlistmaker.creator

import android.app.Application
import android.content.Context
import com.practicum.project.playlistmaker.player.data.PlayerRepositoryImpl
import com.practicum.project.playlistmaker.settings.data.SettingsRepositoryImpl
import com.practicum.project.playlistmaker.search.data.TrackHistoryRepositoryImpl
import com.practicum.project.playlistmaker.search.data.TracksRepositoryImpl
import com.practicum.project.playlistmaker.search.data.network.RetrofitNetworkClient
import com.practicum.project.playlistmaker.player.domain.PlayerInteractor
import com.practicum.project.playlistmaker.player.domain.PlayerRepository
import com.practicum.project.playlistmaker.settings.domain.SettingsInteractor
import com.practicum.project.playlistmaker.settings.domain.SettingsRepository
import com.practicum.project.playlistmaker.search.domain.TracksInteractor
import com.practicum.project.playlistmaker.search.domain.TracksRepository
import com.practicum.project.playlistmaker.player.domain.PlayerInteractorImpl
import com.practicum.project.playlistmaker.settings.domain.SettingsInteractorImpl
import com.practicum.project.playlistmaker.search.domain.TracksInteractorImpl

object Creator {
    private lateinit var application: Application
    fun setApp(application: Application){
        Creator.application = application
    }
    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(
            RetrofitNetworkClient(),
            TrackHistoryRepositoryImpl(
                application.getSharedPreferences(
                    TrackHistoryRepositoryImpl.SEARCH_HISTORY_PREF,
                    Context.MODE_PRIVATE
                )
            )
        )
        }
    fun provideTrackInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }
    private fun getSettingsRepository(): SettingsRepository {
        return SettingsRepositoryImpl(
            application.getSharedPreferences(
                SettingsRepositoryImpl.SAVED_THEME_STATE,
                Context.MODE_PRIVATE
            )
        )
    }
    fun provideSettingsInteractor(): SettingsInteractor {
        return SettingsInteractorImpl(getSettingsRepository())

    }
    private fun getPlayerRepository(): PlayerRepository {
        return PlayerRepositoryImpl()
    }
    fun providePlayerInteractor(): PlayerInteractor {
        return PlayerInteractorImpl(getPlayerRepository())
    }
}