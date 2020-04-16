package com.my.targetDemoTests.screens

import androidx.test.uiautomator.By
import com.my.targetDemoApp.R
import com.schibsted.spain.barista.interaction.BaristaClickInteractions

class NativeScreen {
    val adView = By.res("com.my.targetDemoApp:id/native_container")
    private val rbtVideo = R.id.rbt_video
    private val rbtCards = R.id.rbt_cards
    private val rbtStatic = R.id.rbt_none
    private val showBtn = R.id.btn_gonative
    val banner = R.id.native_container

    fun showContentStreamStatic() {
        BaristaClickInteractions.clickOn(rbtStatic)
        BaristaClickInteractions.clickOn(showBtn)
    }

    fun showContentStreamVideo() {
        BaristaClickInteractions.clickOn(rbtVideo)
        BaristaClickInteractions.clickOn(showBtn)
    }

    fun showContentStreamCards() {
        BaristaClickInteractions.clickOn(rbtCards)
        BaristaClickInteractions.clickOn(showBtn)
    }

}