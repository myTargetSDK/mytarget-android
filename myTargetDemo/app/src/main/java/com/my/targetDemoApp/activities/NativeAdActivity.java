package com.my.targetDemoApp.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.my.targetDemoApp.R;
import com.my.targetDemoApp.fragments.NativeAdFragment;

import java.util.ArrayList;

public class NativeAdActivity extends AdActivity
		implements BottomNavigationView.OnNavigationItemSelectedListener
{
	private static final String TAG_STATIC_FRAGMENT = "fragment_static";
	private static final String TAG_VIDEO_FRAGMENT = "fragment_video";
	private static final String TAG_SLIDER_FRAGMENT = "fragment_slider";
	private FragmentManager fragmentManager;
	private BottomNavigationView bottomNavigationView;
	private Toolbar toolbar;
	private ViewGroup topContainer;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_native_ads);

		toolbar = findViewById(R.id.toolbar);
		topContainer = findViewById(R.id.top_container);
		setSupportActionBar(toolbar);
		if (getSupportActionBar() != null)
		{
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setDisplayShowHomeEnabled(true);
		}
		bottomNavigationView = findViewById(R.id.bottom_navigation);

		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
		{
			hideBottomView();
		}

		bottomNavigationView.setOnNavigationItemSelectedListener(this);
		ArrayList<View> touchables = bottomNavigationView.getTouchables();
		for (int i = 0; i < touchables.size(); i++)
		{
			View view = touchables.get(i);
			view.setContentDescription("Bottom_" + i);
		}
		fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		NativeAdFragment nativeAdFragment =
				NativeAdFragment.newInstance(slotId, R.id.action_native_static);
		fragmentTransaction.add(R.id.fragment_container, nativeAdFragment);
		fragmentTransaction.commit();
	}

	@Override
	void reloadAd()
	{
		for (Fragment fragment : getSupportFragmentManager().getFragments())
		{
			if (fragment != null && fragment instanceof NativeAdFragment)
				((NativeAdFragment) fragment).reloadAd();
		}
	}

	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem item)
	{
		Fragment fragment;
		String tag;
		switch (item.getItemId())
		{
			case R.id.action_native_static:
			default:
				tag = TAG_STATIC_FRAGMENT;
				break;
			case R.id.action_native_video:
				tag = TAG_VIDEO_FRAGMENT;
				break;
			case R.id.action_native_slider:
				tag = TAG_SLIDER_FRAGMENT;
				break;
		}
		fragment = fragmentManager.findFragmentByTag(tag);
		if (fragment != null)
		{
			fragmentManager.beginTransaction()
					.replace(R.id.fragment_container, fragment, tag)
					.commit();
		} else
		{
			fragment = NativeAdFragment.newInstance(slotId, item.getItemId());
			fragmentManager.beginTransaction()
					.add(R.id.fragment_container, fragment, tag)
					.commit();
		}
		return true;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
		{
			hideBottomView();
		}
		else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
		{
			showBottomView();
		}
		super.onConfigurationChanged(newConfig);
	}

	private void showBottomView()
	{
		bottomNavigationView.setVisibility(View.VISIBLE);
		toolbar.setVisibility(View.VISIBLE);
	}

	private void hideBottomView()
	{
		bottomNavigationView.setVisibility(View.GONE);
		toolbar.setVisibility(View.GONE);
	}
}
