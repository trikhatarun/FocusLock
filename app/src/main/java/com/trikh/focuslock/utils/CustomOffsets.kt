package com.trikh.focuslock.utils

import android.view.View
import android.graphics.Rect
import android.util.Log


fun setToastOffsets(view: View): Array<Int?> {

    val parent = view.parent as View

    var toastOffsetsX: Int? = 0
    var toastOffsetsY: Int? = 0
    val rect = Rect()


    //setToastOffsets()


    if (parent.getGlobalVisibleRect(rect) ) {
        val root = view.rootView
        val halfwayWidth = root.right / 2
        val halfwayHeight = root.bottom / 2
        val parentCenterX = ((rect.right - rect.left) / 2) + rect.left
        val parentCenterY = ((rect.bottom - rect.top) / 2) + rect.top

        toastOffsetsY = if (parentCenterY <= halfwayHeight) {
            -(halfwayHeight - parentCenterY)
        } else {
            parentCenterY - halfwayHeight
        }

        if (parentCenterX < halfwayWidth) {
            toastOffsetsX = parentCenterX - halfwayWidth
        }

    }
    val list = arrayOf(toastOffsetsX, toastOffsetsY)



    Log.e("offsets: ", "X: $toastOffsetsX Y: $toastOffsetsY")
    return list

}
