package ru.mail.android.mytarget.demo.ui;

import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.Before;

import com.my.targetDemoApp.activities.BannerAdActivity;

public class StandardBannerTest extends ActivityInstrumentationTestCase2<BannerAdActivity>
{
	private BannerAdActivity mActivity;

	public StandardBannerTest()
	{
		super(BannerAdActivity.class);
	}

	@Before
	public void setUp() throws Exception
	{
		super.setUp();
		injectInstrumentation(InstrumentationRegistry.getInstrumentation());
		mActivity = getActivity();
	}
}
