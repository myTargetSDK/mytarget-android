package com.my.target.ads.mediation;

import com.mopub.common.MoPub;
import com.mopub.common.privacy.ConsentStatus;
import com.mopub.common.privacy.PersonalInfoManager;
import com.my.target.common.MyTargetPrivacy;

import androidx.annotation.Nullable;

public final class MyTargetAdapterUtils
{
	public static void handleConsent()
	{
		PersonalInfoManager personalInfoManager = MoPub.getPersonalInformationManager();
		if (personalInfoManager != null)
		{
			ConsentStatus status = personalInfoManager.getPersonalInfoConsentStatus();
			switch (status)
			{
				case DNT:
				case EXPLICIT_NO:
					MyTargetPrivacy.setUserConsent(false);
					break;
				case EXPLICIT_YES:
					MyTargetPrivacy.setUserConsent(true);
					break;
				default:
					break;
			}
		}
	}

	public static int parseSlot(@Nullable String slotString)
	{
		int slot = -1;
		if (slotString == null)
		{
			return slot;
		}
		try
		{
			slot = Integer.parseInt(slotString);
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
		}
		return slot;
	}
}
