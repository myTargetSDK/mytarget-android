package com.my.targetDemoApp

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
import kotlinx.android.synthetic.main.activity_banners.*
import kotlinx.android.synthetic.main.content_banners.*

class BannersActivity : AppCompatActivity() {

    companion object {
        const val KEY_SLOT = "key"
        const val KEY_SIZE = "size"
    }

    private var bannerHelper: BannerHelper = BannerHelper()
    private var bannersAdapter: BannersAdapter = BannersAdapter()
    private var customBannerShowing: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_banners)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val displayMetrics = resources.displayMetrics
        rbt_728x90.isEnabled = Math.max(displayMetrics.heightPixels / displayMetrics.density,
                displayMetrics.widthPixels / displayMetrics.density) > 728

        rbt_320x50.isChecked = true

        btn_gointerstitial.setOnClickListener { goBanner() }
        initFish()

        val customSize = when (intent.getIntExtra(KEY_SIZE, -1)) {
            1    -> MyTargetView.AdSize.ADSIZE_300x250
            2    -> MyTargetView.AdSize.ADSIZE_728x90
            3    -> MyTargetView.AdSize.getAdSizeForCurrentOrientation(this)
            else -> MyTargetView.AdSize.ADSIZE_320x50
        }
        val customSlot = intent.getIntExtra(KEY_SLOT, -1)
        if (customSlot >= 0) {
            banner_container.visibility = VISIBLE
            goBanner(customSize, customSlot)
            customBannerShowing = true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                if (banner_container.visibility == VISIBLE) {
                    closeOverlay()
                    return true
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initFish() {
        rv_fish.layoutManager = LinearLayoutManager(this,
                RecyclerView.VERTICAL, false)
        rv_fish.adapter = bannersAdapter

    }

    override fun onBackPressed() {
        if (banner_container.visibility == VISIBLE) {
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
            banner_container.removeView(bannerHelper.bannerView)
            banner_container.visibility = View.GONE
        }
    }

    private fun goBanner() {
        if (rbt_adaptive_xml.isChecked) {
            startActivity(Intent(this, SimpleBannerActivity::class.java))
            return
        }

        var adSize: MyTargetView.AdSize = MyTargetView.AdSize.ADSIZE_320x50
        var adType: AdvertisingType = AdvertisingType.STANDARD_BANNER_320X50
        when {
            rbt_300x250.isChecked  -> {
                adSize = MyTargetView.AdSize.ADSIZE_300x250
                adType = AdvertisingType.STANDARD_BANNER_300X250
            }
            rbt_728x90.isChecked   -> {
                adSize = MyTargetView.AdSize.ADSIZE_728x90
                adType = AdvertisingType.STANDARD_BANNER_728X90
            }
            rbt_adaptive.isChecked -> {
                adSize = MyTargetView.AdSize.getAdSizeForCurrentOrientation(this)
                adType = AdvertisingType.STANDARD_BANNER_ADAPTIVE
            }
        }

        goBanner(adSize, adType.defaultSlot)
    }

    private fun goBanner(adSize: MyTargetView.AdSize, slot: Int) {
        banner_container.removeView(bannerHelper.bannerView)
        bannerHelper.destroy()
        bannerHelper.load(slot, adSize, banner_container) {
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
        banner_container.addView(bannerHelper.bannerView, lp)
        banner_container.visibility = VISIBLE
    }

    private fun showBannerInsideList() {
        bannersAdapter.adViewInside = bannerHelper.bannerView
        val lp = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                          ViewGroup.LayoutParams.WRAP_CONTENT)
        lp.gravity = BOTTOM or CENTER_HORIZONTAL
        banner_container.visibility = VISIBLE
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
