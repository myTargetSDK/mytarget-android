package com.my.targetDemoApp

import android.support.design.R
import android.support.design.widget.Snackbar
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.my.target.ads.MyTargetView

class BannerHelper : MyTargetView.MyTargetViewListener {

    var bannerView: MyTargetView? = null
    private var bar: Snackbar? = null
    private var afterLoad: (() -> Unit)? = null
    private var parent: View? = null

    override fun onLoad(p0: MyTargetView) {
        bar?.dismiss()
        afterLoad?.invoke()
    }

    override fun onClick(p0: MyTargetView) {
    }

    override fun onShow(p0: MyTargetView) {
    }

    override fun onNoAd(p0: String, p1: MyTargetView) {
        parent?.let { Snackbar.make(it, "No ad", Snackbar.LENGTH_SHORT).show() }
    }

    fun init(defaultSlot: Int, adSize: Int, parent: View, function: (() -> Unit)? = null) {
        afterLoad = function
        bannerView = MyTargetView(parent.context)
        this.parent = parent
        showLoading(parent)

        bannerView?.let {
            it.listener = this
            it.init(defaultSlot, adSize)
            it.load()
        }
    }

    private fun showLoading(parent: View) {
        bar = Snackbar.make(parent, "Loading", Snackbar.LENGTH_INDEFINITE)
        bar?.let {
            val contentLay = it.view.findViewById<TextView>(R.id.snackbar_text).parent as ViewGroup
            val item = ProgressBar(parent.context)
            contentLay.addView(item, 0)
            it.show()
        }
    }

    fun destroy() {
        bannerView?.destroy()
    }
}