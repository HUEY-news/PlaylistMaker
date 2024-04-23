package com.practicum.playlistmaker.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface SavedTrackDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE) suspend fun addItem(savedTrackEntity: SavedTrackEntity)
}