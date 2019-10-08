package com.my.targetDemoTests.screens

import androidx.test.uiautomator.By
import com.my.targetDemoApp.R
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn

class BannersScreen: WebViewScreen() {
    val adView = By.res("ad_view")
    private val rbtWebview = R.id.rbt_web
    private val rbtHtml = R.id.rbt_html
    private val rbt320x50 = R.id.rbt_320x50
    private val rbt300x250 = R.id.rbt_300x250
    private val rbt728x90 = R.id.rbt_728x90
    private val showBtn = R.id.btn_gointerstitial
    val banner = R.id.banner_container

    fun show320x50Webview() {
        clickOn(rbtWebview)
        clickOn(rbt320x50)
        clickOn(showBtn)
    }

    fun show320x50Html() {
        clickOn(rbtHtml)
        clickOn(rbt320x50)
        clickOn(showBtn)
    }

    fun show300x250Webview() {
        clickOn(rbtWebview)
        clickOn(rbt300x250)
        clickOn(showBtn)
    }

    fun show300x250Html() {
        clickOn(rbtHtml)
        clickOn(rbt300x250)
        clickOn(showBtn)
    }

    fun show728x90Webview() {
        clickOn(rbtWebview)
        clickOn(rbt728x90)
        clickOn(showBtn)
    }

    fun show728x90Html() {
        clickOn(rbtHtml)
        clickOn(rbt728x90)
        clickOn(showBtn)
    }
}