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

class NativeAdHelper(parent: View) : NativeAd.NativeAdListener,
        NativeHelper<NativeAd, NativeAdView>(parent) {

    companion object
    {
        const val TAG = "NativeAdHelper"
    }

    override fun loadAds(slot: Int, params: String?) {
        val nativeAd = NativeAd(slot, parent.context)
        nativeAd.listener = this
        nativeAd.customParams.addParsedString(params)
        nativeAd.load()
    }

    override fun createAdView(context: Context): NativeAdView {
        return NativeViewsFactory.getNativeAdView(context)
    }

    override fun setupView(ad: NativeAd, adView: NativeAdView) {
        adView.setupView(ad.banner)
    }

    override fun registerView(ad: NativeAd, adView: View) {
        ad.registerView(adView)
    }

    override fun unRegisterView(ad: NativeAd?) {
        ad?.unregisterView()
    }

    override fun onLoad(banner: NativePromoBanner, nativeAd: NativeAd) {
        adCalls++
        nativeList.append(nativeList.size() * 6 + 3, nativeAd)
        if (isLoaded()) {
            everythingLoaded()
        }
    }

    override fun onClick(nativeAd: NativeAd) {
    }

    override fun onVideoPlay(nativeAd: NativeAd) {
    }

    override fun onVideoPause(nativeAd: NativeAd) {
    }

    override fun onNoAd(adLoadingError: IAdLoadingError, nativeAd: NativeAd) {
        Log.d(TAG, "onNoAd() called with: adLoadingError = $adLoadingError, nativeAd = $nativeAd")
        adCalls++
        if (isLoaded()) {
            callNoAd(adLoadingError.message)
        }
    }

    override fun onVideoComplete(nativeAd: NativeAd) {
    }

    override fun onShow(nativeAd: NativeAd) {
    }
}