package com.my.targetDemoTests.tests

import android.view.LayoutInflater
import androidx.test.InstrumentationRegistry
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.GeneralLocation
import androidx.test.espresso.action.GeneralSwipeAction
import androidx.test.espresso.action.Press
import androidx.test.espresso.action.Swipe
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.VerificationModes.times
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.facebook.testing.screenshot.Screenshot
import com.facebook.testing.screenshot.ViewHelpers
import com.my.targetDemoApp.R
import com.my.targetDemoApp.activities.BannersActivity
import com.my.targetDemoApp.activities.InstreamActivity
import com.my.targetDemoApp.activities.InterstitialsActivity
import com.my.targetDemoApp.activities.MainActivity
import com.my.targetDemoApp.activities.NativeAdActivity
import com.my.targetDemoTests.helpers.Slot
import com.my.targetDemoTests.screens.MainScreen
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickBack
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import com.schibsted.spain.barista.interaction.BaristaSleepInteractions.sleep
import com.schibsted.spain.barista.rule.BaristaRule
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainTest : TestBase() {

    @get:Rule
    var baristaRule = BaristaRule.create(MainActivity::class.java)

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
    fun test_MainToolbar() {
        val expectedTitle = "myTarget Demo"
        val title = baristaRule.activityTestRule.activity.supportActionBar?.title
        assertEquals(expectedTitle, title)
    }

    @Test
    fun test_MainSdkVersion() {
        val filter = Regex("(SDK Version: [0-9]{1,2}\\.[0-9]{1,2}\\.[0-9]{1,2}\\.[0-9]{4,5})")
        val subtitle = baristaRule.activityTestRule.activity.supportActionBar?.subtitle
        assertTrue("Subtitle - $subtitle", filter.matches(subtitle!!))
    }

    @Test
    fun test_MainWithCustomUnitSnapshot() {
        MainScreen().addUnit(slot = Slot.nativeAds)
        Screenshot.snap(baristaRule.activityTestRule.activity.findViewById(R.id.main_recycler))
                .record()
    }

    @Test
    fun test_CancelAddingNewUnitSnapshot() {
        clickOn(MainScreen().plus)
        clickOn(MainScreen().cancel)
        Screenshot.snap(baristaRule.activityTestRule.activity.findViewById(R.id.main_recycler))
                .record()
    }

// TODO: обновить тест в рамках автоматизации задачи 4060
//    @Test
//    fun test_AddNewUnitDialogSnapshot() {
//        val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
//        val inflater = LayoutInflater.from(targetContext)
//        val view = inflater.inflate(R.layout.fragment_dialog, null, false)
//        ViewHelpers.setupView(view)
//                .setExactWidthDp(300)
//                .layout()
//        Screenshot.snap(view)
//                .record()
//    }

    @Test
    fun test_CustomStandardUnitSnapshot() {
        MainScreen().addUnit(slot = Slot.standard320x50)
        clickOn("Slot ID ${Slot.standard320x50}")
        sleep(1000)
        Screenshot.snap(baristaRule.activityTestRule.activity.findViewById(R.id.main_recycler))
                .record()
    }

    @Test
    fun test_CustomNativeUnitSnapshot() {
        MainScreen().addUnit(slot = Slot.nativeAds)
        clickOn("Slot ID ${Slot.nativeAds}")
        sleep(1000)
        Screenshot.snap(baristaRule.activityTestRule.activity.findViewById(R.id.main_recycler))
                .record()
    }

    @Test
    fun test_CustomInstreamUnitSnapshot() {
        MainScreen().addUnit(slot = Slot.instream)
        clickOn("Slot ID ${Slot.instream}")
        sleep(1000)
        Screenshot.snap(baristaRule.activityTestRule.activity.findViewById(R.id.main_recycler))
                .record()
    }

    @Test
    fun test_AddInterstitialStaticUnit() {
        val expectedValue = "Slot ID ${Slot.interstitialPromo}"
        MainScreen().addUnit(slot = Slot.interstitialPromo)
        assertDisplayed(expectedValue)
        clickOn(expectedValue)
        intended(hasComponent(InterstitialsActivity::class.java.name), times(0))
    }

    @Test
    fun test_AddInterstitialVideoUnit() {
        val expectedValue = "Slot ID ${Slot.interstitialVideo}"
        MainScreen().addUnit(slot = Slot.interstitialVideo)
        assertDisplayed(expectedValue)
        clickOn(expectedValue)
        intended(hasComponent(InterstitialsActivity::class.java.name), times(0))
    }

    @Test
    fun test_AddInterstitialCarouselUnit() {
        val expectedValue = "Slot ID ${Slot.interstitialCarousel}"
        MainScreen().addUnit(slot = Slot.interstitialCarousel)
        assertDisplayed(expectedValue)
        clickOn(expectedValue)
        intended(hasComponent(InterstitialsActivity::class.java.name), times(0))
    }

    @Test
    fun test_AddNativeAdsUnit() {
        val expectedValue = "Slot ID ${Slot.nativeAds}"
        MainScreen().addUnit(slot = Slot.nativeAds)
        assertDisplayed(expectedValue)
        clickOn(expectedValue)
        intended(hasComponent(NativeAdActivity::class.java.name))
    }

    @Test
    fun test_AddInstreamUnit() {
        val expectedValue = "Slot ID ${Slot.instream}"
        MainScreen().addUnit(slot = Slot.instream)
        assertDisplayed(expectedValue)
        clickOn(expectedValue)
        intended(hasComponent(InstreamActivity::class.java.name))
    }

    @Test
    fun test_Add320x50Unit() {
        val expectedValue = "Slot ID ${Slot.standard320x50}"
        MainScreen().addUnit(slot = Slot.standard320x50)
        assertDisplayed(expectedValue)
        clickOn(expectedValue)
        intended(hasComponent(BannersActivity::class.java.name))
    }

    @Test
    fun test_Add300x250Unit() {
        val expectedValue = "Slot ID ${Slot.standard300x250}"
        MainScreen().addUnit(slot = Slot.standard300x250)
        assertDisplayed(expectedValue)
        clickOn(expectedValue)
        intended(hasComponent(BannersActivity::class.java.name))
    }

    @Test
    fun test_Add728x90Unit() {
        val expectedValue = "Slot ID ${Slot.standard728x90}"
        MainScreen().addUnit(slot = Slot.standard728x90)
        assertDisplayed(expectedValue)
        clickOn(expectedValue)
        intended(hasComponent(BannersActivity::class.java.name))
    }

    @Test
    fun test_AddNewUnitWithZeroSlot() {
        val badSlot = 0
        val expectedValue = "Slot ID $badSlot"
        MainScreen().addUnit(slot = badSlot)
        clickOn(expectedValue)
        clickBack()
        assertDisplayed(expectedValue)
    }

    @Test
    fun test_RemoveUnit() {
        val main = MainScreen()
        main.addUnit(slot = Slot.nativeAds)
        val unitsBefore =
                baristaRule.activityTestRule.activity.findViewById<androidx.recyclerview.widget.RecyclerView>(
                        R.id.main_recycler).adapter!!.itemCount
        onView(withId(R.id.main_recycler)).perform(
                actionOnItemAtPosition<androidx.recyclerview.widget.RecyclerView.ViewHolder>(
                        unitsBefore - 1, GeneralSwipeAction(Swipe.FAST, GeneralLocation.CENTER,
                        GeneralLocation.CENTER_LEFT, Press.FINGER)))
        val unitsAfter =
                baristaRule.activityTestRule.activity.findViewById<androidx.recyclerview.widget.RecyclerView>(
                        R.id.main_recycler).adapter!!.itemCount
        assert(unitsBefore - 1 == unitsAfter)
    }

    @Test
    fun test_GoToBanners() {
        clickOn(MainScreen().banners)
        intended(hasComponent(BannersActivity::class.java.name))
    }

    @Test
    fun test_GoToNativeAds() {
        clickOn(MainScreen().native)
        intended(hasComponent(NativeAdActivity::class.java.name))
    }

    @Test
    fun test_GoToInterstitial() {
        clickOn(MainScreen().interstitial)
        intended(hasComponent(InterstitialsActivity::class.java.name))
    }

    @Test
    fun test_GoToInstream() {
        clickOn(MainScreen().instream)
        intended(hasComponent(InstreamActivity::class.java.name))
    }

    @Test
    fun test_AddNewUnitWithEmptySlot() {
        MainScreen().addUnitWithoutSlot()
        Screenshot.snap(baristaRule.activityTestRule.activity.findViewById(R.id.main_recycler))
                .record()
    }
}