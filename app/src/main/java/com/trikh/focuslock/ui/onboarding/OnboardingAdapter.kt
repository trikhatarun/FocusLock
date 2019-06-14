package com.trikh.focuslock.ui.onboarding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.trikh.focuslock.Application
import com.trikh.focuslock.R
import kotlinx.android.synthetic.main.onboarding_item_layout.view.*

class OnboardingAdapter() : PagerAdapter() {

    val onboardingTitles = arrayListOf(
        "Start a block at any time",
        "Say goodbye to distraction apps",
        "Lorem Ipsum is simply dummy"
    )
    val onboardingDescriptions = arrayListOf(
        "Study session? Going to bed? Activate a block by pressing it’s blue lock, or use. a widget!",
        "Automatically block apps at set times, so that you can focus on what’s important",
        "Lorem Ipsum is simply dummy text of the printing and typesetting industry"
    )

    val onBoardingImages = arrayListOf(
        R.drawable.onboarding_one,
        R.drawable.onboarding_two,
        R.drawable.onboarding_three
    )

    override fun isViewFromObject(view: View, o: Any): Boolean {
        return view == o
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(container.context)
        val view = inflater.inflate(R.layout.onboarding_item_layout, null)
        view.title.text = onboardingTitles[position]
        view.description.text = onboardingDescriptions[position]
        view.image.setImageResource(onBoardingImages[position])
        container.addView(view)
        /*if (position == 2){
            view.nextBtn.text = Application.instance.getString(R.string.get_started)
        }else{
            view.nextBtn.text = Application.instance.getString(R.string.next)
        }
        view.nextBtn.setOnClickListener {
            if (position == 2) {

                listener.onGetStartedClick()
            } else {

                listener.onNextClick(position)
            }
        }*/
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }

    override fun getCount() = onboardingTitles.size
}