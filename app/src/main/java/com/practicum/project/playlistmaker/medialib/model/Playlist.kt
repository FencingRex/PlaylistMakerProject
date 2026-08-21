package com.practicum.project.playlistmaker.medialib.model

import android.net.Uri

data class Playlist(
    val id: Long = 0,
    val name: String,
    val description: String,
    val coverUri: String,
    val tracksId: String = "[]",
    val tracksQty: Int
)
