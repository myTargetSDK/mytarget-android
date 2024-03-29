package com.my.targetDemoApp.helpers

import android.content.Context
import android.util.Log
import android.view.View
import com.my.target.common.models.IAdLoadingError
import com.my.target.nativeads.NativeBannerAd
import com.my.target.nativeads.banners.NativeBanner
import com.my.target.nativeads.factories.NativeViewsFactory
import com.my.target.nativeads.views.NativeBannerAdView
import com.my.targetDemoApp.addParsedString

class NativeBannerHelper(parent: View) : NativeBannerAd.NativeBannerAdListener,
        NativeHelper<NativeBannerAd, NativeBannerAdView>(parent) {

    companion object
    {
        const val TAG = "NativeAdHelper"
    }

    override fun loadAds(slot: Int, params: String?) {
        val nativeAd = NativeBannerAd(slot, parent.context)
        nativeAd.listener = this
        nativeAd.customParams.addParsedString(params)
        nativeAd.load()
    }

    override fun createAdView(context: Context): NativeBannerAdView {
        return NativeViewsFactory.getNativeBannerAdView(context)
    }

    override fun setupView(ad: NativeBannerAd, adView: NativeBannerAdView) {
        adView.setupView(ad.banner)
    }

    override fun registerView(ad: NativeBannerAd, adView: View) {
        ad.registerView(adView)
    }

    override fun unRegisterView(ad: NativeBannerAd?) {
        ad?.unregisterView()
    }

    override fun onLoad(banner: NativeBanner, nativeAd: NativeBannerAd) {
        adCalls++
        nativeList.append(nativeList.size() * 6 + 3, nativeAd)
        if (isLoaded()) {
            everythingLoaded()
        }
    }

    override fun onClick(nativeAd: NativeBannerAd) {
    }

    override fun onNoAd(adLoadingError: IAdLoadingError, nativeAd: NativeBannerAd) {
        Log.d(TAG, "onNoAd() called with: adLoadingError = $adLoadingError, nativeAd = $nativeAd")
        adCalls++
        if (isLoaded()) {
            callNoAd(adLoadingError.message)
        }
    }

    override fun onShow(nativeAd: NativeBannerAd) {
    }
}