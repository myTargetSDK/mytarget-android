package com.my.targetDemoApp.activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.my.target.ads.MyTargetView
import com.my.targetDemoApp.databinding.ActivitySimpleBannerBinding

class SimpleBannerActivity : AppCompatActivity(), MyTargetView.MyTargetViewListener {
    private lateinit var viewBinding: ActivitySimpleBannerBinding

    companion object {
        private const val TAG = "SimpleBannerActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivitySimpleBannerBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        setSupportActionBar(viewBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);
        viewBinding.targetView.listener = this
        viewBinding.targetView.load()
    }

    override fun onLoad(p0: MyTargetView) {
        Log.d(TAG, "onLoad() called with: p0 = $p0")
        toast("onLoad")
    }

    override fun onClick(p0: MyTargetView) {
        Log.d(TAG, "onClick() called with: p0 = $p0")
    }

    override fun onNoAd(p0: String, p1: MyTargetView) {
        Log.d(TAG, "onNoAd() called with: p0 = $p0, p1 = $p1")
        toast("onLoad")
    }

    override fun onShow(p0: MyTargetView) {
        Log.d(TAG, "onShow() called with: p0 = $p0")
    }

    private fun toast(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT)
                .show()
    }
}