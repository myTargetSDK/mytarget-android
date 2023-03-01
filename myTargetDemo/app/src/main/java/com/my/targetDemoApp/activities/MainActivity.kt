package com.my.targetDemoApp.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import com.my.target.ads.MyTargetView
import com.my.target.common.MyTargetVersion
import com.my.targetDemoApp.AdvertisingType
import com.my.targetDemoApp.CustomAdvertisingType
import com.my.targetDemoApp.ItemsAdapter
import com.my.targetDemoApp.PlusDialogFragment
import com.my.targetDemoApp.R
import com.my.targetDemoApp.Saver
import com.my.targetDemoApp.databinding.ActivityMainBinding
import com.my.targetDemoApp.helpers.InterstitialHelper
import com.my.targetDemoApp.helpers.RewardedHelper

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainActivityAdapter: ItemsAdapter
    private lateinit var saver: Saver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.subtitle = "SDK Version: ${MyTargetVersion.VERSION}"
        saver = Saver(this)
        binding.fab.setOnClickListener { addElement() }

        val types = ArrayList<ItemsAdapter.ListItem>()
        types.add(ItemsAdapter.ListItem({ goBanners() }, getString(R.string.standard_banners),
                getString(R.string.standard_banners_desc)))
        types.add(ItemsAdapter.ListItem({ goInterstitials() }, getString(R.string.interstitial_ads),
                getString(R.string.interstitial_ads_desc)))
        types.add(ItemsAdapter.ListItem({
            goCustom(CustomAdvertisingType(CustomAdvertisingType.AdType.REWARDED,
                    AdvertisingType.REWARDED.defaultSlot))
        }, getString(R.string.rewarded_ads), getString(R.string.rewarded_ads_desc)))
        types.add(ItemsAdapter.ListItem({ goNativeAd() }, getString(R.string.native_ads),
                getString(R.string.native_ads_desc)))
        types.add(ItemsAdapter.ListItem({ goNativeAdConfiguration() }, getString(R.string.native_ads_configuration),
                                        getString(R.string.native_ads_configuration_desc)))
        types.add(ItemsAdapter.ListItem({ goNativeBanner() }, getString(R.string.native_banner_ads),
                getString(R.string.native_banner_ads_desc)))
        types.add(ItemsAdapter.ListItem({ goInstream() }, getString(R.string.instream_ads),
                getString(R.string.instream_ads_desc)))
        types.add(ItemsAdapter.ListItem({ goAudioInstream() }, getString(R.string.audio_instream_ads),
                                        getString(R.string.audio_instream_ads_desc)))
        val size = types.size

        mainActivityAdapter = ItemsAdapter { saver.remove(it - size) }

        binding.mainRecycler.adapter = mainActivityAdapter
        binding.mainRecycler.layoutManager = LinearLayoutManager(this, VERTICAL, false)
        val callback = TouchCallback { mainActivityAdapter.deleteItem(it) }
        val helper = ItemTouchHelper(callback)
        helper.attachToRecyclerView(binding.mainRecycler)

        callback.protectedTypesSize = size

        val restore = saver.restore()
        restore?.forEach {
            types.add(createListItem(it))
        }

        mainActivityAdapter.setItems(types)
    }

    private fun goBanners(slot: Int? = null, adSize: Int? = null, params: String? = null) {
        val intent = Intent(this, BannersActivity::class.java)
        if (slot != null && adSize != null) {
            intent.putExtra(BannersActivity.KEY_SLOT, slot)
            intent.putExtra(BannersActivity.KEY_SIZE, adSize)
            intent.putExtra(BannersActivity.KEY_PARAMS, params)
        }
        startActivity(intent)
    }

    private fun goInterstitials() {
        startActivity(Intent(this, InterstitialsActivity::class.java))
    }

    private fun goNativeAdConfiguration(slot: Int? = null, params: String? = null) {
        val intent = Intent(this, NativeAdConfigurationActivity::class.java)
        if (slot != null) {
            intent.putExtra(NativeAdConfigurationActivity.KEY_SLOT, slot)
            intent.putExtra(NativeAdConfigurationActivity.KEY_PARAMS, params)
        }
        startActivity(intent)
    }

    private fun goNativeAd(slot: Int? = null, params: String? = null) {
        val intent = Intent(this, NativeAdActivity::class.java)
        if (slot != null) {
            intent.putExtra(NativeAdActivity.KEY_SLOT, slot)
            intent.putExtra(NativeAdActivity.KEY_PARAMS, params)
        }
        startActivity(intent)
    }

    private fun goNativeBanner(slot: Int? = null, params: String? = null) {
        val intent = Intent(this, NativeBannerActivity::class.java)
        if (slot != null) {
            intent.putExtra(NativeBannerActivity.KEY_SLOT, slot)
            intent.putExtra(NativeBannerActivity.KEY_PARAMS, params)

        }
        startActivity(intent)
    }

    private fun goInstream(slot: Int? = null, params: String? = null) {
        val intent = Intent(this, InstreamActivity::class.java)
        if (slot != null) {
            intent.putExtra(InstreamActivity.KEY_SLOT, slot)
            intent.putExtra(InstreamActivity.KEY_PARAMS, params)
        }
        startActivity(intent)
    }

    private fun goAudioInstream(slot: Int? = null, params: String? = null) {
        val intent = Intent(this, AudioInstreamActivity::class.java)
        if (slot != null) {
            intent.putExtra(InstreamActivity.KEY_SLOT, slot)
            intent.putExtra(InstreamActivity.KEY_PARAMS, params)
        }
        startActivity(intent)
    }

    private fun goCustom(adType: CustomAdvertisingType) {
        val slot = adType.slotId ?: return
        when (adType.adType) {
            CustomAdvertisingType.AdType.STANDARD_320X50   -> {
                goBanners(slot, MyTargetView.AdSize.BANNER_320x50, adType.params)
            }
            CustomAdvertisingType.AdType.STANDARD_300X250  -> {
                goBanners(slot, MyTargetView.AdSize.BANNER_300x250, adType.params)
            }
            CustomAdvertisingType.AdType.STANDARD_728X90   -> {
                goBanners(slot, MyTargetView.AdSize.BANNER_728x90, adType.params)
            }
            CustomAdvertisingType.AdType.STANDARD_ADAPTIVE -> {
                goBanners(slot, MyTargetView.AdSize.BANNER_ADAPTIVE, adType.params)
            }
            CustomAdvertisingType.AdType.NATIVE_AD         -> {
                goNativeAd(slot, adType.params)
            }
            CustomAdvertisingType.AdType.NATIVE_AD_CONFIGURATIONS -> {
                goNativeAdConfiguration(slot, adType.params)
            }
            CustomAdvertisingType.AdType.NATIVE_BANNER     -> {
                goNativeBanner(slot, adType.params)
            }
            CustomAdvertisingType.AdType.INSTREAM          -> {
                goInstream(slot, adType.params)
            }

            CustomAdvertisingType.AdType.AUDIO_INSTREAM    -> {
                goAudioInstream(slot, adType.params)
            }
            CustomAdvertisingType.AdType.INTERSTITIAL      -> {
                val helper = InterstitialHelper(binding.mainRecycler)
                helper.init(slot, true, adType.params)
            }
            CustomAdvertisingType.AdType.REWARDED          -> {
                val helper = RewardedHelper(binding.mainRecycler)
                helper.init(slot, true, adType.params)
            }
        }
    }

    private fun addElement() {
        val ft = supportFragmentManager.beginTransaction()
        val prev = supportFragmentManager.findFragmentByTag("dialog")
        if (prev != null) {
            ft.remove(prev)
        }
        ft.addToBackStack(null)

        val newFragment = PlusDialogFragment { adType ->
            saver.save(adType)
            addCustomTypeToList(adType)
        }
        newFragment.show(ft, "dialog")
    }

    private fun addCustomTypeToList(adType: CustomAdvertisingType) {
        mainActivityAdapter.addItem(createListItem(adType))
    }

    private fun createListItem(adType: CustomAdvertisingType): ItemsAdapter.ListItem {
        val params = adType.params
        val paramsDescription = when {
            !params.isNullOrEmpty() -> {
                ", Params: $params"
            }
            else           -> {
                ""
            }
        }
        return ItemsAdapter.ListItem({ goCustom(adType) }, adType.name,
                "Slot ID ${adType.slotId}$paramsDescription")
    }

    class TouchCallback(private val swipeListener: (position: Int) -> Unit) :
            ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                    ItemTouchHelper.LEFT) {
        var protectedTypesSize: Int = 0

        override fun onMove(p0: androidx.recyclerview.widget.RecyclerView,
                            p1: androidx.recyclerview.widget.RecyclerView.ViewHolder,
                            p2: androidx.recyclerview.widget.RecyclerView.ViewHolder): Boolean {
            return true
        }

        override fun onSwiped(p0: androidx.recyclerview.widget.RecyclerView.ViewHolder, p1: Int) {
            swipeListener.invoke(p0.absoluteAdapterPosition)
        }

        override fun getSwipeDirs(recyclerView: androidx.recyclerview.widget.RecyclerView,
                                  viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder): Int {
            return if (viewHolder.absoluteAdapterPosition < protectedTypesSize) {
                0
            }
            else {
                super.getSwipeDirs(recyclerView, viewHolder)
            }
        }
    }
}
