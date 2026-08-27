package com.practicum.project.playlistmaker.playlist.ui


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.project.playlistmaker.medialib.data.converters.PlaylistTrackConverter
import com.practicum.project.playlistmaker.medialib.domain.PlaylistInteractor
import com.practicum.project.playlistmaker.playlist.model.PlaylistEditState
import com.practicum.project.playlistmaker.search.domain.models.Track
import com.practicum.project.playlistmaker.sharing.domain.SharingInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import com.practicum.project.playlistmaker.medialib.data.converters.TrackDbConverter
import kotlinx.coroutines.launch
import java.util.Locale
import java.text.SimpleDateFormat
class PlaylistViewModel(
    private val playlistId: Long,
    private val playlistInteractor: PlaylistInteractor,
    private val sharingInteractor: SharingInteractor
) : ViewModel() {
    private val playlistStateLiveData = MutableLiveData<PlaylistEditState>()
    val playlistLiveData: LiveData<PlaylistEditState> = playlistStateLiveData
    private var isClickAllowed = true

    init {
        viewModelScope.launch {
            getPlaylistData()
        }
    }
    fun getPlaylistData(){
        viewModelScope.launch {
            playlistInteractor.getTracksFromPlaylist(playlistId).collect { playlistsWithTracks ->
                playlistsWithTracks?.let { data ->
                    val domainTracks = PlaylistTrackConverter.toDomainList(data.tracks)
                    val totalInMillis = data.tracks.sumOf { it.trackTimeMillis }
                    val formattedTime = SimpleDateFormat("mm", Locale.getDefault())
                        .format(totalInMillis)
                    Log.d("qty test","${data.tracks.size}")
                    playlistStateLiveData.postValue(
                        PlaylistEditState(
                            image = data.playlist.coverURI,
                            name = data.playlist.name,
                            description = data.playlist.description,
                            tracksCount = domainTracks.size,
                            totalDurationTracks = formattedTime,
                            tracks = domainTracks
                        )
                    )
                }
            }
        }
    }
    
    fun clickDebounce(track: Track): Boolean{
        val currentClick = isClickAllowed
        if (isClickAllowed){
            isClickAllowed = false
            viewModelScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return currentClick
    }
    fun deleteTrack(trackId: Int) {
        viewModelScope.launch {
            playlistInteractor.deleteTrack(playlistId, trackId)
            getPlaylistData()
        }
    }

    fun sharePlaylist(message: String, title: String) {
        sharingInteractor.sharePlaylist(message, title)
    }

    fun deletePlaylist() {
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.deletePlaylist(playlistId)
        }
    }
    companion object{
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}
