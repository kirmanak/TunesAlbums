package com.forasoft.albums.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations.map
import com.forasoft.albums.model.network.TunesInterface
import com.forasoft.albums.model.network.TunesResult
import com.forasoft.albums.model.storage.AlbumDao
import com.forasoft.albums.model.storage.SearchRequestDao
import com.forasoft.albums.model.storage.entity.AlbumEntity
import com.forasoft.albums.model.storage.entity.SearchRequestEntity
import com.forasoft.albums.viewmodel.Album
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Albums repository. Loads data from network and stores it in the storage
 */
@Singleton
class AlbumsRepo @Inject constructor(
    private val api: TunesInterface,
    private val albumDao: AlbumDao,
    private val searchDao: SearchRequestDao
) {
    companion object {
        private val TAG = AlbumsRepo::class.java.simpleName
    }

    fun searchAlbums(request: String): LiveData<Array<Album>> {
        api.search(request).enqueue(object : Callback<TunesResult> {
            override fun onFailure(call: Call<TunesResult>, t: Throwable) {
                Log.e(TAG, "onFailure() called", t)

                TODO("Show error in UI")
            }

            override fun onResponse(
                call: Call<TunesResult>,
                response: Response<TunesResult>
            ) {
                Log.d(TAG, "onResponse() called: $response")

                val result = response.body()?.results ?: emptyArray()
                if (result.isEmpty())
                    return

                val requestId = searchDao.insert(SearchRequestEntity(0, Date(), request))

                val entities = result.map {
                    AlbumEntity(
                        0,
                        it.collectionId ?: return,
                        it.artistName ?: return,
                        it.collectionName ?: return,
                        it.artworkUrl100 ?: return,
                        it.releaseDate ?: return,
                        it.primaryGenreName ?: return,
                        requestId
                    )
                }.toTypedArray()

                Log.d(TAG, "onResponse: inserting ${entities.contentToString()}")
                albumDao.insertAll(entities)
            }

        })

        val albumsEntities = albumDao.getAllBySearchTerm(request)

        return map(albumsEntities) { array ->
            array.map {
                Album(it.artistName, it.name, it.posterUrl, it.releaseDate, it.genre)
            }.toTypedArray()
        }
    }
}