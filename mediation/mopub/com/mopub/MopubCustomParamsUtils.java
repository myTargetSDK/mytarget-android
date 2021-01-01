package com.mopub;

import android.util.Log;

import com.my.target.common.CustomParams;

import java.util.Map;

import androidx.annotation.NonNull;

public final class MopubCustomParamsUtils
{
	private static final String TAG = "MopubCustomParamsUtils";
	private static final String EXTRA_GENDER = "mytarget_gender";
	private static final String EXTRA_AGE = "mytarget_age";
	private static final String EXTRA_VKID = "mytarget_vk_id";
	private static final String EXTRA_OKID = "mytarget_ok_id";

	public static void fillCustomParams(@NonNull CustomParams customParams,
										@NonNull Map<String, String> extras)
	{
		customParams.setCustomParam("mediation", "2");

		String genderValue = extras.get(EXTRA_GENDER);
		if (genderValue != null)
		{
			int gender = 0;
			try
			{
				gender = Integer.parseInt(genderValue);
			}
			catch (NumberFormatException ignored)
			{
			}
			if (gender >= 0 && gender < 3)
			{
				customParams.setGender(gender);
			}
			else
			{
				Log.e(TAG, "Wrong mopub extra value: " + EXTRA_GENDER
						+ " must be 0 (undefined) or 1 (male), or 2 (female)");
			}
		}

		String ageValue = extras.get(EXTRA_AGE);
		if (ageValue != null)
		{
			int age = 0;
			try
			{
				age = Integer.parseInt(ageValue);
			}
			catch (NumberFormatException ignored)
			{
			}
			if (age > 0)
			{
				customParams.setAge(age);
			}
			else
			{
				Log.e(TAG, "Wrong mopub extra value: " + EXTRA_AGE);
			}
		}

		String vkIdValue = extras.get(EXTRA_VKID);
		if (vkIdValue != null)
		{
			if (vkIdValue.length() > 0)
			{
				customParams.setVKId(vkIdValue);
			}
			else
			{
				Log.e(TAG, "Wrong mopub extra value: " + EXTRA_VKID + " is empty");
			}
		}

		String okIdValue = extras.get(EXTRA_OKID);
		if (okIdValue != null)
		{
			if (okIdValue.length() > 0)
			{
				customParams.setOkId(okIdValue);
			}
			else
			{
				Log.e(TAG, "Wrong mopub extra value: " + EXTRA_OKID + " is empty");
			}
		}
	}

	public static void fillLocalCustomParams(@NonNull CustomParams customParams,
											 @NonNull Map<String, Object> extras)
	{
		Map<String,String> localExtras = new HashMap<>();
		for (Map.Entry<String, Object> entry : extras.entrySet())
		{
			localExtras.put(entry.getKey(), String.valueOf(entry.getValue()));
		}
		fillCustomParams(customParams, localExtras);
	}
}
