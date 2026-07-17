package com.practicum.project.playlistmaker.data

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.project.playlistmaker.data.App.Companion.SAVED_THEME_STATE
import com.practicum.project.playlistmaker.data.App.Companion.THEME_VALUE_KEY
import com.practicum.project.playlistmaker.domain.api.SettingsRepository
private val darkThemeEnabled: Boolean = false
class SettingsRepositoryImpl(private val sharedPreferences: SharedPreferences): SettingsRepository {
    override fun switchTheme(darkThemeEnabled: Boolean) {
        //val darkTheme = darkThemeEnabled
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
}