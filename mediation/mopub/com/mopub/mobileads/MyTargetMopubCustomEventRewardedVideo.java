package com.mopub.mobileads;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mopub.MopubCustomParamsUtils;
import com.mopub.common.LifecycleListener;
import com.mopub.common.MoPubReward;
import com.mopub.common.logging.MoPubLog;
import com.mopub.common.logging.MoPubLog.AdapterLogEvent;
import com.my.target.ads.InterstitialAd;
import com.my.target.ads.mediation.MyTargetAdapterUtils;

import java.util.Map;

public class MyTargetMopubCustomEventRewardedVideo extends CustomEventRewardedVideo
{
	private static final String NETWORK_ID = "myTarget";
	private static final String ADAPTER_NAME = MyTargetMopubCustomEventRewardedVideo.class.getSimpleName();
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
			MoPubLog.log(AdapterLogEvent.LOAD_FAILED, ADAPTER_NAME, "", "Unable to get slotId from parameter json. Probably Mopub mediation misconfiguration.");
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
		MoPubLog.log(AdapterLogEvent.LOAD_ATTEMPTED, ADAPTER_NAME);
		if (ad != null)
		{
			MyTargetAdapterUtils.handleConsent();
			ad.load();
		}
		else
		{
			MoPubLog.log(AdapterLogEvent.LOAD_FAILED, ADAPTER_NAME, "", "null ad");
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
		MoPubLog.log(AdapterLogEvent.SHOW_ATTEMPTED, ADAPTER_NAME);
		if (ad != null)
		{
			ad.show();
		}
		else
		{
			MoPubLog.log(AdapterLogEvent.SHOW_FAILED, ADAPTER_NAME, "", "null ad");
		}
	}

	private class RewardedListener implements InterstitialAd.InterstitialAdListener
	{
		@Override
		public void onLoad(@NonNull InterstitialAd ad)
		{
			MoPubLog.log(AdapterLogEvent.LOAD_SUCCESS, ADAPTER_NAME);
			loaded = true;
			MoPubRewardedVideoManager.onRewardedVideoLoadSuccess(
					MyTargetMopubCustomEventRewardedVideo.class,
					NETWORK_ID);
		}

		@Override
		public void onNoAd(@NonNull String reason, @NonNull InterstitialAd ad)
		{
			MoPubLog.log(AdapterLogEvent.LOAD_FAILED, ADAPTER_NAME, "", reason);
			MoPubRewardedVideoManager.onRewardedVideoLoadFailure(
					MyTargetMopubCustomEventRewardedVideo.class, NETWORK_ID,
					MoPubErrorCode.NO_FILL);
		}

		@Override
		public void onClick(@NonNull InterstitialAd ad)
		{
			MoPubLog.log(AdapterLogEvent.CLICKED, ADAPTER_NAME);
			MoPubRewardedVideoManager.onRewardedVideoClicked(
					MyTargetMopubCustomEventRewardedVideo.class, NETWORK_ID);
		}

		@Override
		public void onDismiss(@NonNull InterstitialAd ad)
		{
			MoPubLog.log(AdapterLogEvent.WILL_DISAPPEAR, ADAPTER_NAME);
			MoPubRewardedVideoManager.onRewardedVideoClosed(
					MyTargetMopubCustomEventRewardedVideo.class, NETWORK_ID);
		}

		@Override
		public void onVideoCompleted(@NonNull InterstitialAd ad)
		{
			MoPubReward moPubReward = MoPubReward.success(MoPubReward.NO_REWARD_LABEL,
														  MoPubReward.DEFAULT_REWARD_AMOUNT);
			MoPubLog.log(AdapterLogEvent.SHOULD_REWARD, ADAPTER_NAME, moPubReward.getAmount(), moPubReward.getLabel());
			MoPubRewardedVideoManager.onRewardedVideoCompleted
					(MyTargetMopubCustomEventRewardedVideo.class,
					 NETWORK_ID,
					 moPubReward);
		}

		@Override
		public void onDisplay(@NonNull InterstitialAd ad)
		{
			MoPubLog.log(AdapterLogEvent.SHOW_SUCCESS, ADAPTER_NAME);
			MoPubRewardedVideoManager.onRewardedVideoStarted(
					MyTargetMopubCustomEventRewardedVideo.class, NETWORK_ID);
		}
	}
}
