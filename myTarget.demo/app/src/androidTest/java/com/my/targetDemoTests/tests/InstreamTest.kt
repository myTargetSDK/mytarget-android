package com.my.targetDemoTests.tests

import com.facebook.testing.screenshot.Screenshot
import com.my.targetDemoApp.InstreamActivity
import com.my.targetDemoTests.helpers.Callback
import com.my.targetDemoTests.screens.InstreamScreen
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import com.schibsted.spain.barista.interaction.BaristaSleepInteractions.sleep
import com.schibsted.spain.barista.rule.BaristaRule
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class InstreamTest: TestBase() {

    @get:Rule
    var baristaRule = BaristaRule.create(InstreamActivity::class.java)

    @Before
    override fun setUp() {
        super.setUp()
        baristaRule.launchActivity()
    }

    @Test
    fun test_InstreamLoadAdSnapshot() {
        val instreamScreen = InstreamScreen()
        clickOn(instreamScreen.pauseBtn)
        clickOn(instreamScreen.resumeBtn)
        clickOn(instreamScreen.stopBtn)
        clickOn(instreamScreen.loadBtn)
        clickOn(instreamScreen.video)
        sleep(1000)
        Screenshot.snapActivity(baristaRule.activityTestRule.activity).record()
    }

    @Test
    fun test_InstreamPause() {
        val instreamScreen = InstreamScreen()
        clickOn(instreamScreen.loadBtn)
        sleep(1000)
        clickOn(instreamScreen.play)
        sleep(500)
        clickOn(instreamScreen.pauseBtn)
        assertTrue(device.wait(callback = Callback.pause))
    }

    @Test
    fun test_InstreamResume() {
        val instreamScreen = InstreamScreen()
        clickOn(instreamScreen.loadBtn)
        sleep(1000)
        clickOn(instreamScreen.play)
        sleep(500)
        clickOn(instreamScreen.pauseBtn)
        clickOn(instreamScreen.resumeBtn)
        assertTrue(device.wait(callback = Callback.resume))
    }

    @Test
    fun test_InstreamStop() {
        val instreamScreen = InstreamScreen()
        clickOn(instreamScreen.loadBtn)
        sleep(1000)
        clickOn(instreamScreen.play)
        sleep(500)
        clickOn(instreamScreen.pauseBtn)
        clickOn(instreamScreen.stopBtn)
        clickOn(instreamScreen.resumeBtn)
        assertFalse(device.wait(callback = Callback.resume, timeout = 1000))
    }

}