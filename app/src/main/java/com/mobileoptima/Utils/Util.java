package com.mobileoptima.Utils;

import com.mobileoptima.constants.Settings;

public class Util {
	public static void log(String msg) {
		log(Settings.LOG_TAG, msg);
	}

	public static void log(String tag, String msg) {
		com.android.library.Utils.Util.log(Settings.LOG_ENABLED, tag, msg);
	}
}