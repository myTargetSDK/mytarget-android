package com.my.target.ads.mediation;

import com.mopub.common.MoPub;
import com.mopub.common.privacy.ConsentStatus;
import com.mopub.common.privacy.PersonalInfoManager;
import com.my.target.common.MyTargetPrivacy;

public class MyTargetAdapterUtils
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
}
