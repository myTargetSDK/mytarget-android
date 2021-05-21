package com.my.targetDemoApp.helpers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.my.target.nativeads.INativeAd
import com.my.targetDemoApp.R
import com.my.targetDemoApp.showLoading

abstract class NativeHelper<N : INativeAd, V : View>(val parent: View) {

    val nativeList = SparseArrayCompat<N>()
    var adCalls: Int = 0
    var nativeAdCount = NATIVE_AD_CHUNK_SIZE

    private var loaded = false
    private var afterLoad: (() -> Unit)? = null
    private var afterNoad: ((String) -> Unit)? = null
    private var bar: Snackbar? = null
    private var slot: Int = 0
    private var loadingPosition: Int = 0

    private lateinit var nativeAdapter: NativeAdapter

    abstract fun loadAds(slot: Int)

    abstract fun createAdView(context: Context): V

    abstract fun setupView(ad: N, adView: V)

    abstract fun registerView(ad: N, adView: View)

    abstract fun unRegisterView(ad: N?)

    fun createRecycler(): RecyclerView {
        val recyclerView = RecyclerView(parent.context)
        val layoutManager = LinearLayoutManager(parent.context, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        nativeAdapter = NativeAdapter(nativeList)
        recyclerView.adapter = nativeAdapter
        recyclerView.addOnScrollListener(ScrollListener(layoutManager) { loadMore() })
        return recyclerView
    }

    private fun loadMore() {
        if (!isLoaded()) return
        afterLoad = {
            loadingPosition = nativeAdCount * 4
            nativeAdapter.notifyItemChanged(nativeAdCount * 4)
            nativeAdapter.notifyItemRangeInserted(nativeAdCount * 4 + 1, NATIVE_AD_CHUNK_SIZE * 4)
        }
        nativeAdCount += NATIVE_AD_CHUNK_SIZE
        repeat(NATIVE_AD_CHUNK_SIZE) {
            loadAds(slot)
        }
    }

    fun load(slot: Int, view: View, afterLoad: (() -> Unit)?, afterNoAd: ((String) -> Unit)?) {
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

    fun callNoAd(reason: String) {
        if (!nativeList.isEmpty) {
            afterLoad?.invoke()
            return
        }

        bar?.dismiss()
        afterNoad?.invoke(reason)
    }

    fun isLoaded(): Boolean {
        return adCalls >= nativeAdCount
    }

    private fun showLoading(parent: View) {
        bar = Snackbar.make(parent, "Loading", Snackbar.LENGTH_INDEFINITE)
                .showLoading()
    }

    abstract class NativeViewHolder(view: View) : RecyclerView.ViewHolder(view)

    class NativeAdViewHolder<V : View>(var adView: V) : NativeViewHolder(adView)

    class NativeDummyHolder(var view: View) : NativeViewHolder(view)

    class ScrollListener(private val linearLayoutManager: LinearLayoutManager,
                         private val loadMore: (() -> Unit)) : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            val totalItemCount: Int = linearLayoutManager.itemCount
            val lastVisibleItemPosition: Int = linearLayoutManager.findLastVisibleItemPosition()
            if (totalItemCount - lastVisibleItemPosition < 4) {
                loadMore.invoke()
            }
            super.onScrolled(recyclerView, dx, dy)
        }
    }

    inner class NativeAdapter(private var nativeList: SparseArrayCompat<N>) :
            RecyclerView.Adapter<NativeViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NativeViewHolder {
            return when (viewType) {
                TYPE_AD -> {
                    val adView = createAdView(parent.context)

                    NativeAdViewHolder(adView)
                }
                TYPE_LOADING -> {
                    NativeDummyHolder(LayoutInflater.from(parent.context)
                            .inflate(R.layout.item_native_loading, parent, false) as ViewGroup)
                }
                else         -> {
                    NativeDummyHolder(LayoutInflater.from(parent.context)
                            .inflate(R.layout.item_native, parent, false) as ViewGroup)
                }
            }
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
            if (holder is NativeAdViewHolder<*>) {
                setupView(ad, holder.adView as V)
                registerView(ad, holder.adView)
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

    companion object {
        const val NATIVE_AD_CHUNK_SIZE = 5
        const val TYPE_TEXT = 0
        const val TYPE_AD = 1
        const val TYPE_LOADING = 2
    }
}