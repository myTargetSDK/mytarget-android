package com.my.targetDemoApp.player

import com.google.android.exoplayer2.Player

interface DefaultPlayerEventListener : Player.EventListener {
    override fun onSeekProcessed() {
    }

    override fun onLoadingChanged(isLoading: Boolean) {
    }

    override fun onPositionDiscontinuity(reason: Int) {
    }

    override fun onRepeatModeChanged(repeatMode: Int) {
    }

    override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
    }
}