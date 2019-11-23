package com.forasoft.albums.view

import androidx.recyclerview.widget.RecyclerView
import com.forasoft.albums.databinding.AlbumViewHolderBinding
import com.forasoft.albums.viewmodel.Album

/**
 * Represents album info in UI
 */
class AlbumViewHolder(private val binding: AlbumViewHolderBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun setData(item: Album) {
        binding.album = item
        binding.executePendingBindings()
    }
}
