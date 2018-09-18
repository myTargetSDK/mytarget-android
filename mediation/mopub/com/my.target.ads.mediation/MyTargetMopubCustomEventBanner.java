package com.my.target.ads.mediation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.mopub.MopubCustomParamsUtils;
import com.mopub.mobileads.CustomEventBanner;
import com.mopub.mobileads.MoPubErrorCode;
import com.my.target.ads.MyTargetView;
import com.my.target.common.CustomParams;

import java.util.Map;

public class MyTargetMopubCustomEventBanner extends CustomEventBanner
{
	/**
	 * Static members*
	 */
	private static final String TAG = "MyTargetMopubCustomEven";
	private static final String SLOT_ID_KEY = "slotId";

	/**Static getters and setters**/

	/**
	 * Static methods*
	 */
	private final MyTargetView.MyTargetViewListener myTargetViewListener = new MyTargetView.MyTargetViewListener()
	{
		@Override
		public void onLoad(@NonNull MyTargetView myTargetView)
		{
			myTargetView.start();
			if (bannerListener != null)
			{
				bannerListener.onBannerLoaded(myTargetView);
			}
		}

		@Override
		public void onNoAd(@NonNull String reason, @NonNull MyTargetView myTargetView)
		{
			if (bannerListener != null)
			{
				bannerListener.onBannerFailed(MoPubErrorCode.NO_FILL);
			}
		}

		@Override
		public void onClick(@NonNull MyTargetView myTargetView)
		{
			if (bannerListener != null)
			{
				bannerListener.onBannerClicked();
				bannerListener.onLeaveApplication();
			}
		}
	};

	private @Nullable MyTargetView myTargetView;
	private @Nullable CustomEventBannerListener bannerListener;

	/**
	 * Constructor*
	 */
	public MyTargetMopubCustomEventBanner()
	{
	}

	@Override
	protected void loadBanner(Context context, CustomEventBannerListener customEventBannerListener, Map<String, Object> stringObjectMap, Map<String, String> stringStringMap)
	{
		int slotId;
		if (stringStringMap == null || stringStringMap.size() == 0 || !stringStringMap.containsKey(SLOT_ID_KEY))
		{
			Log.w(TAG, "Unable to get slotId from parameter json. Probably Mopub mediation misconfiguration.");
			if (customEventBannerListener != null)
			{
				customEventBannerListener.onBannerFailed(MoPubErrorCode.NO_FILL);
			}

			return;
		}

		slotId = Integer.parseInt(stringStringMap.get(SLOT_ID_KEY));

		bannerListener = customEventBannerListener;
		if (myTargetView == null)
		{
			myTargetView = new MyTargetView(context);
			myTargetView.init(slotId, MyTargetView.AdSize.BANNER_320x50, false);
			final CustomParams customParams = myTargetView.getCustomParams();
			if (customParams != null)
			{
				MopubCustomParamsUtils.fillCustomParams(customParams, stringObjectMap);
			}
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
