/*
 * ArcToolbarView - An Arc view for the android Toolbar or anywhere.
 *
 * Copyright (c) 2018 ArcToolbarView
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.trikh.focuslock.widget.extensions

import com.google.android.material.appbar.AppBarLayout
import com.trikh.focuslock.widget.ArcToolbarView

fun ArcToolbarView.setAppBarLayout(appbar: AppBarLayout) {
    appbar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
        internal var scrollRange = -1f

        override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
            if (scrollRange == -1f) {
                scrollRange = appBarLayout.totalScrollRange.toFloat()
            }

            val scale = 1 + verticalOffset / scrollRange

            this@setAppBarLayout.setScale(scale)
        }
    })
}
