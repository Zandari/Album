package com.album.ui
import com.google.gson.annotations.SerializedName

data class AlbumImage (
    @SerializedName("url")    var url: String,
    @SerializedName("width")  var width: Int,
    @SerializedName("height") var height: Int,
)