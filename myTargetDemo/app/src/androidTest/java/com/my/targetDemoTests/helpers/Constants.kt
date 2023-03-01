package com.my.targetDemoTests.helpers

class Slot {
    companion object {
        const val standard320x50 = 14170
        const val standard300x250 = 64526
        const val standard728x90 = 81620
        const val interstitialPromo = 6896
        const val interstitialVideo = 10138
        const val interstitialCarousel = 102652
        const val nativeAds = 6590
        const val instream = 9525
        const val audio_instream = 1208427
    }
}

class Callback {
    companion object {
        const val load = "[myTarget]: send stat request"
        const val pause = "playbackPaused"
        const val resume = "playbackResumed"
    }
}