package com.forasoft.albums.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class Resource<ViewResult, StorageResult, NetworkResult, Request> {
    fun asLiveData(request: Request): LiveData<RequestResult<ViewResult>> {
        val result = MediatorLiveData<RequestResult<ViewResult>>()
        result.value = RequestResult.loading()

        GlobalScope.launch { populateResult(result, request) }

        return result
    }

    internal abstract fun fetchFromNetwork(request: Request): LiveData<RequestResult<NetworkResult>>
    internal abstract suspend fun saveToStorage(data: NetworkResult, request: Request)
    internal abstract fun loadFromDb(request: Request): LiveData<StorageResult>
    internal abstract fun map(storageResult: StorageResult): ViewResult
    internal abstract suspend fun shouldUpdate(request: Request): Boolean

    internal fun <T> setValue(holder: MutableLiveData<T>, value: T) {
        if (holder.value != value)
            holder.postValue(value)
    }

    private suspend fun populateResult(
        result: MediatorLiveData<RequestResult<ViewResult>>,
        request: Request
    ) = withContext(Dispatchers.Main) {
        if (shouldUpdate(request)) {
            val networkSource = fetchFromNetwork(request)

            result.addSource(networkSource) { data ->
                result.removeSource(networkSource)
                GlobalScope.launch { processNetworkData(data, request, result) }
            }
        } else {
            loadCached(result, request)
        }
    }

    private suspend fun processNetworkData(
        data: RequestResult<NetworkResult>,
        request: Request,
        result: MediatorLiveData<RequestResult<ViewResult>>
    ) = withContext(Dispatchers.Main) {
        if (data.state == State.SUCCESS && data.result != null) {
            saveToStorage(data.result, request)
            loadCached(result, request)
        } else {
            result.value = RequestResult.failure()
        }
    }

    private suspend fun loadCached(
        result: MediatorLiveData<RequestResult<ViewResult>>,
        request: Request
    ) = withContext(Dispatchers.Main) {
        result.addSource(loadFromDb(request)) {
            result.value = RequestResult.success(map(it))
        }
    }
}