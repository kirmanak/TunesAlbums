package com.forasoft.albums.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.forasoft.albums.model.network.TunesInterface
import com.forasoft.albums.model.network.TunesItem
import com.forasoft.albums.model.network.TunesResult
import com.forasoft.albums.model.storage.AlbumDao
import com.forasoft.albums.model.storage.SearchRequestDao
import com.forasoft.albums.model.storage.SongDao
import com.forasoft.albums.model.storage.entity.AlbumEntity
import com.forasoft.albums.model.storage.entity.SearchRequestEntity
import com.forasoft.albums.model.storage.entity.SongEntity
import com.forasoft.albums.viewmodel.Album
import com.forasoft.albums.viewmodel.Song
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Albums repository. Loads data from network and stores it in the storage
 */
@Singleton
class TunesRepo @Inject constructor(
    private val api: TunesInterface,
    private val albumDao: AlbumDao,
    private val songDao: SongDao,
    private val searchDao: SearchRequestDao
) {
    companion object {
        private val TAG = TunesRepo::class.java.simpleName
        private val CACHE_TIMEOUT = TimeUnit.DAYS.toMillis(1)

    }

    fun searchAlbums(request: String): LiveData<RequestResult<List<Album>>> {
        Log.d(TAG, "searchAlbums() called with $request")

        return object : Resource<List<Album>, Array<AlbumEntity>, List<TunesItem>, String>() {
            override fun fetchFromNetwork(request: String): LiveData<RequestResult<List<TunesItem>>> {
                val result = MutableLiveData<RequestResult<List<TunesItem>>>()

                api.search(request).enqueue(object : Callback<TunesResult> {
                    override fun onFailure(call: Call<TunesResult>, t: Throwable) {
                        Log.e(TAG, "searchAlbums.onFailure() called", t)
                        setValue(result, RequestResult.failure())
                    }

                    override fun onResponse(call: Call<TunesResult>, resp: Response<TunesResult>) {
                        Log.d(TAG, "searchAlbums.onResponse() called with $resp")

                        val received = resp.body()?.results ?: emptyArray()
                        val filtered = received.filter {
                            it.wrapperType == "collection" && it.collectionType == "Album"
                        }
                        setValue(result, RequestResult.success(filtered))
                    }
                })

                return result
            }

            override suspend fun saveToStorage(data: List<TunesItem>, request: String) =
                withContext(Dispatchers.IO) {
                    Log.d(TAG, "searchAlbums.persist() called with '$request', '$data'")

                    val requestId = searchDao.insert(SearchRequestEntity(0, Date(), request))

                    val entities = data.map {
                        AlbumEntity(
                            0,
                            it.collectionId ?: return@withContext,
                            it.artistName ?: return@withContext,
                            it.collectionName ?: return@withContext,
                            it.artworkUrl100 ?: return@withContext,
                            it.releaseDate ?: return@withContext,
                            it.primaryGenreName ?: return@withContext,
                            requestId
                        )
                    }

                    albumDao.insertAll(entities)
                }

            override fun loadFromDb(request: String) = albumDao.getAllBySearchTerm(request)

            override fun map(storageResult: Array<AlbumEntity>) = storageResult.map {
                Album(
                    it.uid, it.remoteId, it.artistName, it.name, it.posterUrl,
                    it.releaseDate, it.genre
                )
            }

            override suspend fun shouldUpdate(request: String) = withContext(Dispatchers.IO) {
                return@withContext searchDao.countAllSince(
                    request,
                    System.currentTimeMillis() - CACHE_TIMEOUT
                ) <= 0
            }

        }.asLiveData(request)
    }

    fun loadTracks(request: Album): LiveData<RequestResult<List<Song>>> {
        Log.d(TAG, "loadTracks() called with $request")

        return object : Resource<List<Song>, Array<SongEntity>, List<TunesItem>, Album>() {
            override fun fetchFromNetwork(request: Album): LiveData<RequestResult<List<TunesItem>>> {
                val result = MutableLiveData<RequestResult<List<TunesItem>>>()

                api.lookup(request.albumId).enqueue(object : Callback<TunesResult> {
                    override fun onFailure(call: Call<TunesResult>, t: Throwable) {
                        Log.e(TAG, "loadTracks.onFailure(): failed to get songs info", t)
                        setValue(result, RequestResult.failure())
                    }

                    override fun onResponse(call: Call<TunesResult>, resp: Response<TunesResult>) {
                        Log.d(TAG, "loadTracks.onResponse(): received $resp")

                        val received = resp.body()?.results ?: emptyArray()
                        val filtered = received.filter {
                            it.wrapperType == "track" && it.kind == "song"
                        }
                        setValue(result, RequestResult.success(filtered))
                    }

                })

                return result
            }

            override suspend fun saveToStorage(data: List<TunesItem>, request: Album) =
                withContext(Dispatchers.IO) {
                    Log.d(TAG, "loadTracks.saveToStorage(): request $request")

                    val entities = data.map {
                        SongEntity(
                            0, request.localId, it.trackName ?: return@withContext,
                            it.previewUrl ?: return@withContext,
                            it.trackTimeMillis ?: return@withContext,
                            it.trackId ?: return@withContext
                        )
                    }

                    songDao.insertAll(entities)
                }

            override fun loadFromDb(request: Album) = songDao.getAllByAlbumId(request.localId)

            override fun map(storageResult: Array<SongEntity>) = storageResult.map {
                Song(it.name, it.trackTimeMillis, it.previewUrl)
            }

            override suspend fun shouldUpdate(request: Album) = withContext(Dispatchers.IO) {
                return@withContext songDao.countAllByAlbumId(request.localId) <= 0
            }
        }.asLiveData(request)
    }
}