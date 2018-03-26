package com.my.targetDemoApp.activities;

import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.my.target.instreamads.InstreamAd;
import com.my.targetDemoApp.R;
import com.my.targetDemoApp.utils.Tools;

import java.util.concurrent.CopyOnWriteArrayList;

public class InstreamAdActivity extends AdActivity
		implements InstreamAd.InstreamAdListener, InstreamAdController.SkipListener,
				   InstreamAdController.AdClickListener

{
	public static final String SECTION_PREROLL = "preroll";
	public static final String SECTION_POSTROLL = "postroll";
	public static final String SECTION_MIDROLL = "midroll";

	@Nullable
	private InstreamAd instreamAd;

	private Button loadButton;
	private Button playButton;
	private TextView statusText;
	private ProgressBar progressBar;
	private VideoView videoView;
	private FrameLayout videoFrame;
	private InstreamAdController instreamAdController;
	private int currentPosition;
	private InstreamAd.InstreamAdBanner instreamAdBanner;
	private Toolbar toolbar;
	private CopyOnWriteArrayList<Float> midrollPositions = new CopyOnWriteArrayList<>();
	private Runnable progressCheck = new Runnable()
	{
		@Override
		public void run()
		{
			if (videoView.isPlaying())
			{
				int prog = videoView.getCurrentPosition();

				for (Float midrollPosition : midrollPositions)
				{
					if (((float) prog / 1000) >= midrollPosition)
					{
						startMidroll(midrollPosition);
						midrollPositions.remove(midrollPosition);
					}
				}

				instreamAdController.setProgress(prog);
			}

			instreamAdController.postDelayed(progressCheck, 500);
		}
	};
	private boolean muted;

	private void setStatus(String s)
	{
		s = " " + s;
		statusText.setText(s);
	}

	private void setupProgressBar()
	{
		midrollPositions.clear();
		if (instreamAd != null)
		{
			for (float v : instreamAd.getMidPoints())
			{
				midrollPositions.add(v);
			}
			instreamAdController.setupMidrolls(instreamAd.getMidPoints());
			instreamAdController.requestLayout();
		}
	}

	public void btnLoadAd(View view)
	{
		loadButton.setVisibility(View.GONE);
		progressBar.setVisibility(View.VISIBLE);
		instreamAd = new InstreamAd(slotId, this);
		Tools.fillCustomParamsUserData(instreamAd.getCustomParams());
		instreamAd.setListener(this);
		instreamAd.useDefaultPlayer();
		instreamAd.load();
		setStatus("loading ad... ");
	}

	public void startVideo(View view)
	{
		if (instreamAd == null) return;
		videoView = new VideoView(this);
		FrameLayout.LayoutParams videoPlayerParams = new FrameLayout.LayoutParams(ViewGroup
				.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		videoPlayerParams.gravity = Gravity.CENTER;
		videoFrame.addView(videoView, videoPlayerParams);
		playButton.setVisibility(View.GONE);

		FrameLayout.LayoutParams adPlayerParams = new FrameLayout.LayoutParams
				(ViewGroup.LayoutParams.MATCH_PARENT,
						ViewGroup.LayoutParams.WRAP_CONTENT);
		adPlayerParams.gravity = Gravity.CENTER;

		View adPlayerView = instreamAd.getPlayer().getView();
		if (adPlayerView.getParent() == null)
			videoFrame.addView(adPlayerView, adPlayerParams);

		instreamAdController.setSkipListener(this);
		instreamAdController.setAdClickListener(this);
		instreamAdController.setAnchorView(videoFrame);
		instreamAdController.setVisibility(View.VISIBLE);

		startPreroll();
	}

	@Override
	public void onLoad(InstreamAd instreamAd)
	{
		loadButton.setVisibility(View.GONE);
		playButton.setVisibility(View.VISIBLE);
		progressBar.setVisibility(View.GONE);
		setStatus("ads loaded, ready to play");
	}

	@Override
	public void onNoAd(String reason, InstreamAd instreamAd)
	{
		progressBar.setVisibility(View.GONE);
		loadButton.setVisibility(View.VISIBLE);
		setStatus("No ad. Reason: " + reason);
	}

	@Override
	public void onError(String reason, InstreamAd ad)
	{
		setStatus("error, reason: " + reason);
	}

	@Override
	public void onBannerStart(InstreamAd ad, InstreamAd.InstreamAdBanner instreamAdBanner)
	{
		this.instreamAdBanner = instreamAdBanner;
		instreamAdController.show();
		instreamAdController.setControlsPosition(instreamAdBanner.videoWidth,
				instreamAdBanner.videoHeight);
		instreamAdController.showVisit();
		if (instreamAdBanner.allowClose)
		{
			if (instreamAdBanner.allowCloseDelay == 0)
			{
				instreamAdController.showClose();
			}
		} else
		{
			instreamAdController.hideClose();
			instreamAdController.hideTimeToClose();
		}
	}

	public void switchVolume(View view)
	{
		if (muted)
		{
			muted = false;
			view.setBackgroundResource(R.drawable.ic_volume_on);
			if (instreamAd != null)
			{
				instreamAd.setVolume(1);
			}
		} else
		{
			muted = true;
			view.setBackgroundResource(R.drawable.ic_volume_off);
			if (instreamAd != null)
			{
				instreamAd.setVolume(0);
			}
		}
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		if (instreamAd != null)
		{
			instreamAd.getPlayer().pauseAdVideo();
		}
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		if (instreamAd != null)
		{
			instreamAd.getPlayer().resumeAdVideo();
		}
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		if (instreamAd != null)
		{
			instreamAd.getPlayer().destroy();
		}
	}

	@Override
	public void onBannerComplete(InstreamAd ad, InstreamAd.InstreamAdBanner instreamAdBanner)
	{
		instreamAdController.hideVisit();
		instreamAdController.hideTimeToClose();
		instreamAdController.hideClose();
	}

	@Override
	public void onBannerTimeLeftChange(float timeLeft, float duration, InstreamAd ad)
	{
		float timeElapsed = duration - timeLeft;

		if (instreamAdBanner != null && instreamAdBanner.allowClose)
		{
			if (instreamAdBanner.allowCloseDelay > 0)
			{
				if (instreamAdBanner.allowCloseDelay < timeElapsed)
				{
					instreamAdController.showClose();
				} else
				{
					instreamAdController.hideClose();
					instreamAdController.showTimeToClose((int) (
							instreamAdBanner.allowCloseDelay - timeElapsed + 1));
				}
			}
		}
	}

	@Override
	public void onComplete(String section, InstreamAd ad)
	{
		hideAd();
		toast("Complete " + section);
		switch (section)
		{
			case SECTION_PREROLL:
				hideAd();
				playVideo();
				break;
			case SECTION_MIDROLL:
				hideAd();
				videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
				{
					@Override
					public void onPrepared(MediaPlayer mp)
					{
						mp.start();
						mp.seekTo(currentPosition);
						setStatus("playing video");
					}
				});
				break;
			case SECTION_POSTROLL:
				playButton.setVisibility(View.VISIBLE);
				videoView.suspend();
				videoView.setVisibility(View.GONE);
				instreamAdController.hideSeekBar();
				instreamAdController.hideVisit();
				instreamAdController.hideTimeToClose();
				instreamAdController.hideClose();
				if (instreamAd != null)
				{
					instreamAd.getPlayer().getView().setVisibility(View.GONE);
				}
				break;
		}
	}

	@Override
	public void onSkipClicked()
	{
		if (instreamAd != null)
		{
			instreamAd.skipBanner();
		}
	}

	@Override
	public void onAdClicked()
	{
		if (instreamAd != null)
		{
			instreamAd.handleClick();
		}
	}



	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case android.R.id.home:
				finish();
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	void reloadAd()
	{

	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_instream);

		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		if (getSupportActionBar() != null)
		{
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setDisplayShowHomeEnabled(true);
		}

		loadButton = (Button) findViewById(R.id.btn_load);
		playButton = (Button) findViewById(R.id.btn_play);
		statusText = (TextView) findViewById(R.id.tv_status);
		progressBar = (ProgressBar) findViewById(R.id.pb_main);
		videoFrame = (FrameLayout) findViewById(R.id.video_frame);
		instreamAdController = new InstreamAdController(this);
		instreamAdController.setVisibility(View.GONE);

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
			if (instreamAd != null)
			{
				instreamAd.setFullscreen(true);
			}
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
			if (instreamAd != null)
			{
				instreamAd.setFullscreen(false);
			}
		}
	}

	private void startPreroll()
	{
		showAd();
		setStatus("playing preroll");
		if (instreamAd != null)
		{
			instreamAd.startPreroll();
		}
	}

	private void toast(String s)
	{
		Snackbar.make(videoFrame, s, Snackbar.LENGTH_LONG).show();
	}

	private void hideAd()
	{
		if (instreamAd != null)
		{
			instreamAd.getPlayer().getView().setVisibility(View.GONE);
		}
		videoView.setVisibility(View.VISIBLE);
		instreamAdController.showSeekBar();
	}

	private void showAd()
	{
		if (instreamAd != null)
		{
			instreamAd.getPlayer().getView().setVisibility(View.VISIBLE);
		}
		videoView.setVisibility(View.GONE);
		instreamAdController.hideSeekBar();
	}

	private void playVideo()
	{
		setStatus("playing video");

		videoView.setVideoURI(
				Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.mytarget));
		progressBar.setVisibility(View.VISIBLE);

		videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
		{
			@Override
			public void onPrepared(MediaPlayer mediaPlayer)
			{
				progressBar.setVisibility(View.GONE);
				instreamAdController.setMax(videoView.getDuration());
				if (instreamAd != null)
				{
					instreamAd.configureMidpoints(videoView.getDuration() / 1000);
				}
				setupProgressBar();
				instreamAdController.postDelayed(progressCheck, 1000);
				mediaPlayer.start();
			}
		});
		videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
		{
			@Override
			public void onCompletion(MediaPlayer mediaPlayer)
			{
				videoView.suspend();
				startPostroll();
			}
		});
		videoView.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		return true;
	}

	private void startMidroll(Float midrollPosition)
	{
		setStatus("playing midroll");
		currentPosition = videoView.getCurrentPosition();
		videoView.pause();
		showAd();
		if (instreamAd != null)
		{
			instreamAd.startMidroll(midrollPosition);
		}
	}

	private void startPostroll()
	{
		showAd();
		if (instreamAd != null)
		{
			instreamAd.startPostroll();
			setStatus("playing postroll");
		}
	}
}

