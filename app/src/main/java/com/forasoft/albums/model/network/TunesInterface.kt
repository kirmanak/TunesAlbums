package com.forasoft.albums.model.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*
import javax.inject.Singleton

/**
 * REST API provided by Apple iTunes service
 */
@Singleton
interface TunesInterface {
    @GET("/search")
    fun search(
        @Query("term") term: String,
        @Query("country") country: String = Locale.getDefault().country,
        @Query("media") media: String = "music",
        @Query("entity") entity: String = "album",
        @Query("attribute") attribute: String = "albumTerm",
        @Query("limit") limit: Int = 100,
        @Query("lang") lang: String = "${Locale.getDefault().language}_${Locale.getDefault().country}",
        @Query("version") version: Int = 2,
        @Query("explicit") explicit: String = "Yes"
    ): Call<TunesResult>

    @GET("/lookup")
    fun lookup(
        @Query("id") id: Long,
        @Query("country") country: String = Locale.getDefault().country,
        @Query("media") media: String = "music",
        @Query("entity") entity: String = "song",
        @Query("limit") limit: Int = 100,
        @Query("lang") lang: String = "${Locale.getDefault().language}_${Locale.getDefault().country}",
        @Query("explicit") explicit: String = "Yes"
    ): Call<TunesResult>
}