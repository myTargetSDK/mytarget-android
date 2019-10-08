package com.my.targetDemoApp

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
        rbt_web.isChecked = true

        btn_gointerstitial.setOnClickListener { goBanner() }
        initFish()

        val customSize = intent.getIntExtra(KEY_SIZE, -1)
        val customSlot = intent.getIntExtra(KEY_SLOT, -1)
        if (customSize >= 0 && customSlot >= 0) {
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
        var adSize: Int = MyTargetView.AdSize.BANNER_320x50
        if (rbt_300x250.isChecked) adSize = MyTargetView.AdSize.BANNER_300x250
        else if (rbt_728x90.isChecked) adSize = MyTargetView.AdSize.BANNER_728x90

        var adType: AdvertisingType = AdvertisingType.STANDARD_BANNER_320X50

        when (adSize) {
            MyTargetView.AdSize.BANNER_320x50  -> when {
                rbt_web.isChecked    -> adType = AdvertisingType.STANDARD_BANNER_320X50
                rbt_html.isChecked   -> adType = AdvertisingType.STANDARD_BANNER_320X50_HTML
            }
            MyTargetView.AdSize.BANNER_300x250 -> when {
                rbt_web.isChecked    -> adType = AdvertisingType.STANDARD_BANNER_300X250_WEB
                rbt_html.isChecked   -> adType = AdvertisingType.STANDARD_BANNER_300X250_HTML
            }
            MyTargetView.AdSize.BANNER_728x90  -> when {
                rbt_web.isChecked    -> adType = AdvertisingType.STANDARD_BANNER_728X90_WEB
                rbt_html.isChecked   -> adType = AdvertisingType.STANDARD_BANNER_728X90_HTML
            }
        }

        goBanner(adSize, adType.defaultSlot)
    }

    private fun goBanner(adSize: Int, slot: Int) {
        banner_container.removeView(bannerHelper.bannerView)
        bannerHelper.destroy()
        bannerHelper.init(slot, adSize, banner_container) {
            if (adSize == MyTargetView.AdSize.BANNER_300x250) {
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

    inner class BannersAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {
        val adPosition = 3

        var adViewInside: MyTargetView? = null

        override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
            val frame = LayoutInflater.from(p0.context).inflate(R.layout.item_banner, p0, false)
            return object : androidx.recyclerview.widget.RecyclerView.ViewHolder(frame) {}
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

        override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
            adViewInside?.let {
                if (getItemViewType(position) == 1 && it.parent == null) {
                    (holder.itemView as FrameLayout?)?.removeAllViews()
                    (holder.itemView as FrameLayout?)?.addView(it)
                }
            }
        }

    }

}
