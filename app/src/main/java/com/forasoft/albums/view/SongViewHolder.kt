package com.forasoft.albums.view

import androidx.recyclerview.widget.RecyclerView
import com.forasoft.albums.databinding.SongViewHolderBinding
import com.forasoft.albums.viewmodel.Song

/**
 * Holds song info to be shown in UI
 */
class SongViewHolder(private val binding: SongViewHolderBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun setData(item: Song) {
        binding.song = item
        binding.executePendingBindings()
    }
}