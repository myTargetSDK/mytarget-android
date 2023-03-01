package com.my.targetDemoTests.screens

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.recyclerview.widget.RecyclerView
import com.my.targetDemoApp.R
import com.my.targetDemoTests.helpers.Slot
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import com.schibsted.spain.barista.interaction.BaristaEditTextInteractions.writeTo

class MainScreen {
    val banners = "Banners"
    val interstitial = "Interstitial"
    val native = "Native Ads"
    val instream = "InStream"
    val plus = R.id.fab
    private val adType320x50 = R.id.adtype_banner_320x50
    private val adType300x250 = R.id.adtype_banner_300x250
    private val adType728x90 = R.id.adtype_banner_728x90
    private val adTypeInterstitial = R.id.adtype_interstitial
    private val adTypeInstream = R.id.adtype_instream
    private val adTypeAudioInstream = R.id.adtype_audio_instream
    private val adTypeNative = R.id.adtype_native
    private val slotId = R.id.editText
    private val ok = R.string.ok
    val cancel = R.string.cancel

    fun addUnit(slot: Int) {
        clickOn(plus)
        when(slot) {
            Slot.standard320x50 -> clickOn(adType320x50)
            Slot.standard300x250 -> clickOn(adType300x250)
            Slot.standard728x90 -> clickOn(adType728x90)
            Slot.interstitialPromo,
            Slot.interstitialVideo,
            Slot.interstitialCarousel -> clickOn(adTypeInterstitial)
            Slot.instream -> clickOn(adTypeInstream)
            Slot.audio_instream -> clickOn(adTypeAudioInstream)
            Slot.nativeAds -> clickOn(adTypeNative)
            else -> { clickOn(adType320x50) }
        }
        writeTo(slotId, slot.toString())
        clickOn(ok)
        onView(withId(R.id.main_recycler)).perform(scrollToPosition<androidx.recyclerview.widget.RecyclerView.ViewHolder>(8))

    }

    fun addUnitWithoutSlot() {
        clickOn(plus)
        clickOn(ok)
    }
}