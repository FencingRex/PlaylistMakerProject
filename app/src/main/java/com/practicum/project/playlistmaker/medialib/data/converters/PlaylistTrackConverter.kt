package com.practicum.project.playlistmaker.medialib.data.converters

import com.practicum.project.playlistmaker.medialib.data.db.entity.PlaylistTracks
import com.practicum.project.playlistmaker.search.domain.models.Track

object PlaylistTrackConverter {
    fun toDomain(playlistTrack: PlaylistTracks): Track {
        return Track(
            trackId = playlistTrack.trackId,
            trackName = playlistTrack.trackName,
            artistName = playlistTrack.artistName,
            trackTimeMillis = playlistTrack.trackTimeMillis,
            artworkUrl100 = playlistTrack.artworkUrl100,
            collectionName = playlistTrack.collectionName,
            releaseDate = playlistTrack.releaseDate,
            primaryGenreName = playlistTrack.primaryGenreName,
            country = playlistTrack.country,
            previewUrl = playlistTrack.previewUrl,
            isFavorite = playlistTrack.isFavorite,
            timestamp = playlistTrack.timestamp
        )
    }

    fun toDomainList(playlistTracks: List<PlaylistTracks>): List<Track> {
        return playlistTracks.map { toDomain(it) }
    }

    fun fromDomain(track: Track, playlistId: Long): PlaylistTracks {
        return PlaylistTracks(
            playlistId = playlistId,
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl100 = track.artworkUrl100,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl,
            isFavorite = track.isFavorite,
            timestamp = track.timestamp
        )
    }
}