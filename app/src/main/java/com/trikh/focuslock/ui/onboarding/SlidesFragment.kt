package com.trikh.focuslock.ui.onboarding


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.trikh.focuslock.R
import com.trikh.focuslock.utils.Constants.Companion.LAST_PAGE
import kotlinx.android.synthetic.main.fragment_slides.*


class SlidesFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_slides, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        slidesPager.adapter = OnboardingAdapter()
        tabLayout.setupWithViewPager(slidesPager)

        getStartedBtn.setOnClickListener {
            findNavController().navigate(SlidesFragmentDirections.actionNext())
        }
    }
}
