package com.practicum.project.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.project.playlistmaker.creator.Creator
import com.practicum.project.playlistmaker.settings.domain.SettingsInteractor

class App: Application(){
    private lateinit var settingsInteractor: SettingsInteractor
    override fun onCreate() {
        super.onCreate()

        Creator.setApp(application = this)
        settingsInteractor = Creator.provideSettingsInteractor()
        settingsInteractor.switchTheme(settingsInteractor.getTheme())
    }
}