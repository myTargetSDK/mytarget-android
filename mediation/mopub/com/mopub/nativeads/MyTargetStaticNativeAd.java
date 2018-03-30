package com.mopub.nativeads;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.my.target.nativeads.NativeAd;

public class MyTargetStaticNativeAd extends StaticNativeAd
{
	private final @NonNull NativeClickHandler nativeClickHandler;
	private final @NonNull ImpressionTracker impressionTracker;
	private @Nullable NativeAd nativeAd;

	public MyTargetStaticNativeAd(@NonNull Context context)
	{
		nativeClickHandler = new NativeClickHandler(context);
		impressionTracker = new ImpressionTracker(context);
	}

	@Override
	public void prepare(@Nullable View view)
	{
		if (view == null)
		{
			return;
		}
		super.prepare(view);
		nativeClickHandler.setOnClickListener(view, this);
		impressionTracker.addView(view, this);
		if (nativeAd != null)
		{
			nativeAd.registerView(view);
		}
	}

	@Override
	public void clear(@NonNull View view)
	{
		super.clear(view);
		nativeClickHandler.clearOnClickListener(view);
		impressionTracker.clear();
		if (nativeAd != null)
		{
			nativeAd.unregisterView();
		}
	}

	public void setNativeAd(@Nullable NativeAd promoAd)
	{
		this.nativeAd = promoAd;
	}

	@Override
	public void destroy()
	{
		super.destroy();
		if (nativeAd != null)
		{
			nativeAd.unregisterView();
		}
	}
}
