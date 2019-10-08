package com.my.targetDemoApp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.activity_natives.*
import kotlinx.android.synthetic.main.content_natives.*

class NativeActivity : AppCompatActivity() {

    companion object {
        var KEY_SLOT = "slotId"
    }

    private lateinit var nativeHelper: NativeHelper
    private var customBannerShowing: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_natives)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        rbt_none.isChecked = true
        rbt_content_stream.isChecked = true

        nativeHelper = NativeHelper(natives_root)

        val customSlot = intent.getIntExtra(NativeActivity.KEY_SLOT, -1)
        if (customSlot >= 0) {
            customBannerShowing = true
            loadAndShow(customSlot)
        }

        btn_gonative.setOnClickListener { loadAndShow(getAdType().defaultSlot) }
        rg_type.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbt_news_feed, R.id.rbt_chat_list -> {
                    rbt_none.isChecked = true
                    rbt_cards.isEnabled = false
                    rbt_video.isEnabled = false
                }
                else                                   -> {
                    rbt_cards.isEnabled = true
                    rbt_video.isEnabled = true
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                if (native_container.visibility == View.VISIBLE && !customBannerShowing) {
                    closeOverlay()
                    return true
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showNative() {
        native_container.visibility = View.VISIBLE
        ll_progress.visibility = View.GONE
        nativeHelper.createRecycler(getViewType())

        if (nativeHelper.recyclerView == null) {
            return
        }

        native_container.removeAllViews()

        val lp = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                          ViewGroup.LayoutParams.MATCH_PARENT)
        lp.gravity = Gravity.CENTER
        native_container.addView(nativeHelper.recyclerView, 0, lp)
        native_container.visibility = View.VISIBLE
    }

    private fun getViewType(): NativeHelper.NativeViewType {
        return when {
            rbt_content_wall.isChecked -> NativeHelper.NativeViewType.CONTENT_WALL
            rbt_news_feed.isChecked    -> NativeHelper.NativeViewType.NEWS_FEED
            rbt_chat_list.isChecked    -> NativeHelper.NativeViewType.CHAT_LIST
            else                       -> NativeHelper.NativeViewType.CONTENT_STREAM
        }
    }

    override fun onBackPressed() {
        if (native_container.visibility == View.VISIBLE) {
            closeOverlay()
        }
        else {
            super.onBackPressed()
        }
    }

    private fun closeOverlay() {
        if (customBannerShowing) {
            finish()
        }
        else {
            native_container.removeAllViews()
            native_container.visibility = View.GONE
        }
    }

    private fun getAdType(): AdvertisingType {
        return when {
            rbt_video.isChecked -> AdvertisingType.NATIVE_VIDEO
            rbt_cards.isChecked -> AdvertisingType.NATIVE_CAROUSEL
            else                -> AdvertisingType.NATIVE
        }
    }

    private fun loadAndShow(customSlot: Int) {
        native_container.visibility = View.VISIBLE
        ll_progress.visibility = View.VISIBLE
        nativeHelper.load(customSlot, natives_root, { showNative() }, { hideNative() })
    }

    private fun hideNative() {
        native_container.visibility = View.GONE
        ll_progress.visibility = View.GONE
        native_container.removeAllViews()
    }

}
