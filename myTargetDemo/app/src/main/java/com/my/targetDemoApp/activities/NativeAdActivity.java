package com.my.targetDemoApp.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.example.android.common.view.SlidingTabLayout;
import com.my.targetDemoApp.AdTypes;
import com.my.targetDemoApp.DefaultSlots;
import com.my.targetDemoApp.R;
import com.my.targetDemoApp.adapters.FeedAdapter;
import com.my.targetDemoApp.fragments.FeedFragment;
import com.my.targetDemoApp.models.AdvertisingType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Timur Voloshin
 * Date: 4/10/15
 * Time: 19:42
 */

public class NativeAdActivity extends AdActivity
{
	/**Static members**/

	/**Static getters and setters**/

	/**Static methods**/

	/**
	 * Members*
	 */
	PagerAdapter pagerAdapter;
	ViewPager viewPager;
	private List<AdvertisingType> typeList;
	private SlidingTabLayout tabLayout;

	/**Getters and setters**/

	/**
	 * Constructor*
	 */

	/**
	 * Methods*
	 */
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_native_ads);

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

		initAds();
	}

	private void initAds()
	{
		int slot = slotId == 0 ? DefaultSlots.CONTENT_STREAM : slotId;

		AdvertisingType contentStream = new AdvertisingType(AdTypes.AD_TYPE_NATIVE, slot);
		contentStream.setName(getResources().getString(R.string.content_stream));
		contentStream.setType(FeedAdapter.Type.CONTENT_STREAM);

		AdvertisingType newsFeed = new AdvertisingType(AdTypes.AD_TYPE_NATIVE, slot);
		newsFeed.setName(getResources().getString(R.string.news_feed));
		newsFeed.setType(FeedAdapter.Type.NEWS_FEED);

		AdvertisingType chatList = new AdvertisingType(AdTypes.AD_TYPE_NATIVE, slot);
		chatList.setName(getResources().getString(R.string.chat_list));
		chatList.setType(FeedAdapter.Type.CHAT_LIST);

		AdvertisingType contentWall = new AdvertisingType(AdTypes.AD_TYPE_NATIVE, slot);
		contentWall.setName(getResources().getString(R.string.content_wall));
		contentWall.setType(FeedAdapter.Type.CONTENT_WALL);

		slot = slotId == 0 ? DefaultSlots.NATIVE_VIDEO : slotId;

		AdvertisingType contentStreamVideo = new AdvertisingType(AdTypes.AD_TYPE_NATIVE, slot);
		contentStreamVideo.setName(getResources().getString(R.string.content_stream_video));
		contentStreamVideo.setType(FeedAdapter.Type.CONTENT_STREAM_VIDEO);

		AdvertisingType contentWallVideo = new AdvertisingType(AdTypes.AD_TYPE_NATIVE, slot);
		contentWallVideo.setName(getResources().getString(R.string.content_wall_video));
		contentWallVideo.setType(FeedAdapter.Type.CONTENT_WALL_VIDEO);

		typeList.add(contentStream);
		typeList.add(newsFeed);
		typeList.add(chatList);
		typeList.add(contentWall);
		typeList.add(contentStreamVideo);
		typeList.add(contentWallVideo);

		pagerAdapter = new PagerAdapter(getSupportFragmentManager(), typeList);
		viewPager = (ViewPager) findViewById(R.id.pager);
		viewPager.setAdapter(pagerAdapter);

		tabLayout.setViewPager(viewPager);
	}

	@Override
	void reloadAd()
	{
		typeList.clear();
		pagerAdapter.notifyDataSetChanged();
		initAds();
	}

	private static class PagerAdapter extends FragmentStatePagerAdapter
	{
		private List<AdvertisingType> advertisingTypes;

		public PagerAdapter(FragmentManager fm, List<AdvertisingType> advertisingTypes)
		{
			super(fm);
			this.advertisingTypes = advertisingTypes;
		}

		@Override
		public android.support.v4.app.Fragment getItem(int i)
		{
			Fragment fragment = new FeedFragment();
			Bundle args = new Bundle();
			args.putParcelable(FeedFragment.ARG_KEY, advertisingTypes.get(i));
			fragment.setArguments(args);
			return fragment;
		}

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
	}
}
