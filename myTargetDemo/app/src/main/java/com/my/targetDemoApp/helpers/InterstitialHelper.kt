package com.my.targetDemoApp.helpers

import android.util.Log
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.my.target.ads.InterstitialAd
import com.my.target.common.models.IAdLoadingError
import com.my.targetDemoApp.R
import com.my.targetDemoApp.addParsedString
import com.my.targetDemoApp.showLoading

class InterstitialHelper(val parent: View) : InterstitialAd.InterstitialAdListener {

    companion object
    {
        const val TAG = "InterstitialHelper"
    }

    private var interstitialAd: InterstitialAd? = null
    private var showImmediatly: Boolean = false
    private var bar: Snackbar? = null

    override fun onLoad(interstitialAd: InterstitialAd) {
        if (showImmediatly) {
            bar?.dismiss()
            interstitialAd.show()
        }
        else {
            Snackbar.make(parent, "InterstitialAd is loaded", Snackbar.LENGTH_SHORT)
                    .show()
        }
    }

    override fun onClick(interstitialAd: InterstitialAd) {
    }

    override fun onDisplay(interstitialAd: InterstitialAd) {
    }

    override fun onDismiss(interstitialAd: InterstitialAd) {
    }

    override fun onNoAd(adLoadingError: IAdLoadingError, ad: InterstitialAd) {
        Log.d(TAG, "onNoAd() called with: adLoadingError = $adLoadingError, ad = $ad")
        val s = String.format(parent.context.getString(R.string.error_msg), adLoadingError.message)
        Snackbar.make(parent, s, Snackbar.LENGTH_SHORT)
                .show()
    }

    override fun onVideoCompleted(interstitialAd: InterstitialAd) {
    }

    fun destroy() {
        interstitialAd?.dismiss()
    }

    fun init(slot: Int, showImmediatly: Boolean = false, params: String?) {
        showLoading(parent)
        this.showImmediatly = showImmediatly
        interstitialAd = InterstitialAd(slot, parent.context).also {
            it.listener = this
            it.customParams.addParsedString(params)
            it.load()
        }
    }

    private fun showLoading(parent: View) {
        bar = Snackbar.make(parent, "Loading", Snackbar.LENGTH_INDEFINITE)
                .showLoading()
    }

    fun show() {
        interstitialAd?.show() ?: Snackbar.make(parent, "Not loaded yet", Snackbar.LENGTH_SHORT)
                .show()
    }
}