package com.practicum.project.playlistmaker.editplaylist.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.practicum.project.playlistmaker.medialib.domain.PlaylistInteractor
import com.practicum.project.playlistmaker.medialib.model.Playlist
import com.practicum.project.playlistmaker.medialib.ui.PlaylistsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class EditPlaylistViewModel(
    private val playlistId: Long,
    private val playlistInteractor: PlaylistInteractor
): PlaylistsViewModel(playlistInteractor) {
    private val playlistLiveData = MutableLiveData<Playlist>()
    val playlistLiveDataState: LiveData<Playlist> = playlistLiveData

    init {
        viewModelScope.launch {
            playlistInteractor.getPlaylistById(playlistId).map{ it.firstOrNull()}
                .collect { playlist -> playlistLiveData.postValue(playlist!!) }
        }
    }
    fun playlistUpdate(name: String, description: String) {
        val updateData = playlistLiveData.value?.copy(
            name = name,
            description = description,
            coverUri = savedCoverPath.value.orEmpty()
        ) ?: return

        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.updatePlaylist(updateData)
        }
    }
}