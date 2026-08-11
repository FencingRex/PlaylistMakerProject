package com.practicum.project.playlistmaker.medialib.data.db

import com.practicum.project.playlistmaker.medialib.data.converters.TrackDbConverter
import com.practicum.project.playlistmaker.medialib.data.db.entity.TrackEntity
import com.practicum.project.playlistmaker.medialib.domain.FavoritesRepository
import com.practicum.project.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoritesRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val converter: TrackDbConverter
): FavoritesRepository {
    override suspend fun addToFavorites(track: Track) {

        appDatabase.trackDao().insertTrack(converter.mapToEntity(track))
    }

    override suspend fun deleteFromFavorites(track: Track) {
        appDatabase.trackDao().deleteTrack(converter.mapToEntity(track))
    }

    override suspend fun getFavorites(): Flow<List<Track>> {
        return appDatabase.trackDao().getTrackList().map { tracks ->
            convertFromEntity(tracks)
        }
    }

    override suspend fun isFavorite(trackId: Int): Boolean{
       return appDatabase.trackDao().isFavorite(trackId)
    }
    private fun convertFromEntity(trackEntity: List<TrackEntity>): List<Track>{
        return trackEntity.map {trackEntity -> converter.mapToTrack(trackEntity)}
    }

}