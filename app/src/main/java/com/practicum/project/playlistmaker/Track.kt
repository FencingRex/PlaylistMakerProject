package com.practicum.project.playlistmaker
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Locale

@Parcelize
data class Track(
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String
): Parcelable {
    val formatedTime: String get() = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)
    val coverArtwork: String get() = artworkUrl100.replaceAfterLast("/", "512x512bb.jpg")
}