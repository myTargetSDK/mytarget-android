package com.my.targetDemoApp.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.my.target.ads.MyTargetView;
import com.my.targetDemoApp.R;
import com.my.targetDemoApp.utils.Tools;

/**
 * Created with IntelliJ IDEA.
 * User: Timur Voloshin
 * Date: 4/13/15
 * Time: 12:52
 */

public class BannerAdActivity extends AdActivity
{
	private ViewGroup layout;
	private ProgressBar progressBar;
	private MyTargetView adView;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_banner);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		if (getSupportActionBar() != null)
		{
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setDisplayShowHomeEnabled(true);
		}

		layout = (RelativeLayout) findViewById(R.id.content_layout);
		progressBar = (ProgressBar) findViewById(R.id.progress_bar);
		ListView listView=(ListView) findViewById(R.id.list_view);
		listView.setAdapter(new ListAdapter(this));

		initAd();
	}

	@Override
	void reloadAd()
	{
		if (adView != null && adView.getParent() != null)
		{
			((ViewGroup) adView.getParent()).removeView(adView);
			adView.destroy();
			adView = null;
			initAd();
		}
	}

	private void initAd()
	{
		adView = new MyTargetView(this);
		Tools.fillCustomParamsUserData(adView.getCustomParams());
		adView.init(slotId);
		adView.setListener(adListener);

		float density = getResources().getDisplayMetrics().density;
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, (int) (50 * density));
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		adView.setLayoutParams(params);

		layout.addView(adView);
		adView.load();
	}

	private MyTargetView.MyTargetViewListener adListener = new MyTargetView.MyTargetViewListener()
	{
		@Override
		public void onLoad(MyTargetView myTargetView)
		{
			progressBar.setVisibility(View.GONE);
			adView.start();
			adView.setVisibility(View.VISIBLE);
		}

		@Override
		public void onNoAd(String s, MyTargetView myTargetView)
		{
			progressBar.setVisibility(View.GONE);
			Toast.makeText(BannerAdActivity.this, getString(R.string.no_ad), Toast.LENGTH_LONG).show();
		}

		@Override
		public void onClick(MyTargetView myTargetView)
		{

		}
	};

	@Override
	protected void onPause()
	{
		if (adView != null) adView.pause();
		super.onPause();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		if (adView != null) adView.resume();
	}

	@Override
	protected void onDestroy()
	{
		if (adView != null) adView.destroy();
		super.onDestroy();
	}

	private static class ListAdapter extends BaseAdapter
	{
		private Context context;

		public ListAdapter(Context context)
		{
			this.context = context;
		}

		@Override
		public int getCount()
		{
			return 50;
		}

		@Override
		public Object getItem(int position)
		{
			return "Object " + position;
		}

		@Override
		public long getItemId(int position)
		{
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			if (convertView == null)
			{
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.feed_item_text, parent, false);
			}
			return convertView;
		}
	}
}
