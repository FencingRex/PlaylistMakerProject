package com.practicum.project.playlistmaker.search.ui

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import com.practicum.project.playlistmaker.creator.Creator

class SearchViewModel(application: Application): AndroidViewModel(application) {
    private val handler = Handler(Looper.getMainLooper())

}