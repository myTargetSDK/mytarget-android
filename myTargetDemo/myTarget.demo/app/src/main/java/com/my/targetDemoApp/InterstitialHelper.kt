package com.my.targetDemoApp

import com.google.android.material.snackbar.Snackbar
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.my.target.ads.InterstitialAd

class InterstitialHelper(val parent: View) : InterstitialAd.InterstitialAdListener {

    private var interstitialAd: InterstitialAd? = null
    private var showImmediatly: Boolean = false
    private var bar: Snackbar? = null

    override fun onLoad(interstitialAd: InterstitialAd) {
        if (showImmediatly) {
            bar?.dismiss()
            interstitialAd.show()
        }
        else {
            Snackbar.make(parent, "InterstitialAd is loaded", Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onClick(interstitialAd: InterstitialAd) {
    }

    override fun onDisplay(interstitialAd: InterstitialAd) {
    }

    override fun onDismiss(interstitialAd: InterstitialAd) {
    }

    override fun onNoAd(interstitialAd: String, p1: InterstitialAd) {
        Snackbar.make(parent, "InterstitialAd: no ad", Snackbar.LENGTH_SHORT).show()
    }

    override fun onVideoCompleted(interstitialAd: InterstitialAd) {
    }

    fun destroy() {
        interstitialAd?.dismiss()
    }

    fun init(slot: Int, showImmediatly: Boolean = false) {
        showLoading(parent)
        this.showImmediatly = showImmediatly
        interstitialAd = InterstitialAd(slot, parent.context)
        interstitialAd?.let {
            it.listener = this
            it.load()
        }
    }



    private fun showLoading(parent: View) {
        bar = Snackbar.make(parent, "Loading", Snackbar.LENGTH_INDEFINITE)
        bar?.let{
            val contentLay = it.view.findViewById<TextView>(R.id.snackbar_text).parent as ViewGroup
            val item = ProgressBar(parent.context)
            contentLay.addView(item, 0)
            it.show()
        }
    }

    fun show() {
        interstitialAd?.show()
                ?: Snackbar.make(parent, "Not loaded yet", Snackbar.LENGTH_SHORT).show()
    }
}