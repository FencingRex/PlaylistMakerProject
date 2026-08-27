package com.practicum.project.playlistmaker.sharing.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.practicum.project.playlistmaker.sharing.ExternalNavigator
import com.practicum.project.playlistmaker.sharing.model.NotificationData
import com.practicum.project.playlistmaker.sharing.model.ShareData
import com.practicum.project.playlistmaker.sharing.model.TermsData

class ExternalNavigatorImpl( private val context: Context): ExternalNavigator {
    override fun shareApp(shareData: ShareData): Intent {
        return Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareData.shareUrl)
        }
    }

    override fun getNotificationData(notifyData: NotificationData): Intent {
        return Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, notifyData.sendTo)
            putExtra(Intent.EXTRA_SUBJECT, notifyData.mailSubject)
            putExtra(Intent.EXTRA_TEXT, notifyData.mailBody)
        }
    }
    override fun openTermsLink(termsData: TermsData): Intent {
        val url = Uri.parse(termsData.termsUrl)
        return Intent(Intent.ACTION_VIEW, url)
    }

    override fun sharePlaylist(message: String, title: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT,message)
        }
        val chooser = Intent.createChooser(intent,title)
        chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooser)
    }
}
