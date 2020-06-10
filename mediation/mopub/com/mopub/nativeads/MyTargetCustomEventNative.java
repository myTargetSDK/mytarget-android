package com.mopub.nativeads;

import android.content.Context;

import com.mopub.MopubCustomParamsUtils;
import com.mopub.common.logging.MoPubLog;
import com.mopub.common.logging.MoPubLog.AdLogEvent;
import com.mopub.common.logging.MoPubLog.AdapterLogEvent;
import com.my.target.ads.mediation.MyTargetAdapterUtils;
import com.my.target.common.CachePolicy;
import com.my.target.nativeads.NativeAd;
import com.my.target.nativeads.banners.NativePromoBanner;

import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyTargetCustomEventNative extends CustomEventNative
{
	private static final String ADAPTER_NAME = MyTargetCustomEventNative.class.getSimpleName();
	private static final String SLOT_ID_KEY = "slotId";
	private @Nullable NativeAd nativeAd;
	private @Nullable CustomEventNativeListener loadedAdListener;
	private @Nullable Context context;
	private MyTargetStaticNativeAd mopubNativeAd;

	@Override
	protected void loadNativeAd(@NonNull Context context,
								@NonNull CustomEventNativeListener customEventNativeListener,
								@NonNull Map<String, Object> stringObjectMap,
								@NonNull Map<String, String> stringStringMap)
	{
		MoPubLog.log(AdapterLogEvent.LOAD_ATTEMPTED, ADAPTER_NAME);
		this.loadedAdListener = customEventNativeListener;
		this.context = context;

		int slotId = -1;
		if (!stringStringMap.isEmpty())
		{
			String sslotId = stringStringMap.get(SLOT_ID_KEY);
			if (sslotId != null)
			{
				try
				{
					slotId = Integer.parseInt(sslotId);
				}
				catch (NumberFormatException e)
				{
					MoPubLog.log(AdLogEvent.LOAD_FAILED, ADAPTER_NAME, "", e.getMessage());
					e.printStackTrace();
				}
			}
		}

		if (slotId < 0)
		{
			MoPubLog.log(AdLogEvent.LOAD_FAILED, ADAPTER_NAME, "", "Unable to get slotId from parameter json. Probably Mopub mediation misconfiguration.");
			if (loadedAdListener != null)
			{
				loadedAdListener.onNativeAdFailed(NativeErrorCode.UNSPECIFIED);
			}
			return;
		}

		nativeAd = new NativeAd(slotId, context);

		MopubCustomParamsUtils.fillCustomParams(nativeAd.getCustomParams(), stringObjectMap);

		nativeAd.setCachePolicy(CachePolicy.NONE);
		nativeAd.setListener(new MyTargetNativeAdListener());
		MyTargetAdapterUtils.handleConsent();
		nativeAd.load();
	}

	private class MyTargetNativeAdListener implements NativeAd.NativeAdListener
	{
		@Override
		public void onLoad(@NonNull NativePromoBanner banner, @NonNull NativeAd ad)
		{
			if (nativeAd != ad || context == null)
			{
				MoPubLog.log(AdapterLogEvent.LOAD_FAILED, ADAPTER_NAME, "", "null context or ad");
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

			if (loadedAdListener != null)
			{
				loadedAdListener.onNativeAdLoaded(mopubNativeAd);
			}
			MoPubLog.log(AdapterLogEvent.LOAD_SUCCESS, ADAPTER_NAME);
		}

		@Override
		public void onNoAd(@NonNull String reason, @NonNull NativeAd ad)
		{
			MoPubLog.log(AdapterLogEvent.LOAD_FAILED, ADAPTER_NAME, "", reason);
			if (loadedAdListener != null)
			{
				loadedAdListener.onNativeAdFailed(NativeErrorCode.EMPTY_AD_RESPONSE);
			}
		}

		@Override
		public void onClick(@NonNull NativeAd ad)
		{
			mopubNativeAd.notifyAdClicked();
			MoPubLog.log(AdapterLogEvent.CLICKED, ADAPTER_NAME);
		}

		@Override
		public void onShow(@NonNull NativeAd ad)
		{
			mopubNativeAd.notifyAdImpressed();
			MoPubLog.log(AdapterLogEvent.SHOW_SUCCESS, ADAPTER_NAME);
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