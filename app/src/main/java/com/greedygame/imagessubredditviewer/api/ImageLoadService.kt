package com.greedygame.imagessubredditviewer.api

import com.greedygame.imagessubredditviewer.model.ImageResponse
import okhttp3.Response
import retrofit2.Call
import retrofit2.http.GET

interface ImageLoadService {
    @GET("r/images/hot.json")
    fun getImages():Call<ImageResponse>
}