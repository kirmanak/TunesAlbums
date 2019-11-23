package com.forasoft.albums.model

enum class State { SUCCESS, FAILURE, LOADING }
data class RequestResult<out Result>(val state: State, val result: Result?) {
    companion object {
        fun <Result> success(result: Result) = RequestResult(State.SUCCESS, result)
        fun loading() = RequestResult(State.LOADING, null)
        fun failure() = RequestResult(State.FAILURE, null)
    }
}