package com.practicum.project.playlistmaker.medialib.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String,
    val coverURI: String,
    val tracksID: String,
    val tracksQty: Int,
    var timestamp: Long = System.currentTimeMillis()
)
