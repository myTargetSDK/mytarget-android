package com.my.targetDemoTests.screens

import androidx.test.uiautomator.By
import com.my.targetDemoApp.R
import com.schibsted.spain.barista.interaction.BaristaClickInteractions

class NativeScreen {
    val adView = By.res("com.my.targetDemoApp:id/native_container")
    private val rbtContentStream = R.id.rbt_content_stream
    private val rbtContentWall = R.id.rbt_content_wall
    private val rbtChatList = R.id.rbt_chat_list
    private val rbtNewsFeed = R.id.rbt_news_feed
    private val rbtVideo = R.id.rbt_video
    private val rbtCards = R.id.rbt_cards
    private val rbtStatic = R.id.rbt_none
    private val showBtn = R.id.btn_gonative
    val banner = R.id.native_container

    fun showContentStreamStatic() {
        BaristaClickInteractions.clickOn(rbtContentStream)
        BaristaClickInteractions.clickOn(rbtStatic)
        BaristaClickInteractions.clickOn(showBtn)
    }

    fun showContentStreamVideo() {
        BaristaClickInteractions.clickOn(rbtContentStream)
        BaristaClickInteractions.clickOn(rbtVideo)
        BaristaClickInteractions.clickOn(showBtn)
    }

    fun showContentStreamCards() {
        BaristaClickInteractions.clickOn(rbtContentStream)
        BaristaClickInteractions.clickOn(rbtCards)
        BaristaClickInteractions.clickOn(showBtn)
    }

    fun showContentWallStatic() {
        BaristaClickInteractions.clickOn(rbtContentWall)
        BaristaClickInteractions.clickOn(rbtStatic)
        BaristaClickInteractions.clickOn(showBtn)
    }

    fun showContentWallVideo() {
        BaristaClickInteractions.clickOn(rbtContentWall)
        BaristaClickInteractions.clickOn(rbtVideo)
        BaristaClickInteractions.clickOn(showBtn)
    }

    fun showContentWallCards() {
        BaristaClickInteractions.clickOn(rbtContentWall)
        BaristaClickInteractions.clickOn(rbtCards)
        BaristaClickInteractions.clickOn(showBtn)
    }

    fun showNewsFeed() {
        BaristaClickInteractions.clickOn(rbtNewsFeed)
        BaristaClickInteractions.clickOn(rbtStatic)
        BaristaClickInteractions.clickOn(showBtn)
    }

    fun showChatList() {
        BaristaClickInteractions.clickOn(rbtChatList)
        BaristaClickInteractions.clickOn(rbtStatic)
        BaristaClickInteractions.clickOn(showBtn)
    }

}