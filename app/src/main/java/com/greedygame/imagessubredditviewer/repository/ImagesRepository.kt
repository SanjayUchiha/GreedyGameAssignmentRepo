package com.greedygame.imagessubredditviewer.repository

import android.content.Context
import android.util.Log
import com.greedygame.imagessubredditviewer.api.AppNetworkService
import com.greedygame.imagessubredditviewer.model.ImageResponse
import com.greedygame.imagessubredditviewer.utils.Event
import com.greedygame.imagessubredditviewer.utils.EventStatus
import com.greedygame.imagessubredditviewer.utils.EventType
import io.reactivex.subjects.PublishSubject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ImagesRepository(val context:Context) {
    var publish= PublishSubject.create<Event>()
    var call:Call<ImageResponse>?=null
    fun getImages(){
        call=AppNetworkService(context).getRetrofitObject().getImages()
        call!!.enqueue(object: Callback<ImageResponse>{
            override fun onFailure(call: Call<ImageResponse>, t: Throwable) {
                publish.onNext(Event(EventType.FetchImage,EventStatus.Failure,null))

            }

            override fun onResponse(call: Call<ImageResponse>, response: Response<ImageResponse>) {
                if(response.isSuccessful){
                    publish.onNext(Event(EventType.FetchImage,EventStatus.Success,response.body()))
                }
                else{
                    publish.onNext(Event(EventType.FetchImage,EventStatus.Failure,null))
                }
            }
        })

    }
    fun cancelRequest(){
        call!!.cancel()
    }
}