package com.practicum.project.playlistmaker.sharing.data

import android.content.Context
import com.practicum.project.playlistmaker.R
import com.practicum.project.playlistmaker.sharing.domain.SharingRepository

class SharingRepositoryImpl(private val context: Context): SharingRepository {
    override fun getShareMessage(): String = context.getString(R.string.shareMsg)
    override fun getSupportEmail(): String = context.getString(R.string.supportMailTo)
    override fun getSupportSubject(): String = context.getString(R.string.supportMailSubject)
    override fun getSupportMessage(): String = context.getString(R.string.supportMailBody)
    override fun getUserAgreementUrl(): String = context.getString(R.string.userAgreemLink)
    override fun getChooserMessage(): String = context.getString(R.string.chooserMsg)
}