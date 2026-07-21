package com.practicum.project.playlistmaker.domain.api

interface SettingsRepository {
    fun switchTheme(darkThemeEnabled: Boolean)
    fun getTheme(): Boolean
}