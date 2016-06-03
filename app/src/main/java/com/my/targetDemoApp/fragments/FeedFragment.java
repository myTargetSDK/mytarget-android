package com.my.targetDemoApp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.my.targetDemoApp.R;
import com.my.targetDemoApp.adapters.FeedAdapter;
import com.my.targetDemoApp.models.AdvertisingType;

/**
 * Created with IntelliJ IDEA.
 * User: Timur Voloshin
 * Date: 4/13/15
 * Time: 18:25
 */

public class FeedFragment extends Fragment
{
	public static String ARG_KEY = "advertisingType";

	private ListView listView;
	private FeedAdapter feedAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.feed_fragment, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);

		Bundle args = getArguments();
		AdvertisingType advertisingType = args.getParcelable(ARG_KEY);
		listView = (ListView) view.findViewById(R.id.list_view);

		if (advertisingType != null)
		{
			feedAdapter = new FeedAdapter(advertisingType, view.getContext());
			listView.setAdapter(feedAdapter);
		}

	}
}
