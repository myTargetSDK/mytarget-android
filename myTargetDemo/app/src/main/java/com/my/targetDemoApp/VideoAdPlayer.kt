package com.my.targetDemoApp

import android.content.Context
import android.net.Uri
import android.view.View
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.PlaybackParameters
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Timeline
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelection
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.my.target.instreamads.InstreamAdPlayer

class VideoAdPlayer(context: Context?) : PlayerView(context), InstreamAdPlayer, Player.EventListener {
    override fun playAdVideo(uri: Uri, width: Int, height: Int, position: Float) {
        playAdVideo(uri, width, height)
        mediaPlayer.seekTo((position * 1000).toLong())
    }

    private val bandwidthMeter = DefaultBandwidthMeter()

    private val extractorsFactory = DefaultExtractorsFactory()
    private var videoTrackSelectionFactory: TrackSelection.Factory = AdaptiveTrackSelection.Factory(bandwidthMeter)
    private val trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)

    private val mediaPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector)
    private val dataSourceFactory = DefaultDataSourceFactory(context, Util.getUserAgent(context, "myTarget"), bandwidthMeter)
    private var mediaSource: ExtractorMediaSource? = null
    private var listener: InstreamAdPlayer.AdPlayerListener? = null

    init {
        player = mediaPlayer
        mediaPlayer.addListener(this)
        mediaPlayer.playWhenReady = false
        keepScreenOn = true

        mediaPlayer.prepare(mediaSource)
        this.requestFocus()
    }

    override fun getAdPlayerListener(): InstreamAdPlayer.AdPlayerListener? {
        return listener
    }

    override fun getAdVideoDuration(): Float {
        return mediaPlayer.duration.toFloat().div(1000)
    }

    override fun getAdVideoPosition(): Float {
        return mediaPlayer.currentPosition.toFloat().div(1000)
    }

    override fun getView(): View {
        return this
    }

    override fun setAdPlayerListener(listener: InstreamAdPlayer.AdPlayerListener?) {
        this.listener = listener
    }

    override fun playAdVideo(uri: Uri, width: Int, height: Int) {
        mediaSource = ExtractorMediaSource(uri, dataSourceFactory, extractorsFactory, null, null)
        mediaPlayer.playWhenReady = true
        mediaPlayer.prepare(mediaSource)
    }

    override fun pauseAdVideo() {
        mediaPlayer.playWhenReady = false
    }

    override fun resumeAdVideo() {
        mediaPlayer.playWhenReady = true
    }

    override fun stopAdVideo() {
        mediaPlayer.stop()
        adPlayerListener?.onAdVideoStopped()
    }

    override fun destroy() {
        mediaPlayer.release()
    }

    override fun setVolume(volume: Float) {
        mediaPlayer.volume = volume
        adPlayerListener?.onVolumeChanged(volume)
    }

    // EXOPLAYER

    override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters?) {
    }

    override fun onSeekProcessed() {
    }

    override fun onTracksChanged(trackGroups: TrackGroupArray?, trackSelections: TrackSelectionArray?) {
    }

    override fun onPlayerError(error: ExoPlaybackException?) {
        stopAdVideo()
    }

    override fun onLoadingChanged(isLoading: Boolean) {
    }

    override fun onPositionDiscontinuity(reason: Int) {
    }

    override fun onRepeatModeChanged(repeatMode: Int) {
    }

    override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
    }

    override fun onTimelineChanged(timeline: Timeline?, manifest: Any?, reason: Int) {
    }

    private var paused: Boolean = false

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        if (playbackState == Player.STATE_ENDED) {
            adPlayerListener?.onAdVideoCompleted()
        }
        else if (playbackState == Player.STATE_READY) {
            if (playWhenReady) {
                if (paused) {
                    adPlayerListener?.onAdVideoResumed()
                    paused = false
                }
                else {
                    adPlayerListener?.onAdVideoStarted()
                }
            }
            else {
                adPlayerListener?.onAdVideoPaused()
                paused = true
            }
        }
    }
}