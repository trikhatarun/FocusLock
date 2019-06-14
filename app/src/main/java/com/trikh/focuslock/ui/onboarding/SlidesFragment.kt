package com.trikh.focuslock.ui.onboarding


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.trikh.focuslock.R
import com.trikh.focuslock.utils.Constants.Companion.TIME_INTERVAL
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.fragment_slides.*
import java.util.concurrent.TimeUnit


class SlidesFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_slides, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        slidesPager.adapter = OnboardingAdapter()
        slidesPager.setScrollDurationFactor(4.0)
        tabLayout.setupWithViewPager(slidesPager)

        getStartedBtn.setOnClickListener {
            findNavController().navigate(SlidesFragmentDirections.actionNext())
        }

        Observable.intervalRange(1,2, 2000,2000,TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread()).subscribeBy {
            slidesPager.currentItem = it.toInt()
        }
    }
}
