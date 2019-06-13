package com.trikh.focuslock.ui.video

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.trikh.focuslock.data.model.Error
import com.trikh.focuslock.data.source.VideoRepository
import com.trikh.focuslock.utils.extensions.error
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy

class VideoViewModel : ViewModel() {
    private val videoRepository = VideoRepository()
    private val videoId = videoRepository.getVideoId()
    private val compositeDisposable = CompositeDisposable()
    val videoDetailsLiveData = MutableLiveData<Pair<String, String>>()
    val imageUrlLiveData = MutableLiveData<String>()
    val errorLiveData = MutableLiveData<Error>()

    fun getVideoDetails() {
        imageUrlLiveData.postValue(videoRepository.getVideoThumbnailUrl(videoId))

        compositeDisposable += videoRepository.getVideoDetails(videoId).subscribeBy({
            errorLiveData.postValue(it.error)
        }, {
            videoDetailsLiveData.postValue(
                Pair(
                    it.items[0].snippet.title,
                    it.items[0].snippet.channel
                )
            )
        })
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}