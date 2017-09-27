package com.my.target.ads.mediation;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.mediation.MediationAdRequest;
import com.google.android.gms.ads.mediation.customevent.CustomEventBanner;
import com.google.android.gms.ads.mediation.customevent.CustomEventBannerListener;
import com.my.target.ads.MyTargetView;

import org.json.JSONObject;

import java.util.Date;
import java.util.GregorianCalendar;

public class MyTargetAdmobCustomEventBanner implements CustomEventBanner
{
	private static final String SLOT_ID_KEY = "slotId";
	private static final String TAG = "MyTargetAdmobEvent";

	@Nullable
	private MyTargetView myTargetView;
	@Nullable
	private CustomEventBannerListener bannerListener;

	public MyTargetAdmobCustomEventBanner()
	{
	}

	@Override
	public void requestBannerAd(Context context,
								CustomEventBannerListener customEventBannerListener,
								String s,
								AdSize adSize,
								MediationAdRequest mediationAdRequest,
								Bundle bundle)
	{
		int slotId;
		try
		{
			JSONObject json = new JSONObject(s);
			slotId = json.getInt(SLOT_ID_KEY);
		} catch (Exception e)
		{
			Log.i(TAG, "Unable to get slotId from parameter json. Probably Admob mediation misconfiguration.");
			if (customEventBannerListener != null)
			{
				customEventBannerListener.onAdFailedToLoad(AdRequest.ERROR_CODE_INTERNAL_ERROR);
			}
			return;
		}

		if (AdSize.MEDIUM_RECTANGLE.equals(adSize))
		{
			load(customEventBannerListener,
				 mediationAdRequest,
				 slotId,
				 MyTargetView.AdSize.BANNER_300x250,
				 context);
		} else if (AdSize.LEADERBOARD.equals(adSize))
		{
			load(customEventBannerListener,
				 mediationAdRequest,
				 slotId,
				 MyTargetView.AdSize.BANNER_728x90,
				 context);
		} else
		{
			load(customEventBannerListener,
				 mediationAdRequest,
				 slotId,
				 MyTargetView.AdSize.BANNER_320x50,
				 context);
		}
	}

	private void load(CustomEventBannerListener customEventBannerListener,
					  MediationAdRequest mediationAdRequest,
					  int slotId,
					  int adSize,
					  Context context)
	{
		bannerListener = customEventBannerListener;
		if (myTargetView == null)
		{
			myTargetView = new MyTargetView(context);

			myTargetView.init(slotId, adSize, false);

			if (mediationAdRequest != null)
			{
				myTargetView.getCustomParams().setGender(mediationAdRequest.getGender());

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
						myTargetView.getCustomParams().setAge(a);
					}
				}
			}

			myTargetView.getCustomParams().setCustomParam("mediation", "1");
			myTargetView.setListener(myTargetViewListener);
		}
		myTargetView.load();
	}


	@Override
	public void onDestroy()
	{
		if (myTargetView != null)
		{
			myTargetView.destroy();
			myTargetView = null;
		}
		bannerListener = null;
	}

	private final MyTargetView.MyTargetViewListener myTargetViewListener = new MyTargetView.MyTargetViewListener()
	{
		@Override
		public void onLoad(MyTargetView myTargetView)
		{
			myTargetView.start();
			if (bannerListener != null)
			{
				bannerListener.onAdLoaded(myTargetView);
			}
		}

		@Override
		public void onNoAd(String reason, MyTargetView myTargetView)
		{
			if (bannerListener != null)
			{
				bannerListener.onAdFailedToLoad(AdRequest.ERROR_CODE_NO_FILL);
			}
		}

		@Override
		public void onClick(MyTargetView myTargetView)
		{
			if (bannerListener != null)
			{
				bannerListener.onAdClicked();
				bannerListener.onAdOpened();
				bannerListener.onAdLeftApplication();
			}
		}
	};

	@Override
	public void onPause()
	{
		if (myTargetView != null)
		{
			myTargetView.pause();
		}
	}

	@Override
	public void onResume()
	{
		if (myTargetView != null)
		{
			myTargetView.resume();
		}
	}
}
