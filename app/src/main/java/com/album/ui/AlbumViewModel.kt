package com.album.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.album.AlbumRepositoryInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException


class AlbumViewModel: ViewModel() {
    private val _images = MutableStateFlow(listOf<AlbumImage>())
    private val _errorMessage = MutableStateFlow("")
    private val _isLoading = MutableStateFlow(false)

    val images: StateFlow<List<AlbumImage>> = _images.asStateFlow()
    val errorMessage: StateFlow<String> = _errorMessage.asStateFlow()
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun update() {
        _isLoading.update { true }
        viewModelScope.launch {
            val response = try {
                AlbumRepositoryInstance.getRandomImages("nature")
            }
            catch (e: IOException) {
                Log.e("asdf", "IOException (((")
                _errorMessage.update {
                    "Check your internet connection"
                }
                return@launch
            }
            catch (e: HttpException) {
                _errorMessage.update {
                    "Error occurred while fetching data"
                }
                return@launch
            }
            if (response.isSuccessful && response.body() != null) {
                response.body()!!.forEach { img ->
                    _images.update {
                        it + AlbumImage(img.urls.regular, img.width, img.height)
                    }
                }
            }
        }
        _errorMessage.update {""}
        _isLoading.update { false }
    }
}