package com.forasoft.albums.viewmodel

import androidx.lifecycle.ViewModel
import com.forasoft.albums.model.AlbumsRepo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlbumsListViewModel @Inject constructor(private val albumsRepo: AlbumsRepo) : ViewModel() {
    fun search(text: String) = albumsRepo.searchAlbums(text)
}
