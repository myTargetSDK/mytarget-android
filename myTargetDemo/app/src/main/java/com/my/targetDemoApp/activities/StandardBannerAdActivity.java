package com.my.targetDemoApp.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.example.android.common.view.SlidingTabLayout;
import com.my.targetDemoApp.AdTypes;
import com.my.targetDemoApp.DefaultSlots;
import com.my.targetDemoApp.R;
import com.my.targetDemoApp.fragments.Standard300x250BannerFragment;
import com.my.targetDemoApp.fragments.Standard320x50BannerFragment;
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
	private SlidingTabLayout tabLayout;
	private List<AdvertisingType> typeList;
	private PagerAdapter pagerAdapter;
	@Nullable
	private Standard320x50BannerFragment fragment320x50;
	@Nullable
	private Standard300x250BannerFragment fragment300x250;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_standard);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

		setSupportActionBar(toolbar);
		if (getSupportActionBar() != null)
		{
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setDisplayShowHomeEnabled(true);
		}

		tabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
		tabLayout.setDividerColors(Color.TRANSPARENT);
		tabLayout.setSelectedIndicatorColors(Color.WHITE);

		typeList = new ArrayList<>();

		viewPager = (ViewPager) findViewById(R.id.pager);

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
		} else if (position == 1)
		{
			if (fragment300x250 != null) fragment300x250.show();
			if (fragment320x50 != null) fragment320x50.hide();
		}
	}

	@Override
	public void onPageScrollStateChanged(int state)
	{

	}

	private void initAds()
	{
		int slotId320x50 = slotId == 0 ? DefaultSlots.SLOT_STANDARD_BANNER_320x50 : slotId;
		AdvertisingType standard320x50 = new AdvertisingType(AdTypes.AD_TYPE_320X50,
				slotId320x50);
		standard320x50.setName(getResources().getString(R.string.standard_banner_320x50));

		fragment320x50 = Standard320x50BannerFragment.newInstance(slotId320x50);

		int slotId300x250 = slotId == 0 ? DefaultSlots.SLOT_STANDARD_BANNER_300x250 : slotId;
		AdvertisingType standard300x250 = new AdvertisingType(AdTypes.AD_TYPE_300X250,
				slotId300x250);
		standard300x250.setName(getResources().getString(R.string.standard_banner_300x250));

		fragment300x250 = Standard300x250BannerFragment.newInstance(slotId300x250);

		typeList.add(standard320x50);
		typeList.add(standard300x250);

		pagerAdapter = new PagerAdapter(getSupportFragmentManager(), typeList);
		viewPager.setAdapter(pagerAdapter);
		viewPager.addOnPageChangeListener(this);

		tabLayout.setViewPager(viewPager);

		fragment320x50.show();
	}

	@Override
	void reloadAd()
	{
		if (fragment300x250 != null) fragment300x250.reloadAd();
		if (fragment320x50 != null) fragment320x50.reloadAd();
	}

	@Override
	protected void onPause()
	{
		if (fragment300x250 != null) fragment300x250.pause();
		if (fragment320x50 != null) fragment320x50.pause();
		super.onPause();
	}

	@Override
	protected void onResume()
	{
		super.onResume();

		if (fragment300x250 != null) fragment300x250.resume();
		if (fragment320x50 != null) fragment320x50.resume();
	}

	@Override
	protected void onDestroy()
	{
		if (fragment300x250 != null) fragment300x250.destroy();
		if (fragment320x50 != null) fragment320x50.destroy();
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
