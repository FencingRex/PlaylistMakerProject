package com.practicum.project.playlistmaker.sharing.domain

import android.content.Intent
import com.practicum.project.playlistmaker.sharing.ExternalNavigator
import com.practicum.project.playlistmaker.sharing.model.NotificationData
import com.practicum.project.playlistmaker.sharing.model.ShareData
import com.practicum.project.playlistmaker.sharing.model.TermsData

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
    private val repository: SharingRepository
    ): SharingInteractor {
    override fun shareApp(): Intent {
        return  externalNavigator.shareApp(getShareData())
    }

    override fun openTermsLink(): Intent {
        return externalNavigator.openTermsLink(getTermsAgreementData())
    }

    override fun openSupport(): Intent {
        return  externalNavigator.getNotificationData(getNotificationData())
    }

    private fun getShareData(): ShareData{
        return ShareData(repository.getShareMessage(),repository.getChooserMessage())
    }
    private fun getNotificationData(): NotificationData {
        return NotificationData(
            repository.getSupportEmail(),
            repository.getSupportSubject(),
            repository.getSupportMessage()
        )
    }

    private fun getTermsAgreementData(): TermsData {
        return TermsData(repository.getUserAgreementUrl())
    }
}