package com.my.targetDemoApp

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.RawResourceDataSource
import com.google.android.exoplayer2.util.Util
import com.google.android.material.snackbar.Snackbar
import com.my.target.instreamads.InstreamAd
import com.my.target.instreamads.InstreamAdPlayer
import kotlinx.android.synthetic.main.activity_normal_instream.*
import kotlinx.android.synthetic.main.exo_playback_control_view.*

class InstreamActivity : AppCompatActivity(), DefaultPlayerEventListener, InstreamAd.InstreamAdListener {

    companion object {
        const val KEY_SLOT = "slotId"
        const val TAG = "InstreamActivity"
    }

    private lateinit var instreamAd: InstreamAd
    private lateinit var exoPlayer: SimpleExoPlayer
    private lateinit var mediaSource: MediaSource

    private var loaded: Boolean = false
    private var slotId: Int = AdvertisingType.INSTREAM.defaultSlot
    private var currentAd: String? = null
    private var prerollPlayed = false
    //    private var pauseRollPlayed = false
    private var postRollPlayed = false
    private var currentMidPoint: Float? = null
    private var instreamAdBanner: InstreamAd.InstreamAdBanner? = null
    private var bigPlayer: InstreamAdPlayer? = null

    private var rootPadding: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_normal_instream)
        InstreamAd.setDebugMode(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        slotId = intent.getIntExtra(KEY_SLOT, AdvertisingType.INSTREAM.defaultSlot)

        initPlayer()
        initAd()

        rootPadding = root_content_layout.paddingLeft
    }

    private fun initAd() {
        instreamAd = InstreamAd(slotId, this)
        instreamAd.useDefaultPlayer()
        bigPlayer = instreamAd.player
        instreamAd.listener = this
    }

    private fun initPlayer() {

        val bandwidthMeter = DefaultBandwidthMeter()
        val dataSourceFactory = DefaultDataSourceFactory(this,
                                                         Util.getUserAgent(this, "myTarget"),
                                                         bandwidthMeter)
        val mediaSourceFactory = ExtractorMediaSource.Factory(dataSourceFactory)
        exoPlayer = ExoPlayerFactory.newSimpleInstance(this)

        mediaSource = mediaSourceFactory.createMediaSource(RawResourceDataSource.buildRawResourceUri(
                R.raw.mytarget))
        exoPlayer.addListener(this)
        exoPlayer.playWhenReady = false
        exoplayer_view.player = exoPlayer
        exoplayer_view.keepScreenOn = true

        exoPlayer.prepare(mediaSource)
        exoplayer_view.requestFocus()

        btn_load.setOnClickListener {
            instreamAd.load()
            setStatus(process = "Ad loading")
        }
        btn_pause.setOnClickListener { instreamAd.pause() }
        btn_resume.setOnClickListener { instreamAd.resume() }
        btn_stop.setOnClickListener { instreamAd.stop() }
        btn_skip_banner.setOnClickListener {
            instreamAd.skipBanner()
        }
        btn_skip.setOnClickListener {
            instreamAd.skip()
        }
        btn_cta.setOnClickListener { instreamAd.handleClick() }
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        if (playbackState == Player.STATE_READY && playWhenReady) {
            startPreroll()
        }
        else if (playbackState == Player.STATE_ENDED) {
            startPostRoll()
        }
    }

    override fun onPause() {
        super.onPause()
        if (currentAd != null) {
            instreamAd.pause()
        }
        else {
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
        exoplayer_view.hideController()
        exoplayer_view.useController = false
        var status = "Banner started: $currentAd"
        if (currentAd == "midroll") {
            status += " на $currentMidPoint"
        }
        setStatus("Banner playing", status)
        processBanner(banner)
    }

    override fun onBannerResume(p0: InstreamAd, p1: InstreamAd.InstreamAdBanner) {
        Log.d(TAG,"onBannerResume")
    }

    override fun onBannerPause(p0: InstreamAd, p1: InstreamAd.InstreamAdBanner) {
        Log.d(TAG,"onBannerPause")
    }

    override fun onBannerComplete(ad: InstreamAd, banner: InstreamAd.InstreamAdBanner) {
        exoplayer_view.useController = true
        exoplayer_view.isEnabled = true
        setStatus(status = "Banner finished")
        processBanner()
        showSkipButton(false)
    }

    override fun onBannerTimeLeftChange(timeLeft: Float, duration: Float, ad: InstreamAd) {
        instreamAdBanner?.let {
            val position: Float = it.duration - timeLeft
            tv_position_content.text = position.toString()
            if (it.allowClose && (position >= it.allowCloseDelay)) {
                showSkipButton(true)
            }
        }
    }

    override fun onComplete(section: String, ad: InstreamAd) {
        setStatus(status = "Ad section finished")
        video_frame.removeView(instreamAd.player?.view)
        currentAd = null
        exoPlayer.playWhenReady = true
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
        if (!instreamAd.midPoints.contains(midPoint)) {
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
            snack("Player already created")
            return false
        }
        video_frame.addView(instreamAd.player?.view,
                            1,
                            ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                   ViewGroup.LayoutParams.MATCH_PARENT))

//        (instreamAd.player?.view as PlayerView).resizeMode = RESIZE_MODE_FIT
        return true
    }

    private fun snack(s: String) {
        Snackbar.make(root_content_layout, s, Snackbar.LENGTH_SHORT).show()
    }

    private fun processLoadedAd(ad: InstreamAd) {
        loaded = true
        tv_fullscreen_content.text = boolTextValue(ad.isFullscreen)
        tv_loading_timeout_content.text = ad.loadingTimeout.toString()
        tv_quality_content.text = ad.videoQuality.toString()

        instreamAd.midPoints
        ad.configureMidpoints((exoPlayer.duration.toFloat() / 1000))

        processMidPoints()
    }

    private fun processBanner(banner: InstreamAd.InstreamAdBanner? = null) {
        instreamAdBanner = banner
        tv_duration_content.text = banner?.duration?.toString() ?: getString(R.string.n_a)
        tv_dimensions_content.text = banner?.let { "${banner.videoWidth}x${banner.videoHeight}" } ?: getString(
                R.string.n_a)
        tv_close_delay_content.text = banner?.allowCloseDelay?.toString() ?: getString(R.string.n_a)
        tv_allowclose_content.text = boolTextValue(banner?.allowClose)
        tv_haspause_content.text = boolTextValue(banner?.allowPause)
        tv_position_content.text = getString(R.string.n_a)
        btn_pause.isEnabled = banner?.allowPause ?: true
        btn_resume.isEnabled = banner?.allowPause ?: true
        showCtaButton(banner?.ctaText)
    }

    private fun showCtaButton(ctaText: String?) {
        if (ctaText == null) {
            btn_cta.visibility = View.GONE
        }
        else {
            btn_cta.visibility = View.VISIBLE
        }
        btn_cta.text = ctaText
    }

    private fun showSkipButton(b: Boolean) {
        if (b) {
            btn_skip_banner.visibility = View.VISIBLE
            btn_skip.visibility = View.VISIBLE
        }
        else {
            btn_skip_banner.visibility = View.GONE
            btn_skip.visibility = View.GONE
        }
    }

    private fun processMidPoints() {
        val adGroupTimesMs = instreamAd.midPoints
        (exo_progress as ExoTimeBar).setAdGroupTimesSec(adGroupTimesMs)

        for (midPoint in adGroupTimesMs) {
            exoPlayer.createMessage { _, _ -> startMidroll(midPoint) }
                    .setPosition((midPoint * 1000).toLong()).setHandler(Handler()).send()
        }
    }

    private fun boolTextValue(bool: Boolean?): String {
        return when (bool) {
            true  -> "☑"
            false -> "☐"
            else  -> getString(R.string.n_a)
        }
    }

    private fun setStatus(process: String = "", status: String = "") {
        tv_process_content.text = process
        tv_status_content.text = status
    }
}