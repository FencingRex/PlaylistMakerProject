package com.practicum.project.playlistmaker.sharing.model

data class NotificationData(
    val sendTo: String,
    val mailSubject: String,
    val mailBody: String
)
