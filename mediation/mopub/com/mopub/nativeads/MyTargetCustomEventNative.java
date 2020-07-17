package com.mopub.nativeads;

import android.content.Context;
import android.view.View;

import com.mopub.MopubCustomParamsUtils;
import com.mopub.common.DataKeys;
import com.mopub.common.logging.MoPubLog;
import com.mopub.mobileads.MyTargetAdapterConfiguration;
import com.my.target.ads.mediation.MyTargetAdapterUtils;
import com.my.target.common.CachePolicy;
import com.my.target.nativeads.NativeAd;
import com.my.target.nativeads.banners.NativePromoBanner;

import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.mopub.common.logging.MoPubLog.AdapterLogEvent.CLICKED;
import static com.mopub.common.logging.MoPubLog.AdapterLogEvent.LOAD_ATTEMPTED;
import static com.mopub.common.logging.MoPubLog.AdapterLogEvent.LOAD_FAILED;
import static com.mopub.common.logging.MoPubLog.AdapterLogEvent.LOAD_SUCCESS;
import static com.mopub.common.logging.MoPubLog.AdapterLogEvent.SHOW_SUCCESS;
import static com.mopub.nativeads.NativeErrorCode.NETWORK_NO_FILL;

public final class MyTargetCustomEventNative extends CustomEventNative
{
	private static final String ADAPTER_NAME = MyTargetCustomEventNative.class.getSimpleName();

	@Override
	protected void loadNativeAd(@NonNull Context context,
								@NonNull CustomEventNativeListener customEventNativeListener,
								@NonNull Map<String, Object> localExtras,
								@NonNull Map<String, String> extras)
	{
		String sslotId = extras.get(MyTargetAdapterConfiguration.SLOT_ID_KEY);
		int slotId = MyTargetAdapterUtils.parseSlot(sslotId);
		if (slotId < 0)
		{
			customEventNativeListener.onNativeAdFailed(NETWORK_NO_FILL);
			MoPubLog.log(sslotId, LOAD_FAILED, ADAPTER_NAME,
						 NETWORK_NO_FILL.getIntCode(), NETWORK_NO_FILL);
			return;
		}

		final String bid = extras.get(DataKeys.ADM_KEY);

		MyTargetAdapterUtils.handleConsent();

		NativeAd nativeAd = new NativeAd(slotId, context);
		MopubCustomParamsUtils.fillCustomParams(nativeAd.getCustomParams(), extras);
		MyTargetNativeAd myTargetNativeAd = new MyTargetNativeAd(nativeAd, customEventNativeListener, bid, sslotId != null ? sslotId : "");
		myTargetNativeAd.loadAd();
	}

	static final class MyTargetNativeAd extends BaseNativeAd implements NativeAd.NativeAdListener
	{

		private final @NonNull NativeAd nativeAd;
		private final @NonNull CustomEventNativeListener listener;
		private final @Nullable String bid;
		private @Nullable NativePromoBanner promoBanner;
		private @NonNull String adNetworkId = "";

		public MyTargetNativeAd(@NonNull NativeAd nativeAd,
								@NonNull CustomEventNativeListener customEventNativeListener,
								@Nullable String bid,
								@NonNull String adNetworkId)
		{
			listener = customEventNativeListener;
			this.nativeAd = nativeAd;
			this.bid = bid;
			this.adNetworkId = adNetworkId;
		}

		public void loadAd()
		{
			nativeAd.setCachePolicy(CachePolicy.NONE);
			nativeAd.setListener(this);
			if (bid == null || bid.length() == 0)
			{
				nativeAd.load();
			}
			else
			{
				nativeAd.loadFromBid(bid);
			}
			MoPubLog.log(adNetworkId, LOAD_ATTEMPTED, ADAPTER_NAME);
		}

		final public @Nullable String getAdvertisingLabel()
		{
			return promoBanner != null ? promoBanner.getAdvertisingLabel() : null;
		}

		final public @Nullable String getTitle()
		{
			return promoBanner != null ? promoBanner.getTitle() : null;
		}

		final public @Nullable String getDescription()
		{
			return promoBanner != null ? promoBanner.getDescription() : null;
		}

		final public @Nullable String getCallToAction()
		{
			return promoBanner != null ? promoBanner.getCtaText() : null;
		}

		@Override
		public void onLoad(@NonNull NativePromoBanner banner, @NonNull NativeAd ad)
		{
			if (!nativeAd.equals(ad))
			{
				listener.onNativeAdFailed(NETWORK_NO_FILL);
				MoPubLog.log(adNetworkId, LOAD_FAILED, ADAPTER_NAME,
							 NETWORK_NO_FILL.getIntCode(), NETWORK_NO_FILL);
				return;
			}

			promoBanner = banner;
			listener.onNativeAdLoaded(this);
			MoPubLog.log(adNetworkId, LOAD_SUCCESS, ADAPTER_NAME);
		}

		@Override
		public void onNoAd(@NonNull String reason, @NonNull NativeAd ad)
		{
			NativeErrorCode errorCode = NativeErrorCode.NETWORK_NO_FILL;
			MoPubLog.log(adNetworkId, LOAD_FAILED, ADAPTER_NAME, errorCode.getIntCode(),
						 errorCode);

			listener.onNativeAdFailed(errorCode);
		}

		@Override
		public void onClick(@NonNull NativeAd ad)
		{
			notifyAdClicked();
			MoPubLog.log(adNetworkId, CLICKED, ADAPTER_NAME);
		}

		@Override
		public void onShow(@NonNull NativeAd ad)
		{
			notifyAdImpressed();
			MoPubLog.log(adNetworkId, SHOW_SUCCESS, ADAPTER_NAME);
		}

		@Override
		public void onVideoPlay(@NonNull NativeAd ad)
		{
		}

		@Override
		public void onVideoPause(@NonNull NativeAd ad)
		{
		}

		@Override
		public void onVideoComplete(@NonNull NativeAd ad)
		{

		}

		@Override
		public void prepare(@NonNull View view)
		{

		}

		@Override
		public void clear(@NonNull View view)
		{
			nativeAd.unregisterView();
		}

		@Override
		public void destroy()
		{
		}

		public void registerView(@NonNull View view)
		{
			nativeAd.registerView(view);
		}
	}
}