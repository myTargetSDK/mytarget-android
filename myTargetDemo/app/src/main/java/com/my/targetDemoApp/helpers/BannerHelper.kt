package com.my.targetDemoApp.helpers

import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import com.my.target.ads.MyTargetView
import com.my.target.common.models.IAdLoadingError
import com.my.targetDemoApp.R
import com.my.targetDemoApp.addParsedString
import com.my.targetDemoApp.showLoading

class BannerHelper : MyTargetView.MyTargetViewListener {

    companion object
    {
        const val TAG = "BannerHelper"
    }

    var bannerView: MyTargetView? = null

    private var bar: Snackbar? = null
    private var afterLoad: (() -> Unit)? = null
    private var parent: View? = null

    override fun onLoad(p0: MyTargetView) {
        bar?.dismiss()
        afterLoad?.invoke()
    }

    override fun onClick(p0: MyTargetView) {
    }

    override fun onShow(p0: MyTargetView) {
    }

    override fun onNoAd(adLoadingError: IAdLoadingError, myTargetView: MyTargetView) {
        Log.d(TAG, "onNoAd() called with: adLoadingError = $adLoadingError, myTargetView = $myTargetView")
        parent?.let {
            Snackbar.make(it, String.format(myTargetView.context.getString(R.string.error_msg), adLoadingError.message),
                    Snackbar.LENGTH_SHORT)
                    .show()
        }
    }

    fun load(defaultSlot: Int, adSize: MyTargetView.AdSize, params: String?, parent: ViewGroup,
             function: (() -> Unit)? = null) {
        afterLoad = function
        bannerView = MyTargetView(parent.context).also { myTargetView ->
            myTargetView.setSlotId(defaultSlot)
            myTargetView.setAdSize(adSize)
            myTargetView.listener = this
            myTargetView.customParams.addParsedString(params)
            myTargetView.load()
        }
        this.parent = parent
        showLoading(parent)
    }

    private fun showLoading(parent: View) {
        bar = Snackbar.make(parent, "Loading", Snackbar.LENGTH_INDEFINITE)
                .showLoading()
    }

    fun destroy() {
        bannerView?.destroy()
    }
}