package com.my.targetDemoApp

import android.content.Context
import android.preference.PreferenceManager
import org.json.JSONArray
import org.json.JSONObject

class Saver(context: Context) {

    companion object {
        const val TAG = "saved_types"
        const val AD_TYPE = "ad_type"
        const val NAME = "name"
        const val ITEMS = "items"
        const val SLOT = "slot"
    }

    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun save(adType: CustomAdvertisingType) {
        val prefs = preferences.getString(TAG, "{\"$ITEMS\":[]}")
        val prefsJO = JSONObject(prefs)
        val array = prefsJO.getJSONArray(ITEMS)
        val itemJO = JSONObject()
        itemJO.put(AD_TYPE, adType.adType)
        itemJO.put(SLOT, adType.slotId)
        itemJO.put(NAME, adType.name)
        array.put(itemJO)
        preferences.edit().putString(TAG, prefsJO.toString()).apply()
    }

    fun restore(): Collection<CustomAdvertisingType>? {
        val types = ArrayList<CustomAdvertisingType>()

        val prefs = preferences.getString(TAG, "{\"$ITEMS\":[]}")
        val prefsJO = JSONObject(prefs)
        val array = prefsJO.getJSONArray(ITEMS)
        for (i in 0 until array.length()) {
            val item = array.getJSONObject(i)
            val type = CustomAdvertisingType(CustomAdvertisingType.AdType.valueOf(item.getString(
                    AD_TYPE)), item.getInt(SLOT))
            type.name = item.getString(NAME)
            types.add(type)
        }
        return types
    }

    fun remove(pos: Int) {
        if (pos < 0) {
            return
        }

        val prefs = preferences.getString(TAG, "{\"$ITEMS\":[]}")
        var prefsJO = JSONObject(prefs)
        val array = prefsJO.getJSONArray(ITEMS)

        val list = ArrayList<JSONObject>()
        val len = array.length()
        if (array != null) {
            for (i in 0 until len) {
                list.add(array.getJSONObject(i))
            }
        }
        list.removeAt(pos)
        val newArray = JSONArray(list)

        prefsJO = JSONObject()
        prefsJO.put(ITEMS, newArray)


        preferences.edit().putString(TAG, prefsJO.toString()).apply()
    }

}
