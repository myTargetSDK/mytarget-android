package com.my.target.ads.mediation;

import android.content.Context;

import com.mopub.MopubCustomParamsUtils;
import com.mopub.common.DataKeys;
import com.mopub.common.logging.MoPubLog;
import com.mopub.common.logging.MoPubLog.AdapterLogEvent;
import com.mopub.mobileads.CustomEventBanner;
import com.mopub.mobileads.MoPubErrorCode;
import com.my.target.ads.MyTargetView;
import com.my.target.ads.MyTargetView.AdSize;
import com.my.target.common.CustomParams;

import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.mopub.common.logging.MoPubLog.AdLogEvent.LOAD_FAILED;

public class MyTargetMopubCustomEventBanner extends CustomEventBanner
{
	private static final String ADAPTER_NAME = MyTargetMopubCustomEventBanner.class.getSimpleName();
	private static final String SLOT_ID_KEY = "slotId";

	private final MyTargetView.MyTargetViewListener myTargetViewListener = new MyTargetView.MyTargetViewListener()
	{
		@Override
		public void onLoad(@NonNull MyTargetView myTargetView)
		{
			MoPubLog.log(AdapterLogEvent.LOAD_SUCCESS, ADAPTER_NAME);
			if (bannerListener != null)
			{
				bannerListener.onBannerLoaded(myTargetView);
			}
		}

		@Override
		public void onNoAd(@NonNull String reason, @NonNull MyTargetView myTargetView)
		{
			MoPubLog.log(AdapterLogEvent.LOAD_FAILED, ADAPTER_NAME, "", reason);
			if (bannerListener != null)
			{
				bannerListener.onBannerFailed(MoPubErrorCode.NO_FILL);
			}
		}

		@Override
		public void onClick(@NonNull MyTargetView myTargetView)
		{
			MoPubLog.log(AdapterLogEvent.CLICKED, ADAPTER_NAME);
			if (bannerListener != null)
			{
				bannerListener.onBannerClicked();
			}
		}

		@Override
		public void onShow(@NonNull final MyTargetView myTargetView)
		{
			MoPubLog.log(AdapterLogEvent.SHOW_SUCCESS, ADAPTER_NAME);
			if (bannerListener != null)
			{
				bannerListener.onBannerImpression();
			}
		}
	};

	private @Nullable MyTargetView myTargetView;
	private @Nullable CustomEventBannerListener bannerListener;

	public MyTargetMopubCustomEventBanner()
	{
	}

	@Override
	protected void loadBanner(Context context,
							  @Nullable CustomEventBannerListener customEventBannerListener,
							  @Nullable Map<String, Object> localExtras,
							  @Nullable Map<String, String> serverExtras)
	{
		int slotId = -1;
		if (serverExtras != null && !serverExtras.isEmpty())
		{
			String sslotId = serverExtras.get(SLOT_ID_KEY);
			if (sslotId != null)
			{
				try
				{
					slotId = Integer.parseInt(sslotId);
				}
				catch (NumberFormatException e)
				{
					MoPubLog.log(LOAD_FAILED, ADAPTER_NAME, "", e.getMessage());
					e.printStackTrace();
				}
			}
		}

		if (slotId < 0)
		{
			callNoFill(customEventBannerListener);
			return;
		}

		bannerListener = customEventBannerListener;
		int adSize;
		if (localExtras != null)
		{
			Integer adWidth = (Integer) localExtras.get(DataKeys.AD_WIDTH);
			Integer adHeight = (Integer) localExtras.get(DataKeys.AD_HEIGHT);
			adSize = calculateSize(adWidth, adHeight);
			if (adSize < 0)
			{
				callNoFill(customEventBannerListener);
				return;
			}
		}
		else
		{
			callNoFill(customEventBannerListener);
			return;
		}

		if (myTargetView == null)
		{
			myTargetView = new MyTargetView(context);
			final CustomParams customParams = myTargetView.getCustomParams();
			if (customParams != null)
			{
				MopubCustomParamsUtils.fillCustomParams(customParams, localExtras);
			}
			myTargetView.init(slotId, adSize, false);
			myTargetView.setListener(myTargetViewListener);
		}
		myTargetView.load();
	}

	private int calculateSize(@Nullable Integer width, @Nullable Integer height)
	{
		if (width == null || height == null)
		{
			return -1;
		}
		if (height <= 50)
		{
			return AdSize.BANNER_320x50;
		}
		else if (width <= 300 && height <= 250)
		{
			return AdSize.BANNER_300x250;
		}
		else if (height <= 90)
		{
			return AdSize.BANNER_728x90;
		}
		return -1;
	}

	private void callNoFill(@Nullable CustomEventBannerListener customEventBannerListener)
	{
		MoPubLog.log(LOAD_FAILED, ADAPTER_NAME,
					 MoPubErrorCode.NETWORK_NO_FILL.getIntCode(),
					 MoPubErrorCode.NETWORK_NO_FILL);

		if (customEventBannerListener != null)
		{
			customEventBannerListener.onBannerFailed(MoPubErrorCode.NETWORK_NO_FILL);
		}
	}

	@Override
	protected void onInvalidate()
	{
		if (myTargetView != null)
		{
			myTargetView.destroy();
			myTargetView = null;
		}
		bannerListener = null;
	}
}
