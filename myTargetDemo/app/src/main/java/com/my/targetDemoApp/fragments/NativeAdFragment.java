package com.my.targetDemoApp.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.common.view.SlidingTabLayout;
import com.my.targetDemoApp.AdTypes;
import com.my.targetDemoApp.DefaultSlots;
import com.my.targetDemoApp.R;
import com.my.targetDemoApp.adapters.FeedAdapter;
import com.my.targetDemoApp.models.AdvertisingType;

import java.util.ArrayList;
import java.util.List;

public class NativeAdFragment extends Fragment
{
	private static final String ARG_SLOT_ID = "slotId";
	private static final String ARG_TYPE = "fragment_type";

	public static NativeAdFragment newInstance(int slotId, int type)
	{
		NativeAdFragment fragment = new NativeAdFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SLOT_ID, slotId);
		args.putInt(ARG_TYPE, type);
		fragment.setArguments(args);
		return fragment;
	}

	NativeAdFragment.PagerAdapter pagerAdapter;
	ViewPager viewPager;
	private List<AdvertisingType> typeList;
	private SlidingTabLayout tabLayout;
	private int slotId;
	private int fragmentType;

	public NativeAdFragment()
	{
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.fragment_native_ad, container, false);

		if (getArguments() != null)
		{
			slotId = getArguments().getInt(ARG_SLOT_ID);
			fragmentType = getArguments().getInt(ARG_TYPE);
		}

		tabLayout = (SlidingTabLayout) v.findViewById(R.id.sliding_tabs);
		tabLayout.setDividerColors(Color.TRANSPARENT);
		tabLayout.setSelectedIndicatorColors(Color.WHITE);
		typeList = new ArrayList<>();

		viewPager = (ViewPager) v.findViewById(R.id.pager);

		initAds();

		return v;
	}

	public void reloadAd()
	{
		typeList.clear();
		pagerAdapter.notifyDataSetChanged();
		initAds();
	}

	private void initAds()
	{
		switch (fragmentType)
		{
			case R.id.action_native_static:
			default:
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

				typeList.add(contentStream);
				typeList.add(newsFeed);
				typeList.add(chatList);
				typeList.add(contentWall);
				break;
			case R.id.action_native_video:
				slot = slotId == 0 ? DefaultSlots.NATIVE_VIDEO : slotId;

				AdvertisingType contentStreamVideo =
						new AdvertisingType(AdTypes.AD_TYPE_NATIVE, slot);
				contentStreamVideo.setName(getResources().getString(R.string
						.content_stream_video));
				contentStreamVideo.setType(FeedAdapter.Type.CONTENT_STREAM_VIDEO);

				AdvertisingType contentWallVideo =
						new AdvertisingType(AdTypes.AD_TYPE_NATIVE, slot);
				contentWallVideo.setName(getResources().getString(R.string.content_wall_video));
				contentWallVideo.setType(FeedAdapter.Type.CONTENT_WALL_VIDEO);

				typeList.add(contentStreamVideo);
				typeList.add(contentWallVideo);
				break;
			case R.id.action_native_slider:
				slot = slotId == 0 ? DefaultSlots.NATIVE_SLIDER : slotId;

				AdvertisingType nativeSlider = new AdvertisingType(AdTypes.AD_TYPE_NATIVE, slot);
				nativeSlider.setName(getResources().getString(R.string.native_slider));
				nativeSlider.setType(FeedAdapter.Type.NATIVE_SLIDER);
				typeList.add(nativeSlider);
				break;
		}

		pagerAdapter = new PagerAdapter(getChildFragmentManager(), typeList);
		viewPager.setAdapter(pagerAdapter);

		tabLayout.setViewPager(viewPager);
	}

	private static class PagerAdapter extends FragmentStatePagerAdapter
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
			Fragment fragment = new FeedFragment();
			Bundle args = new Bundle();
			args.putParcelable(FeedFragment.ARG_KEY, advertisingTypes.get(i));
			fragment.setArguments(args);
			return fragment;
		}

		public PagerAdapter(FragmentManager fm, List<AdvertisingType> advertisingTypes)
		{
			super(fm);
			this.advertisingTypes = advertisingTypes;
		}
	}
}
