package com.my.targetDemoApp.activities

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.my.targetDemoApp.AdvertisingType
import com.my.targetDemoApp.R
import com.my.targetDemoApp.databinding.ActivityNativeBannersBinding
import com.my.targetDemoApp.helpers.NativeBannerHelper

class NativeBannerActivity : AppCompatActivity() {

    companion object {
        var KEY_SLOT = "slotId"
        var KEY_PARAMS = "params"
    }

    private lateinit var binding: ActivityNativeBannersBinding
    private lateinit var nativeBannerHelper: NativeBannerHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNativeBannersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        nativeBannerHelper = NativeBannerHelper(binding.nativesRoot)

        val customSlot = intent.getIntExtra(KEY_SLOT, -1)
        if (customSlot >= 0) {
            loadAndShow(customSlot, intent.getStringExtra(KEY_PARAMS))
        }
        else {
            loadAndShow(AdvertisingType.NATIVE_BANNER.defaultSlot, null)
        }
    }

    private fun showNative() {
        binding.nativeContainer.visibility = View.VISIBLE
        binding.llStatus.visibility = View.GONE
        nativeBannerHelper.createRecycler()

        val recycler = nativeBannerHelper.createRecycler()

        binding.nativeContainer.removeAllViews()

        val lp = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
        lp.gravity = Gravity.CENTER
        binding.nativeContainer.addView(recycler, 0, lp)
        binding.nativeContainer.visibility = View.VISIBLE
    }

    private fun loadAndShow(customSlot: Int, params: String?) {
        binding.nativeContainer.visibility = View.VISIBLE
        binding.llStatus.visibility = View.VISIBLE
        binding.progressBar.visibility = View.VISIBLE
        binding.tvStatus.text = getString(R.string.loading)
        nativeBannerHelper.load(customSlot, params, binding.nativesRoot, { showNative() }, {
            hideNative()
            binding.llStatus.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
            val s = String.format(getString(R.string.error_msg), it)
            binding.tvStatus.text = s
        })
    }

    private fun hideNative() {
        binding.nativeContainer.visibility = View.GONE
        binding.llStatus.visibility = View.GONE
        binding.nativeContainer.removeAllViews()
    }

}
