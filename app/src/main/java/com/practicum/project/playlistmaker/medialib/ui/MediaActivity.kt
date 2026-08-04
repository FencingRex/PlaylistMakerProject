package com.practicum.project.playlistmaker.medialib.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.project.playlistmaker.R
//import com.practicum.project.playlistmaker.databinding.ActivityMediaBinding

class MediaActivity : AppCompatActivity() {
//    private lateinit var binding: ActivityMediaBinding
//    private lateinit var tabMediator: TabLayoutMediator
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityMediaBinding.inflate(layoutInflater)
//        enableEdgeToEdge()
//        setContentView(binding.root)
//        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
//        binding.toolbar.setOnClickListener { finish() }
//        binding.viewPager.adapter = MediaAdapter(supportFragmentManager, lifecycle)
//
//        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
//            when(position) {
//                0 -> tab.text = getString(R.string.favoritesTracks)
//                1 -> tab.text = getString(R.string.playlists)
//            }
//        }
//        tabMediator.attach()
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        tabMediator.detach()
//    }

}