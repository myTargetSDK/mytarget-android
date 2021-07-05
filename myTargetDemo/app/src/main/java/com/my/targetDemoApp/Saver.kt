package com.my.targetDemoApp

import android.content.Context
import androidx.preference.PreferenceManager
import org.json.JSONArray
import org.json.JSONObject

class Saver(context: Context) {

    companion object {
        const val TAG = "saved_types"
        const val AD_TYPE = "ad_type"
        const val NAME = "name"
        const val ITEMS = "items"
        const val PARAMS = "params"
        const val SLOT = "slot"
        const val DEFAULT = "{\"$ITEMS\":[]}"
    }

    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun save(adType: CustomAdvertisingType) {
        val prefs = preferences.getString(TAG, DEFAULT) ?: DEFAULT
        val prefsJO = JSONObject(prefs)
        val array = prefsJO.getJSONArray(ITEMS)
        val itemJO = JSONObject()
        itemJO.put(AD_TYPE, adType.adType)
        itemJO.put(SLOT, adType.slotId)
        itemJO.put(NAME, adType.name)
        val params = adType.params
        if (params != null) {
            itemJO.put(PARAMS, params)
        }
        array.put(itemJO)
        preferences.edit()
                .putString(TAG, prefsJO.toString())
                .apply()
    }

    fun restore(): Collection<CustomAdvertisingType>? {
        val types = ArrayList<CustomAdvertisingType>()

        val prefs = preferences.getString(TAG, DEFAULT) ?: DEFAULT
        val prefsJO = JSONObject(prefs)
        val array = prefsJO.getJSONArray(ITEMS)
        for (i in 0 until array.length()) {
            val item = array.getJSONObject(i)
            @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS") val params =
                    item.optString(PARAMS, null)
            val type = CustomAdvertisingType(
                    CustomAdvertisingType.AdType.valueOf(item.getString(AD_TYPE)),
                    item.getInt(SLOT), params)
            type.name = item.getString(NAME)
            types.add(type)
        }
        return types
    }

    fun remove(pos: Int) {
        if (pos < 0) {
            return
        }

        val prefs = preferences.getString(TAG, DEFAULT) ?: DEFAULT
        var prefsJO = JSONObject(prefs)
        val array = prefsJO.getJSONArray(ITEMS)

        val list = ArrayList<JSONObject>()
        for (i in 0 until array.length()) {
            list.add(array.getJSONObject(i))
        }
        list.removeAt(pos)
        val newArray = JSONArray(list)

        prefsJO = JSONObject()
        prefsJO.put(ITEMS, newArray)

        preferences.edit()
                .putString(TAG, prefsJO.toString())
                .apply()
    }

}
