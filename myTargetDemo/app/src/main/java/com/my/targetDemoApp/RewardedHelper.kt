package com.my.targetDemoApp

import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.my.target.ads.Reward
import com.my.target.ads.RewardedAd

class RewardedHelper(val parent: View) : RewardedAd.RewardedAdListener {

    private var rewardedAd: RewardedAd? = null
    private var showImmediatly: Boolean = false
    private var bar: Snackbar? = null

    override fun onLoad(rewardedAd: RewardedAd) {
        if (showImmediatly) {
            bar?.dismiss()
            rewardedAd.show()
        }
        else {
            Snackbar.make(parent, "RewardedAd is loaded", Snackbar.LENGTH_SHORT)
                    .show()
        }
    }

    override fun onClick(rewardedAd: RewardedAd) {
    }

    override fun onDisplay(rewardedAd: RewardedAd) {
    }

    override fun onDismiss(rewardedAd: RewardedAd) {
    }

    override fun onNoAd(rewardedAd: String, p1: RewardedAd) {
        Snackbar.make(parent, "RewardedAd: no ad", Snackbar.LENGTH_SHORT)
                .show()
    }

    override fun onReward(p0: Reward, p1: RewardedAd) {
        Snackbar.make(parent, "Got ${p0.type} reward", Snackbar.LENGTH_SHORT)
                .show()
    }

    fun destroy() {
        rewardedAd?.dismiss()
    }

    fun init(slot: Int, showImmediatly: Boolean = false) {
        showLoading(parent)
        this.showImmediatly = showImmediatly
        rewardedAd = RewardedAd(slot, parent.context)
        rewardedAd?.let {
            it.listener = this
            it.load()
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

    fun show() {
        rewardedAd?.show() ?: Snackbar.make(parent, "Not loaded yet", Snackbar.LENGTH_SHORT)
                .show()
    }
}