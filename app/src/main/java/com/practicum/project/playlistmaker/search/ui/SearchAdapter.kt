package com.practicum.project.playlistmaker.search.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.project.playlistmaker.search.domain.models.Track

class SearchAdapter(
    private var searchResults: List<Track>,
    private val onClickListener: (Track) -> Unit,
    private val onLongClickListener: ((Track) -> Unit)? = null):
    RecyclerView.Adapter<SearchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(parent)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val track = searchResults[position]
        holder.bind(searchResults[position])

        holder.itemView.setOnClickListener {
            onClickListener(track)
        }
        if (onLongClickListener != null){
            holder.itemView.setOnLongClickListener {
                onLongClickListener(track)
                true
            }
        } else {
            holder.itemView.setOnLongClickListener(null)
        }
    }

    fun updateList(track: List<Track>){
        this.searchResults = track
        notifyDataSetChanged()
    }
    override fun getItemCount() = searchResults.size

}