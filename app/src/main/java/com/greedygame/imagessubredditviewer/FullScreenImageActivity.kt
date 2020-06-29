package com.greedygame.imagessubredditviewer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.greedygame.imageloader.DownloadImageTask
import com.greedygame.imageloader.ImagesCache
import kotlinx.android.synthetic.main.activity_full_screen_image.*

class FullScreenImageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen_image)
        setImage()
    }

    private fun setImage() {
        var url=intent.getStringExtra("url")
        if(url!=null){
            var cache=ImagesCache.instance
            var bitmap=cache!!.getImageFromWarehouse(url)
            if(bitmap==null){
                DownloadImageTask(iv_FullScreenImage,500,500).execute(url)
            }
            else{
                iv_FullScreenImage.setImageBitmap(bitmap)
            }
        }
    }
}