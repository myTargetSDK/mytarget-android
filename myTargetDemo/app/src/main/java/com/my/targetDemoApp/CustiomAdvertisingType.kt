package com.my.targetDemoApp

class CustomAdvertisingType(val adType: AdType, val slotId: Int?) {
    var name = "Custom ${adType.toString().toLowerCase()}"

    enum class AdType {
        STANDARD_320X50,
        STANDARD_300X250,
        STANDARD_728X90,
        INTERSTITIAL,
        NATIVE,
        INSTREAM;
    }
}