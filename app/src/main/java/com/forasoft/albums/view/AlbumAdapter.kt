package com.forasoft.albums.view

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.forasoft.albums.databinding.AlbumViewHolderBinding
import com.forasoft.albums.viewmodel.Album

/**
 * Adapts albums to be shown in {@link RecyclerView}
 */
class AlbumAdapter : RecyclerView.Adapter<AlbumViewHolder>() {

    companion object {
        private val TAG = AlbumAdapter::class.java.simpleName
    }

    private val differ by lazy {
        AsyncListDiffer<Album>(this, object : DiffUtil.ItemCallback<Album>() {
            override fun areItemsTheSame(oldItem: Album, newItem: Album): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: Album, newItem: Album): Boolean {
                return oldItem == newItem
            }
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AlbumViewHolderBinding.inflate(inflater, parent, false)
        return AlbumViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.setData(differ.currentList[position])
    }

    fun updateData(albums: List<Album>) {
        Log.d(TAG, "updateData() called with $albums")
        differ.submitList(albums)
    }
}