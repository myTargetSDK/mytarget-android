package com.my.target.ads.mediation;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.mediation.MediationAdRequest;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.mediation.MediationRewardedVideoAdAdapter;
import com.google.android.gms.ads.reward.mediation.MediationRewardedVideoAdListener;
import com.my.target.ads.InterstitialAd;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.GregorianCalendar;

public class MyTargetAdmobCustomEventRewarded extends AdListener implements
																 MediationRewardedVideoAdAdapter
{
	private static final String TAG = "MyTargetAdmobRewarded";
	private static final String SLOT_ID_KEY = "slotId";
	private final RewardItem rewardItem = new RewardItem()
	{
		@Override
		public String getType()
		{
			return "";
		}

		@Override
		public int getAmount()
		{
			return 0;
		}
	};
	@Nullable
	private MediationRewardedVideoAdListener mediationRewardedVideoAdListener;
	private final InterstitialAd.InterstitialAdListener interstitialAdListener =
			new InterstitialAd.InterstitialAdListener()
			{
				@Override
				public void onLoad(InterstitialAd ad)
				{
					if (mediationRewardedVideoAdListener != null)
					{
						mediationRewardedVideoAdListener
								.onAdLoaded(com.my.target.ads.mediation.MyTargetAdmobCustomEventRewarded.this);
					}
				}

				@Override
				public void onNoAd(String reason, InterstitialAd ad)
				{
					if (mediationRewardedVideoAdListener != null)
					{
						mediationRewardedVideoAdListener.onAdFailedToLoad(
								com.my.target.ads.mediation.MyTargetAdmobCustomEventRewarded.this,
								AdRequest.ERROR_CODE_NO_FILL);
					}
				}

				@Override
				public void onClick(InterstitialAd ad)
				{
					if (mediationRewardedVideoAdListener != null)
					{
						mediationRewardedVideoAdListener
								.onAdClicked(com.my.target.ads.mediation.MyTargetAdmobCustomEventRewarded.this);
					}
				}

				@Override
				public void onDismiss(InterstitialAd ad)
				{
					if (mediationRewardedVideoAdListener != null)
					{
						mediationRewardedVideoAdListener
								.onAdClosed(com.my.target.ads.mediation.MyTargetAdmobCustomEventRewarded.this);
					}
				}

				@Override
				public void onVideoCompleted(InterstitialAd ad)
				{
					if (mediationRewardedVideoAdListener != null)
					{
						mediationRewardedVideoAdListener
								.onRewarded(com.my.target.ads.mediation.MyTargetAdmobCustomEventRewarded.this, rewardItem);
					}
				}

				@Override
				public void onDisplay(InterstitialAd ad)
				{
					if (mediationRewardedVideoAdListener != null)
					{
						mediationRewardedVideoAdListener.onAdOpened(
								com.my.target.ads.mediation.MyTargetAdmobCustomEventRewarded.this);
						mediationRewardedVideoAdListener.onVideoStarted
								(com.my.target.ads.mediation.MyTargetAdmobCustomEventRewarded.this);
					}
				}
			};
	@Nullable
	private InterstitialAd interstitial;

	@Override
	public void onDestroy()
	{
		if (interstitial != null)
		{
			interstitial.setListener(null);
		}
	}

	@Override
	public void onPause()
	{
	}

	@Override
	public void onResume()
	{

	}

	@Override
	public void initialize(Context context,
						   MediationAdRequest mediationAdRequest,
						   String serverJSON,
						   MediationRewardedVideoAdListener mediationRewardedVideoAdListener,
						   Bundle serverParameters,
						   Bundle mediationExtras)
	{
		this.mediationRewardedVideoAdListener = mediationRewardedVideoAdListener;

		int slotId;
		try
		{
			String adUnit = serverParameters.getString(
					MediationRewardedVideoAdAdapter.CUSTOM_EVENT_SERVER_PARAMETER_FIELD);
			JSONObject param = new JSONObject(adUnit);
			slotId = param.getInt(SLOT_ID_KEY);
		} catch (JSONException e)
		{
			Log.i(TAG,
				  "Unable to get slotId from parameter json. Probably Admob mediation " +
						  "misconfiguration.");
			mediationRewardedVideoAdListener.onAdFailedToLoad(
					com.my.target.ads.mediation.MyTargetAdmobCustomEventRewarded.this,
					AdRequest.ERROR_CODE_INTERNAL_ERROR);
			return;
		}

		interstitial = new InterstitialAd(slotId, context);
		interstitial.getCustomParams().setCustomParam("mediation", "1");

		if (mediationAdRequest != null)
		{
			interstitial.getCustomParams().setGender(mediationAdRequest.getGender());
			Date date = mediationAdRequest.getBirthday();
			if (date != null && date.getTime() != -1)
			{
				GregorianCalendar calendar = new GregorianCalendar();
				GregorianCalendar calendarNow = new GregorianCalendar();

				calendar.setTimeInMillis(date.getTime());
				int a = calendarNow.get(GregorianCalendar.YEAR) -
						calendar.get(GregorianCalendar.YEAR);
				if (a >= 0)
				{
					interstitial.getCustomParams().setAge(a);
				}
			}
		}

		interstitial.setListener(interstitialAdListener);
		mediationRewardedVideoAdListener
				.onInitializationSucceeded(com.my.target.ads.mediation.MyTargetAdmobCustomEventRewarded.this);
	}

	@Override
	public void loadAd(MediationAdRequest mediationAdRequest, Bundle bundle, Bundle bundle1)
	{
		if (interstitial != null)
		{
			interstitial.load();
		}
	}

	@Override
	public void showVideo()
	{
		if (interstitial != null)
		{
			interstitial.show();
		}
	}

	@Override
	public boolean isInitialized()
	{
		return interstitial != null;
	}
}