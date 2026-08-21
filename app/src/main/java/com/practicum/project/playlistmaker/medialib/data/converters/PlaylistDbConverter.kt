package com.practicum.project.playlistmaker.medialib.data.converters

import com.practicum.project.playlistmaker.medialib.data.db.entity.PlaylistEntity
import com.practicum.project.playlistmaker.medialib.model.Playlist

class PlaylistDbConverter {
    fun toEntity(playlist: Playlist): PlaylistEntity{
        return PlaylistEntity(playlist.id,playlist.name,playlist.description,playlist.coverUri,playlist.tracksId,playlist.tracksQty)
    }
    fun fromEntity(playlist: PlaylistEntity): Playlist{
        return Playlist(playlist.id,playlist.name,playlist.description,playlist.coverURI,playlist.tracksID,playlist.tracksQty)
    }
}