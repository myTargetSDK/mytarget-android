package com.mopub;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.my.target.common.CustomParams;

import java.util.Map;

public class MopubCustomParamsUtils
{
	private static final String TAG = "MopubCustomParamsUtils";
	private static final String EXTRA_GENDER = "mytarget_gender";
	private static final String EXTRA_AGE = "mytarget_age";
	private static final String EXTRA_VKID = "mytarget_vk_id";
	private static final String EXTRA_OKID = "mytarget_ok_id";

	public static @NonNull CustomParams fillCustomParams(@NonNull CustomParams customParams,
														 @NonNull Map<String, Object> stringObjectMap)
	{
		customParams.setCustomParam("mediation", "mopub");

		try
		{
			if (stringObjectMap.containsKey(EXTRA_GENDER))
			{
				int gender = (int) stringObjectMap.get(EXTRA_GENDER);
				if (gender >= 0 && gender < 3)
				{
					customParams.setGender(gender);
				}
				else
				{
					Log.e(TAG,"Wrong mopub extra value: " + EXTRA_GENDER
									 + " must be 0 (undefined) or 1 (male), or 2 (female)");
				}
			}
		}
		catch (ClassCastException e)
		{
			String msg = "Wrong mopub extra value: " + EXTRA_GENDER + " must be integer";
			Log.e(TAG,msg);
			Log.d(TAG, "message: " + e.getMessage());
		}

		try
		{
			if (stringObjectMap.containsKey(EXTRA_AGE))
			{
				int age = (int) stringObjectMap.get(EXTRA_AGE);
				if (age > 0)
				{
					customParams.setAge(age);
				}
				else
				{
					Log.e(TAG,"Wrong mopub extra value: " + EXTRA_AGE
									 + " must be > 0");
				}
			}
		}
		catch (ClassCastException e)
		{
			String msg = "Wrong mopub extra value: " + EXTRA_AGE + " must be integer";
			Log.e(TAG,msg);
			Log.d(TAG,"message: " + e.getMessage());
		}

		try
		{
			if (stringObjectMap.containsKey(EXTRA_VKID))
			{
				String vkid = (String) stringObjectMap.get(EXTRA_VKID);
				if (!TextUtils.isEmpty(vkid))
				{
					customParams.setVKId(vkid);
				}
				else
				{
					Log.e(TAG,"Wrong mopub extra value: " + EXTRA_VKID
									 + " is empty");
				}
			}
		}
		catch (ClassCastException e)
		{
			String msg = "Wrong mopub extra value: " + EXTRA_VKID + " must be String";
			Log.e(TAG,msg);
			Log.d(TAG,"message: " + e.getMessage());
		}

		try
		{
			if (stringObjectMap.containsKey(EXTRA_OKID))
			{
				String okId = (String) stringObjectMap.get(EXTRA_OKID);
				if (!TextUtils.isEmpty(okId))
				{
					customParams.setOkId(okId);
				}
				else
				{
					Log.e(TAG,"Wrong mopub extra value: " + EXTRA_OKID
									 + " is empty");
				}
			}
		}
		catch (ClassCastException e)
		{
			String msg = "Wrong mopub extra value: " + EXTRA_OKID + " must be String";
			Log.e(TAG,msg);
			Log.d(TAG,"message: " + e.getMessage());
		}

		return customParams;
	}
}
