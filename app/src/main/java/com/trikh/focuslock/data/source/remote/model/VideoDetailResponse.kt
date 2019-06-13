package com.trikh.focuslock.data.source.remote.model

import com.google.gson.annotations.SerializedName

data class VideoDetailResponse(@SerializedName("items") val items: List<Item>)