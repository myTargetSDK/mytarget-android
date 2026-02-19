package com.my.targetDemoTests.tests

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.withClassName
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.adevinta.android.barista.interaction.BaristaSleepInteractions.sleep
import com.adevinta.android.barista.rule.BaristaRule
import com.karumi.shot.ScreenshotTest
import com.my.targetDemoApp.activities.NativeAdActivity
import com.my.targetDemoTests.screens.NativeScreen
import io.qameta.allure.android.runners.AllureAndroidJUnit4
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@RunWith(AllureAndroidJUnit4::class)
class NativeTest : TestBase(), ScreenshotTest
{

    @get:Rule
    var baristaRule = BaristaRule.create(NativeAdActivity::class.java)
    private val nativeScreen = NativeScreen()

    @Before
    override fun setUp() {
        super.setUp()
        baristaRule.launchActivity()
    }

    @Test
    fun test_NativeMenuSnapshot() {
        compareScreenshot(baristaRule.activityTestRule.activity)
    }

    @Test
    fun test_ShowContentStreamStatic() {
        nativeScreen.showContentStreamStatic()
        device.wait(nativeScreen.adView)
        assertDisplayed(nativeScreen.banner)
    }

    @Test
    fun test_ShowContentStreamVideo() {
        nativeScreen.showContentStreamVideo()
        device.wait(nativeScreen.adView)
        assertDisplayed(nativeScreen.banner)
    }

    @Test
    fun test_ShowContentStreamCards() {
        nativeScreen.showContentStreamCards()
        device.wait(nativeScreen.adView)
        assertDisplayed(nativeScreen.banner)
    }

    @Test
    fun test_ShowInfiniteFeed() {
        nativeScreen.showContentStreamStatic()
        device.wait(nativeScreen.adView)
        assertRecyclerViewSize(size = 21)
        scrollToRecyclerViewPosition(position = 20)
        sleep(1, TimeUnit.SECONDS)
        assertRecyclerViewSize(size = 41)
        scrollToRecyclerViewPosition(position = 40)
        sleep(1, TimeUnit.SECONDS)
        assertRecyclerViewSize(size = 61)
    }

    private fun scrollToRecyclerViewPosition(position: Int) {
        Espresso.onView(withClassName(containsString("androidx.recyclerview.widget.RecyclerView")))
                .perform(scrollToPosition<RecyclerView.ViewHolder>(position))
    }

    private fun assertRecyclerViewSize(size: Int) {
        Espresso.onView(withClassName(containsString("androidx.recyclerview.widget.RecyclerView")))
                .check(RecyclerViewItemCountAssertion(size))
    }
}

class RecyclerViewItemCountAssertion(private val expectedCount: Int) : ViewAssertion {
    override fun check(view: View, noViewFoundException: NoMatchingViewException?) {
        if (noViewFoundException != null) {
            throw noViewFoundException
        }
        val recyclerView = view as RecyclerView
        val adapter = recyclerView.adapter
        assertThat(adapter!!.itemCount, equalTo(expectedCount))
    }
}