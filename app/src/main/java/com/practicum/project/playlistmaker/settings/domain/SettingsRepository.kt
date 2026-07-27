package com.practicum.project.playlistmaker.settings.domain

interface SettingsRepository {
    fun switchTheme(darkThemeEnabled: Boolean)
    fun getTheme(): Boolean
}