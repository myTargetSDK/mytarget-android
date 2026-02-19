package com.my.targetDemoTests.tests

import androidx.media3.ui.PlayerView
import com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn
import com.adevinta.android.barista.interaction.BaristaSleepInteractions.sleep
import com.adevinta.android.barista.rule.BaristaRule
import com.karumi.shot.ScreenshotTest
import com.my.targetDemoApp.activities.AudioInstreamActivity
import com.my.targetDemoTests.helpers.Callback
import com.my.targetDemoTests.screens.AudioInstreamScreen
import io.qameta.allure.android.runners.AllureAndroidJUnit4
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AllureAndroidJUnit4::class)
class AudioInstreamTest : TestBase(), ScreenshotTest
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

        // Force hide exoplayer ui before taking screenshot
        // (so as not to depend on possible different ui exoplayer in different versions of the library)
        baristaRule.activityTestRule.activity.runOnUiThread {
            baristaRule.activityTestRule.activity.findViewById<PlayerView>(instreamScreen.video).hideController()
        }

        sleep(1000)
        compareScreenshot(baristaRule.activityTestRule.activity)
    }

//    @Test
//    fun test_InstreamPause()
//    {
//        val instreamScreen = AudioInstreamScreen()
//        clickOn(instreamScreen.loadBtn)
//        device.wait(callback = "[myTarget]: done")
//        clickOn(instreamScreen.preRollBtn)
//        sleep(1000)
//        clickOn(instreamScreen.pauseBtn)
//        assertTrue(device.wait(callback = Callback.pause))
//    }

//    @Test
//    fun test_InstreamResume()
//    {
//        val instreamScreen = AudioInstreamScreen()
//        clickOn(instreamScreen.loadBtn)
//        sleep(1000)
//        device.wait(callback = "[myTarget]: done")
//        clickOn(instreamScreen.preRollBtn)
//        clickOn(instreamScreen.pauseBtn)
//        clickOn(instreamScreen.resumeBtn)
//        assertTrue(device.wait(callback = Callback.resume))
//    }

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