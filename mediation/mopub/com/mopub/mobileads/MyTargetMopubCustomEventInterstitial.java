package com.mopub.mobileads;

import android.app.Activity;
import android.content.Context;

import com.mopub.MopubCustomParamsUtils;
import com.mopub.common.DataKeys;
import com.mopub.common.LifecycleListener;
import com.mopub.common.logging.MoPubLog;
import com.mopub.common.logging.MoPubLog.AdLogEvent;
import com.mopub.mobileads.AdLifecycleListener.InteractionListener;
import com.mopub.mobileads.AdLifecycleListener.LoadListener;
import com.my.target.ads.InterstitialAd;
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

public final class MyTargetMopubCustomEventInterstitial extends BaseAd implements InterstitialAd.InterstitialAdListener
{
	private static final String ADAPTER_NAME = MyTargetMopubCustomEventInterstitial.class.getSimpleName();
	private @Nullable InterstitialAd interstitialAd;
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

		interstitialAd = new InterstitialAd(slotId, context);
		final String adMarkup = extras.get(DataKeys.ADM_KEY);

		MopubCustomParamsUtils.fillCustomParams(interstitialAd.getCustomParams(), adData.getExtras());

		interstitialAd.setListener(this);

		if (adMarkup == null || adMarkup.length() == 0)
		{
			interstitialAd.load();
		}
		else
		{
			interstitialAd.loadFromBid(adMarkup);
		}
		MoPubLog.log(getAdNetworkId(), LOAD_ATTEMPTED, ADAPTER_NAME);
	}

	@Override
	protected void show()
	{
		MoPubLog.log(getAdNetworkId(), SHOW_ATTEMPTED, ADAPTER_NAME);
		if (interstitialAd != null)
		{
			interstitialAd.show();
		}
	}

	@Override
	protected void onInvalidate()
	{
		if (interstitialAd != null)
		{
			interstitialAd.setListener(null);
			interstitialAd.destroy();
		}
		interstitialAd = null;
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
	public void onLoad(@NonNull InterstitialAd ad)
	{
		LoadListener loadListener = mLoadListener;
		if (loadListener != null)
		{
			loadListener.onAdLoaded();
			MoPubLog.log(getAdNetworkId(), LOAD_SUCCESS, ADAPTER_NAME);
		}
	}

	@Override
	public void onNoAd(@NonNull String reason, @NonNull InterstitialAd ad)
	{
		MoPubLog.log(getAdNetworkId(), CUSTOM, ADAPTER_NAME, "myTarget banner ad failed " +
				"to load.");

		MoPubErrorCode code = NETWORK_NO_FILL;
		MoPubLog.log(getAdNetworkId(), AdLogEvent.LOAD_FAILED, ADAPTER_NAME, code.getIntCode(), code);

		LoadListener loadListener = mLoadListener;
		InteractionListener interactionListener = mInteractionListener;
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
	public void onClick(@NonNull InterstitialAd ad)
	{
		MoPubLog.log(getAdNetworkId(), CLICKED, ADAPTER_NAME);
		InteractionListener interactionListener = mInteractionListener;
		if (interactionListener != null)
		{
			interactionListener.onAdClicked();
		}
	}

	@Override
	public void onDismiss(@NonNull InterstitialAd ad)
	{
		InteractionListener interactionListener = mInteractionListener;
		if (interactionListener != null)
		{
			interactionListener.onAdDismissed();
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
		MoPubLog.log(getAdNetworkId(), SHOW_SUCCESS, ADAPTER_NAME);
		InteractionListener interactionListener = mInteractionListener;
		if (interactionListener != null)
		{
			interactionListener.onAdShown();
			interactionListener.onAdImpression();
		}
	}
}
