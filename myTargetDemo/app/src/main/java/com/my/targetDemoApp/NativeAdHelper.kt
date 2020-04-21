package com.my.targetDemoApp

import android.content.Context
import android.view.View
import com.my.target.nativeads.NativeAd
import com.my.target.nativeads.banners.NativePromoBanner
import com.my.target.nativeads.factories.NativeViewsFactory
import com.my.target.nativeads.views.NativeAdView

class NativeAdHelper(parent: View) : NativeAd.NativeAdListener,
        NativeHelper<NativeAd, NativeAdView>(parent) {

    override fun loadAds(slot: Int) {
        val nativeAd = NativeAd(slot, parent.context)
        nativeAd.listener = this
        nativeAd.load()
    }

    override fun createAdView(context: Context): NativeAdView {
        return NativeViewsFactory.getNativeAdView(context)
    }

    override fun setupView(ad: NativeAd, adView: NativeAdView) {
        adView.setupView(ad.banner)
    }

    override fun registerView(ad: NativeAd, adView: NativeAdView) {
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

    override fun onNoAd(s: String, nativeAd: NativeAd) {
        adCalls++
        if (isLoaded()) {
            callNoAd()
        }
    }

    override fun onVideoComplete(nativeAd: NativeAd) {
    }

    override fun onShow(nativeAd: NativeAd) {
    }
}