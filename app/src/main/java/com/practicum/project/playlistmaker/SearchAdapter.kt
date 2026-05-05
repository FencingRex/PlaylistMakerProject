package com.practicum.project.playlistmaker

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class SearchAdapter(
    private var searchResults: MutableList<Track>,
    private val onClickListener: (Track) -> Unit):
    RecyclerView.Adapter<SearchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(parent)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val track = searchResults[position]
        //holder.clearTexts()
        holder.bind(searchResults[position])

        holder.itemView.setOnClickListener {
            onClickListener(track)
        }
    }

    fun updateList(track: MutableList<Track>){
        this.searchResults = track
        notifyDataSetChanged()
    }
    override fun getItemCount() = searchResults.size

}
