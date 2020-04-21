package com.my.targetDemoApp

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.my.target.nativeads.INativeAd

abstract class NativeHelper<N : INativeAd, V : View>(val parent: View) {
    companion object {
        const val NATIVE_AD_CHUNK_SIZE = 5

        const val TYPE_TEXT = 0
        const val TYPE_AD = 1
        const val TYPE_LOADING = 2
    }

    private var loadingPosition: Int = 0
    private lateinit var nativeAdapter: NativeAdapter
    val nativeList = SparseArrayCompat<N>()
    var adCalls: Int = 0
    var nativeAdCount = NATIVE_AD_CHUNK_SIZE
    private var loaded = false
    private var afterLoad: (() -> Unit)? = null
    private var afterNoad: (() -> Unit)? = null
    private var bar: Snackbar? = null
    private var slot: Int = 0

    abstract fun loadAds(slot: Int)

    abstract fun createAdView(context: Context): V

    abstract fun setupView(ad: N, adView: V)

    abstract fun registerView(ad: N, adView: V)

    abstract fun unRegisterView(ad: N?)

    fun createRecycler(): RecyclerView {
        val recyclerView = RecyclerView(parent.context)
        val lm = LinearLayoutManager(parent.context, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = lm
        nativeAdapter = NativeAdapter(nativeList)
        recyclerView.adapter = nativeAdapter
        recyclerView.addOnScrollListener(ScrollListener(lm) { loadMore() })
        return recyclerView
    }

    private fun loadMore() {
        if (isLoaded()) {
            afterLoad = {
                loadingPosition = nativeAdCount * 4
                nativeAdapter.notifyItemChanged(nativeAdCount * 4)
                nativeAdapter.notifyItemRangeInserted(nativeAdCount * 4 + 1,
                        NATIVE_AD_CHUNK_SIZE * 4)
            }
            nativeAdCount += NATIVE_AD_CHUNK_SIZE
            repeat(NATIVE_AD_CHUNK_SIZE) {
                loadAds(slot)
            }
        }
    }

    fun load(slot: Int, view: View, afterLoad: (() -> Unit)?, afterNoAd: (() -> Unit)?) {
        this.slot = slot
        this.afterLoad = afterLoad
        this.afterNoad = afterNoAd
        loaded = false
        nativeList.clear()
        nativeAdCount = NATIVE_AD_CHUNK_SIZE
        loadingPosition = nativeAdCount * 4
        if (afterLoad == null) showLoading(view)
        repeat(nativeAdCount) {
            loadAds(slot)
        }
    }

    fun everythingLoaded() {
        bar?.dismiss()
        loaded = true
        afterLoad?.invoke()
    }

    fun callNoAd() {
        if (nativeList.isEmpty) {
            bar?.dismiss()
            Snackbar.make(parent, "No Ad", Snackbar.LENGTH_SHORT)
                    .show()
            afterNoad?.invoke()
        }
        else {
            afterLoad?.invoke()
        }
    }

    fun isLoaded(): Boolean {
        return adCalls >= nativeAdCount
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

    class ScrollListener(val linearLayoutManager: LinearLayoutManager, val loadMore: (() -> Unit)) :
            RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            var lastVisibleItemPosition = 0
            val totalItemCount: Int = linearLayoutManager.itemCount
            lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition()
            if (totalItemCount - lastVisibleItemPosition < 4) {
                loadMore.invoke()
            }
            super.onScrolled(recyclerView, dx, dy)
        }
    }

    inner class NativeAdapter(private var nativeList: SparseArrayCompat<N>) :
            RecyclerView.Adapter<NativeViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NativeViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_native, parent, false) as ViewGroup
            var adView: V? = null

            when (viewType) {
                TYPE_AD      -> {
                    adView = createAdView(parent.context)
                    view.addView(adView)
                }
                TYPE_LOADING -> {
                    val progressBar =
                            ProgressBar(parent.context, null, android.R.attr.progressBarStyleLarge)
                    progressBar.isIndeterminate = true
                    val lp = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT)
                    lp.gravity = Gravity.CENTER
                    view.addView(progressBar, lp)
                }
                else         -> {
                    val textView = TextView(parent.context)
                    textView.setText(R.string.lorem)
                    view.addView(textView)
                }
            }

            return NativeViewHolder(adView, view)
        }

        override fun getItemCount(): Int {
            return nativeAdCount * 4 + 1
        }

        override fun getItemViewType(position: Int): Int {
            if (nativeList[position] != null) {
                return TYPE_AD
            }
            if (position == loadingPosition) {
                return TYPE_LOADING
            }
            return TYPE_TEXT
        }

        override fun onBindViewHolder(holder: NativeViewHolder, position: Int) {
            val ad = nativeList[position] ?: return
            holder.adView.let {
                (it as? V)?.let { adView ->
                    setupView(ad, adView)
                    registerView(ad, adView)
                }
            }
        }

        override fun onViewRecycled(holder: NativeViewHolder) {
            super.onViewRecycled(holder)
            unRegisterView(nativeList.get(holder.adapterPosition))
        }

        override fun onFailedToRecycleView(holder: NativeViewHolder): Boolean {
            unRegisterView(nativeList.get(holder.adapterPosition))
            return super.onFailedToRecycleView(holder)
        }
    }
}