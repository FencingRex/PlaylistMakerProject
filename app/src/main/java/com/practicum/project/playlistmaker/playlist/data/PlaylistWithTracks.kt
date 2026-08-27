package com.practicum.project.playlistmaker.playlist.data

import androidx.room.Embedded
import androidx.room.Relation
import com.practicum.project.playlistmaker.medialib.data.db.entity.PlaylistEntity
import com.practicum.project.playlistmaker.medialib.data.db.entity.PlaylistTracks


data class PlaylistWithTracks(
    @Embedded val playlist: PlaylistEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "playlistId"
        )
    val tracks: List<PlaylistTracks>
)
