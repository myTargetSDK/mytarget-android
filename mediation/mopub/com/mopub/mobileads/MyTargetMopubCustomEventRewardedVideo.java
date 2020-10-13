package com.mopub.mobileads;

import android.app.Activity;
import android.content.Context;

import com.mopub.MopubCustomParamsUtils;
import com.mopub.common.DataKeys;
import com.mopub.common.LifecycleListener;
import com.mopub.common.MoPubReward;
import com.mopub.common.logging.MoPubLog;
import com.mopub.common.logging.MoPubLog.AdLogEvent;
import com.mopub.mobileads.AdLifecycleListener.InteractionListener;
import com.mopub.mobileads.AdLifecycleListener.LoadListener;
import com.my.target.ads.Reward;
import com.my.target.ads.RewardedAd;
import com.my.target.ads.mediation.MyTargetAdapterUtils;

import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.mopub.common.logging.MoPubLog.AdapterLogEvent.CLICKED;
import static com.mopub.common.logging.MoPubLog.AdapterLogEvent.CUSTOM;
import static com.mopub.common.logging.MoPubLog.AdapterLogEvent.LOAD_ATTEMPTED;
import static com.mopub.common.logging.MoPubLog.AdapterLogEvent.LOAD_FAILED;
import static com.mopub.common.logging.MoPubLog.AdapterLogEvent.LOAD_SUCCESS;
import static com.mopub.common.logging.MoPubLog.AdapterLogEvent.SHOW_ATTEMPTED;
import static com.mopub.common.logging.MoPubLog.AdapterLogEvent.SHOW_SUCCESS;
import static com.mopub.mobileads.MoPubErrorCode.NETWORK_NO_FILL;

public final class MyTargetMopubCustomEventRewardedVideo extends BaseAd implements RewardedAd.RewardedAdListener
{
	private static final String ADAPTER_NAME = MyTargetMopubCustomEventRewardedVideo.class.getSimpleName();
	private @Nullable RewardedAd rewardedAd;
	private @NonNull String adNetworkId = "";

	@Override
	protected void load(@NonNull final Context context, @NonNull final AdData adData)
	{
		setAutomaticImpressionAndClickTracking(false);

		final Map<String, String> extras = adData.getExtras();
		String sslotId = extras.get(MyTargetAdapterConfiguration.SLOT_ID_KEY);
		int slotId = MyTargetAdapterUtils.parseSlot(sslotId);
		if (slotId < 0)
		{
			LoadListener loadListener = mLoadListener;
			if (loadListener != null)
			{
				loadListener.onAdLoadFailed(MoPubErrorCode.NETWORK_NO_FILL);
				MoPubLog.log(getAdNetworkId(), LOAD_FAILED, ADAPTER_NAME,
							 MoPubErrorCode.NETWORK_NO_FILL.getIntCode(), MoPubErrorCode.NETWORK_NO_FILL);
			}
			return;
		}
		adNetworkId = sslotId != null ? sslotId : "";
		MyTargetAdapterUtils.handleConsent();

		rewardedAd = new RewardedAd(slotId, context);
		final String adMarkup = extras.get(DataKeys.ADM_KEY);

		MopubCustomParamsUtils.fillCustomParams(rewardedAd.getCustomParams(), adData.getExtras());

		rewardedAd.setListener(this);

		if (adMarkup == null || adMarkup.length() == 0)
		{
			rewardedAd.load();
		}
		else
		{
			rewardedAd.loadFromBid(adMarkup);
		}
		MoPubLog.log(getAdNetworkId(), LOAD_ATTEMPTED, ADAPTER_NAME);
	}

	@Override
	protected void show()
	{
		MoPubLog.log(getAdNetworkId(), SHOW_ATTEMPTED, ADAPTER_NAME);
		if (rewardedAd != null)
		{
			rewardedAd.show();
		}
	}

	@Override
	protected void onInvalidate()
	{
		if (rewardedAd != null)
		{
			rewardedAd.setListener(null);
			rewardedAd.destroy();
		}
		rewardedAd = null;
	}

	@Override
	protected @Nullable LifecycleListener getLifecycleListener()
	{
		return null;
	}

	@Override
	protected @NonNull String getAdNetworkId()
	{
		return adNetworkId;
	}

	@Override
	protected boolean checkAndInitializeSdk(@NonNull Activity launcherActivity, @NonNull AdData adData)
	{
		return false;
	}

	@Override
	public void onLoad(@NonNull RewardedAd ad)
	{
		LoadListener loadListener = mLoadListener;
		if (loadListener != null)
		{
			loadListener.onAdLoaded();
			MoPubLog.log(getAdNetworkId(), LOAD_SUCCESS, ADAPTER_NAME);
		}
	}

	@Override
	public void onNoAd(@NonNull String reason, @NonNull RewardedAd ad)
	{
		MoPubLog.log(getAdNetworkId(), CUSTOM, ADAPTER_NAME, "myTarget banner ad failed " +
				"to load.");

		MoPubErrorCode code = NETWORK_NO_FILL;
		MoPubLog.log(getAdNetworkId(), AdLogEvent.LOAD_FAILED, ADAPTER_NAME, code.getIntCode(), code);

		InteractionListener interactionListener = mInteractionListener;
		LoadListener loadListener = mLoadListener;
		if (interactionListener == null && loadListener != null)
		{
			loadListener.onAdLoadFailed(code);
		}
		else if (interactionListener != null)
		{
			interactionListener.onAdFailed(code);
		}
	}

	@Override
	public void onClick(@NonNull RewardedAd ad)
	{
		MoPubLog.log(getAdNetworkId(), CLICKED, ADAPTER_NAME);
		InteractionListener interactionListener = mInteractionListener;
		if (interactionListener != null)
		{
			interactionListener.onAdClicked();
		}
	}

	@Override
	public void onDismiss(@NonNull RewardedAd ad)
	{
		InteractionListener interactionListener = mInteractionListener;
		if (interactionListener != null)
		{
			interactionListener.onAdDismissed();
		}
	}

	@Override
	public void onReward(@NonNull Reward reward, @NonNull RewardedAd ad)
	{
		MoPubLog.log(AdLogEvent.CUSTOM, ADAPTER_NAME, "Rewarded");
		InteractionListener interactionListener = mInteractionListener;
		if (interactionListener != null)
		{
			interactionListener.onAdComplete(MoPubReward.success(reward.type, MoPubReward.NO_REWARD_AMOUNT));
		}
	}

	@Override
	public void onDisplay(@NonNull RewardedAd ad)
	{
		MoPubLog.log(getAdNetworkId(), SHOW_SUCCESS, ADAPTER_NAME);
		InteractionListener interactionListener = mInteractionListener;
		if (interactionListener != null)
		{
			interactionListener.onAdShown();
			interactionListener.onAdImpression();
		}
	}
}
