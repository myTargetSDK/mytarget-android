package com.my.targetDemoApp.utils;

import android.app.Activity;
import android.content.Intent;

import com.my.targetDemoApp.AdTypes;
import com.my.targetDemoApp.models.AdvertisingType;

public class ActivityUtils
{
	public static final String AD_TYPE_TAG = "adtype";

	public static void startNewActivity(Activity from, AdvertisingType advertisingType)
	{
		Intent intent = new Intent(from, AdTypes.getActivityByType(advertisingType.getAdType()));
		intent.putExtra(AD_TYPE_TAG, advertisingType);
		from.startActivity(intent);
	}
}
