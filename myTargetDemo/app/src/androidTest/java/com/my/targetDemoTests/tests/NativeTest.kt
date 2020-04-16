package com.my.targetDemoTests.tests

import com.facebook.testing.screenshot.Screenshot
import com.my.targetDemoApp.NativeAdActivity
import com.my.targetDemoTests.screens.NativeScreen
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.rule.BaristaRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class NativeTest: TestBase() {

    @get:Rule
    var baristaRule = BaristaRule.create(NativeAdActivity::class.java)

    @Before
    override fun setUp() {
        super.setUp()
        baristaRule.launchActivity()
    }

    @Test
    fun test_NativeMenuSnapshot() {
        Screenshot.snapActivity(baristaRule.activityTestRule.activity).record()
    }

    @Test
    fun test_ShowContentStreamStatic() {
        val nativeScreen = NativeScreen()
        nativeScreen.showContentStreamStatic()
        device.wait(nativeScreen.adView)
        assertDisplayed(nativeScreen.banner)
    }

    @Test
    fun test_ShowContentStreamVideo() {
        val nativeScreen = NativeScreen()
        nativeScreen.showContentStreamVideo()
        device.wait(nativeScreen.adView)
        assertDisplayed(nativeScreen.banner)
    }

    @Test
    fun test_ShowContentStreamCards() {
        val nativeScreen = NativeScreen()
        nativeScreen.showContentStreamCards()
        device.wait(nativeScreen.adView)
        assertDisplayed(nativeScreen.banner)
    }

}