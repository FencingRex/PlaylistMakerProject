package com.practicum.project.playlistmaker.presentation

import android.content.Context
import android.media.MediaPlayer
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
import com.practicum.project.playlistmaker.R
import com.practicum.project.playlistmaker.domain.models.Track

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
    private var mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT
    private var mainThreadHandler: Handler? = null
    private var secondsLeftTextView: TextView? = null
    private var timerRunnable: Runnable? = null
    private var totalElapsedTime: Long = 0L
    private var startTime: Long = 0L

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
        preparePlayer()

        val radiusInPx = 2.dpToPx(albumCover.context)

        Glide.with(albumCover)
            .load(track?.coverArtwork)
            .placeholder(R.drawable.ic_album_cover_placeholder_light_312)
            .fitCenter()
            .transform(RoundedCorners(radiusInPx))
            .into(albumCover)

        play.setOnClickListener { playbackControl() }

    }
    private fun playbackControl(){
        when(playerState){
            STATE_PLAYING ->{
                play.setImageResource(R.drawable.ic_button_play_100)
                pausePlayer()
            }
            STATE_PREPARED,STATE_PAUSED ->{
                play.setImageResource(R.drawable.ic_button_pause_100)
                startPlayer()
            }
        }
    }
    private fun preparePlayer(){
        mediaPlayer.setDataSource(sampleUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            play.isEnabled = true
            playerState = STATE_PREPARED
            secondsLeftTextView?.text = "00:00"
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
            secondsLeftTextView?.text = "00:00"
        }
    }
    private fun startPlayer(){
        mediaPlayer.start()
        if (playerState == STATE_PAUSED){
            startTimer(totalElapsedTime)
        } else{
            totalElapsedTime = 0L
            startTimer(totalElapsedTime)
        }
        playerState = STATE_PLAYING
    }
    private fun pausePlayer(){
        mediaPlayer.pause()
        playerState = STATE_PAUSED
        stopTimer()
    }
    private fun formatTime(milliseconds: Long): String {
        val seconds = milliseconds / 1000
        return String.format("%02d:%02d", seconds / 60, seconds % 60)
    }
    private fun startTimer(elapsedTimeBeforePause: Long){
        startTime = System.currentTimeMillis()
        timerRunnable = object : Runnable{
            override fun run(){
                val currentElapsedTime = mediaPlayer.currentPosition.toLong()
                val isPlaying = mediaPlayer.isPlaying
                if (isPlaying && playerState == STATE_PLAYING){
                    secondsLeftTextView?.text = formatTime(currentElapsedTime)
                    mainThreadHandler?.postDelayed(this,UPDATE_TIME_INTERVAL)

                } else if(!isPlaying){
                    secondsLeftTextView?.text = "00:00"
                    play.setImageResource(R.drawable.ic_button_play_100)
                    playerState = STATE_PREPARED
                    totalElapsedTime = 0L
                }
            }
        }
        mainThreadHandler?.post(timerRunnable!!)
    }
    private fun stopTimer(){
       if (startTime > 0){
           totalElapsedTime = (System.currentTimeMillis() - startTime) + totalElapsedTime
       }
        timerRunnable?.let{
            mainThreadHandler?.removeCallbacks(it)
        }
        timerRunnable = null
        startTime = 0L
    }
    override fun onPause() {
        super.onPause()
        pausePlayer()
        mainThreadHandler?.removeCallbacksAndMessages(null)
    }
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        mainThreadHandler?.removeCallbacksAndMessages(null)
    }
    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val UPDATE_TIME_INTERVAL = 500L
    }
}