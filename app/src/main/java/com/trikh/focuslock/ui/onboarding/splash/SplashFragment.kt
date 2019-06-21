package com.trikh.focuslock.ui.onboarding.splash

import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.service.media.MediaBrowserService
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.trikh.focuslock.R
import com.trikh.focuslock.utils.Constants
import java.util.*
import kotlin.concurrent.schedule

class SplashFragment : Fragment() {

    private var onBoarding = Constants.DEFAULT_ON_BOARDING
    private lateinit var root: View
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_splash, container, false)

        onBoarding = context!!.getSharedPreferences(Constants.MY_PREF, 0)
            .getBoolean(Constants.ON_BOARDING, Constants.DEFAULT_ON_BOARDING)



        Handler().postDelayed({
            when (onBoarding) {
                true -> {
                    findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToSlidesFragment())
                }
                false -> {
                    findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToNavGraph())
                }
            }
        }, 3000)




        return root
    }
}