package com.practicum.project.playlistmaker.medialib.model

import com.practicum.project.playlistmaker.search.domain.models.Track

sealed interface FavoritesState{
    data class Content(val tracks: List<Track>): FavoritesState
    object Empty: FavoritesState
}
