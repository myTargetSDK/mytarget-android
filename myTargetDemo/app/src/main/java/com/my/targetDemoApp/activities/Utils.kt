package com.my.targetDemoApp.activities

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

fun View.applySimpleWindowInsetsListener() {

    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        val sbInsets = insets.getInsets(
            WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
        )
        v.setPadding(sbInsets.left, sbInsets.top, sbInsets.right, sbInsets.bottom)
        insets
    }
}