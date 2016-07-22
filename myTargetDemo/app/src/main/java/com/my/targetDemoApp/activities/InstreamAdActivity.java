package com.my.targetDemoApp.activities;

import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.my.target.ads.MyTargetVideoView;
import com.my.targetDemoApp.R;


public class InstreamAdActivity extends AdActivity implements
                                                   MyTargetVideoView.MyTargetVideoViewListener,
                                                   InstreamAdController.SkipListener,
                                                   InstreamAdController.AdClickListener
{
	public static final String LINK_TO_MAIN_VIDEO = "http://r.mradx.net/img/8D/548043.mp4";

	private MyTargetVideoView myTargetVideoView;
	private ProgressBar progressBar;
	private VideoView mainVideoView;
	private MyTargetVideoView.BannerInfo bannerInfo;
	private InstreamAdController instreamAdController;
	private RelativeLayout videoContainer;
	private Toolbar toolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_instream_ad);

		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		if (getSupportActionBar() != null)
		{
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setDisplayShowHomeEnabled(true);
		}

		videoContainer = (RelativeLayout) findViewById(R.id.rl_container);

		progressBar = (ProgressBar) findViewById(R.id.pb_instream);
		mainVideoView = (VideoView) findViewById(R.id.vv_kino);
		mainVideoView.setOnPreparedListener(startAfterPreparedListener);
		mainVideoView.setOnCompletionListener(completeonListener);
		mainVideoView.setOnErrorListener(onErrorListener);

		initAds();

	}

	@Override
	void reloadAd()
	{
		if (myTargetVideoView != null && myTargetVideoView.getParent() != null)
		{
			((ViewGroup) myTargetVideoView.getParent()).removeView(myTargetVideoView);
			myTargetVideoView.setListener(null);
			myTargetVideoView.destroy();
			myTargetVideoView = null;
			initAds();
		}
	}

	private MediaPlayer.OnPreparedListener startAfterPreparedListener =
			new MediaPlayer.OnPreparedListener()
			{
				@Override
				public void onPrepared(MediaPlayer mp)
				{
					mainVideoView.start();
				}
			};

	private MediaPlayer.OnCompletionListener completeonListener = new MediaPlayer.OnCompletionListener()
	{
		@Override
		public void onCompletion(MediaPlayer mp)
		{
			mainVideoView.setVisibility(View.GONE);
			myTargetVideoView.startPostroll();
		}
	};

	private MediaPlayer.OnErrorListener onErrorListener = new MediaPlayer.OnErrorListener()
	{
		@Override
		public boolean onError(MediaPlayer mp, int what, int extra)
		{
			return true;
		}
	};

	private void initAds()
	{
		myTargetVideoView = new MyTargetVideoView(getApplicationContext());
		myTargetVideoView.setListener(this);
		videoContainer.addView(myTargetVideoView);

		instreamAdController = new InstreamAdController(getApplicationContext());
		instreamAdController.setSkipListener(this);
		instreamAdController.setAdClickListener(this);
		instreamAdController.setAnchorView(videoContainer);
		instreamAdController.show();

		MediaController mediaController = new MediaController(this);
		mediaController.setAnchorView(mainVideoView);

		mainVideoView.setVideoURI(Uri.parse(LINK_TO_MAIN_VIDEO));
		mainVideoView.setMediaController(mediaController);
		mainVideoView.setVisibility(View.GONE);

		progressBar.setVisibility(View.VISIBLE);


		myTargetVideoView.init(slotId);
		myTargetVideoView.load();
	}

	@Override
	public void onLoad(MyTargetVideoView myTargetVideoView)
	{
		myTargetVideoView.startPreroll();
	}

	@Override
	public void onNoAd(String s, MyTargetVideoView myTargetVideoView)
	{
		if (progressBar != null)
			progressBar.setVisibility(View.GONE);
		Toast.makeText(InstreamAdActivity.this, getString(R.string.no_ad), Toast.LENGTH_LONG).show();
	}

	@Override
	public void onStartBanner(MyTargetVideoView myTargetVideoView, MyTargetVideoView.BannerInfo bannerInfo)
	{
		if (progressBar != null)
			progressBar.setVisibility(View.GONE);

		this.bannerInfo = bannerInfo;
		instreamAdController.setControlsPosition(bannerInfo.videoWidth, bannerInfo.videoHeight);
		instreamAdController.showVisit();
		if (bannerInfo.allowClose)
		{
			if (bannerInfo.allowCloseDelay == 0)
			{
				instreamAdController.showClose();
			}
		} else
		{
			instreamAdController.hideClose();
			instreamAdController.hideTimeToClose();
		}
	}

	@Override
	public void onCompleteBanner(MyTargetVideoView myTargetVideoView,
	                             MyTargetVideoView.BannerInfo bannerInfo, String status)
	{
		instreamAdController.hideVisit();
		instreamAdController.hideTimeToClose();
		instreamAdController.hideClose();
		if (progressBar != null)
			progressBar.setVisibility(View.GONE);

	}

	@Override
	public void onComplete(String s, MyTargetVideoView myTargetVideoView, String status)
	{
		Toast.makeText(InstreamAdActivity.this, "Complete", Toast.LENGTH_SHORT).show();
		playMainVideo();
	}

	@Override
	public void onError(String s, MyTargetVideoView myTargetVideoView)
	{
		if (progressBar != null)
			progressBar.setVisibility(View.GONE);
	}

	@Override
	public void onTimeLeftChange(float timeLeft, float duration, MyTargetVideoView myTargetVideoView)
	{
		float timeElapsed = duration - timeLeft;

		if (bannerInfo != null && bannerInfo.allowClose)
		{
			if (bannerInfo.allowCloseDelay > 0)
			{
				if (bannerInfo.allowCloseDelay < timeElapsed)
				{
					instreamAdController.showClose();
				} else
				{
					instreamAdController.hideClose();
					instreamAdController.showTimeToClose((int) (bannerInfo.allowCloseDelay - timeElapsed + 1));
				}
			}
		}
	}

	@Override
	public void onSuspenseBanner(MyTargetVideoView myTargetVideoView, MyTargetVideoView.BannerInfo bannerInfo)
	{
		if (progressBar != null)
			progressBar.setVisibility(View.VISIBLE);
	}

	@Override
	public void onResumptionBanner(MyTargetVideoView myTargetVideoView, MyTargetVideoView.BannerInfo bannerInfo)
	{
		if (progressBar != null)
			progressBar.setVisibility(View.GONE);
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		myTargetVideoView.pause();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		myTargetVideoView.resume();
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		myTargetVideoView.stop();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		myTargetVideoView.destroy();
	}

	private void playMainVideo()
	{
		if (!mainVideoView.isPlaying())
		{
			mainVideoView.setVisibility(View.VISIBLE);
			mainVideoView.start();
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);

		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) //To fullscreen
		{
			toolbar.setVisibility(View.GONE);
			View decorView = getWindow().getDecorView();
			if (Build.VERSION.SDK_INT >= 16)
			{
				int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
				decorView.setSystemUiVisibility(uiOptions);
			} else
			{
				getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			}
			myTargetVideoView.fullscreen(true);

			LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) videoContainer.getLayoutParams();
			videoContainer.setLayoutParams(params);
		} else
		{
			toolbar.setVisibility(View.VISIBLE);
			View decorView = getWindow().getDecorView();
			if (Build.VERSION.SDK_INT >= 16)
			{
				int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
				decorView.setSystemUiVisibility(uiOptions);
			} else
			{
				getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			}
			myTargetVideoView.fullscreen(false);
			LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) videoContainer.getLayoutParams();
			videoContainer.setLayoutParams(params);
		}
	}

	@Override
	public void onSkipClicked()
	{
		myTargetVideoView.closedByUser();
		instreamAdController.hideClose();
		instreamAdController.hideTimeToClose();
		instreamAdController.hideVisit();
		Toast.makeText(InstreamAdActivity.this, "Closed by user", Toast.LENGTH_SHORT).show();
		playMainVideo();
	}

	@Override
	public void onAdClicked()
	{
		myTargetVideoView.handleClick();
	}
}
