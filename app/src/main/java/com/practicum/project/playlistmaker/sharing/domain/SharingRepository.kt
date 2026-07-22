package com.practicum.project.playlistmaker.sharing.domain

interface SharingRepository {
    fun getShareMessage(): String
    fun getSupportEmail(): String
    fun getSupportSubject(): String
    fun getSupportMessage(): String
    fun getUserAgreementUrl(): String
    fun getChooserMessage(): String
}