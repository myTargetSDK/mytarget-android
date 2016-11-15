package com.my.targetDemoApp.activities;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.FrameLayout;

import com.my.targetDemoApp.R;
import com.my.targetDemoApp.views.MidrollSeekbar;

public class InstreamAdController extends FrameLayout
{
	private Button mSkipButton;
	private Button mCloseInButton;
	private Context mContext;
	private ViewGroup mAnchor;
	private SkipListener skipListener;
	private boolean mShowing;
	private AdClickListener adClickListener;
	private Button mVisitAdvertizerButton;
	private MidrollSeekbar mMidrollSeekBar;
	private OnClickListener mSkipListener = new OnClickListener()
	{
		public void onClick(View v)
		{
			if (skipListener != null)
			{
				skipListener.onSkipClicked();
			}
		}
	};
	private OnClickListener mAdClickListener = new OnClickListener()
	{
		public void onClick(View v)
		{
			if (adClickListener != null)
			{
				adClickListener.onAdClicked();
			}
		}
	};
	private int mediaFileWidth;
	private int mediaFileHeight;

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{

		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		if (widthMode == MeasureSpec.UNSPECIFIED)
			widthMode = MeasureSpec.AT_MOST;

		if (heightMode == MeasureSpec.UNSPECIFIED)
			heightMode = MeasureSpec.AT_MOST;

		if (mediaFileHeight == 0 || mediaFileWidth == 0)
		{
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
			return;
		}

		float imageSideRatio = mediaFileWidth / (float) mediaFileHeight;

		float viewSideRatio = 0;
		if (heightSize != 0)
			viewSideRatio = widthSize / (float) heightSize;

		int childHeight = 0;
		int childWidth = 0;

		if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY)
		{
			childHeight = heightSize;
			childWidth = widthSize;
		} else if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST)
		{
			if (imageSideRatio < viewSideRatio)
			{
				int realWidth = Math.round(heightSize * imageSideRatio);

				if (widthSize > 0 && realWidth > widthSize)
				{
					childHeight = Math.round(widthSize / imageSideRatio);
					childWidth = widthSize;
				} else
				{
					childWidth = realWidth;
					childHeight = heightSize;
				}
			} else
			{
				int realHeight = Math.round(widthSize / imageSideRatio);

				if (heightSize > 0 && realHeight > heightSize)
				{
					childWidth = Math.round(heightSize * imageSideRatio);
					childHeight = heightSize;
				} else
				{
					childWidth = widthSize;
					childHeight = realHeight;
				}
			}
		} else if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.EXACTLY)
		{
			int realWidth = Math.round(heightSize * imageSideRatio);

			if (widthSize > 0 && realWidth > widthSize)
			{
				childHeight = Math.round(widthSize / imageSideRatio);
				childWidth = widthSize;
			} else
			{
				childWidth = realWidth;
				childHeight = heightSize;
			}
		} else if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.AT_MOST)
		{
			int realHeight = Math.round(widthSize / imageSideRatio);

			if (heightSize > 0 && realHeight > heightSize)
			{
				childWidth = Math.round(heightSize * imageSideRatio);
				childHeight = heightSize;
			} else
			{
				childWidth = widthSize;
				childHeight = realHeight;
			}
		}

		widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
		heightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	public void setAdClickListener(AdClickListener adClickListener)
	{
		this.adClickListener = adClickListener;
	}

	/**
	 * Set the view that acts as the anchor for the control view.
	 * This can for example be a VideoView, or your Activity's main view.
	 *
	 * @param view The view to which to anchor the controller when it is visible.
	 */
	public void setAnchorView(ViewGroup view)
	{
		mAnchor = view;

		LayoutParams frameParams = new LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT
		);

		removeAllViews();
		View v = makeControllerView();
		addView(v, frameParams);
	}

	public void setControlsPosition(int mediaFileWidth, int mediaFileHeight)
	{
		this.mediaFileWidth = mediaFileWidth;
		this.mediaFileHeight = mediaFileHeight;
	}

	public void setMax(int duration)
	{
		mMidrollSeekBar.setMax(duration);
	}

	public void setProgress(int prog)
	{
		mMidrollSeekBar.setProgress(prog);
	}

	public void setSkipListener(SkipListener listener)
	{
		this.skipListener = listener;
	}

	public void setupMidrolls(float[] midPoints)
	{
		mMidrollSeekBar.setupMidrolls(midPoints);
	}

	public InstreamAdController(Context context)
	{
		super(context);
		mContext = context;
	}

	public InstreamAdController(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		mContext = context;
	}

	public InstreamAdController(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
		mContext = context;
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public InstreamAdController(Context context,
	                            AttributeSet attrs,
	                            int defStyleAttr,
	                            int defStyleRes)
	{
		super(context, attrs, defStyleAttr, defStyleRes);
		mContext = context;
	}

	public void hideSeekBar()
	{
		mMidrollSeekBar.setVisibility(GONE);
	}

	public void showSeekBar()
	{
		mMidrollSeekBar.setVisibility(VISIBLE);
	}

	public void showVisit()
	{
		if (mVisitAdvertizerButton != null)
		{
			mVisitAdvertizerButton.setVisibility(VISIBLE);
		}
	}

	public void hideVisit()
	{
		if (mVisitAdvertizerButton != null)
		{
			mVisitAdvertizerButton.setVisibility(GONE);
		}
	}

	public void showClose()
	{
		mCloseInButton.setVisibility(GONE);
		mSkipButton.setVisibility(VISIBLE);
	}

	public void hideClose()
	{
		mSkipButton.setVisibility(GONE);
	}

	public void showTimeToClose(int time)
	{
		mSkipButton.setVisibility(GONE);
		mCloseInButton.setVisibility(VISIBLE);
		mCloseInButton.setText(String.format(mContext.getString(R.string.available_in), time + ""));
	}

	public void hideTimeToClose()
	{
		mCloseInButton.setVisibility(GONE);
	}

	public void show()
	{
		if (!mShowing && mAnchor != null)
		{
			LayoutParams tlp = new LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT,
					Gravity.CENTER_VERTICAL
			);

			mAnchor.addView(this, tlp);
			mShowing = true;
		}
	}

	@Override
	public void onInitializeAccessibilityEvent(AccessibilityEvent event)
	{
		super.onInitializeAccessibilityEvent(event);
		event.setClassName(InstreamAdController.class.getName());
	}

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@Override
	public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info)
	{
		super.onInitializeAccessibilityNodeInfo(info);
		info.setClassName(InstreamAdController.class.getName());
	}

	@Override
	public boolean onTrackballEvent(MotionEvent ev)
	{
		show();
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		show();
		return true;
	}

	protected View makeControllerView()
	{
		LayoutInflater inflate =
				(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View mRoot = inflate.inflate(R.layout.ad_controller, this, false);

		initControllerView(mRoot);

		return mRoot;
	}

	private void initControllerView(View v)
	{
		mSkipButton = (Button) v.findViewById(R.id.btn_instream_close);
		if (mSkipButton != null)
		{
			mSkipButton.setOnClickListener(mSkipListener);
		}

		mCloseInButton = (Button) v.findViewById(R.id.tv_can_close_in);

		mVisitAdvertizerButton = (Button) v.findViewById(R.id.btn_visit_advertizer);
		mMidrollSeekBar = (MidrollSeekbar) v.findViewById(R.id.midroll_seek_bar);
		if (mVisitAdvertizerButton != null)
		{
			mVisitAdvertizerButton.requestFocus();
			mVisitAdvertizerButton.setOnClickListener(mAdClickListener);
		}
	}

	public interface SkipListener
	{
		void onSkipClicked();
	}

	public interface AdClickListener
	{
		void onAdClicked();
	}
}

