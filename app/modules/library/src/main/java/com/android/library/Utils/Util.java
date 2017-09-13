package com.android.library.Utils;

import android.util.Log;

public class Util {
	public static void log(boolean isEnabled, String tag, String msg) {
		if(isEnabled) {
			Log.e(tag, msg);
		}
	}
}