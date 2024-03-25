package com.practicum.playlistmaker.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TrackDao {
    @Insert(entity = TrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavouriteList(track: TrackEntity)
    @Delete(entity = TrackEntity::class)
    suspend fun removeFromFavouriteList(track: TrackEntity)
    @Query("SELECT * FROM track_table")
    suspend fun getFavouriteTrackList(): List<TrackEntity>
    @Query("SELECT trackId FROM track_table")
    suspend fun getFavouriteIdList(): List<Int>
}