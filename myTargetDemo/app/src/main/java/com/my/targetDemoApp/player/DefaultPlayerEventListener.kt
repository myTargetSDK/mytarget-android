package com.my.targetDemoApp.player

import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi

interface DefaultPlayerEventListener : Player.Listener {
    @UnstableApi
    @Deprecated("Deprecated in Java")
    override fun onLoadingChanged(isLoading: Boolean) {
    }

    @UnstableApi
    @Deprecated("Deprecated in Java")
    override fun onPositionDiscontinuity(reason: Int) {
    }

    override fun onRepeatModeChanged(repeatMode: Int) {
    }

    override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
    }

    @UnstableApi
    @Deprecated("Deprecated in Java")
    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
    }
}