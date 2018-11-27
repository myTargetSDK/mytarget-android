package com.my.targetDemoTests.screens

import android.support.test.uiautomator.By
import com.my.targetDemoApp.R
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import com.schibsted.spain.barista.interaction.BaristaSleepInteractions.sleep

class InterstitialScreen: WebViewScreen() {
    val adView = By.res("media_view")
    val cards = By.res("ad_view")
    val image = By.clazz("android.widget.ImageView")
    private val rbtPromoStatic = R.id.rbt_promo_static
    private val rbtPromoVideo = R.id.rbt_promo_video
    private val rbtVideo = R.id.rbt_black_theme
    private val rbtVast = R.id.rbt_vast
    private val rbtImage = R.id.rbt_image
    private val rbtCards = R.id.rbt_carousel
    private val rbtHtml = R.id.rbt_html
    private val loadBtn = R.id.btn_load
    private val showDialogBtn = R.id.btn_godialog
    private val showActivityBtn = R.id.btn_gointerstitial

    fun showWithoutLoad() {
        clickOn(showActivityBtn)
        clickOn(showDialogBtn)
    }

    fun showPromoStatic() {
        clickOn(rbtPromoStatic)
        clickOn(loadBtn)
        sleep(1000)
        clickOn(showActivityBtn)
    }

    fun showPromoStaticInDialog() {
        clickOn(rbtPromoStatic)
        clickOn(loadBtn)
        sleep(1000)
        clickOn(showDialogBtn)
    }

    fun showPromoVideo() {
        clickOn(rbtPromoVideo)
        clickOn(loadBtn)
        sleep(1000)
        clickOn(showActivityBtn)
    }

    fun showPromoVideoInDialog() {
        clickOn(rbtPromoVideo)
        clickOn(loadBtn)
        sleep(1000)
        clickOn(showDialogBtn)
    }

    fun showPromoCards() {
        clickOn(rbtCards)
        clickOn(loadBtn)
        sleep(1000)
        clickOn(showActivityBtn)
    }

    fun showPromoCardsInDialog() {
        clickOn(rbtCards)
        clickOn(loadBtn)
        sleep(1000)
        clickOn(showDialogBtn)
    }

    fun showVideo() {
        clickOn(rbtVideo)
        clickOn(loadBtn)
        sleep(1000)
        clickOn(showActivityBtn)
    }

    fun showVideoInDialog() {
        clickOn(rbtVideo)
        clickOn(loadBtn)
        sleep(1000)
        clickOn(showDialogBtn)
    }

    fun showVast() {
        clickOn(rbtVast)
        clickOn(loadBtn)
        sleep(1000)
        clickOn(showActivityBtn)
    }

    fun showVastInDialog() {
        clickOn(rbtVast)
        clickOn(loadBtn)
        sleep(1000)
        clickOn(showDialogBtn)
    }

    fun showImage() {
        clickOn(rbtImage)
        clickOn(loadBtn)
        sleep(1000)
        clickOn(showActivityBtn)
    }

    fun showImageInDialog() {
        clickOn(rbtImage)
        clickOn(loadBtn)
        sleep(1000)
        clickOn(showDialogBtn)
    }

    fun showHtml() {
        clickOn(rbtHtml)
        clickOn(loadBtn)
        sleep(1000)
        clickOn(showActivityBtn)
    }

    fun showHtmlInDialog() {
        clickOn(rbtHtml)
        clickOn(loadBtn)
        sleep(1000)
        clickOn(showDialogBtn)
    }

}