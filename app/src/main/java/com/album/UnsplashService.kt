package com.album

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Header

data class UnsplashImage(
    var width: Int,
    var height: Int,
    @SerializedName("urls")
    var urls: ImageUrls,
)

data class ImageUrls(
    var raw: String,
    var full: String,
    var regular: String,
    var small: String,
)

data class AuthorizationInfo(
    val accessKey: String,
) {
    override fun toString(): String {
        return "Client-ID $accessKey"
    }
}

interface UnsplashService {
    @GET("/photos/random")
    suspend fun getRandomPhotos(@Header("Authorization") authInfo: AuthorizationInfo,
                        @Query("count") count: Int,
                        @Query("query") query: String,
                        @Query("content_filter") contentFilter: String): Response<List<UnsplashImage>>
}