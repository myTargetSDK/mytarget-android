package com.my.targetDemoApp.helpers

import android.view.View
import android.view.ViewGroup
import com.my.target.nativeads.NativeBannerAdViewBinder
import com.my.target.nativeads.views.IconAdView
import com.my.target.nativeads.views.NativeBannerAdView

class DemoNativeBannerAdViewBinder(val nativeBannerAdView: NativeBannerAdView) : NativeBannerAdViewBinder
{
    override fun getRootAdBannerView(): ViewGroup = nativeBannerAdView

    override fun getAgeRestrictionView(): View = nativeBannerAdView.ageRestrictionTextView

    override fun getAdvertisingView(): View = nativeBannerAdView.advertisingTextView

    override fun getIconView(): IconAdView = nativeBannerAdView.iconView

    override fun getTitleView(): View = nativeBannerAdView.titleTextView

    override fun getDomainView(): View = nativeBannerAdView.domainTextView

    override fun getCtaView(): View = nativeBannerAdView.ctaButtonView

    override fun getVotesView(): View = nativeBannerAdView.votesTextView

    override fun getStarsRatingView(): View = nativeBannerAdView.starsRatingView

    override fun getDisclaimerView(): View = nativeBannerAdView.disclaimerTextView

    override fun getAdChoicesView(): View? = null
}