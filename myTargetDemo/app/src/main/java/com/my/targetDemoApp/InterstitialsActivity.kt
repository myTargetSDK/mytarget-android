package com.my.targetDemoApp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_interstitials.*
import kotlinx.android.synthetic.main.content_interstitials.*

class InterstitialsActivity : AppCompatActivity() {

    private lateinit var interstitialHelper: InterstitialHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interstitials)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        interstitialHelper = InterstitialHelper(interstitials_root)

        rbt_promo_static.isChecked = true

        btn_load.setOnClickListener { load(getAdType()) }
        btn_gointerstitial.setOnClickListener { goInterstitial() }
        btn_godialog.setOnClickListener { goDialog() }
    }

    private fun load(adType: AdvertisingType) {
        interstitialHelper.destroy()
        interstitialHelper.init(adType.defaultSlot)
    }

    private fun getAdType(): AdvertisingType {
        return when {
            rbt_promo_static.isChecked -> AdvertisingType.INTERSTITIAL_PROMO_STATIC
            rbt_promo_video.isChecked  -> AdvertisingType.INTERSTITIAL_PROMO_VIDEO
            rbt_black_theme.isChecked  -> AdvertisingType.INTERSTITIAL_PROMO_VIDEO_BLACK

            rbt_vast.isChecked         -> AdvertisingType.INTERSTITIAL_VAST
            rbt_image.isChecked        -> AdvertisingType.INTERSTITIAL_IMAGE
            rbt_carousel.isChecked     -> AdvertisingType.INTERSTITIAL_CAROUSEL
            rbt_html.isChecked         -> AdvertisingType.INTERSTITIAL_HTML
            else                       -> AdvertisingType.INTERSTITIAL_PROMO_STATIC
        }
    }

    private fun goInterstitial() {
        interstitialHelper.show()
    }

    private fun goDialog() {
        interstitialHelper.showDialog()
    }

}
