package com.my.targetDemoApp.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.my.target.ads.InterstitialAd;
import com.my.targetDemoApp.AdTypes;
import com.my.targetDemoApp.DefaultSlots;
import com.my.targetDemoApp.R;
import com.my.targetDemoApp.models.AdvertisingType;
import com.my.targetDemoApp.utils.MaterialColors;

import java.util.ArrayList;
import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: Timur Voloshin
 * Date: 4/13/15
 * Time: 15:24
 */

public class InterstitialAdActivity extends AdActivity implements View.OnClickListener
{
	private static final int PROMO_AD = 0;
	private static final int VIDEO_AD = 1;
	private static final int IMAGE_AD = 2;
	private static final int VIDEO_AD_STYLE = 3;

	private RecyclerView recyclerView;
	private CheckBox checkBox;
	private ArrayList<InterstitialAd> interstitialAds = new ArrayList<>();
	private InterstitialListAdapter adapter;
	private ArrayList<AdvertisingType> typeList;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_interstitial_ads);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		if (getSupportActionBar() != null)
		{
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setDisplayShowHomeEnabled(true);
		}

		recyclerView = (RecyclerView) findViewById(R.id.interstitial_grid);

		checkBox = (CheckBox) findViewById(R.id.interstitial_checkbox);

		initAds();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case android.R.id.home:
				if (Build.VERSION.SDK_INT >= 21) finishAfterTransition();
				else finish();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	void reloadAd()
	{
		for (InterstitialAd interstitialAd : interstitialAds)
		{
			interstitialAd.setListener(null);
		}
		interstitialAds.clear();
		typeList.clear();
		adapter.notifyDataSetChanged();
		initAds();
	}

	private void initAds()
	{
		typeList = new ArrayList<>();

		if (slotId != 0)
		{
			AdvertisingType fullscreenAd = new AdvertisingType(AdTypes.AD_TYPE_NATIVE, slotId);
			fullscreenAd.setName(getResources().getString(R.string.interstitial_promo));
			fullscreenAd.setImageResource(R.drawable.img_fullscreen_promo);
			fullscreenAd.setDescription(getString(R.string.interstitial_promo_desc));

			interstitialAds.add(new InterstitialAd(slotId, this));
			typeList.add(fullscreenAd);
		} else
		{
			AdvertisingType promoAd = new AdvertisingType(AdTypes.AD_TYPE_NATIVE, DefaultSlots.SLOT_PROMO_AD);
			promoAd.setName(getResources().getString(R.string.interstitial_promo));
			promoAd.setImageResource(R.drawable.img_fullscreen_promo);
			promoAd.setDescription(getString(R.string.interstitial_promo_desc));
			interstitialAds.add(PROMO_AD, new InterstitialAd(DefaultSlots.SLOT_PROMO_AD, this));

			AdvertisingType videoAd = new AdvertisingType(AdTypes.AD_TYPE_NATIVE, DefaultSlots.SLOT_PROMO_VIDEO_AD);
			videoAd.setName(getResources().getString(R.string.interstitial_video));
			videoAd.setImageResource(R.drawable.img_fullscreen_video);
			videoAd.setDescription(getString(R.string.interstitial_video_desc));
			interstitialAds.add(VIDEO_AD, new InterstitialAd(DefaultSlots.SLOT_PROMO_VIDEO_AD, this));

			AdvertisingType imageAd = new AdvertisingType(AdTypes.AD_TYPE_NATIVE, DefaultSlots.SLOT_IMAGE_AD);
			imageAd.setName(getResources().getString(R.string.interstitial_image));
			imageAd.setImageResource(R.drawable.img_fullscreen_image);
			imageAd.setDescription(getString(R.string.interstitial_image_desc));
			interstitialAds.add(IMAGE_AD, new InterstitialAd(DefaultSlots.SLOT_IMAGE_AD, this));

			AdvertisingType videoStyleAd = new AdvertisingType(AdTypes.AD_TYPE_NATIVE,
					DefaultSlots.SLOT_PROMO_VIDEO_STYLE_AD);
			videoStyleAd.setName(getResources().getString(R.string.interstitial_video_style));
			videoStyleAd.setImageResource(R.drawable.img_fullscreen_promo_style);
			videoStyleAd.setDescription(getString(R.string.interstitial_video_style_desc));
			interstitialAds.add(VIDEO_AD_STYLE,
					new InterstitialAd(DefaultSlots.SLOT_PROMO_VIDEO_STYLE_AD, this));

			typeList.add(PROMO_AD, promoAd);
			typeList.add(VIDEO_AD, videoAd);
			typeList.add(IMAGE_AD, imageAd);
			typeList.add(VIDEO_AD_STYLE, videoStyleAd);
		}

		adapter = new InterstitialListAdapter(typeList);

		GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),
				getResources().getInteger(R.integer.grid_coloumns));
		recyclerView.setLayoutManager(gridLayoutManager);
		recyclerView.setAdapter(adapter);

		for (InterstitialAd interstitialAd : interstitialAds)
		{
			interstitialAd.setListener(adListener);
			interstitialAd.load();
		}
	}

	private InterstitialAd.InterstitialAdListener adListener = new InterstitialAd.InterstitialAdListener()
	{
		@Override
		public void onLoad(InterstitialAd interstitialAd)
		{
			Log.d("InterstitialAdActivity", "Ad loaded");
			InterstitialListAdapter adapter = (InterstitialListAdapter) recyclerView.getAdapter();

			adapter.setReady(interstitialAds.indexOf(interstitialAd));
		}

		@Override
		public void onNoAd(String s, InterstitialAd interstitialAd)
		{
			Log.d("InterstitialAdActivity", "Ad load error: " + s);
			Toast.makeText(InterstitialAdActivity.this, getString(R.string.no_ad), Toast.LENGTH_LONG).show();

		}

		@Override
		public void onClick(InterstitialAd interstitialAd)
		{

		}

		@Override
		public void onDismiss(InterstitialAd interstitialAd)
		{

		}

		@Override
		public void onVideoCompleted(InterstitialAd interstitialAd)
		{

		}

		@Override
		public void onDisplay(InterstitialAd interstitialAd)
		{

		}
	};

	@Override
	public void onClick(View v)
	{

	}

	private class InterstitialListAdapter
			extends RecyclerView.Adapter<InterstitialListAdapter.ViewHolder>
	{
		private final MaterialColors materialColors;
		private List<AdvertisingType> advertisingTypes;

		private InterstitialListAdapter(List<AdvertisingType> advertisingTypes)
		{
			this.advertisingTypes = advertisingTypes;
			materialColors = new MaterialColors();
		}

		@Override
		public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
		{
			View v = LayoutInflater.from(parent.getContext()).inflate(
					R.layout.interstitial_type_card, parent, false);
			return new ViewHolder(v);
		}

		@Override
		public void onBindViewHolder(ViewHolder holder, int position)
		{
			AdvertisingType type = advertisingTypes.get(position);

			holder.nameLabel.setText(type.getName());
			holder.imageView.setImageResource(type.getImageResource());
			holder.descriptionLabel.setText(type.getDescription());
			holder.cardFrame.setBackgroundColor(materialColors.get(position));

			if (type.isReady()) holder.progressBar.setVisibility(View.GONE);

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			{
				if (type.isReady())
				{
					holder.imageView.setAlpha(1.0f);
					holder.nameLabel.setAlpha(1.0f);
				} else
				{
					holder.imageView.setAlpha(0.5f);
					holder.nameLabel.setAlpha(0.5f);
				}
			}
		}

		@Override
		public long getItemId(int position)
		{
			return position;
		}

		@Override
		public int getItemCount()
		{
			return advertisingTypes.size();
		}

		public void setReady(int position)
		{
			if (position < advertisingTypes.size())
			{
				advertisingTypes.get(position).setReady(true);
				notifyDataSetChanged();
			}
		}

		class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
		{
			ImageView imageView;
			TextView nameLabel;
			TextView descriptionLabel;
			ProgressBar progressBar;
			FrameLayout cardFrame;


			public ViewHolder(View itemView)
			{
				super(itemView);

				imageView = (ImageView) itemView.findViewById(R.id.iv_card_image);
				nameLabel = (TextView) itemView.findViewById(R.id.tv_card_title);
				descriptionLabel = (TextView) itemView.findViewById(R.id.tv_card_description);
				progressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar);
				cardFrame = (FrameLayout) itemView.findViewById(R.id.card_frame);

				itemView.setOnClickListener(this);
			}

			@Override
			public void onClick(View v)
			{
				if (advertisingTypes.get(getLayoutPosition()).isReady())
				{
					if (checkBox.isChecked()) interstitialAds.get(getLayoutPosition()).showDialog();
					else interstitialAds.get(getLayoutPosition()).show();
				}
			}
		}
	}
}