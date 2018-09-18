package com.my.targetDemoApp

import android.content.Context
import android.preference.PreferenceManager

class Saver(context: Context) {

    companion object {
        const val TAG = "saved_types"
        const val DELIMETER = ":"
    }

    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun save(adType: CustomAdvertisingType) {
        val item = preferences.getStringSet(TAG, HashSet<String>())
        item.add("${adType.name}:${adType.slotId}")
        preferences.edit().remove(TAG).apply()
        preferences.edit().putStringSet(TAG, item).apply()
    }

    fun restore(): Collection<CustomAdvertisingType>? {
        val item = preferences.getStringSet(TAG, null)
        val types = if (item != null) ArrayList<CustomAdvertisingType>() else null
        item?.forEach {
            val split = it.split(DELIMETER)
            val type = CustomAdvertisingType.valueOf(split[0])
            type.slotId = split[1].toInt()
            types?.add(type)
        }
        return types
    }

    fun remove(it: CustomAdvertisingType) {
        val item = "${it.name}:${it.slotId}"
        val items = preferences.getStringSet(TAG, null)
        if (items.contains(item)) {
            items.remove(item)
            preferences.edit().remove(TAG).apply()
            preferences.edit().putStringSet(TAG, items).apply()
        }
    }
}