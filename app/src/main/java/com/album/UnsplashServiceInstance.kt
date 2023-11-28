package com.album

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object UnsplashServiceInstance {
    private const val BASE_URL: String = "https://api.unsplash.com/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: UnsplashService = retrofit.create(UnsplashService::class.java)
}