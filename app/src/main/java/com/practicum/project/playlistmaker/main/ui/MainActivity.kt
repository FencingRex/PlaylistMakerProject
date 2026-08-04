package com.practicum.project.playlistmaker.main.ui

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.practicum.project.playlistmaker.databinding.ActivityMainBinding
import androidx.navigation.ui.setupWithNavController
import com.practicum.project.playlistmaker.R

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        window.decorView.apply { systemUiVisibility = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) }
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.rootFragmentContainerView) as NavHostFragment

        val navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)
    }
}