package com.practicum.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database (
    version = 1,
    entities = [
        FavoriteTrackEntity::class,
        SavedTrackEntity::class,
        PlaylistEntity::class,
    ]
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun favoriteTrackDao(): FavoriteTrackDao
    abstract fun savedTrackDao(): SavedTrackDao
    abstract fun playlistDao(): PlaylistDao
}