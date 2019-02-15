package com.mopub.mobileads;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.mopub.MopubCustomParamsUtils;
import com.mopub.common.LifecycleListener;
import com.mopub.common.MoPubReward;
import com.my.target.ads.InterstitialAd;

import java.util.Map;

public class MyTargetMopubCustomEventRewardedVideo extends CustomEventRewardedVideo
{
	private static final String NETWORK_ID = "myTarget";
	private static final String TAG = "MyTargetMopubCustomEven";
	private static final String SLOT_ID_KEY = "slotId";
	private @Nullable InterstitialAd ad;
	private boolean loaded;

	@Nullable
	@Override
	protected LifecycleListener getLifecycleListener()
	{
		return null;
	}

	@NonNull
	@Override
	protected String getAdNetworkId()
	{
		return NETWORK_ID;
	}

	@Override
	protected void onInvalidate()
	{
		if (ad != null)
		{
			ad.setListener(null);
		}
	}

	@SuppressWarnings("RedundantThrows")
	@Override
	protected boolean checkAndInitializeSdk(@NonNull Activity launcherActivity,
											@NonNull Map<String, Object> localExtras,
											@NonNull Map<String, String> stringStringMap)
			throws Exception
	{
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
					e.printStackTrace();
				}
			}
		}

		if (slotId < 0)
		{
			Log.w(TAG, "Unable to get slotId from parameter json. Probably Mopub mediation misconfiguration.");
			return false;
		}

		ad = new InterstitialAd(slotId, launcherActivity);

		MopubCustomParamsUtils.fillCustomParams(ad.getCustomParams(), localExtras);

		ad.setListener(new RewardedListener());
		return true;
	}

	@SuppressWarnings("RedundantThrows")
	@Override
	protected void loadWithSdkInitialized(@NonNull Activity activity,
										  @NonNull Map<String, Object> localExtras,
										  @NonNull Map<String, String> serverExtras) throws
																					 Exception
	{
		Log.d(TAG, "Loading mopub mediation rewarded");
		if (ad != null)
		{
			ad.load();
		}
	}

	@Override
	protected boolean hasVideoAvailable()
	{
		return loaded;
	}

	@Override
	protected void showVideo()
	{
		if (ad != null)
		{
			ad.show();
		}
	}

	private class RewardedListener implements InterstitialAd.InterstitialAdListener
	{
		@Override
		public void onLoad(@NonNull InterstitialAd ad)
		{
			loaded = true;
			MoPubRewardedVideoManager.onRewardedVideoLoadSuccess(
					MyTargetMopubCustomEventRewardedVideo.class,
					NETWORK_ID);
		}

		@Override
		public void onNoAd(@NonNull String reason, @NonNull InterstitialAd ad)
		{
			MoPubRewardedVideoManager.onRewardedVideoLoadFailure(
					MyTargetMopubCustomEventRewardedVideo.class, NETWORK_ID,
					MoPubErrorCode.NO_FILL);
		}

		@Override
		public void onClick(@NonNull InterstitialAd ad)
		{
			MoPubRewardedVideoManager.onRewardedVideoClicked(
					MyTargetMopubCustomEventRewardedVideo.class, NETWORK_ID);
		}

		@Override
		public void onDismiss(@NonNull InterstitialAd ad)
		{
			MoPubRewardedVideoManager.onRewardedVideoClosed(
					MyTargetMopubCustomEventRewardedVideo.class, NETWORK_ID);
		}

		@Override
		public void onVideoCompleted(@NonNull InterstitialAd ad)
		{
			MoPubReward moPubReward = MoPubReward.success(MoPubReward.NO_REWARD_LABEL,
														  MoPubReward.DEFAULT_REWARD_AMOUNT);
			MoPubRewardedVideoManager.onRewardedVideoCompleted
					(MyTargetMopubCustomEventRewardedVideo.class,
					 NETWORK_ID,
					 moPubReward);
		}

		@Override
		public void onDisplay(@NonNull InterstitialAd ad)
		{
			MoPubRewardedVideoManager.onRewardedVideoStarted(
					MyTargetMopubCustomEventRewardedVideo.class, NETWORK_ID);
		}
	}
}
