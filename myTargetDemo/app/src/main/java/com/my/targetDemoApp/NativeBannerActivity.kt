package com.my.targetDemoApp

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_natives.*
import kotlinx.android.synthetic.main.content_natives.*

class NativeBannerActivity : AppCompatActivity() {

    companion object {
        var KEY_SLOT = "slotId"
    }

    private lateinit var nativeBannerHelper: NativeBannerHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_native_banners)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        nativeBannerHelper = NativeBannerHelper(natives_root)

        val customSlot = intent.getIntExtra(KEY_SLOT, -1)
        if (customSlot >= 0) {
            loadAndShow(customSlot)
        }
        else {
            loadAndShow(AdvertisingType.NATIVE_BANNER.defaultSlot)
        }
    }

    private fun showNative() {
        native_container.visibility = View.VISIBLE
        ll_progress.visibility = View.GONE
        nativeBannerHelper.createRecycler()

        if (nativeBannerHelper.recyclerView == null) {
            return
        }

        native_container.removeAllViews()

        val lp = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
        lp.gravity = Gravity.CENTER
        native_container.addView(nativeBannerHelper.recyclerView, 0, lp)
        native_container.visibility = View.VISIBLE
    }

    private fun loadAndShow(customSlot: Int) {
        native_container.visibility = View.VISIBLE
        ll_progress.visibility = View.VISIBLE
        nativeBannerHelper.load(customSlot, natives_root, { showNative() }, { hideNative() })
    }

    private fun hideNative() {
        native_container.visibility = View.GONE
        ll_progress.visibility = View.GONE
        native_container.removeAllViews()
    }

}
