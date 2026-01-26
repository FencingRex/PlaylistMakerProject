package com.practicum.project.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class SearchAdapter(private val searchResults: List<Track>): RecyclerView.Adapter<SearchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        //val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_search,parent,false)
        return SearchViewHolder(parent)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(searchResults[position])
    }

    override fun getItemCount() = searchResults.size
}
