package com.my.targetDemoApp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.my.target.ads.MyTargetView
import kotlinx.android.synthetic.main.activity_simple_banner.*

class SimpleBannerActivity : AppCompatActivity(), MyTargetView.MyTargetViewListener {
    companion object {
        private const val TAG = "SimpleBannerActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_banner)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);
        target_view.listener = this
        target_view.load()
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