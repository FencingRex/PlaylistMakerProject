package com.practicum.project.playlistmaker.sharing

import android.content.Intent
import androidx.appcompat.widget.DialogTitle
import com.practicum.project.playlistmaker.sharing.model.NotificationData
import com.practicum.project.playlistmaker.sharing.model.ShareData
import com.practicum.project.playlistmaker.sharing.model.TermsData

interface ExternalNavigator {
    fun openTermsLink(termsData: TermsData): Intent
    fun getNotificationData(notificationData: NotificationData): Intent
    fun shareApp(shareData: ShareData): Intent
    fun sharePlaylist(message: String,title: String)
}