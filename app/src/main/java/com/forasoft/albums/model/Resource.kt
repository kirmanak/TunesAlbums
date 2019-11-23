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

        val networkSource = fetchFromNetwork(request)

        result.addSource(networkSource) { data ->
            result.removeSource(networkSource)

            if (data.state == State.SUCCESS && data.result != null) {
                GlobalScope.launch { persist(data.result, request) }

                result.addSource(loadFromDb(request)) {
                    result.value = RequestResult.success(map(it))
                }
            } else {
                result.value = RequestResult.failure()
            }
        }

        return result
    }

    internal abstract fun fetchFromNetwork(request: Request): LiveData<RequestResult<NetworkResult>>
    internal abstract fun saveToStorage(data: NetworkResult, request: Request)
    internal abstract fun loadFromDb(request: Request): LiveData<StorageResult>
    internal abstract fun map(storageResult: StorageResult): ViewResult

    internal fun <T> setValue(holder: MutableLiveData<T>, value: T) {
        if (holder.value != value)
            holder.postValue(value)
    }

    private suspend fun persist(data: NetworkResult, request: Request) =
        withContext(Dispatchers.IO) { saveToStorage(data, request) }
}