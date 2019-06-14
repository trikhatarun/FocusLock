package com.trikh.focuslock.ui.video

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import com.trikh.focuslock.R
import com.trikh.focuslock.utils.Constants
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_video.*

class VideoFragment : Fragment() {

    lateinit var viewModel: VideoViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.fabMenu?.visibility = View.GONE

        viewModel = ViewModelProviders.of(this).get(VideoViewModel::class.java)
        viewModel.getVideoDetails()

        viewModel.videoDetailsLiveData.observe(this, Observer {
            videoTitle.text = it.first
            videoAuthor.text = it.second
        })

        viewModel.imageUrlLiveData.observe(this, Observer {
            Picasso.get().load(it).fit().centerCrop().into(videoThumbnail)
        })

        viewModel.errorLiveData.observe(this, Observer {
            Snackbar.make(videoThumbnail, it.message!!, Snackbar.LENGTH_SHORT).show()
        })
        return inflater.inflate(R.layout.fragment_video, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        shareBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = Constants.PLAIN_TEXT
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
            intent.putExtra(
                Intent.EXTRA_TEXT,
                "https://www.youtube.com/watch?v=${viewModel.videoId}"
            )
            startActivity(Intent.createChooser(intent, Constants.SHARE_FOCUS_LOCK))
        }

        playVideoBtn.setOnClickListener {
            val playAction = VideoFragmentDirections.actionPlayVideo(viewModel.videoId)
            findNavController().navigate(playAction)
        }
    }

}
