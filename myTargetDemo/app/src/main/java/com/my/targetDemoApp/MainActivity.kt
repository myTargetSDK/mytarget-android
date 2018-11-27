package com.my.targetDemoApp

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
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
        main_recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val callback = TouchCallback { mainActivityAdapter.deleteItem(it) }
        val helper = ItemTouchHelper(callback)
        helper.attachToRecyclerView(main_recycler)

        val types = ArrayList<ItemsAdapter.ListItem>()
        types.add(ItemsAdapter.ListItem({ goBanners() },
                                        getString(R.string.standard_banners),
                                        getString(R.string.standard_banners_desc)))
        types.add(ItemsAdapter.ListItem({ goInterstitials() },
                                        getString(R.string.interstitial_ads),
                                        getString(R.string.interstitial_ads_desc)))
        types.add(ItemsAdapter.ListItem({ goNative() },
                                        getString(R.string.native_ads),
                                        getString(R.string.native_ads_desc)))
        types.add(ItemsAdapter.ListItem({ goInstream() },
                                        getString(R.string.instream_ads),
                                        getString(R.string.instream_ads_desc)))
        callback.protectedTypesSize = types.size

        saver.restore()?.forEach { types.add(createListItem(it)) }

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
        when (adType) {
            CustomAdvertisingType.STANDARD_320X50  -> {
                goBanners(slot, MyTargetView.AdSize.BANNER_320x50)
            }
            CustomAdvertisingType.STANDARD_300X250 -> {
                goBanners(slot, MyTargetView.AdSize.BANNER_300x250)
            }
            CustomAdvertisingType.STANDARD_728X90  -> {
                goBanners(slot, MyTargetView.AdSize.BANNER_728x90)
            }
            CustomAdvertisingType.NATIVE           -> {
                goNative(slot)
            }
            CustomAdvertisingType.INSTREAM         -> {
                goInstream(slot)
            }
            CustomAdvertisingType.INTERSTITIAL     -> {
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
        return ItemsAdapter.ListItem({ goCustom(adType) },
                                     "Custom ${adType.toString().toLowerCase()}",
                                     "Slot ID ${adType.slotId}",
                                     {
                                         saver.remove(adType)
                                         mainActivityAdapter.deleteItem(it)
                                     })
    }

    class TouchCallback(private val swipeListener: (position: Int) -> Unit) : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT) {
        var protectedTypesSize: Int = 0

        override fun onMove(p0: RecyclerView,
                            p1: RecyclerView.ViewHolder,
                            p2: RecyclerView.ViewHolder): Boolean {
            return true
        }

        override fun onSwiped(p0: RecyclerView.ViewHolder, p1: Int) {
            swipeListener.invoke(p1)
        }

        override fun getSwipeDirs(recyclerView: RecyclerView,
                                  viewHolder: RecyclerView.ViewHolder): Int {
            return if (viewHolder.adapterPosition < protectedTypesSize) {
                0
            }
            else {
                super.getSwipeDirs(recyclerView, viewHolder)
            }
        }
    }
}
