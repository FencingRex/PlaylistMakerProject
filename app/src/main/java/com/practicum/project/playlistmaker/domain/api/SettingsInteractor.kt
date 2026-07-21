package com.practicum.project.playlistmaker.domain.api

interface SettingsInteractor {
    fun switchTheme(darkThemeEnabled: Boolean)
    fun getTheme(): Boolean
}