package com.practicum.project.playlistmaker.medialib.data.converters

import com.practicum.project.playlistmaker.medialib.data.db.entity.TrackEntity
import com.practicum.project.playlistmaker.search.domain.models.Track

class TrackDbConverter {
    fun mapToEntity(track: Track): TrackEntity{
        return TrackEntity(track.trackId,track.trackName,track.artistName,track.trackTimeMillis,track.artworkUrl100,track.collectionName,track.releaseDate,track.primaryGenreName,track.country,track.previewUrl,timestamp=System.currentTimeMillis())
    }
    fun mapToTrack(track: TrackEntity): Track{
            return Track(track.trackId,track.trackName,track.artistName,track.trackTimeMillis,track.artworkUrl100,track.collectionName,track.releaseDate,track.primaryGenreName,track.country,track.previewUrl,isFavorite=true)
    }
}