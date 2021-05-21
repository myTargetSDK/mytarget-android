package com.my.targetDemoApp.helpers

import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.my.target.ads.Reward
import com.my.target.ads.RewardedAd
import com.my.targetDemoApp.R
import com.my.targetDemoApp.showLoading

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

    override fun onNoAd(reason: String, p1: RewardedAd) {
        val s = String.format(parent.context.getString(R.string.error_msg), reason)
        Snackbar.make(parent, s, Snackbar.LENGTH_SHORT)
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
                .showLoading()
    }

    fun show() {
        rewardedAd?.show() ?: Snackbar.make(parent, "Not loaded yet", Snackbar.LENGTH_SHORT)
                .show()
    }
}