package com.my.targetDemoApp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.my.target.ads.MyTargetView;
import com.my.targetDemoApp.DefaultSlots;
import com.my.targetDemoApp.R;
import com.my.targetDemoApp.utils.Tools;

public class Standard300x250BannerFragment extends Fragment implements StandardAdFragment
{
	private static final String SLOT_ID = "slot_id";
	private boolean visible;
	private boolean loaded;

	public static Standard300x250BannerFragment newInstance(int slotId)
	{
		Standard300x250BannerFragment fragment = new Standard300x250BannerFragment();
		Bundle args = new Bundle();
		args.putInt(SLOT_ID, slotId);
		fragment.setArguments(args);
		return fragment;
	}

	private ProgressBar progressBar;
	@Nullable
	private MyTargetView adView;
	private int slotId;
	private ListAdapter adapter;
	private MyTargetView.MyTargetViewListener adListener = new MyTargetView.MyTargetViewListener()
	{
		@Override
		public void onLoad(MyTargetView myTargetView)
		{
			loaded = true;
			progressBar.setVisibility(View.GONE);
			myTargetView.setVisibility(View.VISIBLE);
			if (visible)
				myTargetView.start();
			adapter.notifyDataSetChanged();
		}

		@Override
		public void onNoAd(String s, MyTargetView myTargetView)
		{
			progressBar.setVisibility(View.GONE);
			Snackbar.make(listView, getString(R.string.no_ad), Snackbar.LENGTH_LONG).show();
		}

		@Override
		public void onClick(MyTargetView myTargetView)
		{

		}
	};
	private ListView listView;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater,
	                         @Nullable ViewGroup container,
	                         @Nullable Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.fragment_banner_300x250, container, false);

		progressBar = (ProgressBar) v.findViewById(R.id.progress_bar);
		listView = (ListView) v.findViewById(R.id.list_view);
		adapter = new ListAdapter(getActivity());
		listView.setAdapter(adapter);
		return v;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);
		Bundle args = getArguments();
		slotId = args.getInt(SLOT_ID, DefaultSlots.SLOT_STANDARD_BANNER_300x250);
		initAd();
	}

	@Override
	public void reloadAd()
	{
		adView = null;
		listView.setAdapter(new ListAdapter(getContext()));
	}

	@Override
	public void pause()
	{
		if (adView != null) adView.pause();
	}

	@Override
	public void resume()
	{
		if (adView != null) adView.resume();
	}

	@Override
	public void destroy()
	{
		if (adView != null) adView.destroy();
		loaded = false;
	}

	@Override
	public void show()
	{
		visible = true;
		if (loaded && adView != null)
		{
			adView.start();
			adView.resume();
		}
	}

	@Override
	public void hide()
	{
		visible = false;
		pause();
	}

	private void initAd()
	{
		final Context context = getActivity();
		if (context == null)
		{
			return;
		}
		adView = new MyTargetView(context);
		adView.init(slotId, MyTargetView.AdSize.BANNER_300x250);
		Tools.fillCustomParamsUserData(adView.getCustomParams());
		adView.setListener(adListener);
		adView.load();
	}

	public class ListAdapter extends BaseAdapter
	{
		private Context context;

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
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			if (getItemViewType(position) == 0)
			{
				if (convertView == null)
				{
					LayoutInflater inflater =
							(LayoutInflater) context.getSystemService(Context
									.LAYOUT_INFLATER_SERVICE);
					convertView = inflater.inflate(R.layout.feed_item_text, parent, false);
				}
				return convertView;
			} else
			{
				if (adView == null)
				{
					initAd();
				}
				return adView;
			}
		}

		@Override
		public int getItemViewType(int position)
		{
			if (position == 1)
			{
				return 1;
			}
			return 0;
		}

		@Override
		public int getViewTypeCount()
		{
			return 2;
		}

		public ListAdapter(Context context)
		{
			this.context = context;
		}
	}
}
