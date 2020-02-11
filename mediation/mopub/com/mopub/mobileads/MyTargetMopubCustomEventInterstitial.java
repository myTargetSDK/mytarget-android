package com.mopub.mobileads;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mopub.MopubCustomParamsUtils;
import com.mopub.common.logging.MoPubLog;
import com.mopub.common.logging.MoPubLog.AdLogEvent;
import com.mopub.common.logging.MoPubLog.AdapterLogEvent;
import com.my.target.ads.InterstitialAd;

import java.util.Map;

public class MyTargetMopubCustomEventInterstitial extends CustomEventInterstitial
{
	private static final String SLOT_ID_KEY = "slotId";
	private static final String ADAPTER_NAME = MyTargetMopubCustomEventInterstitial.class.getSimpleName();
	private @Nullable InterstitialAd ad;
	private @Nullable CustomEventInterstitialListener mopubInterstitialListener;

	@Override
	protected void loadInterstitial(@Nullable Context context,
									@Nullable CustomEventInterstitialListener customEventInterstitialListener,
									@Nullable Map<String, Object> stringObjectMap,
									@Nullable Map<String, String> stringStringMap)
	{
		this.mopubInterstitialListener = customEventInterstitialListener;
		MoPubLog.log(AdLogEvent.LOAD_ATTEMPTED, ADAPTER_NAME);

		if (context == null)
		{
			MoPubLog.log(AdLogEvent.LOAD_FAILED, ADAPTER_NAME, "", "null context");
			return;
		}

		int slotId = -1;
		if (stringStringMap != null && !stringStringMap.isEmpty())
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
					MoPubLog.log(AdLogEvent.LOAD_FAILED, ADAPTER_NAME, e.getMessage());
					e.printStackTrace();
				}
			}
		}

		if (slotId < 0)
		{
			MoPubLog.log(AdLogEvent.LOAD_FAILED, ADAPTER_NAME, "", "Unable to get slotId from parameter json. Probably Mopub mediation misconfiguration.");
			if (mopubInterstitialListener != null)
			{
				mopubInterstitialListener.onInterstitialFailed(MoPubErrorCode.MISSING_AD_UNIT_ID);
			}
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
		MoPubLog.log(AdLogEvent.SHOW_ATTEMPTED, ADAPTER_NAME);
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
			MoPubLog.log(AdLogEvent.LOAD_SUCCESS, ADAPTER_NAME);
			if (mopubInterstitialListener != null)
			{
				mopubInterstitialListener.onInterstitialLoaded();
			}
		}

		@Override
		public void onNoAd(@NonNull String reason, @NonNull InterstitialAd ad)
		{
			MoPubLog.log(AdLogEvent.LOAD_FAILED, ADAPTER_NAME, "", reason);
			if (mopubInterstitialListener != null)
			{
				mopubInterstitialListener.onInterstitialFailed(MoPubErrorCode.MRAID_LOAD_ERROR);
			}
		}

		@Override
		public void onClick(@NonNull InterstitialAd ad)
		{
			MoPubLog.log(AdLogEvent.CLICKED, ADAPTER_NAME);
			if (mopubInterstitialListener != null)
			{
				mopubInterstitialListener.onInterstitialClicked();
			}
		}

		@Override
		public void onDismiss(@NonNull InterstitialAd ad)
		{
			MoPubLog.log(AdapterLogEvent.WILL_DISAPPEAR, ADAPTER_NAME);
			if (mopubInterstitialListener != null)
			{
				mopubInterstitialListener.onInterstitialDismissed();
			}
		}

		@Override
		public void onVideoCompleted(@NonNull InterstitialAd ad)
		{
			MoPubLog.log(AdLogEvent.CUSTOM, ADAPTER_NAME, "Video Completed");
		}

		@Override
		public void onDisplay(@NonNull InterstitialAd ad)
		{
			MoPubLog.log(AdLogEvent.SHOW_SUCCESS, ADAPTER_NAME);
			if (mopubInterstitialListener != null)
			{
				mopubInterstitialListener.onInterstitialShown();
			}
		}
	}
}
