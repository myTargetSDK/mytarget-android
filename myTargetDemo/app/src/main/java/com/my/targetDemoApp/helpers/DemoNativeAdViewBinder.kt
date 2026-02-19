package com.my.targetDemoApp.helpers

import android.view.View
import android.view.ViewGroup
import com.my.target.nativeads.NativeAdViewBinder
import com.my.target.nativeads.views.IconAdView
import com.my.target.nativeads.views.MediaAdView
import com.my.target.nativeads.views.NativeAdView
import com.my.target.nativeads.views.PromoCardRecyclerView

class DemoNativeAdViewBinder(val nativeAdView: NativeAdView): NativeAdViewBinder
{
    override fun getRootAdView(): ViewGroup = nativeAdView

    override fun getAdvertisingView(): View = nativeAdView.advertisingTextView

    override fun getAgeRestrictionView(): View = nativeAdView.ageRestrictionTextView

    override fun getCtaView(): View = nativeAdView.ctaButtonView

    override fun getDescriptionView(): View = nativeAdView.descriptionTextView

    override fun getDisclaimerView(): View = nativeAdView.disclaimerTextView

    override fun getDomainOrCategoryView(): View = nativeAdView.domainOrCategoryTextView

    override fun getIconView(): IconAdView = nativeAdView.iconView

    override fun getPromoCardRecyclerView(): PromoCardRecyclerView? = nativeAdView.promoCardRecyclerView

    override fun getStarsRatingView(): View = nativeAdView.starsRatingView

    override fun getTitleView(): View = nativeAdView.titleTextView

    override fun getVotesView(): View = nativeAdView.votesTextView

    override fun getMediaAdView(): MediaAdView? = nativeAdView.mediaAdView

    override fun getAdChoicesView(): View? = null
}