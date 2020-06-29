package com.greedygame.imagessubredditviewer.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.greedygame.imageloader.DownloadImageTask
import com.greedygame.imageloader.ImagesCache
import com.greedygame.imagessubredditviewer.MainActivity
import com.greedygame.imagessubredditviewer.R
import com.greedygame.imagessubredditviewer.model.ChildrenItem
import kotlinx.android.synthetic.main.images_item.view.*
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


class ImagesAdapter(val cache:ImagesCache):RecyclerView.Adapter<ImagesAdapter.ImagesHolder>() {


var children=ArrayList<ChildrenItem>()
    var bitmap:Bitmap?=null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesHolder {
      var view=LayoutInflater.from(parent.context).inflate(R.layout.images_item,parent,false)

        return ImagesHolder(
            view
        )
    }
fun setValues(list:ArrayList<ChildrenItem>){
    children=list
    notifyDataSetChanged()
}
    override fun getItemCount(): Int {
return children.size
    }

    override fun onBindViewHolder(holder: ImagesHolder, position: Int) {
        holder.imageView.setOnClickListener {
            (holder.itemView.context as MainActivity).onClick(children[position].data!!.thumbnail!!)
        }
        bitmap=cache!!.getImageFromWarehouse(children[position].data!!.thumbnail!!)
        if(bitmap==null){
            DownloadImageTask(holder.imageView,500,500).execute(children[position].data!!.thumbnail!!)
        }
        else{
            holder.imageView.setImageBitmap(bitmap)
        }
//        ImageLoadTask(children[position].data!!.thumbnail!!,holder.imageView).execute()
    }
    class ImagesHolder(v: View):RecyclerView.ViewHolder(v){
var imageView=v.findViewById<ImageView>(R.id.iv_Image)
    }
    interface OnClickCallBacks{
        fun onClick(url:String)
    }
}

