package com.my.targetDemoApp.player

import android.content.Context
import android.util.AttributeSet
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.DefaultTimeBar

@UnstableApi
class ExoTimeBar(context: Context, attrs: AttributeSet?) : DefaultTimeBar(context, attrs) {

    override fun setAdGroupTimesMs(adGroupTimesMs: LongArray?, playedAdGroups: BooleanArray?, adGroupCount: Int) {
        //nothing
    }

    fun setAdGroupTimesSec(adGroupTimesMs: FloatArray) {
        val longs = LongArray(adGroupTimesMs.size)
        for ((index, value) in adGroupTimesMs.withIndex()) {
            longs[index] = (value * 1000).toLong()
        }
        super.setAdGroupTimesMs(longs, BooleanArray(adGroupTimesMs.size) { true }, adGroupTimesMs.size)
    }

}