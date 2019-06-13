package com.trikh.focuslock.data.source.remote

import com.trikh.focuslock.data.source.remote.api.ApiClient
import io.reactivex.schedulers.Schedulers

class VideoRemoteRepository {
    private val apiInterface = ApiClient.client

    fun getVideoDetails(videoId: String)  = apiInterface.getVideoDetails(videoId).subscribeOn(Schedulers.io())
}