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

class NativeBannerHelper(parent: View) : NativeHelper<NativeBannerAd, NativeBannerAdView>(parent) {

    companion object
    {
        const val TAG = "NativeAdHelper"
    }

    val demoNativeBannerAdListener = object : NativeBannerAd.NativeBannerAdListener {

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

    override fun loadAds(slot: Int, params: String?) {
        val nativeAd = NativeBannerAd(slot, parent.context)
        nativeAd.listener = demoNativeBannerAdListener
        nativeAd.customParams.addParsedString(params)
        nativeAd.load()
    }

    override fun createAdView(context: Context): NativeBannerAdView {
        return NativeViewsFactory.getNativeBannerAdView(context)
    }

    override fun setupView(ad: NativeBannerAd, adView: NativeBannerAdView) {
        adView.setupView(ad.banner)
    }

    override fun registerView(ad: NativeBannerAd, adView: NativeBannerAdView) {

        val demoBannerAdBinder = if (true) {
            // NativeBannerAdView has its own binder
            adView.nativeBannerAdViewBinder
        } else {
            // Or we can create application specific view-binder
            DemoNativeBannerAdViewBinder(adView)
        }

        ad.registerView(demoBannerAdBinder)
    }

    override fun unRegisterView(ad: NativeBannerAd?) {
        ad?.unregisterView()
    }
}