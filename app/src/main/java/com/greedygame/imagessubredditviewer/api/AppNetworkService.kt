package com.greedygame.imagessubredditviewer.api

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.logging.Logger


class AppNetworkService(val context:Context) {

    var loader:ImageLoadService?=null
        fun getRetrofitObject():ImageLoadService{
            if(loader==null) {
                loader= Retrofit.Builder()
                    .client(getOkHTTPClient())
                    .baseUrl("https://www.reddit.com/")
                    .addConverterFactory(gsonConverterFactory()!!)
                    .build().create(ImageLoadService::class.java)
            }

            return loader!!
        }
        private fun gson(): Gson? {
            val gsonBuilder = GsonBuilder()
            return gsonBuilder.create()
        }
    private fun gsonConverterFactory(): GsonConverterFactory? {
            return GsonConverterFactory.create(gson()!!)
        }
    private fun getOkHTTPClient():OkHttpClient{
            return OkHttpClient()
                .newBuilder()
//                .cache(cache(file(context)))
                .addInterceptor(httpLoggingInterceptor()!!)
                .build()
        }
//        fun cache(cacheFile: File?): Cache? {
//            return Cache(cacheFile!!, 10 * 1000 * 1000) //10 MB
//        }
//
//        fun file(context: Context): File? {
//            val file = File(context.getCacheDir(), "HttpCache")
//            file.mkdirs()
//            return file
//        }

    private fun httpLoggingInterceptor(): HttpLoggingInterceptor? {
            val httpLoggingInterceptor =
                HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger{
                    override fun log(message: String) {
                        Log.d("Body",message)
                    }
                })
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            return httpLoggingInterceptor
        }


}