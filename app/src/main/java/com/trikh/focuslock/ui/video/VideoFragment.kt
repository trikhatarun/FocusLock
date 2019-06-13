package com.trikh.focuslock.ui.video

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import com.trikh.focuslock.R
import com.trikh.focuslock.data.source.local.VideoLocalRepository
import kotlinx.android.synthetic.main.fragment_video.*

class VideoFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_video, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val url = getVideoUrl(VideoLocalRepository.getInstance().getTodaysVideoId())
        Picasso.get().load(url).fit().centerCrop().into(videoThumbnail)
    }

    fun getVideoUrl(videoId: String): String {
        return "http://img.youtube.com/vi/$videoId/hqdefault.jpg"
    }
}
