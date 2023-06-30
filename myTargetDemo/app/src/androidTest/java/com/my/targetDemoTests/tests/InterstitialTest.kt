package com.my.targetDemoTests.tests

import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.web.sugar.Web.onWebView
import com.facebook.testing.screenshot.Screenshot
import com.my.target.common.MyTargetActivity
import com.my.targetDemoApp.activities.InterstitialsActivity
import com.my.targetDemoTests.helpers.Callback
import com.my.targetDemoTests.screens.InterstitialScreen
import com.schibsted.spain.barista.rule.BaristaRule
import io.qameta.allure.android.runners.AllureAndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AllureAndroidJUnit4::class)
class InterstitialTest: TestBase() {

    @get:Rule
    var baristaRule = BaristaRule.create(InterstitialsActivity::class.java)

    @Before
    override fun setUp() {
        super.setUp()
        Intents.init()
        baristaRule.launchActivity()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun test_InterstitialMenuSnapshot() {
        Screenshot.snapActivity(baristaRule.activityTestRule.activity).record()
    }

    @Test
    fun test_ShowPromoStatic() {
        val interstitialScreen = InterstitialScreen()
        interstitialScreen.showPromoStatic()
        device.wait(interstitialScreen.adView)
        intended(hasComponent(MyTargetActivity::class.java.name))
    }

    @Test
    fun test_ShowPromoVideo() {
        val interstitialScreen = InterstitialScreen()
        interstitialScreen.showPromoVideo()
        device.wait(interstitialScreen.adView)
        intended(hasComponent(MyTargetActivity::class.java.name))
    }

    @Test
    fun test_ShowVideo() {
        val interstitialScreen = InterstitialScreen()
        interstitialScreen.showVideo()
        device.wait(interstitialScreen.adView)
        intended(hasComponent(MyTargetActivity::class.java.name))
    }

    @Test
    fun test_ShowVideoStyle2() {
        val interstitialScreen = InterstitialScreen()
        interstitialScreen.showVideoStyle2()
        device.wait(interstitialScreen.adView)
        intended(hasComponent(MyTargetActivity::class.java.name))
    }

    @Test
    fun test_ShowImage() {
        val interstitialScreen = InterstitialScreen()
        interstitialScreen.showImage()
        device.wait(interstitialScreen.image)
        intended(hasComponent(MyTargetActivity::class.java.name))
    }

    @Test
    fun test_ShowPromoCards() {
        val interstitialScreen = InterstitialScreen()
        interstitialScreen.showPromoCards()
        device.wait(interstitialScreen.cards)
        intended(hasComponent(MyTargetActivity::class.java.name))
    }

    @Test
    fun test_ShowHtmlCards() {
        val interstitialScreen = InterstitialScreen()
        interstitialScreen.showHtml()
        device.wait(interstitialScreen.webview)
        onWebView().forceJavascriptEnabled()
    }

    @Test
    fun test_ShowFSWithoutLoadSnapshot() {
        val interstitialScreen = InterstitialScreen()
        interstitialScreen.showWithoutLoad()
        device.wait(interstitialScreen.adView, timeout = 3000)
        Screenshot.snapActivity(baristaRule.activityTestRule.activity).record()
    }

    @Test
    fun test_ShowVast() {
        val interstitialScreen = InterstitialScreen()
        interstitialScreen.showVast()
        device.wait(callback = Callback.load)
        intended(hasComponent(MyTargetActivity::class.java.name))
    }
}