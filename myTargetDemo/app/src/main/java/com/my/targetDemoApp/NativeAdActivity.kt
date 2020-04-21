package com.my.targetDemoApp

import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_natives.*
import kotlinx.android.synthetic.main.content_natives.*

class NativeAdActivity : AppCompatActivity() {

    companion object {
        var KEY_SLOT = "slotId"
    }

    private lateinit var nativeAdHelper: NativeAdHelper
    private var customBannerShowing: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_natives)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        rbt_none.isChecked = true

        nativeAdHelper = NativeAdHelper(natives_root)

        val customSlot = intent.getIntExtra(KEY_SLOT, -1)
        if (customSlot >= 0) {
            customBannerShowing = true
            loadAndShow(customSlot)
        }

        btn_gonative.setOnClickListener { loadAndShow(getAdType().defaultSlot) }
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

    private fun showNativeAd() {
        native_container.visibility = View.VISIBLE
        ll_progress.visibility = View.GONE
        val recycler = nativeAdHelper.createRecycler()

        native_container.removeAllViews()

        val lp = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
        lp.gravity = Gravity.CENTER
        native_container.addView(recycler, 0, lp)
        native_container.visibility = View.VISIBLE
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
            rbt_video.isChecked -> AdvertisingType.NATIVE_AD_VIDEO
            rbt_cards.isChecked -> AdvertisingType.NATIVE_AD_CAROUSEL
            else                -> AdvertisingType.NATIVE_AD
        }
    }

    private fun loadAndShow(customSlot: Int) {
        native_container.visibility = View.VISIBLE
        ll_progress.visibility = View.VISIBLE
        nativeAdHelper.load(customSlot, natives_root, { showNativeAd() }, { hideNative() })
    }

    private fun hideNative() {
        native_container.visibility = View.GONE
        ll_progress.visibility = View.GONE
        native_container.removeAllViews()
    }

}
