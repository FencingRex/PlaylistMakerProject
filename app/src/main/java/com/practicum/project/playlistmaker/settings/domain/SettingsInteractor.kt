package com.practicum.project.playlistmaker.settings.domain

interface SettingsInteractor {
    fun switchTheme(darkThemeEnabled: Boolean)
    fun getTheme(): Boolean
}