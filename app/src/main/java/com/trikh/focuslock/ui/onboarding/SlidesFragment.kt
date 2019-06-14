package com.trikh.focuslock.ui.onboarding


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.trikh.focuslock.R
import kotlinx.android.synthetic.main.fragment_slides.*
import java.text.FieldPosition


class SlidesFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_slides, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        slidesPager.adapter = OnboardingAdapter(object : OnboardingAdapter.InteractionListener{
            override fun onNextClick(position: Int) {
                slidesPager.currentItem = position+1
            }

            override fun onGetStartedClick() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }


}
