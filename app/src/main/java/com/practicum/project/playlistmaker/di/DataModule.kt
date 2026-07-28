package com.practicum.project.playlistmaker.di

import android.content.Context.MODE_PRIVATE
import android.media.MediaPlayer
import com.google.gson.Gson
import com.practicum.project.playlistmaker.search.data.TrackHistoryRepositoryImpl
import com.practicum.project.playlistmaker.search.data.TrackHistoryRepositoryImpl.Companion.SEARCH_HISTORY_PREF
import com.practicum.project.playlistmaker.search.data.network.NetworkClient
import com.practicum.project.playlistmaker.search.data.network.RetrofitNetworkClient
import com.practicum.project.playlistmaker.search.data.network.SearchAPI
import com.practicum.project.playlistmaker.search.domain.TrackHistoryRepository
import com.practicum.project.playlistmaker.settings.data.SettingsRepositoryImpl.Companion.SAVED_THEME_STATE
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
    single<NetworkClient> {
        RetrofitNetworkClient(get(),
            androidContext())
    }
    single<SearchAPI> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SearchAPI::class.java)
    }
    single<TrackHistoryRepository>{
        TrackHistoryRepositoryImpl(get(),
            get(named("historyPrefs")))
    }
    factory { Gson() }
    single(named("historyPrefs")) {
        androidContext()
            .getSharedPreferences(SEARCH_HISTORY_PREF,MODE_PRIVATE)
    }
    single(named("themePrefs")) {
        androidContext()
            .getSharedPreferences(SAVED_THEME_STATE,MODE_PRIVATE)
    }
    factory {
        MediaPlayer()
    }
}