package com.forasoft.albums.model.network

import com.google.gson.annotations.SerializedName

data class TunesResult(
    @SerializedName("resultCount") val resultCount: Long?,
    @SerializedName("results") val results: Array<TunesItem>?
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TunesResult

        if (resultCount != other.resultCount) return false
        if (results != null) {
            if (other.results == null) return false
            if (!results.contentEquals(other.results)) return false
        } else if (other.results != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = resultCount?.hashCode() ?: 0
        result = 31 * result + (results?.contentHashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "TunesResult(resultsCount=$resultCount, results=${results?.contentToString()})"
    }
}