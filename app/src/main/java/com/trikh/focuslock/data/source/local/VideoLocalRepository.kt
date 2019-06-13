package com.trikh.focuslock.data.source.local


import java.util.*

class VideoLocalRepository {
    val videoIds = arrayOf<String>()

    fun getTodaysVideoId(): String {
        val calendar = Calendar.getInstance()
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        return videoIds[dayOfMonth]
    }

    companion object {
        @Volatile
        private var instance: VideoLocalRepository? = null
        private val Lock = Any()

        fun getInstance() = instance ?: synchronized(Lock) {
            instance ?: VideoLocalRepository()
        }
    }
}