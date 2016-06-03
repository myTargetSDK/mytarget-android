package com.my.targetDemoApp.activities;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.FrameLayout;

import com.my.targetDemoApp.R;

public class InstreamAdController extends FrameLayout
{
	private Button mSkipButton;
	private Button mCloseInButton;
	private Context mContext;
	private ViewGroup mAnchor;
	private View mRoot;
	private SkipListener skipListener;
	private boolean mShowing;
	private AdClickListener adClickListener;
	private Button mVisitAdvertizerButton;

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
	public InstreamAdController(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
	{
		super(context, attrs, defStyleAttr, defStyleRes);
		mContext = context;
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

		FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT
		);

		removeAllViews();
		View v = makeControllerView();
		addView(v, frameParams);
	}

	/**
	 * Create the view that holds the widgets that control playback.
	 * Derived classes can override this to create their own.
	 *
	 * @return The controller view.
	 * @hide This doesn't work as advertised
	 */
	protected View makeControllerView()
	{
		LayoutInflater inflate = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mRoot = inflate.inflate(R.layout.ad_controller, this, false);

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
		if (mVisitAdvertizerButton != null)
		{
			mVisitAdvertizerButton.requestFocus();
			mVisitAdvertizerButton.setOnClickListener(mAdClickListener);
		}
	}

	private View.OnClickListener mSkipListener = new View.OnClickListener()
	{
		public void onClick(View v)
		{
			if (skipListener != null)
			{
				skipListener.onSkipClicked();
			}
		}
	};

	private View.OnClickListener mAdClickListener = new View.OnClickListener()
	{
		public void onClick(View v)
		{
			if (adClickListener != null)
			{
				adClickListener.onAdClicked();
			}
		}
	};

	public void setSkipListener(SkipListener listener)
	{
		this.skipListener = listener;
	}

	public void setControlsPosition(int mediaFileWidth, int mediaFileHeight)
	{
		WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();

		int width;
		int height;
		if (Build.VERSION.SDK_INT > 13)
		{
			display.getSize(size);
			width = size.x;
			height = size.y;
		} else
		{
			width = display.getWidth();
			height = display.getHeight();
		}

		int newHeight;
		int newWidth;
		float ratio;

		if (Math.max(mediaFileHeight, mediaFileWidth) == mediaFileHeight)
		{
			ratio = (float) height / mediaFileHeight;

			newHeight = height;

			newWidth = (int) (mediaFileWidth * ratio);
		} else
		{
			ratio = (float) width / mediaFileWidth;

			newWidth = width;

			newHeight = (int) (mediaFileHeight * ratio);
		}

		int paddingY;
		int paddingX;

		if (newHeight < height)
		{
			paddingY = (height - newHeight) / 2;
		} else
		{
			paddingY = 0;
		}

		if (newWidth < width)
		{
			paddingX = (width - newWidth) / 2;
		} else
		{
			paddingX = 0;
		}

		mRoot.setPadding(paddingX, paddingY, paddingX, paddingY);
	}

	public void setAdClickListener(AdClickListener adClickListener)
	{
		this.adClickListener = adClickListener;
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

	public interface SkipListener
	{
		void onSkipClicked();
	}

	public interface AdClickListener
	{
		void onAdClicked();
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
		mCloseInButton.setText(String.format(mContext.getString(R.string.available_in), time));
	}

	public void hideTimeToClose()
	{
		mCloseInButton.setVisibility(GONE);
	}

	public void show()
	{
		if (!mShowing && mAnchor != null)
		{
			FrameLayout.LayoutParams tlp = new FrameLayout.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT,
					Gravity.BOTTOM
			);

			mAnchor.addView(this, tlp);
			mShowing = true;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		show();
		return true;
	}

	@Override
	public boolean onTrackballEvent(MotionEvent ev)
	{
		show();
		return false;
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
}

