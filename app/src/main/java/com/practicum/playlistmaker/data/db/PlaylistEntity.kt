package com.practicum.playlistmaker.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
class PlaylistEntity (
    @PrimaryKey(autoGenerate = true) val playlistId: Int = 0,
    val playlistName: String,
    val playlistDescription: String,
    val playlistCoverUri: String?,
    val tracksIdentifiers: String,
    val numberOfTracks: Int
)