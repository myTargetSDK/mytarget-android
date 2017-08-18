package com.my.target.ads.mediation;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.mediation.MediationAdRequest;
import com.google.android.gms.ads.mediation.customevent.CustomEventInterstitial;
import com.google.android.gms.ads.mediation.customevent.CustomEventInterstitialListener;
import com.my.target.ads.InterstitialAd;
import com.my.target.core.Tracer;
import com.my.target.core.enums.SDKKeys;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.GregorianCalendar;

public class MyTargetAdmobCustomEventInterstitial extends AdListener implements
                                                                     CustomEventInterstitial
{
	private static final String SLOT_ID_KEY = "slotId";
	@Nullable
	private CustomEventInterstitialListener interstitialListener;
	private final InterstitialAd.InterstitialAdListener interstitialAdListener =
			new InterstitialAd.InterstitialAdListener()
	{
		@Override
		public void onLoad(InterstitialAd ad)
		{
			Tracer.d("admob mediation interstitial loaded");
			if (interstitialListener != null)
			{
				interstitialListener.onAdLoaded();
			}
		}

		@Override
		public void onNoAd(String reason, InterstitialAd ad)
		{
			Tracer.d("admob mediation interstitial failed to load: " + reason);

			if (interstitialListener != null)
			{
				interstitialListener.onAdFailedToLoad(AdRequest.ERROR_CODE_NO_FILL);
			}
		}

		@Override
		public void onClick(InterstitialAd ad)
		{
			Tracer.d("admob mediation interstitial clicked");

			if (interstitialListener != null)
			{
				interstitialListener.onAdClicked();
			}
		}

		@Override
		public void onDismiss(InterstitialAd ad)
		{
			Tracer.d("admob mediation interstitial dismissed");

			if (interstitialListener != null)
			{
				interstitialListener.onAdClosed();
			}
		}

		@Override
		public void onVideoCompleted(InterstitialAd ad)
		{
			//stub
		}

		@Override
		public void onDisplay(InterstitialAd ad)
		{
			Tracer.d("admob mediation interstitial displayed");

			if (interstitialListener != null)
			{
				interstitialListener.onAdOpened();
			}
		}
	};
	@Nullable
	private InterstitialAd interstitial;

	@Override
	public void requestInterstitialAd(Context context,
	                                  CustomEventInterstitialListener
			                                  customEventInterstitialListener,
	                                  String serverParameter,
	                                  MediationAdRequest mediationAdRequest,
	                                  Bundle bundle)
	{
		this.interstitialListener = customEventInterstitialListener;

		int slotId;
		try
		{
			JSONObject json = new JSONObject(serverParameter);
			slotId = json.getInt(SLOT_ID_KEY);
		} catch (JSONException e)
		{
			Tracer.i(
					"Unable to get slotId from parameter json. Probably Admob mediation " +
							"misconfiguration.");
			if (interstitialListener != null)
			{
				interstitialListener.onAdFailedToLoad(AdRequest.ERROR_CODE_INTERNAL_ERROR);
			}
			return;
		}

		interstitial = new InterstitialAd(slotId, context);
		interstitial.getCustomParams().setCustomParam(SDKKeys.MEDIATION, SDKKeys.ADMOB);
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
		interstitial.load();
	}

	@Override
	public void showInterstitial()
	{
		if (interstitial != null)
		{
			interstitial.show();
		}
	}

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
}
