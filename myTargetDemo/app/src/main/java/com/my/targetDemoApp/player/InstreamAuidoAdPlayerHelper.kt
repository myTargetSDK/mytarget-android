package com.my.targetDemoApp.player

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util

import com.my.target.instreamads.InstreamAudioAdPlayer

class InstreamAuidoAdPlayerHelper(private val currentExoplayer: ExoPlayer, val context: Context) : InstreamAudioAdPlayer, DefaultPlayerEventListener
{

    private var playerListener: InstreamAudioAdPlayer.AdPlayerListener? = null
    private var paused = false

    init
    {
        currentExoplayer.addListener(this)
    }

    override fun onPlayerError(error: PlaybackException)
    {
        adPlayerListener?.onAdAudioError(error.message ?: "No message")
        stopAdAudio()
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int)
    {

        if (playbackState == Player.STATE_ENDED)
        {
            onAdAudioComplete()
            return
        }

        if (playbackState != Player.STATE_READY)
        {
            return
        }

        if (!playWhenReady)
        {
            adPlayerListener?.onAdAudioPaused()
            paused = true
            return
        }

        if (paused)
        {
            adPlayerListener?.onAdAudioResumed()
            paused = false
            return
        }

        adPlayerListener?.onAdAudioStarted()
    }

    override fun pauseAdAudio()
    {
        currentExoplayer.pause()
        adPlayerListener?.onAdAudioPaused()
    }

    override fun resumeAdAudio()
    {
        currentExoplayer.play()
        adPlayerListener?.onAdAudioResumed()
    }

    override fun stopAdAudio()
    {
        currentExoplayer.stop()
        adPlayerListener?.onAdAudioStopped()
    }

    override fun destroy()
    {
        currentExoplayer.release()
    }

    override fun setVolume(volume: Float)
    {
        currentExoplayer.volume = volume
        adPlayerListener?.onVolumeChanged(volume)
    }

    override fun getAdPlayerListener(): InstreamAudioAdPlayer.AdPlayerListener?
    {
        return playerListener
    }

    override fun getAdAudioDuration(): Float
    {
        return currentExoplayer.duration.toFloat()
            .div(1000)
    }

    override fun getAdAudioPosition(): Float
    {
        return currentExoplayer.currentPosition.toFloat()
            .div(1000)
    }

    override fun getCurrentContext(): Context
    {
        return context
    }

    override fun setAdPlayerListener(adPlayerListener: InstreamAudioAdPlayer.AdPlayerListener?)
    {
        this.playerListener = adPlayerListener;
    }

    override fun playAdAudio(uri: Uri)
    {
        setAudioSource(uri, true)
    }

    fun setAudioSource(uri: Uri, playWhenReady: Boolean)
    {
        val mediaSource = ProgressiveMediaSource.Factory(
            DefaultDataSourceFactory(context, Util.getUserAgent(context, "myTarget"))
        )
            .createMediaSource(MediaItem.fromUri(uri))
        currentExoplayer.playWhenReady = playWhenReady
        currentExoplayer.setMediaSource(mediaSource)
        currentExoplayer.prepare()
    }

    fun onAdAudioComplete()
    {
        adPlayerListener?.onAdAudioCompleted()
    }
}