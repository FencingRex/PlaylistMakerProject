package com.practicum.project.playlistmaker

import android.app.Application
import com.practicum.project.playlistmaker.di.dataModule
import com.practicum.project.playlistmaker.di.interactorModule
import com.practicum.project.playlistmaker.di.repositoryModule
import com.practicum.project.playlistmaker.di.viewModelModule
import com.practicum.project.playlistmaker.settings.domain.SettingsInteractor
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App: Application(){
    private val settingsInteractor: SettingsInteractor by inject()
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(dataModule, interactorModule, repositoryModule, viewModelModule)
        }
        settingsInteractor.switchTheme(settingsInteractor.getTheme())
    }
}