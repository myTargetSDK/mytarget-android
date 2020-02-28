package com.my.targetDemoTests.screens

import androidx.test.uiautomator.By
import com.my.targetDemoApp.R
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import com.schibsted.spain.barista.interaction.BaristaSleepInteractions.sleep

class InterstitialScreen : WebViewScreen() {
    val adView = By.res("media_view")
    val cards = By.res("ad_view")
    val image = By.clazz("android.widget.ImageView")
    private val rbtPromoStatic = R.id.rbt_promo_static
    private val rbtPromoVideo = R.id.rbt_promo_video
    private val rbtVideo = R.id.rbt_black_theme
    private val rbtVideoStyle2 = R.id.rbt_promo_video_style_2
    private val rbtVast = R.id.rbt_vast
    private val rbtImage = R.id.rbt_image
    private val rbtCards = R.id.rbt_carousel
    private val rbtHtml = R.id.rbt_html
    private val loadBtn = R.id.btn_load
    private val showActivityBtn = R.id.btn_gointerstitial

    fun showWithoutLoad() {
        clickOn(showActivityBtn)
    }

    fun showPromoStatic() {
        clickOn(rbtPromoStatic)
        loadAndShow()
    }

    fun showPromoVideo() {
        clickOn(rbtPromoVideo)
        loadAndShow()
    }

    fun showPromoCards() {
        clickOn(rbtCards)
        loadAndShow()
    }

    fun showVideo() {
        clickOn(rbtVideo)
        loadAndShow()
    }

    fun showVideoStyle2() {
        clickOn(rbtVideoStyle2)
        loadAndShow()
    }

    fun showVast() {
        clickOn(rbtVast)
        loadAndShow()
    }

    fun showImage() {
        clickOn(rbtImage)
        loadAndShow()
    }

    fun showHtml() {
        clickOn(rbtHtml)
        loadAndShow()
    }

    private fun loadAndShow() {
        clickOn(loadBtn)
        sleep(1000)
        clickOn(showActivityBtn)
    }
}