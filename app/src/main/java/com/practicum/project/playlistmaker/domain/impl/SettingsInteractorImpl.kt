package com.practicum.project.playlistmaker.domain.impl

import android.content.SharedPreferences
import com.practicum.project.playlistmaker.domain.api.SettingsInteractor
import com.practicum.project.playlistmaker.domain.api.SettingsRepository

class SettingsInteractorImpl(private val repository: SettingsRepository): SettingsInteractor {
    override fun getTheme(): Boolean {
        return repository.getTheme()
    }

    override fun switchTheme(darkThemeEnabled: Boolean) {
        repository.switchTheme(darkThemeEnabled)
    }
}