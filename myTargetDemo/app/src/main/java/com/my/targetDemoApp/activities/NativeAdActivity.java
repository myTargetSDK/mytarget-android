package com.my.targetDemoApp.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.my.targetDemoApp.R;
import com.my.targetDemoApp.fragments.NativeAdFragment;

public class NativeAdActivity extends AdActivity
		implements BottomNavigationView.OnNavigationItemSelectedListener
{
	private static final String TAG_STATIC_FRAGMENT = "fragment_static";
	private static final String TAG_VIDEO_FRAGMENT = "fragment_video";
	private static final String TAG_SLIDER_FRAGMENT = "fragment_slider";
	private FragmentManager fragmentManager;

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

		BottomNavigationView bottomNavigationView = (BottomNavigationView)
				findViewById(R.id.bottom_navigation);

		bottomNavigationView.setOnNavigationItemSelectedListener(this);

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
		String tag = null;
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
}
