package com.my.targetDemoApp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ItemTouchHelper
import com.my.target.ads.MyTargetView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var mainActivityAdapter: ItemsAdapter
    private lateinit var saver: Saver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        saver = Saver(this)
        fab.setOnClickListener { addElement() }
        mainActivityAdapter = ItemsAdapter()
        main_recycler.adapter = mainActivityAdapter
        main_recycler.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this,
                androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)
        val callback = TouchCallback { mainActivityAdapter.deleteItem(it) }
        val helper = ItemTouchHelper(callback)
        helper.attachToRecyclerView(main_recycler)

        val types = ArrayList<ItemsAdapter.ListItem>()
        types.add(ItemsAdapter.ListItem({ goBanners() }, getString(R.string.standard_banners),
                getString(R.string.standard_banners_desc)))
        types.add(ItemsAdapter.ListItem({ goInterstitials() }, getString(R.string.interstitial_ads),
                getString(R.string.interstitial_ads_desc)))
        types.add(ItemsAdapter.ListItem({
            goCustom(CustomAdvertisingType(CustomAdvertisingType.AdType.REWARDED,
                    AdvertisingType.REWARDED.defaultSlot))
        }, getString(R.string.rewarded_ads), getString(R.string.rewarded_ads_desc)))
        types.add(ItemsAdapter.ListItem({ goNative() }, getString(R.string.native_ads),
                getString(R.string.native_ads_desc)))
        types.add(ItemsAdapter.ListItem({ goInstream() }, getString(R.string.instream_ads),
                getString(R.string.instream_ads_desc)))
        val size = types.size
        callback.protectedTypesSize = size

        mainActivityAdapter.setDeleteListener { saver.remove(it - size) }

        val restore = saver.restore()
        restore?.forEach {
            types.add(createListItem(it))
        }

        mainActivityAdapter.setItems(types)
    }

    private fun goBanners(slot: Int? = null, adSize: Int? = null) {
        val intent = Intent(this, BannersActivity::class.java)
        if (slot != null && adSize != null) {
            intent.putExtra(BannersActivity.KEY_SLOT, slot)
            intent.putExtra(BannersActivity.KEY_SIZE, adSize)
        }
        startActivity(intent)
    }

    private fun goInterstitials() {
        startActivity(Intent(this, InterstitialsActivity::class.java))
    }

    private fun goNative(slot: Int? = null) {
        val intent = Intent(this, NativeActivity::class.java)
        if (slot != null) {
            intent.putExtra(NativeActivity.KEY_SLOT, slot)
        }
        startActivity(intent)
    }

    private fun goInstream(slot: Int? = null) {
        val intent = Intent(this, InstreamActivity::class.java)
        if (slot != null) {
            intent.putExtra(InstreamActivity.KEY_SLOT, slot)
        }
        startActivity(intent)
    }

    private fun goCustom(adType: CustomAdvertisingType) {
        val slot = adType.slotId ?: return
        when (adType.adType) {
            CustomAdvertisingType.AdType.STANDARD_320X50                                     -> {
                goBanners(slot, MyTargetView.AdSize.BANNER_320x50)
            }
            CustomAdvertisingType.AdType.STANDARD_300X250                                    -> {
                goBanners(slot, MyTargetView.AdSize.BANNER_300x250)
            }
            CustomAdvertisingType.AdType.STANDARD_728X90                                     -> {
                goBanners(slot, MyTargetView.AdSize.BANNER_728x90)
            }
            CustomAdvertisingType.AdType.NATIVE                                              -> {
                goNative(slot)
            }
            CustomAdvertisingType.AdType.INSTREAM                                            -> {
                goInstream(slot)
            }
            CustomAdvertisingType.AdType.INTERSTITIAL, CustomAdvertisingType.AdType.REWARDED -> {
                val helper = InterstitialHelper(main_recycler)
                helper.init(slot, true)
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

        val newFragment = PlusDialogFragment()
        newFragment.setSaveTypeListener { adType ->
            saver.save(adType)
            addCustomTypeToList(adType)
        }

        newFragment.show(ft, "dialog")
    }

    private fun addCustomTypeToList(adType: CustomAdvertisingType) {
        mainActivityAdapter.addItem(createListItem(adType))
    }

    private fun createListItem(adType: CustomAdvertisingType): ItemsAdapter.ListItem {
        return ItemsAdapter.ListItem({ goCustom(adType) }, adType.name, "Slot ID ${adType.slotId}")
    }

    class TouchCallback(private val swipeListener: (position: Int) -> Unit) :
            ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                    ItemTouchHelper.LEFT) {
        var protectedTypesSize: Int = 0

        override fun onMove(p0: androidx.recyclerview.widget.RecyclerView, p1: androidx.recyclerview.widget.RecyclerView.ViewHolder,
                            p2: androidx.recyclerview.widget.RecyclerView.ViewHolder): Boolean {
            return true
        }

        override fun onSwiped(p0: androidx.recyclerview.widget.RecyclerView.ViewHolder, p1: Int) {
            swipeListener.invoke(p0.adapterPosition)
        }

        override fun getSwipeDirs(recyclerView: androidx.recyclerview.widget.RecyclerView,
                                  viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder): Int {
            return if (viewHolder.adapterPosition < protectedTypesSize) {
                0
            }
            else {
                super.getSwipeDirs(recyclerView, viewHolder)
            }
        }
    }
}
