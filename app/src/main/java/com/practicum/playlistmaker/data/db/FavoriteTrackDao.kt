package com.practicum.playlistmaker.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteTrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun addTrackToFavoriteList(favoriteTrackEntity: FavoriteTrackEntity)
    @Delete suspend fun removeTrackFromFavoriteList(favoriteTrackEntity: FavoriteTrackEntity)
    @Query("SELECT * FROM favorite_track_table") suspend fun getFavoriteTrackList(): List<FavoriteTrackEntity>
    @Query("SELECT trackId FROM favorite_track_table") suspend fun getFavoriteIdList(): List<Int>
}