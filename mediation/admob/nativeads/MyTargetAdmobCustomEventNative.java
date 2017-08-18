package com.my.target.nativeads.mediation;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.mediation.NativeAdMapper;
import com.google.android.gms.ads.mediation.NativeMediationAdRequest;
import com.google.android.gms.ads.mediation.customevent.CustomEventNative;
import com.google.android.gms.ads.mediation.customevent.CustomEventNativeListener;
import com.my.target.core.Tracer;
import com.my.target.core.enums.SDKKeys;
import com.my.target.nativeads.NativeAd;
import com.my.target.nativeads.banners.NavigationType;

import org.json.JSONObject;

import java.util.Date;
import java.util.GregorianCalendar;

public class MyTargetAdmobCustomEventNative implements CustomEventNative
{
	private static final String SLOT_ID_KEY = "slotId";

	@Nullable
	private CustomEventNativeListener customEventNativeListener;
	@NonNull
	private final NativeAd.NativeAdListener nativeAdListener = new NativeAd.NativeAdListener()
	{
		@Override
		public void onLoad(NativeAd ad)
		{
			if (resources == null || ad == null || ad.getBanner() == null)
			{
				Tracer.d("MyTargetAdmobCustomEventNative failed to load, resources or ad null");
				if (customEventNativeListener != null)
				{
					customEventNativeListener.onAdFailedToLoad(AdRequest.ERROR_CODE_INTERNAL_ERROR);
				}
				return;
			}

			String navigationType = ad.getBanner().getNavigationType();

			NativeAdMapper nativeAdMapper = createNativeAdMapper(ad, navigationType);

			if (nativeAdMapper == null)
			{
				Tracer.d("MyTargetAdmobCustomEventNative failed to load, unable to create ad mapper");
				if (customEventNativeListener != null)
				{
					customEventNativeListener.onAdFailedToLoad(AdRequest.ERROR_CODE_INTERNAL_ERROR);
				}
				return;
			}

			if (customEventNativeListener != null)
			{
				customEventNativeListener.onAdLoaded(nativeAdMapper);
			}
		}

		@Override
		public void onNoAd(String reason, NativeAd ad)
		{
			Tracer.d("no ad, reason: " + reason);
			if (customEventNativeListener != null)
			{
				customEventNativeListener.onAdFailedToLoad(AdRequest.ERROR_CODE_NO_FILL);
			}
		}

		@Override
		public void onClick(NativeAd ad)
		{
			Tracer.d("native ad clicked");
			if (customEventNativeListener != null)
			{
				customEventNativeListener.onAdClicked();
			}
		}

		@Override
		public void onShow(NativeAd ad)
		{
			Tracer.d("native ad show");
			if (customEventNativeListener != null)
			{
				customEventNativeListener.onAdImpression();
			}
		}
	};

	@Nullable
	private NativeAdMapper createNativeAdMapper(@NonNull NativeAd ad, @NonNull String navigationType)
	{
		if (NavigationType.STORE.equals(navigationType) || NavigationType.DEEPLINK.equals(navigationType))
		{
			if (!isAppInstallAdRequested)
			{
				Tracer.d("MyTargetAdmobCustomEventNative failed to load: got banner with type " + navigationType +
						" but not allowed in AdMob request");
				if (customEventNativeListener != null)
				{
					customEventNativeListener.onAdFailedToLoad(AdRequest.ERROR_CODE_INVALID_REQUEST);
				}
				return null;
			}
			return new MyTargetNativeInstallAdMapper(ad, resources);
		} else
		{
			if (!isContentAdRequested)
			{
				Tracer.d("MyTargetAdmobCustomEventNative failed to load: got banner with type " + navigationType +
						" but not allowed in AdMob request");
				if (customEventNativeListener != null)
				{
					customEventNativeListener.onAdFailedToLoad(AdRequest.ERROR_CODE_INVALID_REQUEST);
				}
				return null;
			}
			return new MyTargetNativeContentAdMapper(ad, resources);
		}
	}

	private boolean isAppInstallAdRequested;
	private boolean isContentAdRequested;
	@Nullable
	private Resources resources;

	@Override
	public void requestNativeAd(@Nullable Context context,
	                            @Nullable CustomEventNativeListener customEventNativeListener,
	                            @Nullable String serverParameter,
	                            @Nullable NativeMediationAdRequest nativeMediationAdRequest,
	                            @Nullable
	                            Bundle customEventExtras)
	{
		if (context == null)
		{
			Tracer.d("unable to request native ad, context is null");
			if (customEventNativeListener != null)
			{
				customEventNativeListener.onAdFailedToLoad(AdRequest.ERROR_CODE_INTERNAL_ERROR);
			}
			return;
		}

		this.resources = context.getResources();

		this.customEventNativeListener = customEventNativeListener;

		NativeAdOptions options = null;
		int gender = 0;
		Date birthday = null;
		if (nativeMediationAdRequest != null)
		{
			options = nativeMediationAdRequest.getNativeAdOptions();
			isAppInstallAdRequested = nativeMediationAdRequest.isAppInstallAdRequested();
			isContentAdRequested = nativeMediationAdRequest.isContentAdRequested();
			gender = nativeMediationAdRequest.getGender();
			birthday = nativeMediationAdRequest.getBirthday();
		}

		int slotId;
		try
		{
			JSONObject json = new JSONObject(serverParameter);
			slotId = json.getInt(SLOT_ID_KEY);
		} catch (Exception e)
		{
			Tracer.i("Unable to get slotId from parameter json. Probably Admob mediation misconfiguration.");
			if (customEventNativeListener != null)
			{
				customEventNativeListener.onAdFailedToLoad(AdRequest.ERROR_CODE_INTERNAL_ERROR);
			}
			return;
		}

		NativeAd nativeAd = new NativeAd(slotId, context.getApplicationContext());

		if (options != null)
		{
			nativeAd.setAutoLoadImages(!options.shouldReturnUrlsForImageAssets());
		}

		nativeAd.getCustomParams().setGender(gender);

		if (birthday != null && birthday.getTime() != -1)
		{
			GregorianCalendar calendar = new GregorianCalendar();
			GregorianCalendar calendarNow = new GregorianCalendar();

			calendar.setTimeInMillis(birthday.getTime());
			int a = calendarNow.get(GregorianCalendar.YEAR) - calendar.get(GregorianCalendar.YEAR);
			if (a >= 0)
			{
				nativeAd.getCustomParams().setAge(a);
			}
		}

		nativeAd.getCustomParams().setCustomParam(SDKKeys.MEDIATION, SDKKeys.ADMOB);
		nativeAd.setListener(nativeAdListener);
		nativeAd.load();
	}

	@Override
	public void onDestroy()
	{
	}

	@Override
	public void onPause()
	{

	}

	@Override
	public void onResume()
	{

	}
}
