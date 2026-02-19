package com.my.targetDemoApp.helpers

import android.content.Context
import android.util.Log
import android.view.View
import com.my.target.common.models.IAdLoadingError
import com.my.target.nativeads.NativeAd
import com.my.target.nativeads.banners.NativePromoBanner
import com.my.target.nativeads.factories.NativeViewsFactory
import com.my.target.nativeads.views.NativeAdView
import com.my.targetDemoApp.addParsedString

class NativeAdHelper(parent: View) : NativeHelper<NativeAd, NativeAdView>(parent) {

    companion object
    {
        const val TAG = "NativeAdHelper"
    }

    val demoNativeAdListener = object : NativeAd.NativeAdListener {
        override fun onLoad(banner: NativePromoBanner, nativeAd: NativeAd) {
            adCalls++
            nativeList.append(nativeList.size() * 6 + 3, nativeAd)
            if (isLoaded()) {
                everythingLoaded()
            }
        }

        override fun onNoAd(adLoadingError: IAdLoadingError, nativeAd: NativeAd) {
            Log.d(TAG, "onNoAd() called with: adLoadingError = $adLoadingError, nativeAd = $nativeAd")
            adCalls++
            if (isLoaded()) {
                callNoAd(adLoadingError.message)
            }
        }

        @Deprecated("Deprecated in SDK")
        override fun onClick(ad: NativeAd) {
        }

        override fun onClick(clickedView: View?, ad: NativeAd) {
            Log.d(TAG, "onClick")
        }

        override fun onShow(ad: NativeAd) {
            Log.d(TAG, "onShow")
        }

        @Deprecated("Deprecated in myTarget SDK")
        override fun onVideoPlay(ad: NativeAd) {
            // Use NativeAd.NativeAdVideoListener, see below
        }

        @Deprecated("Deprecated in myTarget SDK")
        override fun onVideoPause(ad: NativeAd) {
            // Use NativeAd.NativeAdVideoListener, see below
        }

        @Deprecated("Deprecated in myTarget SDK")
        override fun onVideoComplete(ad: NativeAd) {
            // Use NativeAd.NativeAdVideoListener, see below
        }
    }

    val demoNativeAdVideoListener = object : NativeAd.NativeAdVideoListener {
        override fun onVideoStart(ad: NativeAd) {
            Log.d(TAG, "onVideoStart")
        }

        override fun onVideoPause(ad: NativeAd) {
            Log.d(TAG, "onVideoPause")
        }

        override fun onVideoResume(ad: NativeAd) {
            Log.d(TAG, "onVideoResume")
        }

        override fun onVideoComplete(ad: NativeAd) {
            Log.d(TAG, "onVideoComplete")
        }

        override fun onVideoReplay(ad: NativeAd) {
            Log.d(TAG, "onVideoReplay")
        }

        override fun onVideoProgress(
            position: Float, // seconds
            actualDuration: Float, // seconds
            ad: NativeAd
        ) {
            Log.d(TAG, "onVideoProgress, position=$position s, actualDuration=$actualDuration s")
        }

        override fun onVideoError(message: String, ad: NativeAd) {
            Log.d(TAG, "onVideoError, message=$message")
        }

        override fun onVideoVolumeChanged(volume: Float, ad: NativeAd)
        {
            Log.d(TAG, "onVideoVolumeChanged, volume=$volume")
        }
    }

    override fun loadAds(slot: Int, params: String?) {
        val nativeAd = NativeAd(slot, parent.context)
        nativeAd.listener = demoNativeAdListener // required listener
        nativeAd.nativeAdVideoListener = demoNativeAdVideoListener // optional listener
        nativeAd.customParams.addParsedString(params)
        nativeAd.load()
    }

    override fun createAdView(context: Context): NativeAdView {
        return NativeViewsFactory.getNativeAdView(context)
    }

    override fun setupView(ad: NativeAd, adView: NativeAdView) {
        adView.setupView(ad.banner)
    }

    override fun registerView(ad: NativeAd, adView: NativeAdView) {

        val demoAdBinder = if (true) {
            // NativeAdView has its own binder
            adView.nativeAdViewBinder
        } else {
            // Or we can create application specific view-binder
            DemoNativeAdViewBinder(adView)
        }

        ad.registerView(demoAdBinder)
    }

    override fun unRegisterView(ad: NativeAd?) {
        ad?.unregisterView()
    }
}