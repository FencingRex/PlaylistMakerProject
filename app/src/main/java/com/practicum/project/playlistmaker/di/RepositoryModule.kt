package com.practicum.project.playlistmaker.di

import com.practicum.project.playlistmaker.medialib.data.converters.TrackDbConverter
import com.practicum.project.playlistmaker.medialib.data.db.FavoritesRepositoryImpl
import com.practicum.project.playlistmaker.medialib.domain.FavoritesRepository
import com.practicum.project.playlistmaker.player.data.PlayerRepositoryImpl
import com.practicum.project.playlistmaker.player.domain.PlayerRepository
import com.practicum.project.playlistmaker.search.data.TracksRepositoryImpl
import com.practicum.project.playlistmaker.search.domain.TracksRepository
import com.practicum.project.playlistmaker.settings.data.SettingsRepositoryImpl
import com.practicum.project.playlistmaker.settings.domain.SettingsRepository
import com.practicum.project.playlistmaker.sharing.ExternalNavigator
import com.practicum.project.playlistmaker.sharing.data.ExternalNavigatorImpl
import com.practicum.project.playlistmaker.sharing.data.SharingRepositoryImpl
import com.practicum.project.playlistmaker.sharing.domain.SharingRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {
    single<TracksRepository>{
        TracksRepositoryImpl(get(),get())
    }
    factory<PlayerRepository>{
        PlayerRepositoryImpl(get())
    }
    single<SettingsRepository>{
        SettingsRepositoryImpl(get(named("themePrefs")))
    }
    single<SharingRepository>{
        SharingRepositoryImpl(androidContext())
    }
    single<ExternalNavigator> {
        ExternalNavigatorImpl()
    }
    factory{
        TrackDbConverter()
    }
    single<FavoritesRepository>{
        FavoritesRepositoryImpl(get(),get())
    }
}