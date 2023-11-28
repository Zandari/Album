package com.album

import com.example.album.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object AlbumRepositoryInstance {
    private var service = UnsplashServiceInstance.service

    suspend fun getRandomImages(query: String) =
        withContext(Dispatchers.IO) {
            service.getRandomPhotos(
                authInfo = AuthorizationInfo(BuildConfig.unsplashAccessKey),
                query = query,
                count = 30,
                contentFilter = "high",
            )
        }
}