package com.coursion.freakycoder.mediapicker.helper

import android.content.res.ColorStateList
import android.os.Build
import android.support.design.widget.FloatingActionButton
import android.support.v4.view.ViewCompat

/**
 * Created by WrathChaos on 5.03.2018.
 */
class Util{
    fun setButtonTint(button: FloatingActionButton, tint: ColorStateList) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            button.backgroundTintList = tint
        } else {
            ViewCompat.setBackgroundTintList(button, tint)
        }
    }
}