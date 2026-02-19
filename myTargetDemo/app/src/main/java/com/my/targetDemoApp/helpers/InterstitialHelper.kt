package com.my.targetDemoApp.helpers

import android.util.Log
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.my.target.ads.InterstitialAd
import com.my.target.common.models.IAdLoadingError
import com.my.targetDemoApp.R
import com.my.targetDemoApp.addParsedString
import com.my.targetDemoApp.showLoading

class InterstitialHelper(val parent: View) {

    companion object
    {
        const val TAG = "InterstitialHelper"
    }

    private var interstitialAd: InterstitialAd? = null
    private var showImmediately: Boolean = false
    private var bar: Snackbar? = null

    private val demoInterstitialAdListener2 = object : InterstitialAd.InterstitialAdListener2() {
        override fun onLoad(interstitialAd: InterstitialAd) {
            if (showImmediately) {
                bar?.dismiss()
                interstitialAd.show()
            }
            else {
                Snackbar.make(parent, "InterstitialAd is loaded", Snackbar.LENGTH_SHORT)
                    .show()
            }
        }

        override fun onFailedToShow(interstitialAd: InterstitialAd) {
            Log.d(TAG, "onFailedToShow()")
        }

        override fun onDisplay(interstitialAd: InterstitialAd) {
        }

        override fun onNoAd(adLoadingError: IAdLoadingError, ad: InterstitialAd) {
            Log.d(TAG, "onNoAd() called with: adLoadingError = $adLoadingError, ad = $ad")
            val s = String.format(parent.context.getString(R.string.error_msg), adLoadingError.message)
            Snackbar.make(parent, s, Snackbar.LENGTH_SHORT)
                .show()
        }

        override fun onClose(ad: InterstitialAd) {
            Log.d(TAG, "onClose")
        }
    }

    val demoInterstitialAdBannerListener = object : InterstitialAd.InterstitialAdBannerListener() {
        override fun onClick(ad: InterstitialAd, banner: InterstitialAd.BannerInfo?) {
            Log.d(TAG, "onClick")
        }

        override fun onImpressionTracked(ad: InterstitialAd, banner: InterstitialAd.BannerInfo?) {
            Log.d(TAG, "onImpressionTracked")
        }

        override fun onSkip(ad: InterstitialAd, banner: InterstitialAd.BannerInfo?) {
            Log.d(TAG, "onSkip")
        }
    }

    val demoInterstitialVideoListener = object : InterstitialAd.InterstitialVideoListener() {
        override fun onVideoCompleted(ad: InterstitialAd, banner: InterstitialAd.BannerInfo?) {
            Log.d(TAG, "onVideoCompleted")
        }

        override fun onVideoVolumeChanged(
            ad: InterstitialAd,
            banner: InterstitialAd.BannerInfo,
            volume: Float
        ) {
            Log.d(TAG, "onVideoVolumeChanged, volume=$volume")
        }
    }

    fun destroy() {
        interstitialAd?.dismiss()
    }

    fun init(slot: Int, showImmediately: Boolean = false, params: String?) {
        showLoading(parent)
        this.showImmediately = showImmediately
        interstitialAd = InterstitialAd(slot, parent.context).also {
            it.listener2 = demoInterstitialAdListener2 // required listener

            it.bannerListener = demoInterstitialAdBannerListener // optional listener
            it.videoListener = demoInterstitialVideoListener // optional listener

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