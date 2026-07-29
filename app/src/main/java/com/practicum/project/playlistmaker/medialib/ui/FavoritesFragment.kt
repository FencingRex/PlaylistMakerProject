package com.practicum.project.playlistmaker.medialib.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.project.playlistmaker.databinding.FragmentFavoritesBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment: Fragment() {
    private var binding: FragmentFavoritesBinding? = null

    private val viewModel by viewModel<FavoritesViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding?.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
    companion object {
        fun newInstance() = FavoritesFragment()
    }
}