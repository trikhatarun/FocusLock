package com.trikh.focuslock.data.source.remote.api

import com.trikh.focuslock.data.source.remote.model.VideoDetailResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("videos")
    fun getVideoDetails(@Query("id") id: String, @Query("part") part: String = "snippet") : Single<VideoDetailResponse>
}