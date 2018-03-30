package com.mopub.mobileads;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.mopub.MopubCustomParamsUtils;
import com.my.target.ads.InterstitialAd;

import java.util.Map;

public class MyTargetMopubCustomEventInterstitial extends CustomEventInterstitial
{
	private static final String TAG = "MyTargetMopubCustomEven";
	private static final String SLOT_ID_KEY = "slotId";
	private @Nullable InterstitialAd ad;
	private @Nullable CustomEventInterstitialListener mopubInterstitialListener;

	@Override
	protected void loadInterstitial(Context context,
									CustomEventInterstitialListener customEventInterstitialListener,
									Map<String, Object> stringObjectMap,
									Map<String, String> stringStringMap)
	{
		this.mopubInterstitialListener = customEventInterstitialListener;
		Log.d(TAG, "Loading mopub mediation interstitial");
		if (context == null)
		{
			return;
		}

		int slotId;
		if (stringStringMap != null && (stringStringMap.size() == 0 || !stringStringMap.containsKey(SLOT_ID_KEY)))
		{
			Log.d(TAG, "Unable to get slotId from parameter json. Probably MoPub mediation misconfiguration.");
			if (customEventInterstitialListener != null)
			{
				customEventInterstitialListener.onInterstitialFailed(MoPubErrorCode.UNSPECIFIED);
			}
			return;
		}

		if (stringStringMap != null)
		{
			slotId = Integer.parseInt(stringStringMap.get(SLOT_ID_KEY));
		}
		else
		{
			Log.d(TAG, "Unable to get myTarget slotId");
			return;
		}

		ad = new InterstitialAd(slotId, context);
		if (stringObjectMap != null)
		{
			MopubCustomParamsUtils.fillCustomParams(ad.getCustomParams(), stringObjectMap);
		}

		ad.setListener(new MyTargetInterstitialAdListener());
		ad.load();
	}

	@Override
	protected void showInterstitial()
	{
		Log.d(TAG, "Showing mopub mediation interstitial");
		if (ad != null)
		{
			ad.show();
		}
	}

	@Override
	protected void onInvalidate()
	{
		if (ad != null)
		{
			ad.setListener(null);
		}
	}

	private class MyTargetInterstitialAdListener implements InterstitialAd.InterstitialAdListener
	{
		@Override
		public void onLoad(@NonNull InterstitialAd ad)
		{
			Log.d(TAG, "Mediation interstitial ad loaded");
			if (mopubInterstitialListener != null)
			{
				mopubInterstitialListener.onInterstitialLoaded();
			}
		}

		@Override
		public void onNoAd(@NonNull String reason, @NonNull InterstitialAd ad)
		{
			Log.d(TAG, "Mediation interstitial failed to load: " + reason);
			if (mopubInterstitialListener != null)
			{
				mopubInterstitialListener.onInterstitialFailed(MoPubErrorCode.MRAID_LOAD_ERROR);
			}
		}

		@Override
		public void onClick(@NonNull InterstitialAd ad)
		{
			Log.d(TAG, "Mediation interstitial clicked");
			if (mopubInterstitialListener != null)
			{
				mopubInterstitialListener.onInterstitialClicked();
			}
		}

		@Override
		public void onDismiss(@NonNull InterstitialAd ad)
		{
			Log.d(TAG, "Mediation interstitial dismissed");
			if (mopubInterstitialListener != null)
			{
				mopubInterstitialListener.onInterstitialDismissed();
			}
		}

		@Override
		public void onVideoCompleted(@NonNull InterstitialAd ad)
		{

		}

		@Override
		public void onDisplay(@NonNull InterstitialAd ad)
		{
			Log.d(TAG, "Mediation interstitial shown");
			if (mopubInterstitialListener != null)
			{
				mopubInterstitialListener.onInterstitialShown();
			}
		}
	}
}
