package com.my.targetDemoApp.helpers

import android.util.Log
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.my.target.ads.Reward
import com.my.target.ads.RewardedAd
import com.my.target.common.models.IAdLoadingError
import com.my.targetDemoApp.R
import com.my.targetDemoApp.addParsedString
import com.my.targetDemoApp.showLoading

class RewardedHelper(val parent: View) {

    companion object
    {
        const val TAG = "RewardedHelper"
    }

    private var rewardedAd: RewardedAd? = null
    private var showImmediatly: Boolean = false
    private var bar: Snackbar? = null

    private val demoRewardedAdListener = object : RewardedAd.RewardedAdListener {
        override fun onLoad(rewardedAd: RewardedAd) {
            if (showImmediatly) {
                bar?.dismiss()
                rewardedAd.show()
            } else {
                Snackbar.make(parent, "RewardedAd is loaded", Snackbar.LENGTH_SHORT)
                    .show()
            }
        }

        override fun onClick(rewardedAd: RewardedAd) {
            Log.d(TAG, "onClick")
        }

        override fun onFailedToShow(rewardedAd: RewardedAd) {
            Log.d(TAG, "onFailedToShow")
        }

        override fun onDisplay(rewardedAd: RewardedAd) {
            Log.d(TAG, "onDisplay")
        }

        override fun onDismiss(rewardedAd: RewardedAd) {
            Log.d(TAG, "onDismiss")
        }

        override fun onNoAd(adLoadingError: IAdLoadingError, rewardedAd: RewardedAd) {
            Log.d(TAG, "onNoAd() called with: adLoadingError = $adLoadingError, rewardedAd = $rewardedAd")
            val s = String.format(parent.context.getString(R.string.error_msg), adLoadingError.message)
            Snackbar.make(parent, s, Snackbar.LENGTH_SHORT)
                .show()
        }

        override fun onReward(reward: Reward, rewardedAd: RewardedAd) {
            Log.d(TAG, "onReward")
            Snackbar.make(parent, "Got ${reward.type} reward", Snackbar.LENGTH_SHORT)
                .show()
        }
    }

    fun destroy() {
        rewardedAd?.dismiss()
    }

    fun init(slot: Int, showImmediatly: Boolean = false, params: String?) {
        showLoading(parent)
        this.showImmediatly = showImmediatly
        rewardedAd = RewardedAd(slot, parent.context)
        rewardedAd?.let {
            it.listener = demoRewardedAdListener
            it.customParams.addParsedString(params)
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