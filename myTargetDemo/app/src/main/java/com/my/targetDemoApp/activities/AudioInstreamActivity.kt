package com.my.targetDemoApp.activities

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.exoplayer2.ExoPlayer
import com.my.target.common.models.IAdLoadingError
import com.my.target.instreamads.InstreamAudioAd
import com.my.targetDemoApp.AdChoicesMenuFactory
import com.my.targetDemoApp.AdvertisingType
import com.my.targetDemoApp.addParsedString
import com.my.targetDemoApp.databinding.ActivityAudioInstreamBinding
import com.my.targetDemoApp.player.InstreamAuidoAdPlayerHelper

class AudioInstreamActivity : AppCompatActivity(),
                              InstreamAudioAd.InstreamAudioAdListener
{
    companion object
    {
        const val TAG = "AudioInstreamActivity"
        const val KEY_SLOT = "slotId"
        const val KEY_PARAMS = "params"
    }

    private var prerollPlayed = false
    private var postRollPlayed = false
    private var loaded: Boolean = false
    private var needToResumeAudio: Boolean = false
    private var slotId: Int = AdvertisingType.INSTREAM_AUDIO.defaultSlot
    private var params: String? = null
    private var currentAd: String? = null

    private lateinit var viewBinding: ActivityAudioInstreamBinding
    private lateinit var instreamAd: InstreamAudioAd
    private lateinit var instreamAudioAdPlayerHelper: InstreamAuidoAdPlayerHelper
    private lateinit var exoPlayer: ExoPlayer
    private var currentAudioPosition: Long = 0

    private val audioUri = Uri.parse("https://r.mradx.net/img/E5/E8EF84.mp3")

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityAudioInstreamBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        slotId = intent.getIntExtra(KEY_SLOT, AdvertisingType.INSTREAM_AUDIO.defaultSlot)
        params = intent.getStringExtra(KEY_PARAMS)

        initPlayer()
        initAd()

        viewBinding.apply {
            btnPause.setOnClickListener {
                exoPlayer.pause()
            }
            btnStop.setOnClickListener {
                exoPlayer.stop()
            }
            btnResume.setOnClickListener {
                exoPlayer.playWhenReady = true
            }
            btnAdchoices.setOnClickListener {
                instreamAd.handleAdChoicesClick(this@AudioInstreamActivity)
            }

            btnLoad.setOnClickListener {
                instreamAd.load()
                setStatus(process = "Ad loading")
            }
            btnPause.setOnClickListener { instreamAd.pause() }
            btnResume.setOnClickListener { instreamAd.resume() }
            btnStop.setOnClickListener {
                instreamAudioAdPlayerHelper.onAdAudioComplete()
                instreamAd.stop()
            }

            btnStartPreroll.setOnClickListener { startPreroll() }
            btnMidRoll.setOnClickListener { startMidroll() }
            btnPostroll.setOnClickListener { startPostRoll() }
        }
    }

    private fun initPlayer()
    {
        exoPlayer = ExoPlayer.Builder(this).build()
        exoPlayer.playWhenReady = false
        instreamAudioAdPlayerHelper = InstreamAuidoAdPlayerHelper(exoPlayer, this)
        instreamAudioAdPlayerHelper.setAudioSource(audioUri, false)

        viewBinding.exoplayerView.apply {
            player = exoPlayer
            keepScreenOn = true
        }
    }

    private fun initAd()
    {
        instreamAd = InstreamAudioAd(slotId, AdChoicesMenuFactory(), this)
        instreamAd.customParams.addParsedString(params)
        instreamAd.listener = this
        instreamAd.player = instreamAudioAdPlayerHelper
    }

    override fun onPause()
    {
        super.onPause()
        if (currentAd != null)
        {
            instreamAd.pause()
        } else
        {
            exoPlayer.playWhenReady = false
        }
    }

    override fun onResume()
    {
        super.onResume()
        if (currentAd != null)
        {
            instreamAd.resume()
        }
    }

    private fun startPreroll()
    {
        if (prerollPlayed || !loaded)
        {
            return
        }
        setStatus(status = "Starting preroll")
        currentAd = "preroll"
        needToResumeAudio = exoPlayer.isPlaying
        currentAudioPosition = exoPlayer.currentPosition
        prerollPlayed = true

        instreamAd.startPreroll()
    }

    private fun startPostRoll()
    {
        if (postRollPlayed || !loaded)
        {
            return
        }
        setStatus(status = "Starting postroll")
        currentAd = "postroll"
        needToResumeAudio = exoPlayer.isPlaying
        currentAudioPosition = exoPlayer.currentPosition
        postRollPlayed = true

        instreamAd.startPostroll()

    }

    private fun startMidroll(midPoint: Float = 5f)
    {
        if (postRollPlayed || !loaded)
        {
            return
        }
        setStatus(status = "Starting midroll")
        currentAd = "midroll"
        needToResumeAudio = exoPlayer.isPlaying
        currentAudioPosition = exoPlayer.currentPosition
        postRollPlayed = true

        instreamAd.startMidroll(midPoint)
    }

    override fun onLoad(ad: InstreamAudioAd)
    {
        setStatus(status = "Ad is loaded")
        loaded = true
    }

    override fun onNoAd(adLoadingError: IAdLoadingError, ad: InstreamAudioAd)
    {
        Log.d(TAG, "onNoAd() called with: adLoadingError = $adLoadingError, ad = $ad")
        setStatus(status = "No ad")
    }

    override fun onError(error: String, p1: InstreamAudioAd)
    {
        setStatus(status = "onError $error")
    }

    override fun onBannerStart(ad: InstreamAudioAd, instreamAdBanner: InstreamAudioAd.InstreamAudioAdBanner)
    {
        setStatus(status = "onBannerStart")

        viewBinding.apply {
            if (instreamAdBanner.hasAdChoices)
            {
                btnAdchoices.setImageBitmap(instreamAdBanner.adChoicesIcon?.bitmap)
                btnAdchoices.isVisible = true
            }

            val advertisingLabel = instreamAdBanner.advertisingLabel
            if (advertisingLabel.isNotEmpty())
            {
                tvAdvertising.text = instreamAdBanner.advertisingLabel
                tvAdvertising.isVisible = true
            }

            tvDurationContent.text = instreamAdBanner.duration.toString()
            tvAllowcloseContent.text = instreamAdBanner.allowSkip.toString()
            tvAllowPauseContent.text = instreamAdBanner.allowPause.toString()
        }
    }

    override fun onBannerComplete(ad: InstreamAudioAd, instreamAdBanner: InstreamAudioAd.InstreamAudioAdBanner)
    {
        setStatus(status = "onBannerComplete")
        currentAd = null
        viewBinding.apply {
            btnAdchoices.setImageBitmap(null)
            btnAdchoices.isVisible = false
            tvAdvertising.text = null
            tvAdvertising.isVisible = false
        }
        instreamAudioAdPlayerHelper.setAudioSource(audioUri, needToResumeAudio)
        exoPlayer.seekTo(currentAudioPosition)
    }

    override fun onBannerTimeLeftChange(timeLeft: Float, duration: Float, instreamAdBanner: InstreamAudioAd)
    {
        viewBinding.tvPositionContent.text = (duration - timeLeft).toString()
    }

    override fun onComplete(p0: String, p1: InstreamAudioAd)
    {
        setStatus(status = "onComplete")
        instreamAudioAdPlayerHelper.setAudioSource(audioUri, needToResumeAudio)
        exoPlayer.seekTo(currentAudioPosition)
    }

    override fun onBannerShouldClose(ad: InstreamAudioAd, instreamAdBanner: InstreamAudioAd.InstreamAudioAdBanner)
    {
        setStatus(status = "onBannerShouldClose")
        ad.skipBanner()
    }

    private fun setStatus(process: String = "", status: String = "")
    {
        viewBinding.tvProcessContent.text = process
        viewBinding.tvStatusContent.text = status
    }
}