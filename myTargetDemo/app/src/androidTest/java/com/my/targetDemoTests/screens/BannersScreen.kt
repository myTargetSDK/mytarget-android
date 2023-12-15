package com.my.targetDemoTests.screens

import androidx.test.uiautomator.By
import com.my.targetDemoApp.R
import com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn

class BannersScreen: WebViewScreen() {
    val adView = By.res("ad_view")
    private val rbtAdaptive = R.id.rbt_adaptive
    private val rbtHtml = R.id.rbt_html
    private val rbt320x50 = R.id.rbt_320x50
    private val rbt300x250 = R.id.rbt_300x250
    private val rbt728x90 = R.id.rbt_728x90
    private val showBtn = R.id.btn_gointerstitial
    val banner = R.id.banner_container

    fun showAdaptive() {
        clickOn(rbtAdaptive)
        clickOn(showBtn)
    }

    fun show320x50() {
        clickOn(rbt320x50)
        clickOn(showBtn)
    }

    fun show300x250() {
        clickOn(rbt300x250)
        clickOn(showBtn)
    }

    fun show728x90() {
        clickOn(rbt728x90)
        clickOn(showBtn)
    }
}