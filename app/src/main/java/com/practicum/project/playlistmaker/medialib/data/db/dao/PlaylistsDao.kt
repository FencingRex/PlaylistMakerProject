package com.practicum.project.playlistmaker.medialib.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.practicum.project.playlistmaker.medialib.data.db.entity.PlaylistEntity
import com.practicum.project.playlistmaker.medialib.model.Playlist
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistsDao {
    @Insert(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPlaylist(playlist: PlaylistEntity)

    @Delete
    suspend fun deletePlaylist(playlist: PlaylistEntity)

    @Query("select * from playlist_table order by timestamp desc")
    fun getAllPlaylists(): Flow<List<PlaylistEntity>>

    @Query("SELECT * FROM playlist_table WHERE id = :playlistId")
    suspend fun getPlaylistById(playlistId: Long): PlaylistEntity?
    @Update
    suspend fun updatePlaylist(playlist: PlaylistEntity)
}