package com.mopub.mobileads;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.mopub.common.BaseAdapterConfiguration;
import com.mopub.common.OnNetworkInitializationFinishedListener;
import com.my.target.common.MyTargetVersion;

import java.util.Map;

public class MyTargetAdapterConfiguration extends BaseAdapterConfiguration
{
	private static final String ADAPTER_VERSION = MyTargetVersion.VERSION + ".0";
	private static final String NETWORK_NAME = "myTarget";

	@Override
	public @NonNull String getAdapterVersion()
	{
		return ADAPTER_VERSION;
	}

	@Override
	public @Nullable String getBiddingToken(@NonNull Context context)
	{
		return null;
	}

	@Override
	public @NonNull String getMoPubNetworkName()
	{
		return NETWORK_NAME;
	}

	@Override
	public @NonNull String getNetworkSdkVersion()
	{
		return MyTargetVersion.VERSION;
	}

	@Override
	public void initializeNetwork(@NonNull Context context, @Nullable Map<String, String> configuration, @NonNull OnNetworkInitializationFinishedListener listener)
	{
		listener.onNetworkInitializationFinished(MyTargetAdapterConfiguration.class, MoPubErrorCode.ADAPTER_INITIALIZATION_SUCCESS);
	}
}
