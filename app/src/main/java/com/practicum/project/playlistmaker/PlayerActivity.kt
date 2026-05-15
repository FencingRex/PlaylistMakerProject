package com.practicum.project.playlistmaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.content.IntentCompat
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class PlayerActivity: AppCompatActivity() {
    private lateinit var txTrack: TextView
    private lateinit var txArtistName : TextView
    private lateinit var txTrackDuration: TextView
    private lateinit var txAlbumName: TextView
    private lateinit var txTrackYear: TextView
    private lateinit var txTrackGenre: TextView
    private lateinit var txOriginCountry: TextView
    private lateinit var albumCover: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        enableEdgeToEdge()

        val track = IntentCompat.getParcelableExtra<Track>(intent,"Track", Track::class.java)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.playerActivity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val playerBack = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        Log.d("track Value","$track")

        playerBack.setOnClickListener { finish() }

       fun Int.dpToPx(context: Context): Int {
           return (this * context.resources.displayMetrics.density).toInt()
       }
        txTrack = findViewById(R.id.trackName)
        txArtistName = findViewById(R.id.artistName)
        txTrackDuration = findViewById(R.id.trackDurationValue)
        txAlbumName = findViewById(R.id.albumNameValue)
        txTrackYear = findViewById(R.id.trackYearValue)
        txTrackGenre = findViewById(R.id.trackGenreValue)
        txOriginCountry = findViewById(R.id.countryValue)
        albumCover = findViewById(R.id.albumCover)

        txTrack.text = track?.trackName.toString()
        txArtistName.text = track?.artistName.toString()
        txTrackDuration.text = track?.formatedTime.toString()
        txAlbumName.text = track?.collectionName.toString()
        txTrackYear.text = track?.releaseDate?.take(4)
        txTrackGenre.text = track?.primaryGenreName.toString()
        txOriginCountry.text = track?.country.toString()

        val radiusInPx = 2.dpToPx(albumCover.context)

        Glide.with(albumCover)
            .load(track?.coverArtwork)
            .placeholder(R.drawable.ic_album_cover_placeholder_light_312)
            .fitCenter()
            .transform(RoundedCorners(radiusInPx))
            .into(albumCover)

    }
}