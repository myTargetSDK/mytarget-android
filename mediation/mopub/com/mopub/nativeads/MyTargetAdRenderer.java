package com.mopub.nativeads;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mopub.nativeads.MyTargetCustomEventNative.MyTargetNativeAd;

import java.util.WeakHashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public final class MyTargetAdRenderer implements MoPubAdRenderer<MyTargetNativeAd>
{
	private final @NonNull MyTargetViewBinder viewBinder;

	@NonNull final WeakHashMap<View, MyTargetNativeViewHolder> viewHolderMap;

	public MyTargetAdRenderer(@NonNull MyTargetViewBinder binder)
	{
		viewBinder = binder;
		viewHolderMap = new WeakHashMap<>();
	}

	@Override
	public @NonNull View createAdView(@NonNull Context context, @Nullable ViewGroup parent)
	{
		return LayoutInflater
				.from(context)
				.inflate(viewBinder.layoutId, parent, false);
	}

	@Override
	public void renderAdView(@NonNull View view, @NonNull MyTargetNativeAd ad)
	{
		MyTargetNativeViewHolder myTargetNativeViewHolder = viewHolderMap.get(view);
		if (myTargetNativeViewHolder == null)
		{
			myTargetNativeViewHolder = MyTargetNativeViewHolder.fromViewBinder(view, viewBinder);
			viewHolderMap.put(view, myTargetNativeViewHolder);
		}

		update(myTargetNativeViewHolder, ad);
	}

	@Override
	public boolean supports(@NonNull BaseNativeAd nativeAd)
	{
		return nativeAd instanceof MyTargetNativeAd;
	}

	private void update(final @NonNull MyTargetNativeViewHolder myTargetNativeViewHolder,
						final @NonNull MyTargetNativeAd nativeAd)
	{
		NativeRendererHelper.addTextView(myTargetNativeViewHolder.getTitleView(),
										 nativeAd.getTitle());
		NativeRendererHelper.addTextView(myTargetNativeViewHolder.getTextView(), nativeAd.getDescription());
		NativeRendererHelper.addTextView(myTargetNativeViewHolder.getCallToActionView(),
										 nativeAd.getCallToAction());
		NativeRendererHelper.addTextView(myTargetNativeViewHolder.getAdvertisingLabelView(),
										 nativeAd.getAdvertisingLabel());
		View rootView = myTargetNativeViewHolder.getMainView();
		NativeRendererHelper.addCtaButton(myTargetNativeViewHolder.getCallToActionView(),
										  rootView,
										  nativeAd.getCallToAction());
		if (rootView != null)
		{
			nativeAd.registerView(rootView);
		}
	}

	static final class MyTargetNativeViewHolder
	{
		private @Nullable View mainView;
		private @Nullable TextView titleView;
		private @Nullable TextView textView;
		private @Nullable TextView callToActionView;
		private @Nullable TextView advertisingLabelView;

		// Use fromViewBinder instead of a constructor
		private MyTargetNativeViewHolder()
		{
		}

		static @NonNull MyTargetNativeViewHolder fromViewBinder(@Nullable final View view,
																@Nullable final MyTargetViewBinder myTargetViewBinder)
		{
			if (view == null || myTargetViewBinder == null)
			{
				return new MyTargetNativeViewHolder();
			}

			final MyTargetNativeViewHolder viewHolder = new MyTargetNativeViewHolder();
			viewHolder.mainView = view;
			viewHolder.titleView = view.findViewById(myTargetViewBinder.titleId);
			viewHolder.textView = view.findViewById(myTargetViewBinder.descriptionId);
			viewHolder.callToActionView =
					view.findViewById(myTargetViewBinder.callToActionId);
			viewHolder.advertisingLabelView = view.findViewById(myTargetViewBinder.advertisingLabelId);
			return viewHolder;
		}

		public @Nullable View getMainView()
		{
			return mainView;
		}

		public @Nullable TextView getTitleView()
		{
			return titleView;
		}

		public @Nullable TextView getTextView()
		{
			return textView;
		}

		public @Nullable TextView getCallToActionView()
		{
			return callToActionView;
		}

		public @Nullable TextView getAdvertisingLabelView()
		{
			return advertisingLabelView;
		}
	}

	public final static class MyTargetViewBinder
	{

		final int layoutId;
		final int titleId;
		final int descriptionId;
		final int callToActionId;
		final int advertisingLabelId;

		private MyTargetViewBinder(@NonNull final Builder builder)
		{
			this.layoutId = builder.layoutId;
			this.titleId = builder.titleId;
			this.descriptionId = builder.descriptionId;
			this.callToActionId = builder.callToActionId;
			this.advertisingLabelId = builder.advertisingLabelId;
		}

		public static class Builder
		{

			private final int layoutId;
			private int titleId;
			private int descriptionId;
			private int callToActionId;
			private int advertisingLabelId;

			public Builder(final int layoutId)
			{
				this.layoutId = layoutId;
			}

			@NonNull
			public final Builder titleId(final int titleId)
			{
				this.titleId = titleId;
				return this;
			}

			@NonNull
			public final Builder descriptionId(final int descriptionId)
			{
				this.descriptionId = descriptionId;
				return this;
			}

			@NonNull
			public final Builder callToActionId(final int callToActionId)
			{
				this.callToActionId = callToActionId;
				return this;
			}

			@NonNull
			public Builder advertisingLabelId(final int advertisingLabelId)
			{
				this.advertisingLabelId = advertisingLabelId;
				return this;
			}

			@NonNull
			public MyTargetViewBinder build()
			{
				return new MyTargetViewBinder(this);
			}
		}
	}
}
