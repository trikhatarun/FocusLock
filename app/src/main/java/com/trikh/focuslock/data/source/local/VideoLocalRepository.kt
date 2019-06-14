package com.trikh.focuslock.data.source.local


import java.util.*

class VideoLocalRepository {
    val videoIds = arrayOf<String>("ISZCSikwSlI")

    fun getTodaysVideoId(): String {
        val calendar = Calendar.getInstance()
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        if (videoIds.size >= 31) {
            return videoIds[dayOfMonth]
        } else {
            return videoIds.random()
        }
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