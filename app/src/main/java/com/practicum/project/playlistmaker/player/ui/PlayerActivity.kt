package com.practicum.project.playlistmaker.player.ui

import android.content.Context
//import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.IntentCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.project.playlistmaker.creator.Creator
import com.practicum.project.playlistmaker.R
import com.practicum.project.playlistmaker.search.domain.models.Track
private const val UPDATE_TIME_INTERVAL = 500L
class PlayerActivity: AppCompatActivity() {
    private lateinit var txTrack: TextView
    private lateinit var txArtistName : TextView
    private lateinit var txTrackDuration: TextView
    private lateinit var txAlbumName: TextView
    private lateinit var txTrackYear: TextView
    private lateinit var txTrackGenre: TextView
    private lateinit var txOriginCountry: TextView
    private lateinit var albumCover: ImageView
    private lateinit var play: ImageButton
    private lateinit var sampleUrl: String
    private var mainThreadHandler: Handler? = null
    private var secondsLeftTextView: TextView? = null
    private val playerInteractor = Creator.providePlayerInteractor()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        enableEdgeToEdge()

        mainThreadHandler = Handler(Looper.getMainLooper())
        secondsLeftTextView = findViewById(R.id.playbackDuration)

        val track = IntentCompat.getParcelableExtra<Track>(intent,"Track", Track::class.java)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.playerActivity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val playerBack = findViewById<Toolbar>(R.id.toolbar)

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

        play = findViewById(R.id.playButton)


        txTrack.text = track?.trackName.toString()
        txArtistName.text = track?.artistName.toString()
        txTrackDuration.text = track?.formatedTime.toString()
        txAlbumName.text = track?.collectionName.toString()
        txTrackYear.text = track?.releaseDate?.take(4)
        txTrackGenre.text = track?.primaryGenreName.toString()
        txOriginCountry.text = track?.country.toString()

        sampleUrl = track?.previewUrl.toString()
        secondsLeftTextView?.text = "00:00"

        val radiusInPx = 2.dpToPx(albumCover.context)

        Glide.with(albumCover)
            .load(track?.coverArtwork)
            .placeholder(R.drawable.ic_album_cover_placeholder_light_312)
            .fitCenter()
            .transform(RoundedCorners(radiusInPx))
            .into(albumCover)
        preparePlayer()
        play.setOnClickListener { playbackControl() }
    }
    private fun playbackControl(){
        playerInteractor.playbackControl(
            {play.setImageResource(R.drawable.ic_button_pause_100)
                mainThreadHandler?.post(startTimer())

            },
            {
                play.setImageResource(R.drawable.ic_button_play_100)
                mainThreadHandler?.removeCallbacks(startTimer())
                secondsLeftTextView?.text = "00:00"
            }
        )
    }
   private fun preparePlayer(){
        playerInteractor.preparePlayer(
            url = sampleUrl,
            onPrepared = {play.isEnabled = true},
            onCompletion = {
                secondsLeftTextView?.text = "00:00"
                mainThreadHandler?.removeCallbacks(startTimer())
            })
        }
    private fun startTimer() = object : Runnable {
        override fun run(){
            if (playerInteractor.isPlaying()){
                secondsLeftTextView?.text = playerInteractor.formatTime(playerInteractor.getCurrentPosition().toLong()) //formatTime(currentElapsedTime)
                mainThreadHandler?.postDelayed(this,UPDATE_TIME_INTERVAL)
            }
        }
    }
        override fun onPause() {
            super.onPause()
            playerInteractor.pausePlayer()
            mainThreadHandler?.removeCallbacksAndMessages(null)
        }
        override fun onDestroy() {
            super.onDestroy()
            playerInteractor.releasePlayer()
            mainThreadHandler?.removeCallbacksAndMessages(null)
        }
}

