package com.my.targetDemoApp.activities

import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.DefaultTimeBar
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.google.android.material.snackbar.Snackbar
import com.my.target.instreamads.InstreamAd
import com.my.target.instreamads.InstreamAdPlayer
import com.my.targetDemoApp.AdvertisingType
import com.my.targetDemoApp.R
import com.my.targetDemoApp.addParsedString
import com.my.targetDemoApp.databinding.ActivityNormalInstreamBinding
import com.my.targetDemoApp.player.DefaultPlayerEventListener

class InstreamActivity : AppCompatActivity(), DefaultPlayerEventListener,
    InstreamAd.InstreamAdListener {

    companion object {
        const val KEY_SLOT = "slotId"
        const val KEY_PARAMS = "params"
        const val TAG = "InstreamActivity"
    }

    private var loaded: Boolean = false
    private var slotId: Int = AdvertisingType.INSTREAM.defaultSlot
    private var params: String? = null
    private var currentAd: String? = null
    private var prerollPlayed = false
    private var postRollPlayed = false
    private var currentMidPoint: Float? = null
    private var instreamAdBanner: InstreamAd.InstreamAdBanner? = null
    private var bigPlayer: InstreamAdPlayer? = null
    private var rootPadding: Int = 0

    private lateinit var viewBinding: ActivityNormalInstreamBinding
    private lateinit var instreamAd: InstreamAd
    private lateinit var exoPlayer: SimpleExoPlayer
    private lateinit var mediaSource: MediaSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityNormalInstreamBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        slotId = intent.getIntExtra(KEY_SLOT, AdvertisingType.INSTREAM.defaultSlot)
        params = intent.getStringExtra(KEY_PARAMS)

        initPlayer()
        initAd()

        rootPadding = viewBinding.rootContentLayout.paddingLeft
    }

    private fun initAd() {
        instreamAd = InstreamAd(slotId, this)
        instreamAd.customParams.addParsedString(params)
        instreamAd.useDefaultPlayer()
        bigPlayer = instreamAd.player
        instreamAd.listener = this
    }

    private fun initPlayer() {
        exoPlayer = SimpleExoPlayer.Builder(this)
            .build()
        mediaSource = ProgressiveMediaSource.Factory(
            DefaultDataSourceFactory(this, Util.getUserAgent(this, "myTarget"))
        )
            .createMediaSource(
                MediaItem.fromUri(Uri.parse("https://r.mradx.net/img/ED/518795.mp4"))
            )
        exoPlayer.addListener(this)
        exoPlayer.playWhenReady = false
        viewBinding.exoplayerView.player = exoPlayer
        viewBinding.exoplayerView.keepScreenOn = true

        exoPlayer.setMediaSource(mediaSource)
        exoPlayer.prepare()
        viewBinding.exoplayerView.requestFocus()

        viewBinding.btnLoad.setOnClickListener {
            instreamAd.load()
            setStatus(process = "Ad loading")
        }
        viewBinding.btnPause.setOnClickListener { instreamAd.pause() }
        viewBinding.btnResume.setOnClickListener { instreamAd.resume() }
        viewBinding.btnStop.setOnClickListener { instreamAd.stop() }
        viewBinding.btnSkipBanner.setOnClickListener {
            instreamAd.skipBanner()
        }
        viewBinding.btnSkip.setOnClickListener {
            instreamAd.skip()
        }
        viewBinding.btnCta.setOnClickListener { instreamAd.handleClick() }
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        if (playbackState == Player.STATE_READY && playWhenReady) {
            startPreroll()
        } else if (playbackState == Player.STATE_ENDED) {
            startPostRoll()
        }
    }

    override fun onPause() {
        super.onPause()
        if (currentAd != null) {
            instreamAd.pause()
        } else {
            exoPlayer.playWhenReady = false
        }
    }

    override fun onResume() {
        super.onResume()
        if (currentAd != null) {
            instreamAd.resume()
        }
    }

    override fun onLoad(ad: InstreamAd) {
        setStatus(status = "Ad is loaded")
        processLoadedAd(ad)
    }

    override fun onNoAd(reason: String, ad: InstreamAd) {
        setStatus(status = "No ad")
    }

    override fun onError(reason: String, ad: InstreamAd) {
        setStatus(status = "Error $reason")
    }

    override fun onBannerStart(ad: InstreamAd, banner: InstreamAd.InstreamAdBanner) {
        viewBinding.exoplayerView.hideController()
        viewBinding.exoplayerView.useController = false
        var status = "Banner started: $currentAd"
        if (currentAd == "midroll") {
            status += " на $currentMidPoint"
        }
        setStatus("Banner playing", status)
        processBanner(banner)
    }

    override fun onBannerResume(p0: InstreamAd, p1: InstreamAd.InstreamAdBanner) {
        Log.d(TAG, "onBannerResume")
    }

    override fun onBannerPause(p0: InstreamAd, p1: InstreamAd.InstreamAdBanner) {
        Log.d(TAG, "onBannerPause")
    }

    override fun onBannerComplete(ad: InstreamAd, banner: InstreamAd.InstreamAdBanner) {
        viewBinding.exoplayerView.useController = true
        viewBinding.exoplayerView.isEnabled = true
        setStatus(status = "Banner finished")
        processBanner()
        showSkipButton(false)
    }

    override fun onBannerTimeLeftChange(timeLeft: Float, duration: Float, ad: InstreamAd) {
        instreamAdBanner?.let {
            val position: Float = it.duration - timeLeft
            viewBinding.tvPositionContent.text = position.toString()
            if (it.allowClose && (position >= it.allowCloseDelay)) {
                showSkipButton(true)
            }
        }
    }

    override fun onComplete(section: String, ad: InstreamAd) {
        setStatus(status = "Ad section finished")
        viewBinding.videoFrame.removeView(instreamAd.player?.view)
        currentAd = null
        exoPlayer.playWhenReady = true
    }

    override fun onBannerShouldClose()
    {
    }

    private fun startPreroll() {
        if (prerollPlayed || !loaded) {
            return
        }
        prerollPlayed = true
        currentAd = "preroll"
        exoPlayer.playWhenReady = false

        if (addAdPlayer()) {
            instreamAd.startPreroll()

            setStatus(status = "Starting preroll")
        }
    }

    private fun startPostRoll() {
        if (postRollPlayed || !loaded) {
            return
        }
        postRollPlayed = true
        currentAd = "postroll"
        exoPlayer.playWhenReady = false

        if (addAdPlayer()) {
            instreamAd.startPostroll()
            setStatus(status = "Starting postroll")
        }
    }

    private fun startMidroll(midPoint: Float) {
        if (!loaded) {
            return
        }
        if (!instreamAd.midPoints.any { it == midPoint }) {
            return
        }
        currentAd = "midroll"
        exoPlayer.playWhenReady = false
        if (addAdPlayer()) {
            instreamAd.startMidroll(midPoint)
            setStatus(status = "Starting midroll at $midPoint")
            currentMidPoint = midPoint
        }
    }

    private fun addAdPlayer(): Boolean {
        if (instreamAd.player?.view?.parent != null) {
            Snackbar.make(
                viewBinding.rootContentLayout, "Player already created",
                Snackbar.LENGTH_SHORT
            )
                .show()
            return false
        }
        viewBinding.videoFrame.addView(
            instreamAd.player?.view, 1,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )

        return true
    }

    private fun processLoadedAd(ad: InstreamAd) {
        loaded = true
        viewBinding.tvFullscreenContent.text = boolTextValue(ad.isFullscreen)
        viewBinding.tvLoadingTimeoutContent.text = ad.loadingTimeout.toString()
        viewBinding.tvQualityContent.text = ad.videoQuality.toString()

        instreamAd.midPoints
        ad.configureMidpoints((exoPlayer.duration.toFloat() / 1000))

        processMidPoints()
    }

    private fun processBanner(banner: InstreamAd.InstreamAdBanner? = null) {
        instreamAdBanner = banner
        viewBinding.tvDurationContent.text = banner?.duration?.toString() ?: getString(R.string.n_a)
        viewBinding.tvDimensionsContent.text =
            banner?.let { "${banner.videoWidth}x${banner.videoHeight}" } ?: getString(
                R.string.n_a
            )
        viewBinding.tvCloseDelayContent.text =
            banner?.allowCloseDelay?.toString() ?: getString(R.string.n_a)
        viewBinding.tvAllowcloseContent.text = boolTextValue(banner?.allowClose)
        viewBinding.tvHaspauseContent.text = boolTextValue(banner?.allowPause)
        viewBinding.tvPositionContent.text = getString(R.string.n_a)
        viewBinding.btnPause.isEnabled = banner?.allowPause ?: true
        viewBinding.btnResume.isEnabled = banner?.allowPause ?: true
        showCtaButton(banner?.ctaText)
    }

    private fun showCtaButton(ctaText: String?) {
        if (ctaText == null) {
            viewBinding.btnCta.visibility = View.GONE
        } else {
            viewBinding.btnCta.visibility = View.VISIBLE
        }
        viewBinding.btnCta.text = ctaText
    }

    private fun showSkipButton(b: Boolean) {
        if (b) {
            viewBinding.btnSkipBanner.visibility = View.VISIBLE
            viewBinding.btnSkip.visibility = View.VISIBLE
        } else {
            viewBinding.btnSkipBanner.visibility = View.GONE
            viewBinding.btnSkip.visibility = View.GONE
        }
    }

    private fun processMidPoints() {
        val adGroupTimesMs = instreamAd.midPoints.map { (it * 1000).toLong() }
            .toLongArray()
        viewBinding.root.findViewById<DefaultTimeBar>(R.id.exo_progress)
            .setAdGroupTimesMs(
                adGroupTimesMs, BooleanArray(adGroupTimesMs.size) { true },
                adGroupTimesMs.size
            )

        for (midPoint in adGroupTimesMs) {
            exoPlayer.createMessage { _, _ -> startMidroll((midPoint / 1000).toFloat()) }
                .setPosition(midPoint)
                .setLooper(Looper.getMainLooper())
                .send()
        }
    }

    private fun boolTextValue(bool: Boolean?): String {
        return when (bool) {
            true -> "☑"
            false -> "☐"
            else -> getString(R.string.n_a)
        }
    }

    private fun setStatus(process: String = "", status: String = "") {
        viewBinding.tvProcessContent.text = process
        viewBinding.tvStatusContent.text = status
    }
}