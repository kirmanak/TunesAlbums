package com.forasoft.albums.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.forasoft.albums.model.network.TunesInterface
import com.forasoft.albums.model.network.TunesItem
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

    fun searchAlbums(request: String): LiveData<RequestResult<List<Album>>> {
        Log.d(TAG, "searchAlbums() called with $request")

        return object : Resource<List<Album>, Array<AlbumEntity>, Array<TunesItem>, String>() {
            override fun fetchFromNetwork(request: String): LiveData<RequestResult<Array<TunesItem>>> {
                val result = MutableLiveData<RequestResult<Array<TunesItem>>>()

                api.search(request).enqueue(object : Callback<TunesResult> {
                    override fun onFailure(call: Call<TunesResult>, t: Throwable) {
                        Log.e(TAG, "searchAlbums.onFailure() called", t)
                        setValue(result, RequestResult.failure())
                    }

                    override fun onResponse(
                        call: Call<TunesResult>,
                        resp: Response<TunesResult>
                    ) {
                        Log.d(TAG, "searchAlbums.onResponse() called with $resp")

                        val received = resp.body()?.results ?: emptyArray()
                        setValue(result, RequestResult.success(received))
                    }
                })

                return result
            }

            override fun saveToStorage(data: Array<TunesItem>, request: String) {
                Log.d(TAG, "searchAlbums.persist() called with '$request', '$data'")

                val requestId = searchDao.insert(SearchRequestEntity(0, Date(), request))

                val entities = data.map {
                    AlbumEntity(
                        0, it.collectionId ?: return, it.artistName ?: return,
                        it.collectionName ?: return, it.artworkUrl100 ?: return,
                        it.releaseDate ?: return, it.primaryGenreName ?: return,
                        requestId
                    )
                }.toTypedArray()

                albumDao.insertAll(entities)
            }

            override fun loadFromDb(request: String) = albumDao.getAllBySearchTerm(request)

            override fun map(storageResult: Array<AlbumEntity>) = storageResult.map {
                Album(it.artistName, it.name, it.posterUrl, it.releaseDate, it.genre)
            }
        }.asLiveData(request)
    }
}