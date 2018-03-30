package com.mopub.nativeads;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.mopub.MopubCustomParamsUtils;
import com.my.target.core.Tracer;
import com.my.target.nativeads.NativeAd;
import com.my.target.nativeads.banners.NativePromoBanner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyTargetCustomEventNative extends CustomEventNative
{
	private static final String TAG = "MyTargetCustomNative";
	private static final String SLOT_ID_KEY = "slotId";
	private static final List<MyTargetCustomEventNative> activeEvents = new ArrayList<>();
	private @Nullable NativeAd nativeAd;
	private @Nullable CustomEventNativeListener loadedAdListener;
	private @Nullable Context context;
	private @Nullable MyTargetStaticNativeAd mopubNativeAd;

	@Override
	protected void loadNativeAd(@NonNull Context context,
								@NonNull CustomEventNativeListener customEventNativeListener,
								@NonNull Map<String, Object> stringObjectMap,
								@NonNull Map<String, String> stringStringMap)
	{
		this.loadedAdListener = customEventNativeListener;
		this.context = context;

		int slotId;

		if (stringStringMap.containsKey(SLOT_ID_KEY))
		{
			slotId = Integer.parseInt(stringStringMap.get(SLOT_ID_KEY));
		}
		else
		{
			Tracer.i("Unable to get slotId. Probably MoPub custom network misconfiguration.");
			loadedAdListener.onNativeAdFailed(NativeErrorCode.INVALID_RESPONSE);
			return;
		}

		activeEvents.add(this);

		nativeAd = new NativeAd(slotId, context);

		MopubCustomParamsUtils.fillCustomParams(nativeAd.getCustomParams(), stringObjectMap);

		nativeAd.setAutoLoadImages(false);
		nativeAd.setListener(new MyTargetNativeAdListener());
		nativeAd.load();
	}

	private class MyTargetNativeAdListener implements NativeAd.NativeAdListener
	{
		@Override
		public void onLoad(@NonNull NativeAd ad)
		{
			if (nativeAd != ad || context == null)
			{
				Log.d(TAG, "Weird things happening");
				return;
			}

			Log.d(TAG, "Received ad from myTarget, converting to MoPub ad");
			NativePromoBanner banner = nativeAd.getBanner();
			if (banner == null)
			{
				Log.d(TAG, "null banner in myTarget NativeAd");
				return;
			}

			mopubNativeAd = new MyTargetStaticNativeAd(context);
			mopubNativeAd.setNativeAd(ad);
			mopubNativeAd.setTitle(banner.getTitle());
			mopubNativeAd.setCallToAction(banner.getCtaText());

			if (banner.getIcon() != null)
			{
				mopubNativeAd.setIconImageUrl(banner.getIcon().getUrl());
			}

			if (banner.getImage() != null)
			{
				mopubNativeAd.setMainImageUrl(banner.getImage().getUrl());
			}

			mopubNativeAd.setStarRating((double) banner.getRating());
			mopubNativeAd.setText(banner.getDescription());

			if (activeEvents.contains(MyTargetCustomEventNative.this))
			{
				activeEvents.remove(MyTargetCustomEventNative.this);
			}

			if (mopubNativeAd != null && loadedAdListener != null)
			{
				loadedAdListener.onNativeAdLoaded(mopubNativeAd);
			}

		}

		@Override
		public void onNoAd(@NonNull String reason, @NonNull NativeAd ad)
		{
			Log.d(TAG, "NativeAd: no ad, failing mediation");
			if (loadedAdListener != null)
			{
				loadedAdListener.onNativeAdFailed(NativeErrorCode.EMPTY_AD_RESPONSE);
			}

			if (activeEvents.contains(MyTargetCustomEventNative.this))
			{
				activeEvents.remove(MyTargetCustomEventNative.this);
			}
		}

		@Override
		public void onClick(@NonNull NativeAd ad)
		{
			// stub
		}

		@Override
		public void onShow(@NonNull NativeAd ad)
		{

		}

		@Override
		public void onVideoPlay(@NonNull NativeAd ad)
		{
		}

		@Override
		public void onVideoPause(@NonNull NativeAd ad)
		{
		}

		@Override
		public void onVideoComplete(@NonNull NativeAd ad)
		{

		}
	}
}
