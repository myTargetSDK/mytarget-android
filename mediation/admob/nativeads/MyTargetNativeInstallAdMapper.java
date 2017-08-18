package com.my.target.nativeads.mediation;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.google.android.gms.ads.mediation.NativeAppInstallAdMapper;
import com.my.target.nativeads.NativeAd;
import com.my.target.nativeads.banners.NativePromoBanner;
import com.my.target.nativeads.models.ImageData;

import java.util.ArrayList;

public class MyTargetNativeInstallAdMapper extends NativeAppInstallAdMapper
{

	private final @NonNull NativeAd nativeAd;

	@Override
	public void setAdChoicesContent(View view)
	{
		super.setAdChoicesContent(view);
	}

	@Override
	public View getAdChoicesContent()
	{
		return super.getAdChoicesContent();
	}

	public MyTargetNativeInstallAdMapper(@NonNull NativeAd nativeAd, @NonNull Resources resources)
	{
		this.nativeAd = nativeAd;
		setOverrideClickHandling(true);
		setOverrideImpressionRecording(true);
		NativePromoBanner banner = nativeAd.getBanner();
		if (banner == null)
		{
			return;
		}
		setBody(banner.getDescription());
		setCallToAction(banner.getCtaText());
		setHeadline(banner.getTitle());
		ImageData icon = banner.getIcon();
		if (icon != null && !TextUtils.isEmpty(icon.getUrl()))
		{
			setIcon(new MyTargetAdmobNativeImage(icon, resources));
		}
		ImageData image = banner.getImage();
		if (image != null && !TextUtils.isEmpty(image.getUrl()))
		{
			ArrayList<com.google.android.gms.ads.formats.NativeAd.Image> imageArrayList = new ArrayList<>();
			imageArrayList.add(new MyTargetAdmobNativeImage(image, resources));
			setImages(imageArrayList);
		}
		setStarRating(banner.getRating());
		setStore(null);
		setPrice(null);
	}

	@Override
	public void trackView(View view)
	{
		nativeAd.registerView(view);
	}

	@Override
	public void untrackView(View view)
	{
		nativeAd.unregisterView();
	}

	@Override
	public void recordImpression()
	{
		nativeAd.handleShow();
	}

	@Override
	public void handleClick(View view)
	{
		nativeAd.handleClick(view);
	}
}
