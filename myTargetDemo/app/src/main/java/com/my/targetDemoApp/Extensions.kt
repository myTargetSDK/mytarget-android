package com.my.targetDemoApp

import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.my.target.common.CustomParams

fun Snackbar.showLoading(): Snackbar {
    val contentLay = this.view.findViewById<TextView>(R.id.snackbar_text).parent as ViewGroup
    val item = ProgressBar(this.view.context)
    contentLay.addView(item, 0)
    this.show()
    return this
}

fun CustomParams.addParsedString(params: String?) {
    if (params == null) {
        return
    }
    try {
        params.split("&")
                .forEach {
                    val split = it.split("=")
                    this.setCustomParam(split[0], split[1])
                }
    }
    catch (e: Exception) {
    }
}