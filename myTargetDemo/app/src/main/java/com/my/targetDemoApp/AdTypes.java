package com.my.targetDemoApp;

import com.my.targetDemoApp.activities.AdActivity;
import com.my.targetDemoApp.activities.StandardBannerAdActivity;
import com.my.targetDemoApp.activities.InstreamAdActivity;
import com.my.targetDemoApp.activities.InterstitialAdActivity;
import com.my.targetDemoApp.activities.NativeAdActivity;

public class AdTypes
{
	public static final int AD_TYPE_320X50 = 0;
	public static final int AD_TYPE_FULLSCREEN = 1;
	public static final int AD_TYPE_NATIVE = 2;
	public static final int AD_TYPE_INSTREAM = 3;
	public static final int AD_TYPE_300X250 = 4;

	public static Class<? extends AdActivity> getActivityByType(int type)
	{
		switch (type)
		{
			case AD_TYPE_FULLSCREEN:
				return InterstitialAdActivity.class;
			case AD_TYPE_NATIVE:
				return NativeAdActivity.class;
			case AD_TYPE_INSTREAM:
				return InstreamAdActivity.class;
			default:
				return StandardBannerAdActivity.class;
		}
	}
}
