package com.practicum.project.playlistmaker.di

import com.practicum.project.playlistmaker.medialib.domain.FavoritesInteractor
import com.practicum.project.playlistmaker.medialib.domain.FavoritesInteractorImpl
import com.practicum.project.playlistmaker.medialib.domain.PlaylistInteractor
import com.practicum.project.playlistmaker.medialib.domain.PlaylistInteractorImpl
import com.practicum.project.playlistmaker.player.domain.PlayerInteractor
import com.practicum.project.playlistmaker.player.domain.PlayerInteractorImpl
import com.practicum.project.playlistmaker.search.domain.TracksInteractor
import com.practicum.project.playlistmaker.search.domain.TracksInteractorImpl
import com.practicum.project.playlistmaker.settings.domain.SettingsInteractor
import com.practicum.project.playlistmaker.settings.domain.SettingsInteractorImpl
import com.practicum.project.playlistmaker.sharing.ExternalNavigator
import com.practicum.project.playlistmaker.sharing.data.ExternalNavigatorImpl
import com.practicum.project.playlistmaker.sharing.domain.SharingInteractor
import com.practicum.project.playlistmaker.sharing.domain.SharingInteractorImpl
import org.koin.dsl.module

val interactorModule = module {
    factory<TracksInteractor>{
        TracksInteractorImpl(get())
    }
    factory<PlayerInteractor>{
        PlayerInteractorImpl(get())
    }
    factory<SettingsInteractor>{
        SettingsInteractorImpl(get())
    }
    factory<SharingInteractor>{
        SharingInteractorImpl(get(),get())
    }
    factory<ExternalNavigator>{
        ExternalNavigatorImpl()
    }
    single<FavoritesInteractor>{
        FavoritesInteractorImpl(get())
    }
    single<PlaylistInteractor> {
        PlaylistInteractorImpl(get())
    }
}