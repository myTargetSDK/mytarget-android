package com.my.targetDemoApp.activities

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.my.target.common.menu.Menu
import com.my.target.common.menu.MenuFactory
import com.my.target.common.models.IAdLoadingError
import com.my.target.nativeads.AdChoicesPlacement
import com.my.target.nativeads.NativeAd
import com.my.target.nativeads.banners.NativePromoBanner
import com.my.target.nativeads.factories.NativeViewsFactory
import com.my.targetDemoApp.AdvertisingType
import com.my.targetDemoApp.addParsedString
import com.my.targetDemoApp.databinding.ActivityNativeConfigurationBinding
import com.my.targetDemoApp.helpers.AdChoicesMenuFactory

class NativeAdConfigurationActivity : AppCompatActivity(),
                                      NativeAd.NativeAdChoicesOptionListener, MenuFactory
{
    private lateinit var binding: ActivityNativeConfigurationBinding

    var nativeAd: NativeAd? = null
    var params: String? = null
    var nativeAdSlot = -1

    val demoNativeAdListener = object : NativeAd.NativeAdListener {
        override fun onLoad(banner: NativePromoBanner, ad: NativeAd)
        {
            val nativeAdView = NativeViewsFactory.getNativeAdView(this@NativeAdConfigurationActivity)
            nativeAdView.setupView(ad.banner)

            binding.apply {
                llStatus.visibility = View.GONE
                nativeRootContainer.visibility = View.VISIBLE
                nativeContainer.addView(nativeAdView)
                ibAdhcoicesIcon.setImageBitmap(ad.banner?.adChoicesIcon?.bitmap)
            }

            ad.registerView(nativeAdView)
        }

        override fun onNoAd(adLoadingError: IAdLoadingError, ad: NativeAd) {
            Log.d(TAG, "onNoAd() called with: adLoadingError = $adLoadingError, ad = $ad")
            hideNative()
        }

        @Deprecated("Deprecated in SDK")
        override fun onClick(ad: NativeAd) {
        }

        override fun onClick(clickedView: View?, ad: NativeAd) {
            Log.d(TAG, "onClick")
        }

        override fun onShow(ad: NativeAd) {
            Log.d(TAG, "onShow")
        }

        @Deprecated("Deprecated in myTarget SDK")
        override fun onVideoPlay(ad: NativeAd) {
            // Use NativeAd.NativeAdVideoListener, see below
        }

        @Deprecated("Deprecated in myTarget SDK")
        override fun onVideoPause(ad: NativeAd) {
            // Use NativeAd.NativeAdVideoListener, see below
        }

        @Deprecated("Deprecated in myTarget SDK")
        override fun onVideoComplete(ad: NativeAd) {
            // Use NativeAd.NativeAdVideoListener, see below
        }
    }

    val demoNativeAdVideoListener = object : NativeAd.NativeAdVideoListener {
        override fun onVideoStart(ad: NativeAd) {
            Log.d(TAG, "demoNativeAdVideoListener")
        }

        override fun onVideoPause(ad: NativeAd) {
            Log.d(TAG, "onVideoPause")
        }

        override fun onVideoResume(ad: NativeAd) {
            Log.d(TAG, "onVideoResume")
        }

        override fun onVideoComplete(ad: NativeAd) {
            Log.d(TAG, "onVideoComplete")
        }

        override fun onVideoReplay(ad: NativeAd) {
            Log.d(TAG, "onVideoReplay")
        }

        override fun onVideoProgress(
            position: Float, // seconds
            actualDuration: Float, // seconds
            ad: NativeAd
        ) {
            Log.d(TAG, "onVideoProgress, position=$position s, actualDuration=$actualDuration s")
        }

        override fun onVideoError(message: String, ad: NativeAd) {
            Log.d(TAG, "onVideoError, message=$message")
        }

        override fun onVideoVolumeChanged(volume: Float, ad: NativeAd)
        {
            Log.d(TAG, "onVideoVolumeChanged, volume=$volume")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityNativeConfigurationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.applySimpleWindowInsetsListener()

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        params = intent.getStringExtra(KEY_PARAMS)
        nativeAdSlot = intent.getIntExtra(KEY_SLOT, AdvertisingType.NATIVE_AD.defaultSlot)

        binding.ibAdhcoicesIcon.setOnClickListener {
            nativeAd?.handleAdChoicesClick(this)
        }
        binding.btnGonative.setOnClickListener {
            loadAndShow(nativeAdSlot, params)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        if (binding.nativeRootContainer.visibility == View.VISIBLE)
        {
            closeOverlay()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed()
    {
        if (binding.nativeRootContainer.visibility == View.VISIBLE)
        {
            closeOverlay()
        } else
        {
            super.onBackPressed()
        }
    }

    private fun closeOverlay()
    {
        // We must unregister ad view
        nativeAd?.unregisterView()

        binding.nativeContainer.removeAllViews()
        binding.nativeRootContainer.visibility = View.GONE
        binding.llStatus.visibility = View.GONE
    }

    private fun loadAndShow(customSlot: Int, params: String?)
    {
        binding.apply {
            val usingCustomAdChoicesView = cbUseCustomAdchoicesView.isChecked
            ibAdhcoicesIcon.visibility = if (usingCustomAdChoicesView) View.VISIBLE else View.GONE
            nativeRootContainer.visibility = View.VISIBLE
            llStatus.visibility = View.VISIBLE
            progressBar.visibility = View.VISIBLE

            nativeAd = NativeAd(
                customSlot,
                if (cbDrawAdchoicesOptions.isChecked) this@NativeAdConfigurationActivity else null,
                this@NativeAdConfigurationActivity
            )

            nativeAd?.apply {
                listener = demoNativeAdListener // required listener
                nativeAdVideoListener =  demoNativeAdVideoListener // optional listener

                adChoicesOptionListener = if (rbtDontUseOptionsListener.isChecked) null else this@NativeAdConfigurationActivity
                adChoicesPlacement = if (usingCustomAdChoicesView) AdChoicesPlacement.DRAWING_MANUAL else AdChoicesPlacement.TOP_RIGHT
                customParams.addParsedString(params)
                load()
            }
        }
    }

    private fun hideNative()
    {
        binding.nativeRootContainer.visibility = View.GONE
        binding.nativeContainer.removeAllViews()
        binding.llStatus.visibility = View.GONE
    }

    override fun createMenu(): Menu
    {
        return AdChoicesMenuFactory()
    }

    override fun onDestroy()
    {
        // We must unregister ad view
        nativeAd?.unregisterView()
        super.onDestroy()
    }

    override fun shouldCloseAutomatically(): Boolean
    {
        if (binding.rbtAutoClose.isChecked)
        {
            Log.d(TAG, "shouldCloseAutomatically is true")
            return true
        }

        if (binding.rbtCloseByDeveloper.isChecked)
        {
            Log.d(TAG, "shouldCloseAutomatically is false")
            return false
        }
        Log.d(TAG, "shouldCloseAutomatically is false")
        return false
    }

    override fun onCloseAutomatically(ad: NativeAd)
    {
        Log.d(TAG, "banner closed automatically")
        binding.ibAdhcoicesIcon.visibility = View.GONE
    }

    override fun closeIfAutomaticallyDisabled(ad: NativeAd)
    {
        Log.d(TAG, "banner closed by developer")
        hideNative()
    }

    companion object
    {
        private const val TAG = "NativeConfigurations:"

        const val KEY_SLOT = "slotId"
        const val KEY_PARAMS = "params"
    }
}