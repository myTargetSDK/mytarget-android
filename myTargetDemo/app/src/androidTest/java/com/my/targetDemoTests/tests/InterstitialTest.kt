package com.my.targetDemoTests.tests

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.intent.Intents
import android.support.test.espresso.intent.Intents.intended
import android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent
import android.support.test.espresso.matcher.RootMatchers.isDialog
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.isRoot
import android.support.test.espresso.web.sugar.Web.onWebView
import com.facebook.testing.screenshot.Screenshot
import com.my.target.common.MyTargetActivity
import com.my.targetDemoApp.InterstitialsActivity
import com.my.targetDemoTests.helpers.Callback
import com.my.targetDemoTests.screens.InterstitialScreen
import com.schibsted.spain.barista.rule.BaristaRule
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


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
    fun test_ShowPromoStaticInDialog() {
        val interstitialScreen = InterstitialScreen()
        interstitialScreen.showPromoStaticInDialog()
        device.wait(interstitialScreen.adView)
        onView(isRoot()).inRoot(isDialog()).check(matches(isDisplayed()))
    }

    @Test
    fun test_ShowPromoVideo() {
        val interstitialScreen = InterstitialScreen()
        interstitialScreen.showPromoVideo()
        device.wait(interstitialScreen.adView)
        intended(hasComponent(MyTargetActivity::class.java.name))
    }

    @Test
    fun test_ShowPromoVideoInDialog() {
        val interstitialScreen = InterstitialScreen()
        interstitialScreen.showPromoVideoInDialog()
        device.wait(interstitialScreen.adView)
        onView(isRoot()).inRoot(isDialog()).check(matches(isDisplayed()))
    }

    @Test
    fun test_ShowVideo() {
        val interstitialScreen = InterstitialScreen()
        interstitialScreen.showVideo()
        device.wait(interstitialScreen.adView)
        intended(hasComponent(MyTargetActivity::class.java.name))
    }

    @Test
    fun test_ShowVideoInDialog() {
        val interstitialScreen = InterstitialScreen()
        interstitialScreen.showVideoInDialog()
        device.wait(interstitialScreen.adView)
        onView(isRoot()).inRoot(isDialog()).check(matches(isDisplayed()))
    }

    @Test
    fun test_ShowImage() {
        val interstitialScreen = InterstitialScreen()
        interstitialScreen.showImage()
        device.wait(interstitialScreen.image)
        intended(hasComponent(MyTargetActivity::class.java.name))
    }

    @Test
    fun test_ShowImageInDialog() {
        val interstitialScreen = InterstitialScreen()
        InterstitialScreen().showImageInDialog()
        device.wait(interstitialScreen.image)
        onView(isRoot()).inRoot(isDialog()).check(matches(isDisplayed()))
    }

    @Test
    fun test_ShowPromoCards() {
        val interstitialScreen = InterstitialScreen()
        interstitialScreen.showPromoCards()
        device.wait(interstitialScreen.cards)
        intended(hasComponent(MyTargetActivity::class.java.name))
    }

    @Test
    fun test_ShowPromoCardsInDialog() {
        val interstitialScreen = InterstitialScreen()
        interstitialScreen.showPromoCardsInDialog()
        device.wait(interstitialScreen.cards)
        onView(isRoot()).inRoot(isDialog()).check(matches(isDisplayed()))
    }

    @Test
    fun test_ShowHtmlCards() {
        val interstitialScreen = InterstitialScreen()
        interstitialScreen.showHtml()
        device.wait(interstitialScreen.webview)
        onWebView().forceJavascriptEnabled()
    }

    @Test
    fun test_ShowHtmlInDialog() {
        val interstitialScreen = InterstitialScreen()
        interstitialScreen.showHtmlInDialog()
        device.wait(interstitialScreen.webview)
        onView(isRoot()).inRoot(isDialog()).check(matches(isDisplayed()))
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