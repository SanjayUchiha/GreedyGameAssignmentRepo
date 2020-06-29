package com.greedygame.imagessubredditviewer.viewmodel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.greedygame.imagessubredditviewer.repository.ImagesRepository
import com.greedygame.imagessubredditviewer.utils.Event
import com.greedygame.imagessubredditviewer.utils.EventStatus
import com.greedygame.imagessubredditviewer.utils.EventType
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class ImageViewModel(val repo:ImagesRepository):ViewModel() {
    var response= MutableLiveData<Event>()
    private var disposable = CompositeDisposable()

    init {
        subscribeToRepo()
    }

    fun subscribeToRepo() {
        disposable.add(
            repo.publish.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<Event>() {
                    override fun onComplete() {

                    }

                    override fun onNext(t: Event) {
                        response.value = t

                    }

                    override fun onError(e: Throwable) {

                    }
                })
        )
    }
    fun loadImage(){
        response.value=Event(
            EventType.FetchImage,
            EventStatus.Loading,null)
        repo.getImages()
    }
    fun cancelRequest(){
        repo.cancelRequest()
    }
    override fun onCleared() {
        super.onCleared()
    }
}