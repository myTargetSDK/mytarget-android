package com.my.targetDemoApp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.my.targetDemoApp.AdvertisingType
import com.my.targetDemoApp.databinding.ActivityInterstitialsBinding
import com.my.targetDemoApp.helpers.InterstitialHelper

class InterstitialsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInterstitialsBinding
    private lateinit var interstitialHelper: InterstitialHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInterstitialsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        interstitialHelper = InterstitialHelper(binding.interstitialsRoot)

        binding.rbtPromoStatic.isChecked = true

        binding.btnLoad.setOnClickListener { load(getAdType()) }
        binding.btnGointerstitial.setOnClickListener { goInterstitial() }
    }

    private fun load(adType: AdvertisingType) {
        interstitialHelper.destroy()
        interstitialHelper.init(adType.defaultSlot)
    }

    private fun getAdType(): AdvertisingType {
        return when {
            binding.rbtPromoStatic.isChecked      -> AdvertisingType.INTERSTITIAL_PROMO_STATIC
            binding.rbtPromoVideo.isChecked       -> AdvertisingType.INTERSTITIAL_PROMO_VIDEO
            binding.rbtBlackTheme.isChecked       -> AdvertisingType.INTERSTITIAL_PROMO_VIDEO_BLACK
            binding.rbtVast.isChecked             -> AdvertisingType.INTERSTITIAL_VAST
            binding.rbtImage.isChecked            -> AdvertisingType.INTERSTITIAL_IMAGE
            binding.rbtCarousel.isChecked         -> AdvertisingType.INTERSTITIAL_CAROUSEL
            binding.rbtHtml.isChecked             -> AdvertisingType.INTERSTITIAL_HTML
            binding.rbtPromoVideoStyle2.isChecked -> AdvertisingType.INTERSTITIAL_STYLE_2
            else                                  -> AdvertisingType.INTERSTITIAL_PROMO_STATIC
        }
    }

    private fun goInterstitial() {
        interstitialHelper.show()
    }
}
