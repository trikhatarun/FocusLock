package com.trikh.focuslock.data.source.remote.model

import com.google.gson.annotations.SerializedName

data class Snippet(
    @SerializedName("title") val title: String,
    @SerializedName("channelTitle") val channel: String
)