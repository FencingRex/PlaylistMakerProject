package com.practicum.project.playlistmaker.data

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.project.playlistmaker.Creator
import com.practicum.project.playlistmaker.domain.api.SettingsInteractor

class App: Application(){
    var darkTheme = false
    private lateinit var settingsInteractor: SettingsInteractor
    override fun onCreate() {
        super.onCreate()

        Creator.setApp(application = this)
        settingsInteractor = Creator.provideSettingsInteractor()
        val sharedPrefs = getSharedPreferences(SAVED_THEME_STATE,MODE_PRIVATE)

        darkTheme =sharedPrefs.getBoolean(THEME_VALUE_KEY,false)

        switchTheme(darkTheme)

    }
    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled){
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        getSharedPreferences(SAVED_THEME_STATE,MODE_PRIVATE)
            .edit()
            .putBoolean(THEME_VALUE_KEY,darkThemeEnabled)
            .apply()
    }
    companion object{
        const val SAVED_THEME_STATE = "savedThemeValue"
        const val THEME_VALUE_KEY = "false"
    }
}