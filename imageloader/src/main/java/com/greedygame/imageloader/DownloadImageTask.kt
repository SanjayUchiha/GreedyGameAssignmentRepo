package com.greedygame.imageloader

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import android.widget.BaseAdapter
import android.widget.ImageView
import com.greedygame.imageloader.ImagesCache
import java.net.HttpURLConnection
import java.net.URL

class DownloadImageTask : AsyncTask<String?, Void?, Bitmap?> {
    private var inSampleSize = 0
    private var imageUrl: String? = null
//    private var adapter: ImagesAdapter? = null
    private var cache: ImagesCache
    private var desiredWidth: Int
    private var desiredHeight: Int
    private var image: Bitmap? = null
    private var ivImageView: ImageView? = null

    constructor(ivImageView: ImageView?, desiredWidth: Int, desiredHeight: Int) {
        this.ivImageView=ivImageView
        cache = ImagesCache.instance!!
        this.desiredWidth = desiredWidth
        this.desiredHeight = desiredHeight
    }

    constructor(
        cache: ImagesCache,
        ivImageView: ImageView?,
        desireWidth: Int,
        desireHeight: Int
    ) {
        this.cache = cache
        this.ivImageView = ivImageView
        desiredHeight = desireHeight
        desiredWidth = desireWidth
    }



    override fun onPostExecute(result: Bitmap?) {
        super.onPostExecute(result)
        if (result != null) {
            cache.addImageToWarehouse(imageUrl, result)
            if (ivImageView != null) {
                ivImageView!!.setImageBitmap(result)
            }
//            else if (adapter != null) {
//                adapter!!.notifyDataSetChanged()
//            }
        }
    }

    private fun getImage(imageUrl: String?): Bitmap? {
        if (cache.getImageFromWarehouse(imageUrl) == null) {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            options.inSampleSize = inSampleSize
            try {
                val url = URL(imageUrl)
                var connection =
                    url.openConnection() as HttpURLConnection
                var stream = connection.inputStream
                image = BitmapFactory.decodeStream(stream, null, options)
                val imageWidth = options.outWidth
                val imageHeight = options.outHeight
                if (imageWidth > desiredWidth || imageHeight > desiredHeight) {
                    println("imageWidth:$imageWidth, imageHeight:$imageHeight")
                    inSampleSize = inSampleSize + 2
                    getImage(imageUrl)
                } else {
                    options.inJustDecodeBounds = false
                    connection = url.openConnection() as HttpURLConnection
                    stream = connection.inputStream
                    image = BitmapFactory.decodeStream(stream, null, options)
                    return image
                }
            } catch (e: Exception) {
                Log.e("getImage", e.toString())
            }
        }
        return image
    }

    override fun doInBackground(vararg params: String?): Bitmap? {
        imageUrl = params[0]
        return getImage(imageUrl)
    }
}