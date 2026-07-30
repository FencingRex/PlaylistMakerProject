package com.practicum.project.playlistmaker.di

import com.practicum.project.playlistmaker.medialib.ui.FavoritesViewModel
import com.practicum.project.playlistmaker.medialib.ui.PlaylistsViewModel
import com.practicum.project.playlistmaker.player.ui.PlayerViewModel
import com.practicum.project.playlistmaker.search.ui.SearchViewModel
import com.practicum.project.playlistmaker.settings.ui.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        SearchViewModel(get())
    }
    viewModel {
        (url: String) ->
        PlayerViewModel(url,get())
    }
    viewModel {
        SettingsViewModel(get(),get())
    }
    viewModel {
        FavoritesViewModel()
    }
    viewModel {
        PlaylistsViewModel()
    }
}