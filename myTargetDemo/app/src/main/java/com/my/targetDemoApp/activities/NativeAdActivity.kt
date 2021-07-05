package com.my.targetDemoApp.activities

import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.my.targetDemoApp.AdvertisingType
import com.my.targetDemoApp.R
import com.my.targetDemoApp.databinding.ActivityNativesBinding
import com.my.targetDemoApp.helpers.NativeAdHelper

class NativeAdActivity : AppCompatActivity() {

    private var customBannerShowing: Boolean = false
    private lateinit var binding: ActivityNativesBinding
    private lateinit var nativeAdHelper: NativeAdHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNativesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.rbtNone.isChecked = true

        nativeAdHelper = NativeAdHelper(binding.nativesRoot)

        val customSlot = intent.getIntExtra(KEY_SLOT, -1)
        if (customSlot >= 0) {
            customBannerShowing = true
            loadAndShow(customSlot, intent.getStringExtra(KEY_PARAMS))
        }

        binding.btnGonative.setOnClickListener { loadAndShow(getAdType().defaultSlot, null) }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                if (binding.nativeContainer.visibility == View.VISIBLE && !customBannerShowing) {
                    closeOverlay()
                    return true
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showNativeAd() {
        binding.nativeContainer.visibility = View.VISIBLE
        binding.llStatus.visibility = View.GONE
        val recycler = nativeAdHelper.createRecycler()

        binding.nativeContainer.removeAllViews()

        val lp = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
        lp.gravity = Gravity.CENTER
        binding.nativeContainer.addView(recycler, 0, lp)
        binding.nativeContainer.visibility = View.VISIBLE
    }

    override fun onBackPressed() {
        if (binding.nativeContainer.visibility == View.VISIBLE) {
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
            binding.nativeContainer.removeAllViews()
            binding.nativeContainer.visibility = View.GONE
            binding.llStatus.visibility = View.GONE
        }
    }

    private fun getAdType(): AdvertisingType {
        return when {
            binding.rbtVideo.isChecked -> AdvertisingType.NATIVE_AD_VIDEO
            binding.rbtCards.isChecked -> AdvertisingType.NATIVE_AD_CAROUSEL
            else                       -> AdvertisingType.NATIVE_AD
        }
    }

    private fun loadAndShow(customSlot: Int, params: String?) {
        binding.nativeContainer.visibility = View.VISIBLE
        binding.llStatus.visibility = View.VISIBLE
        binding.progressBar.visibility = View.VISIBLE
        binding.tvStatus.text = getString(R.string.loading)
        nativeAdHelper.load(customSlot, params, binding.nativesRoot, { showNativeAd() }, {
            hideNative()
            binding.nativeContainer.visibility = View.VISIBLE
            binding.llStatus.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
            val s = String.format(getString(R.string.error_msg), it)
            binding.tvStatus.text = s
        })
    }

    private fun hideNative() {
        binding.nativeContainer.visibility = View.GONE
        binding.nativeContainer.removeAllViews()
        binding.llStatus.visibility = View.GONE
    }

    companion object {
        var KEY_SLOT = "slotId"
        var KEY_PARAMS = "params"
    }

}
