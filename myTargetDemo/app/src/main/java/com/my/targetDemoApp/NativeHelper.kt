package com.my.targetDemoApp

import android.view.View
import androidx.recyclerview.widget.RecyclerView

interface NativeHelper {
    var recyclerView: RecyclerView?
    fun createRecycler()
    fun load(slot: Int, view: View, afterLoad: (() -> Unit)? = null, afterNoAd: (() -> Unit)? = null)
}