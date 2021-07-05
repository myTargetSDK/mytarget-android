package com.my.targetDemoApp.activities

import android.content.Intent
import android.os.Bundle
import android.view.Gravity.BOTTOM
import android.view.Gravity.CENTER_HORIZONTAL
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.my.target.ads.MyTargetView
import com.my.targetDemoApp.AdvertisingType
import com.my.targetDemoApp.R
import com.my.targetDemoApp.databinding.ActivityBannersBinding
import com.my.targetDemoApp.helpers.BannerHelper
import kotlin.math.max

class BannersActivity : AppCompatActivity() {

    companion object {
        const val KEY_SLOT = "key"
        const val KEY_SIZE = "size"
        const val KEY_PARAMS = "params"
    }

    private lateinit var viewBinding: ActivityBannersBinding

    private var bannerHelper: BannerHelper = BannerHelper()
    private var bannersAdapter: BannersAdapter = BannersAdapter()
    private var customBannerShowing: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityBannersBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        setSupportActionBar(viewBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val displayMetrics = resources.displayMetrics
        viewBinding.rbt728x90.isEnabled = max(displayMetrics.heightPixels / displayMetrics.density,
                displayMetrics.widthPixels / displayMetrics.density) > 728

        viewBinding.rbt320x50.isChecked = true

        viewBinding.btnGointerstitial.setOnClickListener { goBanner() }
        initFish()

        val customSize = when (intent.getIntExtra(KEY_SIZE, -1)) {
            1    -> MyTargetView.AdSize.ADSIZE_300x250
            2    -> MyTargetView.AdSize.ADSIZE_728x90
            3    -> MyTargetView.AdSize.getAdSizeForCurrentOrientation(this)
            else -> MyTargetView.AdSize.ADSIZE_320x50
        }
        val customSlot = intent.getIntExtra(KEY_SLOT, -1)
        if (customSlot >= 0) {
            viewBinding.bannerContainer.visibility = VISIBLE
            goBanner(customSize, customSlot, intent.getStringExtra(KEY_PARAMS))
            customBannerShowing = true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                if (viewBinding.bannerContainer.visibility == VISIBLE) {
                    closeOverlay()
                    return true
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initFish() {
        viewBinding.rvFish.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        viewBinding.rvFish.adapter = bannersAdapter

    }

    override fun onBackPressed() {
        if (viewBinding.bannerContainer.visibility == VISIBLE) {
            closeOverlay()
        }
        else {
            bannerHelper.destroy()
            super.onBackPressed()
        }
    }

    private fun closeOverlay() {
        bannerHelper.destroy()
        if (customBannerShowing) {
            finish()
        }
        else {
            viewBinding.bannerContainer.removeView(bannerHelper.bannerView)
            viewBinding.bannerContainer.visibility = View.GONE
        }
    }

    private fun goBanner() {
        if (viewBinding.rbtAdaptiveXml.isChecked) {
            startActivity(Intent(this, SimpleBannerActivity::class.java))
            return
        }

        var adSize: MyTargetView.AdSize = MyTargetView.AdSize.ADSIZE_320x50
        var adType: AdvertisingType = AdvertisingType.STANDARD_BANNER_320X50
        when {
            viewBinding.rbt300x250.isChecked  -> {
                adSize = MyTargetView.AdSize.ADSIZE_300x250
                adType = AdvertisingType.STANDARD_BANNER_300X250
            }
            viewBinding.rbt728x90.isChecked   -> {
                adSize = MyTargetView.AdSize.ADSIZE_728x90
                adType = AdvertisingType.STANDARD_BANNER_728X90
            }
            viewBinding.rbtAdaptive.isChecked -> {
                adSize = MyTargetView.AdSize.getAdSizeForCurrentOrientation(this)
                adType = AdvertisingType.STANDARD_BANNER_ADAPTIVE
            }
        }

        goBanner(adSize, adType.defaultSlot, null)
    }

    private fun goBanner(adSize: MyTargetView.AdSize, slot: Int, params: String?) {
        viewBinding.bannerContainer.removeView(bannerHelper.bannerView)
        bannerHelper.destroy()
        bannerHelper.load(slot, adSize, params, viewBinding.bannerContainer) {
            if (adSize == MyTargetView.AdSize.ADSIZE_300x250) {
                showBannerInsideList()
            }
            else {
                showBanner()
            }
        }
    }

    private fun showBanner() {
        val lp = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        lp.gravity = BOTTOM or CENTER_HORIZONTAL
        viewBinding.bannerContainer.addView(bannerHelper.bannerView, lp)
        viewBinding.bannerContainer.visibility = VISIBLE
    }

    private fun showBannerInsideList() {
        bannersAdapter.adViewInside = bannerHelper.bannerView
        val lp = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        lp.gravity = BOTTOM or CENTER_HORIZONTAL
        viewBinding.bannerContainer.visibility = VISIBLE
        bannersAdapter.notifyItemChanged(bannersAdapter.adPosition)
    }

    inner class BannersAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        val adPosition = 3

        var adViewInside: MyTargetView? = null

        override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val frame = LayoutInflater.from(p0.context)
                    .inflate(R.layout.item_banner, p0, false)
            return object : RecyclerView.ViewHolder(frame) {}
        }

        override fun getItemCount(): Int {
            return 10
        }

        override fun getItemViewType(position: Int): Int {
            if (position == adPosition) {
                return 1
            }
            return 0
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            adViewInside?.let {
                if (getItemViewType(position) == 1 && it.parent == null) {
                    (holder.itemView as FrameLayout?)?.removeAllViews()
                    (holder.itemView as FrameLayout?)?.addView(it)
                }
            }
        }

    }
}
