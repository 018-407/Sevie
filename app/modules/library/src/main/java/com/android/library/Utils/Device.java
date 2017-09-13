package com.android.library.Utils;

import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;

public class Device {
	public static String getDeviceID(Context context) {
		TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		if(manager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE) {
			return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
		}
		return manager.getDeviceId();
	}
}