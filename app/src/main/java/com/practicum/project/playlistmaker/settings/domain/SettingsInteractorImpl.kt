package com.practicum.project.playlistmaker.settings.domain

class SettingsInteractorImpl(private val repository: SettingsRepository): SettingsInteractor {
    override fun getTheme(): Boolean {
        return repository.getTheme()
    }

    override fun switchTheme(darkThemeEnabled: Boolean) {
        repository.switchTheme(darkThemeEnabled)
    }
}