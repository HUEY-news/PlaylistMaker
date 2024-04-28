package com.practicum.playlistmaker.presentation.library

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.library.Playlist
import com.practicum.playlistmaker.domain.library.PlaylistInteractor
import com.practicum.playlistmaker.domain.search.Track
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor
): ViewModel() {

    private val currentPlaylist = MutableLiveData<Playlist>()
    fun observeCurrentPlaylist(): LiveData<Playlist> = currentPlaylist

    private val currentPlaylistTracks = MutableLiveData<List<Track>>()
    fun observeCurrentPlaylistTracks(): LiveData<List<Track>> = currentPlaylistTracks

    private val currentPlaylistInfo = MutableLiveData<Pair<String, String>>()
    fun observeCurrentPlaylistInfo(): LiveData<Pair<String, String>> = currentPlaylistInfo

    private val hasCurrentPlaylistContent = MutableLiveData<Boolean>()
    fun observeIfCurrentPlaylistHasContent(): LiveData<Boolean> = hasCurrentPlaylistContent

    private val showEmptyMessage = MutableLiveData<Boolean>()
    fun observeIfEmptyMessageShow(): LiveData<Boolean> = showEmptyMessage

    fun getPlaylist(playlistId: Int) {
        viewModelScope.launch {
            playlistInteractor
                .getPlaylistFromLibrary(playlistId)
                .collect { playlist ->
                    currentPlaylist.postValue(playlist)
                    getPlaylistTracks(playlist.tracksIdentifiers)
                }
        }
    }

    private fun getPlaylistTracks(idList: String) {
        viewModelScope.launch {
            playlistInteractor
                .getTracksFromPlaylist(idList)
                .collect { trackList ->
                    currentPlaylistTracks.postValue(trackList)
                    getPlaylistInfo(trackList)
                    hasCurrentPlaylistContent.postValue(trackList.isNotEmpty())
                }
        }
    }

    private fun getPlaylistInfo(trackList: List<Track>) {
        var durationSum = 0
        trackList.forEach { track -> durationSum += track.trackTimeMillis }
        val durationMinutes = SimpleDateFormat("mm", Locale.getDefault()).format(durationSum).toInt()
        val pair = Pair(declensionMinutesByCase(durationMinutes), declensionTracksByCase(trackList.size))
        currentPlaylistInfo.postValue(pair)
    }

    private fun declensionMinutesByCase(minutesCount: Int): String {
        val preLastDigit = minutesCount % 100 / 10
        if (preLastDigit == 1) return "$minutesCount минут"

        return when (minutesCount % 10) {
            1 -> "$minutesCount минута"
            2 -> "$minutesCount минуты"
            3 -> "$minutesCount минуты"
            4 -> "$minutesCount минуты"
            else -> "$minutesCount минут"
        }
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

    fun removePlaylistFromLibrary(playlistId: Int) {
        viewModelScope.launch {
            playlistInteractor.removePlaylistFromLibrary(playlistId)
        }
    }

    fun removeTrackFromPlaylist(track: Track, playlistId: Int) {
        viewModelScope.launch {
            playlistInteractor.removeTrackFromPlaylist(track, playlistId)
            getPlaylist(playlistId)
        }
    }

    fun sharePlaylist(playlistId: Int) {
        viewModelScope.launch {
            if (hasCurrentPlaylistContent.value == false) {
                showEmptyMessage.postValue(true)
                delay(CHECK_DELAY)
                showEmptyMessage.postValue(false)
            } else playlistInteractor.sharePlaylist(playlistId)
        }
    }

    companion object{
        const val CHECK_DELAY = 300L
    }
}