package com.my.target.ads.mediation;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mopub.MopubCustomParamsUtils;
import com.mopub.common.logging.MoPubLog;
import com.mopub.common.logging.MoPubLog.AdLogEvent;
import com.mopub.common.logging.MoPubLog.AdapterLogEvent;
import com.mopub.mobileads.CustomEventBanner;
import com.mopub.mobileads.MoPubErrorCode;
import com.my.target.ads.MyTargetView;
import com.my.target.common.CustomParams;

import java.util.Map;

public class MyTargetMopubCustomEventBanner extends CustomEventBanner
{
	private static final String ADAPTER_NAME = MyTargetMopubCustomEventBanner.class.getSimpleName();
	private static final String SLOT_ID_KEY = "slotId";
	private static final String SIZE_KEY = "mytarget_adsize";

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
							  @Nullable Map<String, Object> stringObjectMap,
							  @Nullable Map<String, String> stringStringMap)
	{
		MoPubLog.log(AdapterLogEvent.LOAD_ATTEMPTED, ADAPTER_NAME);
		if (context == null)
		{
			MoPubLog.log(AdapterLogEvent.LOAD_FAILED, ADAPTER_NAME, "", "null context");
			if (customEventBannerListener != null)
			{
				customEventBannerListener.onBannerFailed(MoPubErrorCode.INTERNAL_ERROR);
			}
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
					MoPubLog.log(AdLogEvent.LOAD_FAILED, ADAPTER_NAME, "", e.getMessage());
					e.printStackTrace();
				}
			}
		}

		if (slotId < 0)
		{
			MoPubLog.log(AdLogEvent.LOAD_FAILED, ADAPTER_NAME, "", "Unable to get slotId from parameter json. Probably Mopub mediation misconfiguration.");
			if (customEventBannerListener != null)
			{
				customEventBannerListener.onBannerFailed(MoPubErrorCode.MISSING_AD_UNIT_ID);
			}
			return;
		}

		bannerListener = customEventBannerListener;
		int adSize = MyTargetView.AdSize.BANNER_320x50;
		if (stringObjectMap != null)
		{
			Object size = stringObjectMap.get(SIZE_KEY);
			if (size != null)
			{
				adSize = (int) size;
			}
		}

		if (myTargetView == null)
		{
			myTargetView = new MyTargetView(context);
			final CustomParams customParams = myTargetView.getCustomParams();
			if (customParams != null && stringObjectMap != null)
			{
				MopubCustomParamsUtils.fillCustomParams(customParams, stringObjectMap);
			}
			myTargetView.init(slotId, adSize, false);
			myTargetView.setListener(myTargetViewListener);
		}
		myTargetView.load();
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
