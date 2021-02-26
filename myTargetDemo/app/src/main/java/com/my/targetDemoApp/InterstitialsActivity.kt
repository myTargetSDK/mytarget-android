package com.my.targetDemoApp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.my.target.ads.InterstitialAd
import com.my.target.common.MyTargetManager
import kotlinx.android.synthetic.main.activity_interstitials.*
import kotlinx.android.synthetic.main.content_interstitials.*

class InterstitialsActivity : AppCompatActivity() {

    private lateinit var interstitialHelper: InterstitialHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interstitials)
        MyTargetManager.setDebugMode(true)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        interstitialHelper = InterstitialHelper(interstitials_root)

        rbt_promo_static.isChecked = true

        btn_load.setOnClickListener { load(getAdType()) }
        btn_gointerstitial.setOnClickListener { goInterstitial() }
    }

    private fun load(adType: AdvertisingType) {
        interstitialHelper.destroy()
        interstitialHelper.init(adType.defaultSlot)
    }

    private fun getAdType(): AdvertisingType {
        return when {
            rbt_promo_static.isChecked        -> AdvertisingType.INTERSTITIAL_PROMO_STATIC
            rbt_promo_video.isChecked         -> AdvertisingType.INTERSTITIAL_PROMO_VIDEO
            rbt_black_theme.isChecked         -> AdvertisingType.INTERSTITIAL_PROMO_VIDEO_BLACK
            rbt_vast.isChecked                -> AdvertisingType.INTERSTITIAL_VAST
            rbt_image.isChecked               -> AdvertisingType.INTERSTITIAL_IMAGE
            rbt_carousel.isChecked            -> AdvertisingType.INTERSTITIAL_CAROUSEL
            rbt_html.isChecked                -> AdvertisingType.INTERSTITIAL_HTML
            rbt_promo_video_style_2.isChecked -> AdvertisingType.INTERSTITIAL_STYLE_2
            else                              -> AdvertisingType.INTERSTITIAL_PROMO_STATIC
        }
    }

    private fun goInterstitial() {
        interstitialHelper.show()
    }
}
