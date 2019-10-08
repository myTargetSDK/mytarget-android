package com.my.targetDemoApp

import android.content.Context
import android.util.AttributeSet
import com.google.android.exoplayer2.ui.DefaultTimeBar

class ExoTimeBar(context: Context?, attrs: AttributeSet?) : DefaultTimeBar(context, attrs) {

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