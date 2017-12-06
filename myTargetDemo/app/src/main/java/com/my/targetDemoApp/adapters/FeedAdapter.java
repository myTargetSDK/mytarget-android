package com.my.targetDemoApp.adapters;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.my.target.nativeads.NativeAd;
import com.my.target.nativeads.banners.NativePromoBanner;
import com.my.target.nativeads.factories.NativeViewsFactory;
import com.my.target.nativeads.views.ChatListAdView;
import com.my.target.nativeads.views.ContentStreamAdView;
import com.my.target.nativeads.views.ContentWallAdView;
import com.my.target.nativeads.views.NewsFeedAdView;
import com.my.targetDemoApp.R;
import com.my.targetDemoApp.models.AdvertisingType;
import com.my.targetDemoApp.utils.Tools;

/**
 * Created with IntelliJ IDEA.
 * User: Timur Voloshin
 * Date: 4/13/15
 * Time: 18:27
 */

public class FeedAdapter extends BaseAdapter implements NativeAd.NativeAdListener
{

	public static final String TAG = "FeedAdapter";
	private final int slotId;

	public interface Type
	{
		int CONTENT_WALL = 0;
		int CONTENT_STREAM = 1;
		int CONTENT_WALL_VIDEO = 2;
		int CONTENT_STREAM_VIDEO = 3;
		int CHAT_LIST = 4;
		int NEWS_FEED = 5;
		int NATIVE_SLIDER = 6;
	}

	private int feedType;
	private Context context;
	private NativeAd nativeAd;
	private NativePromoBanner banner;

	public FeedAdapter(AdvertisingType adType, Context context)
	{
		this.feedType = adType.getFeedType();
		this.context = context;
		this.slotId = adType.getSlotId();

		initAd();
	}

	private void initAd()
	{
		nativeAd = new NativeAd(slotId, context);
		Tools.fillCustomParamsUserData(nativeAd.getCustomParams());
		nativeAd.setListener(this);
		nativeAd.load();
	}

	@Override
	public int getCount()
	{
		if (banner == null)
			return 8;
		return 9;
	}

	@Override
	public Object getItem(int position)
	{
		if (banner != null && position == 3)
			return "ad block";

		return "text block";
	}

	@Override
	public long getItemId(int position)
	{
		return 0;
	}

	@Override
	public int getViewTypeCount()
	{
		return 2;
	}

	@Override
	public int getItemViewType(int position)
	{
		if (banner != null && position == 3)
		{
			return 1;
		}

		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			if (getItemViewType(position) == 1)
			{
				convertView = getAdView();
				nativeAd.registerView(convertView);
			}
			else
			{
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.feed_item_text, parent, false);
			}
		}
		return convertView;

	}

	public View getAdView()
	{
		View view = null;
		RelativeLayout.LayoutParams params;
		switch (feedType)
		{
			case Type.CONTENT_WALL:
			case Type.CONTENT_WALL_VIDEO:
				ContentWallAdView contentWallView =
						NativeViewsFactory.getContentWallView(nativeAd.getBanner(), context);
				contentWallView.loadImages();
				view = contentWallView;
				break;
			case Type.CONTENT_STREAM:
			case Type.CONTENT_STREAM_VIDEO:
			case Type.NATIVE_SLIDER:
				ContentStreamAdView contentStreamView = NativeViewsFactory.getContentStreamView(nativeAd.getBanner(), context);
				params = (RelativeLayout.LayoutParams) contentStreamView.getCtaButtonView().getLayoutParams();
				params.height = getPx(30);
				contentStreamView.getCtaButtonView().setLayoutParams(params);
				contentStreamView.loadImages();
				view = contentStreamView;
				break;
			case Type.CHAT_LIST:
				ChatListAdView chatListView =
						NativeViewsFactory.getChatListView(nativeAd.getBanner(), context);
				chatListView.loadImages();
				view = chatListView;
				break;
			case Type.NEWS_FEED:
				NewsFeedAdView newsFeedView =
						NativeViewsFactory.getNewsFeedView(nativeAd.getBanner(), context);
				params = (RelativeLayout.LayoutParams) newsFeedView.getCtaButtonView().getLayoutParams();
				params.height = getPx(30);
				newsFeedView.getCtaButtonView().setLayoutParams(params);
				newsFeedView.loadImages();
				view = newsFeedView;
				break;
		}
		return view;
	}

	@Override
	public void onLoad(NativeAd ad)
	{
		Log.d(TAG, "Ad loaded");
		banner = nativeAd.getBanner();
		notifyDataSetChanged();
	}

	@Override
	public void onNoAd(String s, NativeAd ad)
	{
		Toast.makeText(context, context.getString(R.string.no_ad), Toast.LENGTH_LONG).show();
		Log.d(TAG, "Ad load error: " + s);
	}

	@Override
	public void onClick(NativeAd ad)
	{

	}

	@Override
	public void onShow(NativeAd ad)
	{
		Log.d(TAG, "onShow() called with: ad = [" + ad + "]");
	}

	@Override
	public void onVideoPlay(NativeAd ad)
	{
		Log.d(TAG, "onVideoPlay() called with: ad = [" + ad + "]");
	}

	@Override
	public void onVideoPause(NativeAd ad)
	{
		Log.d(TAG, "onVideoPause() called with: ad = [" + ad + "]");
	}

	@Override
	public void onVideoComplete(NativeAd ad)
	{
		Log.d(TAG, "onVideoComplete() called with: ad = [" + ad + "]");
	}

	private int getPx(int dp)
	{
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
	}
}
