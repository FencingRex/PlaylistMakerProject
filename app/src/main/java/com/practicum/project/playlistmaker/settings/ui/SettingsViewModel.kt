package com.practicum.project.playlistmaker.settings.ui

import androidx.lifecycle.ViewModel
import com.practicum.project.playlistmaker.settings.domain.SettingsInteractor
import com.practicum.project.playlistmaker.sharing.domain.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor
): ViewModel() {

}