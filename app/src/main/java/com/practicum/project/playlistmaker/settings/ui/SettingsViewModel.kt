package com.practicum.project.playlistmaker.settings.ui

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.project.playlistmaker.settings.domain.SettingsInteractor
import com.practicum.project.playlistmaker.sharing.domain.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor
): ViewModel() {
    private val appThemeLiveData = MutableLiveData<Boolean>()

    init {
        appThemeLiveData.value = settingsInteractor.getTheme()
    }

    fun getThemeData(): LiveData<Boolean> = appThemeLiveData

    fun  switchTheme(isDarkEnabled: Boolean){
        settingsInteractor.switchTheme(isDarkEnabled)
        appThemeLiveData.value = isDarkEnabled
    }

    fun shareApp(): Intent {
        return sharingInteractor.shareApp()
    }

    fun openSupport(): Intent{
        return sharingInteractor.openSupport()
    }

    fun openTermsLink(): Intent{
        return sharingInteractor.openTermsLink()
    }
}