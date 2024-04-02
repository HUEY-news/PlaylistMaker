package com.practicum.playlistmaker.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTrackToFavoriteList(trackEntity: TrackEntity)
    @Delete
    suspend fun removeTrackFromFavoriteList(trackEntity: TrackEntity)
    @Query("SELECT * FROM track_table")
    suspend fun getFavoriteTrackList(): List<TrackEntity>
    @Query("SELECT trackId FROM track_table")
    suspend fun getFavoriteIdList(): List<Int>
}