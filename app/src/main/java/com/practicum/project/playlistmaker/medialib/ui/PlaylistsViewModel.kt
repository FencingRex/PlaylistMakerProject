package com.practicum.project.playlistmaker.medialib.ui

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModel
import com.practicum.project.playlistmaker.medialib.domain.PlaylistInteractor
import com.practicum.project.playlistmaker.medialib.model.Playlist
import com.practicum.project.playlistmaker.medialib.model.PlaylistState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlaylistsViewModel(private val playlistInteractor: PlaylistInteractor): ViewModel() {
    private val playlistLiveData = MutableLiveData<PlaylistState>()
    private val _savedCoverPath = MutableLiveData<String?>()
    private var isClickAllowed = true
    val savedCoverPath: LiveData<String?> = _savedCoverPath
    val playlistState: LiveData<PlaylistState> = playlistLiveData

    init {
        viewModelScope.launch {
            getPlaylists()
        }
    }

    fun createPlaylist(name: String, description: String) {
        val coverUri = _savedCoverPath.value.orEmpty()
        Log.d("Cover path","${coverUri}")
        viewModelScope.launch {
            playlistInteractor.addPlaylist(
                Playlist(
                    name = name,
                    description = description,
                    coverUri = coverUri,
                    tracksQty = 0
                )
            )
        }
    }

    fun getPlaylists() {
        viewModelScope.launch {
            playlistInteractor.getAllPlaylists().collect {
                if (it.isEmpty()) setState(PlaylistState.Empty) else setState(
                    PlaylistState.Content(
                        it
                    )
                )
            }
        }
    }
    private fun setState(state: PlaylistState) {
        playlistLiveData.postValue(state)
    }

    fun saveCover(uri: Uri) {
        viewModelScope.launch {
            val path = playlistInteractor.saveCover(uri)
            _savedCoverPath.postValue(path)
        }
    }
    fun clickDebounce(playlist: Playlist): Boolean {
        val currentClick = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewModelScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return currentClick
    }
    companion object{
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}