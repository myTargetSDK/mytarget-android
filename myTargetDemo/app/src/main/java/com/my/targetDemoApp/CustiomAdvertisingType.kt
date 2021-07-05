package com.my.targetDemoApp

import java.util.*

class CustomAdvertisingType(val adType: AdType, val slotId: Int?,
                            val params: String? = null) {
    var name = "Custom ${
        adType.toString()
                .toLowerCase(Locale.getDefault())
                .replace("_", " ")
    }"

    enum class AdType {
        STANDARD_320X50,
        STANDARD_300X250,
        STANDARD_728X90,
        STANDARD_ADAPTIVE,
        INTERSTITIAL,
        REWARDED,
        NATIVE_AD,
        NATIVE_BANNER,
        INSTREAM;
    }
}