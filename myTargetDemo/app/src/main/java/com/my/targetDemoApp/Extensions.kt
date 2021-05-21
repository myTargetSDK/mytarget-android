package com.my.targetDemoApp

import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar

fun Snackbar.showLoading(): Snackbar {
    val contentLay = this.view.findViewById<TextView>(R.id.snackbar_text).parent as ViewGroup
    val item = ProgressBar(this.view.context)
    contentLay.addView(item, 0)
    this.show()
    return this
}