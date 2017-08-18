package com.my.target.nativeads.mediation;

import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.ads.formats.NativeAd;
import com.my.target.nativeads.models.ImageData;

public class MyTargetAdmobNativeImage extends NativeAd.Image
{
	@Nullable
	private Drawable drawable;

	@NonNull
	private final Uri uri;

	public MyTargetAdmobNativeImage(@NonNull ImageData imageData, @NonNull Resources resources)
	{
		if (imageData.getBitmap() != null)
		{
			drawable = new BitmapDrawable(resources, imageData.getBitmap());
		}
		uri = Uri.parse(imageData.getUrl());
	}

	@Nullable
	@Override
	public Drawable getDrawable()
	{
		return drawable;
	}

	@NonNull
	@Override
	public Uri getUri()
	{
		return uri;
	}

	@Override
	public double getScale()
	{
		return 1;
	}
}
