package com.practicum.project.playlistmaker.settings.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.practicum.project.playlistmaker.R
import com.practicum.project.playlistmaker.databinding.ActivitySettingsBinding


class SettingsActivity : AppCompatActivity() {
    private lateinit var viewModel: SettingsViewModel
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root){
            v, insets ->
            val systemBars= insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left,systemBars.top,systemBars.right,systemBars.bottom)
            insets
        }

        val backBtn = findViewById<Toolbar>(R.id.toolbar)

        backBtn.setOnClickListener { finish() }

        viewModel = ViewModelProvider(this, SettingsViewModel.getViewModelFactory())[SettingsViewModel::class.java]

        viewModel.getThemeData().observe(this){
            isDarkThemeEnabled -> binding.themeSwitcher.isChecked = isDarkThemeEnabled
        }
        binding.themeSwitcher.setOnCheckedChangeListener { switcher, checked ->viewModel.switchTheme(checked)  }

        binding.shareBtn.setOnClickListener {
           startActivity(viewModel.shareApp())
        }

        binding.supportBtn.setOnClickListener {
            startActivity(viewModel.openSupport())
        }

        binding.userAgreemBtn.setOnClickListener {
            startActivity(viewModel.openTermsLink())

        }
    }
}