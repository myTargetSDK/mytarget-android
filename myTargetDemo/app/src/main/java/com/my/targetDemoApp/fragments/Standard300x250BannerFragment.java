package com.my.targetDemoApp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
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
	private RecyclerView recyclerView;

	private MyTargetView.MyTargetViewListener adListener = new MyTargetView.MyTargetViewListener()
	{
		@Override
		public void onLoad(@NonNull MyTargetView myTargetView)
		{
			loaded = true;
			progressBar.setVisibility(View.GONE);
			myTargetView.setVisibility(View.VISIBLE);
			if (visible)
				myTargetView.start();
			adapter.notifyItemChanged(1);
		}

		@Override
		public void onNoAd(@NonNull String s, @NonNull MyTargetView myTargetView)
		{
			progressBar.setVisibility(View.GONE);
			Snackbar.make(recyclerView, getString(R.string.no_ad), Snackbar.LENGTH_LONG).show();
		}

		@Override
		public void onClick(@NonNull MyTargetView myTargetView)
		{
		}
	};

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater,
	                         @Nullable ViewGroup container,
	                         @Nullable Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.fragment_banner_300x250, container, false);

		progressBar = v.findViewById(R.id.progress_bar);
		recyclerView = v.findViewById(R.id.recycler_view);
		adapter = new ListAdapter(getActivity());
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
		recyclerView.setAdapter(adapter);
		return v;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);
		Bundle args = getArguments();
		slotId = args.getInt(SLOT_ID, DefaultSlots.SLOT_STANDARD_BANNER_300x250);
	}

	@Override
	public void reloadAd()
	{
		if (adView != null)
		{
			adView.destroy();
			adView = null;
		}
		createView();
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

	private void createView()
	{
		adView = new MyTargetView(getContext());
		adView.init(slotId, MyTargetView.AdSize.BANNER_300x250);
		Tools.fillCustomParamsUserData(adView.getCustomParams());
		adView.setListener(adListener);
		LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		adView.setLayoutParams(layoutParams);
		adView.load();
	}

	private static class Holder extends ViewHolder
	{
		Holder(View itemView)
		{
			super(itemView);
		}
	}

	public class ListAdapter extends RecyclerView.Adapter<Holder>
	{
		private Context context;

		@Override
		public long getItemId(int position)
		{
			return position;
		}

		@Override
		public int getItemCount()
		{
			return 50;
		}

		@Override
		public Holder onCreateViewHolder(ViewGroup parent, int viewType)
		{

			if (viewType == 0)
			{
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				if (inflater != null)
				{
					return new Holder(inflater.inflate(R.layout.feed_item_text, parent, false));
				}
			}
			else
			{
				createView();
				return new Holder(adView);
			}
			return null;
		}

		@Override
		public void onBindViewHolder(Holder holder, int position)
		{

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

		ListAdapter(Context context)
		{
			this.context = context;
		}
	}
}
