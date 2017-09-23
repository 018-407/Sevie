package com.mobileoptima.constants;

import com.android.library.Utils.Time;

public class Settings {
	public static final boolean EXTERNAL_STORAGE = true;
	public static final boolean LAST_NAME_FIRST = true;
	public static final boolean LOG_ENABLED = true;
	public static final String LOG_TAG = "paul";
	public static final int HTTP_REQUEST_TIMEOUT_RX = (int) Time.convertMinutesToMilli(5);
	public static final int HTTP_REQUEST_TIMEOUT_TX = (int) Time.convertSecondsToMilli(30);
	public static final long TIME_SECURITY_ALLOWANCE = Time.convertSecondsToMilli(5);
}