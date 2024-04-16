package com.practicum.playlistmaker.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun addItem(playlistEntity: PlaylistEntity)
    @Delete suspend fun removeItem(playlistEntity: PlaylistEntity)
    @Update suspend fun updateItem(playlistEntity: PlaylistEntity)
    @Query("SELECT * FROM playlist_table WHERE playlistId = :id") suspend fun getItem(id: Int): PlaylistEntity
    @Query("SELECT * FROM playlist_table") suspend fun getAllItems(): List<PlaylistEntity>
    @Query("DELETE FROM playlist_table") suspend fun deleteAllItems()
}