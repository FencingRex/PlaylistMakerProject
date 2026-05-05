package com.practicum.project.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate


class App: Application (){
    var darkTheme = false
    override fun onCreate() {
        super.onCreate()

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