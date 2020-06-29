package com.greedygame.imagessubredditviewer


import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.greedygame.imageloader.ImagesCache
import com.greedygame.imagessubredditviewer.adapter.ImagesAdapter
import com.greedygame.imagessubredditviewer.model.ChildrenItem
import com.greedygame.imagessubredditviewer.model.ImageResponse
import com.greedygame.imagessubredditviewer.repository.ImagesRepository
import com.greedygame.imagessubredditviewer.utils.BaseActivity
import com.greedygame.imagessubredditviewer.utils.EventStatus
import com.greedygame.imagessubredditviewer.utils.EventType
import com.greedygame.imagessubredditviewer.viewmodel.ImageViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(),ImagesAdapter.OnClickCallBacks,View.OnClickListener {
    var dialog:ProgressDialog? = null

    var viewModel=ImageViewModel(ImagesRepository(this))
    var adapter:ImagesAdapter?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnRetry.setOnClickListener(this)
        rvImages.layoutManager=LinearLayoutManager(this)
        var cache=ImagesCache.instance
        cache!!.initializeCache()
        adapter= ImagesAdapter(cache)
        pullToRefresh.setOnRefreshListener {
            viewModel.loadImage()
        }
        rvImages.adapter=adapter
        viewModel.loadImage()
        setObserver()
    }

    fun showProgress(canshow:Boolean,msg:String){

        if(dialog == null){
            dialog = ProgressDialog(this)
        }
        dialog!!.setMessage(msg)
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.setCancelable(false)
        dialog!!.setButton(
            DialogInterface.BUTTON_NEGATIVE,"Cancel"
        ) { dialog, which -> viewModel.cancelRequest() }
        if(canshow){
            dialog!!.show()
        }else
        {
            dialog!!.dismiss()
        }
    }

    override fun onResume() {
//        viewModel.loadImage()
        super.onResume()
    }

    private fun setObserver() {
        viewModel.response.observe(this, Observer {
            when(it.eventType){
                EventType.FetchImage->{
                    when(it.valeventStatus){
                        EventStatus.Loading->{
                            ll_retry.visibility= View.GONE
                            pullToRefresh.isRefreshing=false
                            showProgress(true,"Please wait...! fetching details")
                        }
                        EventStatus.Failure->{
                            pullToRefresh.isRefreshing=false

                            ll_retry.visibility= View.VISIBLE
                            showToast("Failed to fetch please try again later")
                            showProgress(false,"")
                        }
                        EventStatus.Success->{
                            pullToRefresh.isRefreshing=false

                            ll_retry.visibility= View.GONE
                            showToast("Success.")
                            showProgress(false,"")
                            adapter!!.setValues((it.data as ImageResponse).data!!.children as ArrayList<ChildrenItem>)
                        }
                    }
                }
            }
        })
    }

    override fun onClick(url: String) {
        startActivity(Intent(this,FullScreenImageActivity::class.java).putExtra("url",url))
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnRetry->viewModel.loadImage()
        }
    }
}