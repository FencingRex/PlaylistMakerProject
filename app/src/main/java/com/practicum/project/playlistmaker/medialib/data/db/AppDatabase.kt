package com.practicum.project.playlistmaker.medialib.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.project.playlistmaker.medialib.data.db.dao.PlaylistsDao
import com.practicum.project.playlistmaker.medialib.data.db.dao.TrackDao
import com.practicum.project.playlistmaker.medialib.data.db.entity.PlaylistEntity
import com.practicum.project.playlistmaker.medialib.data.db.entity.PlaylistTracks
import com.practicum.project.playlistmaker.medialib.data.db.entity.TrackEntity

@Database(version = 5,
    entities = [TrackEntity::class,
                PlaylistEntity::class,
                PlaylistTracks::class])
abstract class AppDatabase: RoomDatabase() {
    abstract fun trackDao(): TrackDao
    abstract fun playlistsDao() : PlaylistsDao
}