package com.practicum.project.playlistmaker.sharing.domain

import android.content.Intent

interface SharingInteractor {
    fun shareApp(): Intent
    fun openTermsLink(): Intent
    fun openSupport(): Intent
    fun sharePlaylist(message: String, title: String)
}