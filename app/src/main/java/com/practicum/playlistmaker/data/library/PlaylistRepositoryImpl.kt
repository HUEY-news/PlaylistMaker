package com.practicum.playlistmaker.data.library

import android.net.Uri
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.convertor.DbConvertor
import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.data.db.PlaylistEntity
import com.practicum.playlistmaker.data.db.SavedTrackEntity
import com.practicum.playlistmaker.data.settings.ExternalNavigator
import com.practicum.playlistmaker.domain.library.Playlist
import com.practicum.playlistmaker.domain.library.PlaylistRepository
import com.practicum.playlistmaker.domain.search.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class PlaylistRepositoryImpl @Inject constructor(
    private val database: AppDatabase,
    private val convertor: DbConvertor,
    private val navigator: ExternalNavigator,
    private val gson: Gson
): PlaylistRepository {

    override suspend fun createNewPlaylist(name: String, description: String, cover: Uri?) {
        val playlistEntity = PlaylistEntity(
            playlistName = name,
            playlistDescription = description,
            playlistCoverUri = cover?.toString(),
            tracksIdentifiers = gson.toJson(emptyList<Int>()),
            numberOfTracks = 0
        )
        database.playlistDao().addItem(playlistEntity)
    }

    override suspend fun removePlaylistFromLibrary(playlistId: Int) {
        database.playlistDao().deletePlaylist(playlistId)
    }

    override suspend fun updatePlaylist(track: Track, playlist: Playlist) {
        val idList = createIdListFromJson(playlist.tracksIdentifiers)
        idList.add(0, track.trackId)
        val idListString = createJsonFromIdList(idList)
        val updatedPlaylist = playlist.copy(tracksIdentifiers = idListString, numberOfTracks = idList.size)
        val playlistEntity = convertor.map(updatedPlaylist)
        database.playlistDao().updateItem(playlistEntity)
    }

    override suspend fun updatePlaylist(id: Int, name: String, description: String, cover: Uri?) {
        val playlistEntity = database.playlistDao().getItem(id)
        val playlist = convertor.map(playlistEntity)
        val updatedPlaylist = playlist.copy(
            playlistName = name,
            playlistDescription = description,
            playlistCoverUri = cover.toString()
        )

        database.playlistDao().updateItem(convertor.map(updatedPlaylist))
    }

    override suspend fun deleteAllPlaylistsFromLibrary() {
        database.playlistDao().deleteAllItems()
    }

    override fun getPlaylistFromLibrary(id: Int): Flow<Playlist> = flow {
        val playlistEntity = database.playlistDao().getItem(id)
        val playlist = convertor.map(playlistEntity)
        emit(playlist)
    }

    override fun getAllPlaylistsFromLibrary(): Flow<List<Playlist>> = flow {
        val playlistEntityList = database.playlistDao().getAllItems()
        val itemList = convertFromPlaylistEntity(playlistEntityList.sortedByDescending { it.playlistId })
        emit(itemList)
    }

    override fun checkIfPlaylistContainsTrack(track: Track, playlist: Playlist): Boolean {
        val idList = createIdListFromJson(playlist.tracksIdentifiers)
        val id = track.trackId
        return (idList.contains(id))
    }

    override suspend fun addTrackToSavedList(track: Track) {
        val trackEntity = convertor.mapTrackToSaved(track)
        database.savedTrackDao().addItem(trackEntity)
    }

    override suspend fun removeTrackFromSavedList(track: Track) {
        val trackEntity = convertor.mapTrackToSaved(track)
        database.savedTrackDao().removeItem(trackEntity)
    }

    override suspend fun removeTrackFromPlaylist(track: Track, playlistId: Int) {
        val oldPlaylistEntity = database.playlistDao().getItem(playlistId)
        val oldPlaylist = convertor.map(oldPlaylistEntity)
        val oldIdListInt = createIdListFromJson(oldPlaylist.tracksIdentifiers)
        val newIdListInt = oldIdListInt.filterNot { id -> id == track.trackId }
        val newIdListString = createJsonFromIdList(newIdListInt)
        val newPlaylist = oldPlaylist.copy(tracksIdentifiers = newIdListString, numberOfTracks = newIdListInt.size)
        val newPlaylistEntity = convertor.map(newPlaylist)
        database.playlistDao().updateItem(newPlaylistEntity)

        var isExistAnywhere = false
        val playlistLibrary = database.playlistDao().getAllItems()
        playlistLibrary.forEach { playlist ->
            val idList = createIdListFromJson(playlist.tracksIdentifiers)
            if (idList.contains(track.trackId)) {
                isExistAnywhere = true
                return@forEach
            }
        }
        if (!isExistAnywhere) {
            val trackEntity = convertor.mapTrackToSaved(track)
            database.savedTrackDao().removeItem(trackEntity)
        }
    }

    override fun getTracksFromPlaylist(idListString: String): Flow<List<Track>> = flow {
        val savedTrackEntityList = database.savedTrackDao().getAllItems()
        val savedTrackList = convertFromSavedTrackEntity(savedTrackEntityList.sortedByDescending { it.addingTime })
        val idListInt = createIdListFromJson(idListString)
        val filteredTrackList = savedTrackList.filter { track -> idListInt.contains(track.trackId) }
        val favoriteIdList = database.favoriteTrackDao().getFavoriteIdList()
        for (track in filteredTrackList) track.isFavorite = favoriteIdList.contains(track.trackId)
        emit(filteredTrackList)
    }

    override suspend fun sharePlaylist(playlistId: Int) {
        var sharedText = ""
        val playlistEntity = database.playlistDao().getItem(playlistId)
        val trackList = getTracksFromPlaylist(playlistEntity.tracksIdentifiers)
        trackList.collect { sharedText = createSharedText(playlistEntity, it) }
        navigator.sharePlaylist(sharedText)
    }

    private fun createSharedText(playlistEntity: PlaylistEntity, trackList: List<Track>): String {

        var index = 1

        var text = "${ playlistEntity.playlistName }\n" +
                   "${ playlistEntity.playlistDescription }\n" +
                   "${ declensionTracksByCase(trackList.size) }"

        trackList.forEach {
            text += "\n${index}.${ it.artistName } - ${ it.trackName } ${ SimpleDateFormat("mm:ss", Locale.getDefault()).format(it.trackTimeMillis.toLong()) }"
            index++
        }
        return text
    }

    private fun declensionTracksByCase(tracksCount: Int): String {
        val preLastDigit = tracksCount % 100 / 10
        if (preLastDigit == 1) return "$tracksCount треков"

        return when (tracksCount % 10) {
            1 -> "$tracksCount трек"
            2 -> "$tracksCount трека"
            3 -> "$tracksCount трека"
            4 -> "$tracksCount трека"
            else -> "$tracksCount треков"
        }
    }

    private fun convertFromPlaylistEntity(playlistEntityList: List<PlaylistEntity>): List<Playlist> {
        return playlistEntityList.map { playlistEntity -> convertor.map(playlistEntity) }
    }

    private fun convertFromSavedTrackEntity(trackEntityList: List<SavedTrackEntity>): List<Track> {
        return trackEntityList.map { trackEntity -> convertor.mapSavedToTrack(trackEntity) }
    }

    private fun createIdListFromJson(json: String): ArrayList<Int> {
        val listType = object : TypeToken<ArrayList<Int>>() {}.type
        return gson.fromJson(json, listType)
    }

    private fun createJsonFromIdList(idList: List<Int>): String {
        return gson.toJson(idList)
    }
}
