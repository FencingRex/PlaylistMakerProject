package com.practicum.project.playlistmaker.settings.data

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.project.playlistmaker.settings.domain.SettingsRepository

class SettingsRepositoryImpl(private val sharedPreferences: SharedPreferences): SettingsRepository {
    override fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled){
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        sharedPreferences.edit()
            .putBoolean(THEME_VALUE_KEY,darkThemeEnabled)
            .apply()
    }

    override fun getTheme(): Boolean {
        return sharedPreferences.getBoolean(THEME_VALUE_KEY,false)
    }
    companion object{
        const val SAVED_THEME_STATE = "savedThemeValue"
        const val THEME_VALUE_KEY = "false"
    }
}