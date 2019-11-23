package com.forasoft.albums.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.forasoft.albums.model.AlbumsRepo
import com.forasoft.albums.model.RequestResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TunesViewModel @Inject constructor(private val albumsRepo: AlbumsRepo) : ViewModel() {
    fun search(text: String): LiveData<RequestResult<List<Album>>> = albumsRepo.searchAlbums(text)
}
