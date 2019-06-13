package com.trikh.focuslock.data.source

import com.trikh.focuslock.data.source.local.VideoLocalRepository
import com.trikh.focuslock.data.source.remote.VideoRemoteRepository

class VideoRepository {
    private val remoteRepository = VideoRemoteRepository()
    private var localRepository = VideoLocalRepository.getInstance()

    fun getVideoDetails(videoId: String) = remoteRepository.getVideoDetails(videoId)

    fun getVideoThumbnailUrl(videoId: String) = "http://img.youtube.com/vi/$videoId/hqdefault.jpg"

    fun getVideoId() = localRepository.getTodaysVideoId()
}