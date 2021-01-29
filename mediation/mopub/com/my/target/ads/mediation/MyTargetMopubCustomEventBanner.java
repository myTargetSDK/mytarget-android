package com.my.target.ads.mediation;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.mopub.MopubCustomParamsUtils;
import com.mopub.common.DataKeys;
import com.mopub.common.LifecycleListener;
import com.mopub.common.logging.MoPubLog;
import com.mopub.common.util.Views;
import com.mopub.mobileads.AdData;
import com.mopub.mobileads.AdLifecycleListener.InteractionListener;
import com.mopub.mobileads.AdLifecycleListener.LoadListener;
import com.mopub.mobileads.BaseAd;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MyTargetAdapterConfiguration;
import com.my.target.ads.MyTargetView;
import com.my.target.ads.MyTargetView.AdSize;
import com.my.target.ads.MyTargetView.MyTargetViewListener;

import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.mopub.common.logging.MoPubLog.AdapterLogEvent.CLICKED;
import static com.mopub.common.logging.MoPubLog.AdapterLogEvent.CUSTOM;
import static com.mopub.common.logging.MoPubLog.AdapterLogEvent.LOAD_ATTEMPTED;
import static com.mopub.common.logging.MoPubLog.AdapterLogEvent.LOAD_FAILED;
import static com.mopub.common.logging.MoPubLog.AdapterLogEvent.LOAD_SUCCESS;
import static com.mopub.mobileads.MoPubErrorCode.NETWORK_NO_FILL;

public final class MyTargetMopubCustomEventBanner extends BaseAd implements MyTargetViewListener
{
	private static final String ADAPTER_NAME = MyTargetMopubCustomEventBanner.class.getSimpleName();

	private @Nullable MyTargetView myTargetView;
	private @NonNull String adNetworkId = "";

	@Override
	public void onLoad(@NonNull MyTargetView myTargetView)
	{
		MoPubLog.log(getAdNetworkId(), CUSTOM, ADAPTER_NAME, "myTarget banner ad loaded " +
				"successfully. Showing ad...");

		LoadListener loadListener = mLoadListener;
		if (loadListener != null)
		{
			loadListener.onAdLoaded();
			MoPubLog.log(getAdNetworkId(), LOAD_SUCCESS, ADAPTER_NAME);
		}
	}

	@Override
	public void onNoAd(@NonNull String reason, @NonNull MyTargetView myTargetView)
	{
		MoPubLog.log(getAdNetworkId(), CUSTOM, ADAPTER_NAME, "myTarget banner ad failed to load.");

		MoPubErrorCode code = NETWORK_NO_FILL;
		MoPubLog.log(getAdNetworkId(), LOAD_FAILED, ADAPTER_NAME, code.getIntCode(), code);

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
	public void onClick(@NonNull MyTargetView myTargetView)
	{
		InteractionListener interactionListener = mInteractionListener;
		if (interactionListener != null)
		{
			interactionListener.onAdClicked();
		}
		MoPubLog.log(getAdNetworkId(), CLICKED, ADAPTER_NAME);
	}

	@Override
	public void onShow(@NonNull final MyTargetView myTargetView)
	{
		MoPubLog.log(getAdNetworkId(), CUSTOM, ADAPTER_NAME, "myTarget banner ad onShow");
		InteractionListener interactionListener = mInteractionListener;
		if (interactionListener != null)
		{
			interactionListener.onAdImpression();
		}
	}

	private @Nullable AdSize calculateSize(int width, int height, @NonNull Context context)
	{
		if (width == 300 && height == 250)
		{
			return AdSize.ADSIZE_300x250;
		}
		else if (width == 320 && height == 50)
		{
			return AdSize.ADSIZE_320x50;
		}
		else if (width == 728 && height == 90)
		{
			return AdSize.ADSIZE_728x90;
		}
		else if (width > 0)
		{
			return AdSize.getAdSizeForCurrentOrientation(width, context);
		}
		return null;
	}

	@Override
	protected void onInvalidate()
	{
		if (myTargetView != null)
		{
			Views.removeFromParent(myTargetView);
			myTargetView.setListener(null);
			myTargetView.destroy();
			myTargetView = null;
		}
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
	protected @Nullable View getAdView()
	{
		return myTargetView;
	}

	@Override
	protected void load(@NonNull Context context, @NonNull AdData adData)
	{
		setAutomaticImpressionAndClickTracking(false);

		Map<String, String> extras = adData.getExtras();
		String sslotId = extras.get(MyTargetAdapterConfiguration.SLOT_ID_KEY);
		int slotId = MyTargetAdapterUtils.parseSlot(sslotId);
		if (slotId < 0)
		{
			MoPubLog.log(LOAD_FAILED, ADAPTER_NAME, NETWORK_NO_FILL.getIntCode(), NETWORK_NO_FILL);
			LoadListener loadListener = mLoadListener;
			if (loadListener != null)
			{
				loadListener.onAdLoadFailed(NETWORK_NO_FILL);
			}
			return;
		}
		adNetworkId = sslotId != null ? sslotId : "";
		MyTargetAdapterUtils.handleConsent();
		Integer adHeight = adData.getAdHeight();
		Integer adWidth = adData.getAdWidth();
		AdSize adSize = calculateSize(adWidth == null ? 0 : adWidth, adHeight == null ? 0 : adHeight, context);

		if (myTargetView == null)
		{
			myTargetView = new MyTargetView(context);
			MopubCustomParamsUtils.fillCustomParams(myTargetView.getCustomParams(), extras);
			myTargetView.setSlotId(slotId);
			if (adSize != null)
			{
				myTargetView.setAdSize(adSize);
			}
			myTargetView.setRefreshAd(false);
			myTargetView.setListener(this);
		}
		final String adMarkup = extras.get(DataKeys.ADM_KEY);
		if (adMarkup == null || adMarkup.length() == 0)
		{
			myTargetView.load();
		}
		else
		{
			myTargetView.loadFromBid(adMarkup);
		}
		MoPubLog.log(getAdNetworkId(), LOAD_ATTEMPTED, ADAPTER_NAME);
	}
}
