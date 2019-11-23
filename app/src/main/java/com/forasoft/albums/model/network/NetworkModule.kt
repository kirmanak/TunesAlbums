package com.forasoft.albums.model.network

import dagger.Module
import dagger.Provides
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executors

@Module
class NetworkModule {
    @Provides
    fun getTunesInterface(retrofit: Retrofit): TunesInterface {
        return retrofit.create(TunesInterface::class.java)
    }

    @Provides
    fun getRetrofit(converter: Converter.Factory): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(converter)
            .callbackExecutor(Executors.newCachedThreadPool())
            .baseUrl("https://itunes.apple.com/")
            .build()
    }

    @Provides
    fun getConverter(): Converter.Factory {
        return GsonConverterFactory.create()
    }
}