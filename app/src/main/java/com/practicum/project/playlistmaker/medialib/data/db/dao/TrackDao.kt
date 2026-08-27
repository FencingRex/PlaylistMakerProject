package com.practicum.project.playlistmaker.medialib.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.project.playlistmaker.medialib.data.db.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)

    @Delete
    suspend fun deleteTrack(track: TrackEntity)

    @Query("select * from track_table order by timestamp DESC")
    fun getTrackList(): Flow<List<TrackEntity>>

    @Query("select trackId from track_table order by timestamp DESC")
    suspend fun getTracksId(): List<Int>

    @Query("SELECT * FROM track_table WHERE trackId IN (:ids)")
    suspend fun getTracksByIds(ids: List<Int>): List<TrackEntity>

    @Query("select COUNT(*) from track_table where trackId = :trackId")
    suspend fun isFavorite(trackId: Int): Boolean
}