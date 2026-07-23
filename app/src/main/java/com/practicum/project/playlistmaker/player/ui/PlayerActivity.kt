package com.practicum.project.playlistmaker.player.ui

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.project.playlistmaker.R
import com.practicum.project.playlistmaker.databinding.ActivityPlayerBinding
import com.practicum.project.playlistmaker.player.model.PlayerState
import com.practicum.project.playlistmaker.search.domain.models.Track
class PlayerActivity: AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding
    private lateinit var viewModel: PlayerViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val track = IntentCompat.getParcelableExtra<Track>(intent, "Track", Track::class.java)

        binding.toolbar.setOnClickListener { finish() }

        viewModel = ViewModelProvider(
            this,
            PlayerViewModel.getViewModelFactory(track!!.previewUrl)
        )[PlayerViewModel::class.java]

        fun Int.dpToPx(context: Context): Int {
            return (this * context.resources.displayMetrics.density).toInt()
        }

        binding.trackName.text = track?.trackName.toString()
        binding.artistName.text = track?.artistName.toString()
        binding.trackDurationValue.text = track?.formatedTime.toString()
        binding.albumNameValue.text = track?.collectionName.toString()
        binding.trackYearValue.text = track?.releaseDate?.take(4)
        binding.trackGenreValue.text = track?.primaryGenreName.toString()
        binding.countryValue.text = track?.country.toString()
        binding.playbackDuration?.text = "00:00"

        val radiusInPx = 2.dpToPx(binding.albumCover.context)

        Glide.with(binding.albumCover)
            .load(track?.coverArtwork)
            .placeholder(R.drawable.ic_album_cover_placeholder_light_312)
            .fitCenter()
            .transform(RoundedCorners(radiusInPx))
            .into(binding.albumCover)

        binding.playButton.setOnClickListener { viewModel.playbackControl() }
        viewModel.getState().observe(this) {
            render(it)
        }
        viewModel.getCurrentPosition().observe(this) { position ->
            binding.trackDurationValue.text = position
        }
    }
    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }
    override fun onDestroy() {
        super.onDestroy()
        viewModel.releasePlayer()
    }
    private fun render(state: PlayerState) {
        when (state) {
            PlayerState.STATE_PREPARED -> {
                binding.playButton.isEnabled = true
                binding.playbackDuration?.text = "00:00"
                binding.playButton.setImageResource(R.drawable.ic_button_play_100)
            }
            PlayerState.STATE_PLAYING -> {
                binding.playButton.setImageResource(R.drawable.ic_button_pause_100)
            }
            PlayerState.STATE_PAUSED -> {
                binding.playButton.setImageResource(R.drawable.ic_button_play_100)
            }
        PlayerState.STATE_DEFAULT -> {}
        }
    }
}
