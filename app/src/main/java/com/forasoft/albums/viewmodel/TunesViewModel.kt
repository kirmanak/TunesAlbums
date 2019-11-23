package com.forasoft.albums.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.forasoft.albums.model.RequestResult
import com.forasoft.albums.model.TunesRepo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TunesViewModel @Inject constructor(private val tunesRepo: TunesRepo) : ViewModel() {
    fun search(text: String): LiveData<RequestResult<List<Album>>> = tunesRepo.searchAlbums(text)
    fun loadTracks(album: Album): LiveData<RequestResult<List<Song>>> = tunesRepo.loadTracks(album)
}
