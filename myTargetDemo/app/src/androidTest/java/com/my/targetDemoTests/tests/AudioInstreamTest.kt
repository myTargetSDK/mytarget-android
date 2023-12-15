package com.my.targetDemoTests.tests

import com.facebook.testing.screenshot.Screenshot
import com.my.targetDemoApp.activities.AudioInstreamActivity
import com.my.targetDemoTests.helpers.Callback
import com.my.targetDemoTests.screens.AudioInstreamScreen
import com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn
import com.adevinta.android.barista.interaction.BaristaSleepInteractions.sleep
import com.adevinta.android.barista.rule.BaristaRule
import io.qameta.allure.android.runners.AllureAndroidJUnit4
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AllureAndroidJUnit4::class)
class AudioInstreamTest : TestBase()
{

    @get:Rule
    var baristaRule = BaristaRule.create(AudioInstreamActivity::class.java)

    @Before
    override fun setUp()
    {
        super.setUp()
        baristaRule.launchActivity()
    }

    @Test
    fun test_InstreamLoadAdSnapshot()
    {
        val instreamScreen = AudioInstreamScreen()
        clickOn(instreamScreen.pauseBtn)
        clickOn(instreamScreen.resumeBtn)
        clickOn(instreamScreen.stopBtn)
        clickOn(instreamScreen.loadBtn)
        clickOn(instreamScreen.video)
        sleep(1000)
        Screenshot.snapActivity(baristaRule.activityTestRule.activity).record()
    }

    @Test
    fun test_InstreamPause()
    {
        val instreamScreen = AudioInstreamScreen()
        clickOn(instreamScreen.loadBtn)
        device.wait(callback = "[myTarget]: done")
        clickOn(instreamScreen.preRollBtn)
        sleep(1000)
        clickOn(instreamScreen.pauseBtn)
        assertTrue(device.wait(callback = Callback.pause))
    }

    @Test
    fun test_InstreamResume()
    {
        val instreamScreen = AudioInstreamScreen()
        clickOn(instreamScreen.loadBtn)
        sleep(1000)
        device.wait(callback = "[myTarget]: done")
        clickOn(instreamScreen.preRollBtn)
        clickOn(instreamScreen.pauseBtn)
        clickOn(instreamScreen.resumeBtn)
        assertTrue(device.wait(callback = Callback.resume))
    }

    @Test
    fun test_InstreamStop()
    {
        val instreamScreen = AudioInstreamScreen()
        clickOn(instreamScreen.play)
        clickOn(instreamScreen.loadBtn)
        sleep(3000)
        clickOn(instreamScreen.preRollBtn)
        sleep(1000)
        clickOn(instreamScreen.pauseBtn)
        clickOn(instreamScreen.stopBtn)
        clickOn(instreamScreen.resumeBtn)
        assertFalse(device.wait(callback = Callback.resume, timeout = 3000))
    }
}