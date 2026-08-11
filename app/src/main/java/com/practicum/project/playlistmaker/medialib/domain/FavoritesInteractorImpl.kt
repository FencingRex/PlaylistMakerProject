package com.practicum.project.playlistmaker.medialib.domain

import com.practicum.project.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class FavoritesInteractorImpl(
    private val favoritesRepository: FavoritesRepository
): FavoritesInteractor {
    override suspend fun addToFavorites(track: Track) {
        favoritesRepository.addToFavorites(track)
    }

    override suspend fun deleteFromFavorites(track: Track) {
        favoritesRepository.deleteFromFavorites(track)
    }

    override suspend fun getFavorites(): Flow<List<Track>> {
        return favoritesRepository.getFavorites()
    }

    override suspend fun isFavorite(trackId: Int): Boolean {
        return favoritesRepository.isFavorite(trackId)
    }
}