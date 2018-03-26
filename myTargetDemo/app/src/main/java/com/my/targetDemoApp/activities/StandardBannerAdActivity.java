package com.my.targetDemoApp.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.my.targetDemoApp.AdTypes;
import com.my.targetDemoApp.DefaultSlots;
import com.my.targetDemoApp.R;
import com.my.targetDemoApp.fragments.Standard300x250BannerFragment;
import com.my.targetDemoApp.fragments.Standard320x50BannerFragment;
import com.my.targetDemoApp.fragments.Standard728x90BannerFragment;
import com.my.targetDemoApp.models.AdvertisingType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Timur Voloshin
 * Date: 4/13/15
 * Time: 12:52
 */

public class StandardBannerAdActivity extends AdActivity implements ViewPager.OnPageChangeListener
{
	ViewPager viewPager;
	private TabLayout tabLayout;
	private List<AdvertisingType> typeList;
	@Nullable
	private Standard320x50BannerFragment fragment320x50;
	@Nullable
	private Standard300x250BannerFragment fragment300x250;
	@Nullable
	private Standard728x90BannerFragment fragment728x90;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_standard);
		Toolbar toolbar = findViewById(R.id.toolbar);

		setSupportActionBar(toolbar);
		if (getSupportActionBar() != null)
		{
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setDisplayShowHomeEnabled(true);
		}

		tabLayout = findViewById(R.id.sliding_tabs);
//		tabLayout.setDividerColors(Color.TRANSPARENT);
		tabLayout.setSelectedTabIndicatorColor(Color.WHITE);

		typeList = new ArrayList<>();

		viewPager = findViewById(R.id.pager);

		initAds();
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
	{

	}

	@Override
	public void onPageSelected(int position)
	{
		if (position == 0)
		{
			if (fragment320x50 != null) fragment320x50.show();
			if (fragment300x250 != null) fragment300x250.hide();
			if (fragment728x90 != null) fragment728x90.hide();
		} else if (position == 1)
		{
			if (fragment300x250 != null) fragment300x250.show();
			if (fragment320x50 != null) fragment320x50.hide();
			if (fragment728x90 != null) fragment728x90.hide();
		} else if (position == 2)
		{
			if (fragment300x250 != null) fragment300x250.hide();
			if (fragment320x50 != null) fragment320x50.hide();
			if (fragment728x90 != null) fragment728x90.show();
		}
	}

	@Override
	public void onPageScrollStateChanged(int state)
	{

	}

	private void initAds()
	{
		if (slotId == 0 || adType == AdTypes.AD_TYPE_320X50)
		{
			int slotId320x50 = slotId == 0 ? DefaultSlots.SLOT_STANDARD_BANNER_320x50 : slotId;
			AdvertisingType standard320x50 = new AdvertisingType(AdTypes.AD_TYPE_320X50,
					slotId320x50);
			standard320x50.setName(getResources().getString(R.string.standard_banner_320x50));

			fragment320x50 = Standard320x50BannerFragment.newInstance(slotId320x50);

			typeList.add(standard320x50);

			if (fragment320x50 != null)
			{
				fragment320x50.show();
			}
		}

		if (slotId == 0 || adType == AdTypes.AD_TYPE_300X250)
		{
			int slotId300x250 = slotId == 0 ? DefaultSlots.SLOT_STANDARD_BANNER_300x250 : slotId;
			AdvertisingType standard300x250 = new AdvertisingType(AdTypes.AD_TYPE_300X250,
					slotId300x250);
			standard300x250.setName(getResources().getString(R.string.standard_banner_300x250));

			fragment300x250 = Standard300x250BannerFragment.newInstance(slotId300x250);

			typeList.add(standard300x250);
			if (fragment300x250 != null)
			{
				fragment300x250.show();
			}
		}

		if (slotId == 0 || adType == AdTypes.AD_TYPE_728X90)
		{
			int slotId728x90 = slotId == 0 ? DefaultSlots.SLOT_STANDARD_BANNER_728x90 : slotId;
			AdvertisingType standard728x90 = new AdvertisingType(AdTypes.AD_TYPE_728X90, slotId728x90);
			standard728x90.setName(getResources().getString(R.string.standard_banner_728x90));

			fragment728x90 = Standard728x90BannerFragment.newInstance(slotId728x90);

			typeList.add(standard728x90);
			if (fragment728x90 != null)
			{
				fragment728x90.show();
			}
		}

		PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), typeList);
		viewPager.setAdapter(pagerAdapter);
		viewPager.addOnPageChangeListener(this);

		tabLayout.setupWithViewPager(viewPager);
	}

	@Override
	void reloadAd()
	{
		if (fragment300x250 != null) fragment300x250.reloadAd();
		if (fragment320x50 != null) fragment320x50.reloadAd();
		if (fragment728x90 != null) fragment728x90.reloadAd();
	}

	@Override
	protected void onPause()
	{
		if (fragment300x250 != null) fragment300x250.pause();
		if (fragment320x50 != null) fragment320x50.pause();
		if (fragment728x90 != null) fragment728x90.pause();
		super.onPause();
	}

	@Override
	protected void onResume()
	{
		super.onResume();

		if (fragment300x250 != null) fragment300x250.resume();
		if (fragment320x50 != null) fragment320x50.resume();
		if (fragment728x90 != null) fragment728x90.resume();
	}

	@Override
	protected void onDestroy()
	{
		if (fragment300x250 != null) fragment300x250.destroy();
		if (fragment320x50 != null) fragment320x50.destroy();
		if (fragment728x90 != null) fragment728x90.destroy();
		super.onDestroy();
	}

	private class PagerAdapter extends FragmentStatePagerAdapter
	{
		private List<AdvertisingType> advertisingTypes;

		@Override
		public int getCount()
		{
			return advertisingTypes.size();
		}

		@Override
		public CharSequence getPageTitle(int position)
		{
			return advertisingTypes.get(position).getName();
		}

		@Override
		public android.support.v4.app.Fragment getItem(int i)
		{
			if (advertisingTypes.get(i).getAdType() == AdTypes.AD_TYPE_300X250)
			{
				return fragment300x250;
			} else if (advertisingTypes.get(i).getAdType() == AdTypes.AD_TYPE_728X90)
			{
				return fragment728x90;
			} else
			{
				return fragment320x50;
			}
		}

		public PagerAdapter(FragmentManager fm, List<AdvertisingType> advertisingTypes)
		{
			super(fm);
			this.advertisingTypes = advertisingTypes;
		}
	}
}
