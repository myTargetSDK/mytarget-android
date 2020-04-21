package com.my.targetDemoApp

import android.content.Context
import android.view.View
import com.my.target.nativeads.NativeBannerAd
import com.my.target.nativeads.banners.NativeBanner
import com.my.target.nativeads.factories.NativeViewsFactory
import com.my.target.nativeads.views.NativeBannerAdView

class NativeBannerHelper(parent: View) : NativeBannerAd.NativeBannerAdListener,
        NativeHelper<NativeBannerAd, NativeBannerAdView>(parent) {

    override fun loadAds(slot: Int) {
        val nativeAd = NativeBannerAd(slot, parent.context)
        nativeAd.listener = this
        nativeAd.load()
    }

    override fun createAdView(context: Context): NativeBannerAdView {
        return NativeViewsFactory.getNativeBannerAdView(context)
    }

    override fun setupView(ad: NativeBannerAd, adView: NativeBannerAdView) {
        adView.setupView(ad.banner)
    }

    override fun registerView(ad: NativeBannerAd, adView: NativeBannerAdView) {
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

    override fun onNoAd(s: String, nativeAd: NativeBannerAd) {
        adCalls++
        if (isLoaded()) {
            callNoAd()
        }
    }

    override fun onShow(nativeAd: NativeBannerAd) {
    }
}