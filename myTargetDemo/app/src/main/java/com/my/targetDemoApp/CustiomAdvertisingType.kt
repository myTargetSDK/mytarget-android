package com.my.targetDemoApp

enum class CustomAdvertisingType {
    STANDARD_320X50,
    STANDARD_300X250,
    STANDARD_728X90,
    INTERSTITIAL,
    NATIVE,
    INSTREAM;

    var slotId: Int? = null
}