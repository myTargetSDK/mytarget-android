package com.my.targetDemoTests.tests

import androidx.test.espresso.web.sugar.Web.onWebView
import com.facebook.testing.screenshot.Screenshot
import com.my.targetDemoApp.BannersActivity
import com.my.targetDemoTests.helpers.Callback
import com.my.targetDemoTests.screens.BannersScreen
import com.schibsted.spain.barista.rule.BaristaRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class BannersTest : TestBase() {

    @get:Rule
    var baristaRule = BaristaRule.create(BannersActivity::class.java)

    @Before
    override fun setUp() {
        super.setUp()
        baristaRule.launchActivity()
    }

    @Test
    fun test_BannersMenuSnapshot() {
        Screenshot.snapActivity(baristaRule.activityTestRule.activity)
                .record()
    }

    @Test
    fun test_Show320x50Webview() {
        val bannersScreen = BannersScreen()
        bannersScreen.show320x50Webview()
        device.wait(bannersScreen.webview)
        onWebView().forceJavascriptEnabled()
    }

    @Test
    fun test_Show320x50Html() {
        val bannersScreen = BannersScreen()
        bannersScreen.show320x50Html()
        device.wait(bannersScreen.webview)
        onWebView().forceJavascriptEnabled()
    }

    @Test
    fun test_Show300x250Webview() {
        val bannersScreen = BannersScreen()
        bannersScreen.show300x250Webview()
        device.wait(callback = Callback.load)
        onWebView().forceJavascriptEnabled()
    }

    @Test
    fun test_Show300x250Html() {
        val bannersScreen = BannersScreen()
        bannersScreen.show300x250Html()
        device.wait(bannersScreen.webview)
        onWebView().forceJavascriptEnabled()
    }

    @Test
    fun test_Show728x90Webview() {
        val bannersScreen = BannersScreen()
        bannersScreen.show728x90Webview()
        device.wait(bannersScreen.webview)
        onWebView().forceJavascriptEnabled()
    }

    @Test
    fun test_Show728x90Html() {
        val bannersScreen = BannersScreen()
        bannersScreen.show728x90Html()
        device.wait(bannersScreen.webview)
        onWebView().forceJavascriptEnabled()
    }
}