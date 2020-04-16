package com.my.targetDemoApp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.my.target.nativeads.NativeBannerAd
import com.my.target.nativeads.banners.NativeBanner
import com.my.target.nativeads.factories.NativeViewsFactory
import com.my.target.nativeads.views.NativeBannerAdView

class NativeBannerHelper(val parent: View) : NativeBannerAd.NativeBannerAdListener, NativeHelper {

    companion object {
        private const val NATIVE_AD_COUNT = 5
    }

    private val nativeList = SparseArrayCompat<NativeBannerAd>()
    private var loaded = false
    override var recyclerView: RecyclerView? = null
    private var afterLoad: (() -> Unit)? = null
    private var afterNoad: (() -> Unit)? = null
    private var bar: Snackbar? = null
    private var adCalls: Int = 0

    override fun createRecycler() {
        if (nativeList.size() == 0) {
            Snackbar.make(parent, "NativeAd is not loaded", Snackbar.LENGTH_SHORT)
                    .show()
            return
        }

        recyclerView = RecyclerView(parent.context)
        recyclerView?.layoutManager =
                androidx.recyclerview.widget.LinearLayoutManager(parent.context,
                        androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)
        recyclerView?.adapter = NativeAdapter(nativeList)
    }

    override fun load(slot: Int, view: View, afterLoad: (() -> Unit)?, afterNoAd: (() -> Unit)?) {
        this.afterLoad = afterLoad
        this.afterNoad = afterNoAd
        loaded = false
        nativeList.clear()
        if (afterLoad == null) showLoading(view)
        repeat(NATIVE_AD_COUNT) {
            val nativeAd = NativeBannerAd(slot, parent.context)
            nativeAd.listener = this
            nativeAd.load()
        }
    }

    private fun showLoading(parent: View) {
        bar = Snackbar.make(parent, "Loading", Snackbar.LENGTH_INDEFINITE)
        bar?.let {
            val contentLay = it.view.findViewById<TextView>(R.id.snackbar_text).parent as ViewGroup
            val item = ProgressBar(parent.context)
            contentLay.addView(item, 0)
            it.show()
        }
    }

    class NativeViewHolder(var adView: View?, var view: View) : RecyclerView.ViewHolder(view)

    class NativeAdapter(private var nativeList: SparseArrayCompat<NativeBannerAd>) :
            RecyclerView.Adapter<NativeViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NativeViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_native, parent, false) as ViewGroup
            var adView: View? = null

            if (viewType == 1) {
                adView =  NativeViewsFactory.getNativeBannerAdView(view.context)
                view.addView(adView)
            }
            else {
                val textView = TextView(parent.context)
                textView.setText(R.string.lorem)
                view.addView(textView)
            }

            return NativeViewHolder(adView, view)
        }

        override fun getItemCount(): Int {
            return NATIVE_AD_COUNT * 4
        }

        override fun getItemViewType(position: Int): Int {
            if (nativeList[position] != null) {
                return 1
            }
            return 0
        }

        override fun onBindViewHolder(holder: NativeViewHolder, position: Int) {
            val ad = nativeList[position] ?: return
            holder.adView.let {
                (it as NativeBannerAdView).setupView(ad.banner)
                ad.registerView(it)
            }
        }

        override fun onViewRecycled(holder: NativeViewHolder) {
            super.onViewRecycled(holder)
            val nativeAd: NativeBannerAd? = nativeList.get(holder.adapterPosition)
            nativeAd?.unregisterView()
        }
    }

    override fun onLoad(nativeBanner: NativeBanner, nativeBannerAd: NativeBannerAd) {
        adCalls++
        nativeList.append(nativeList.size() * 6 + 3, nativeBannerAd)
        if (adCalls >= NATIVE_AD_COUNT) {
            bar?.dismiss()
            loaded = true
            Snackbar.make(parent, "NativeAd is loaded", Snackbar.LENGTH_SHORT)
                    .show()
            afterLoad?.invoke()
        }
    }

    override fun onClick(p0: NativeBannerAd) {}

    override fun onNoAd(p0: String, p1: NativeBannerAd) {
        adCalls++
        if (adCalls >= NATIVE_AD_COUNT) {
            if (nativeList.isEmpty) {
                bar?.dismiss()
                Snackbar.make(parent, "No Ad", Snackbar.LENGTH_SHORT)
                        .show()
                afterNoad?.invoke()
            }
        }
    }

    override fun onShow(p0: NativeBannerAd) {}
}