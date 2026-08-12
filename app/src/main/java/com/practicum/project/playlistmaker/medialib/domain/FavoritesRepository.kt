package com.practicum.project.playlistmaker.medialib.domain

import com.practicum.project.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    suspend fun addToFavorites(track: Track)
    suspend fun deleteFromFavorites(track: Track)
    suspend fun getFavorites(): Flow<List<Track>>
    suspend fun isFavorite(trackId:Int): Boolean
}