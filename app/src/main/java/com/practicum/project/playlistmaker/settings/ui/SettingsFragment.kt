package com.practicum.project.playlistmaker.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.practicum.project.playlistmaker.databinding.FragmentSettingsBinding
import androidx.fragment.app.Fragment

import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {
    private val viewModel by viewModel<SettingsViewModel>()
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?){

        ViewCompat.setOnApplyWindowInsetsListener(binding.root){
            v, insets ->
            val systemBars= insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left,systemBars.top,systemBars.right,systemBars.bottom)
            insets
        }

        viewModel.getThemeData().observe(viewLifecycleOwner){
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
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}