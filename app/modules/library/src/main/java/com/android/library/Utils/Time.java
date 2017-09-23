package com.android.library.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class Time {
	private static final Locale locale = Locale.ENGLISH;

	public static long convertSecondsToMilli(int seconds) {
		return seconds * 1000;
	}

	public static long convertMinutesToMilli(int minutes) {
		return minutes * 1000 * 60;
	}

	public static long convertHoursToMilli(int hours) {
		return hours * 1000 * 60 * 60;
	}

	public static long convertDaysToMilli(int days) {
		return days * 1000 * 60 * 60 * 24;
	}

	public static long convertTimestampToMilli(String timestamp) {
		long milli = 0;
		if(timestamp != null && timestamp.length() == 19) {
			try {
				milli = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", locale).parse(timestamp).getTime();
			}
			catch(ParseException e) {
				e.printStackTrace();
			}
		}
		return milli;
	}

	public static String convertMilliToTimestamp(long milli) {
		StringBuilder timestamp = new StringBuilder();
		if(milli != 0) {
			timestamp.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", locale).format(new java.util.Date(milli)));
		}
		return timestamp.toString();
	}

	public static String formatDateTime(String dateTime, String input, String output) {
		StringBuilder formattedDateTime = new StringBuilder();
		if(dateTime != null && !dateTime.isEmpty()) {
			try {
				formattedDateTime.append(new SimpleDateFormat(output, locale).format(new SimpleDateFormat(input, locale).parse(dateTime).getTime()));
			}
			catch(ParseException e) {
				e.printStackTrace();
			}
		}
		return formattedDateTime.toString();
	}

	public static String getTimestamp() {
		return convertMilliToTimestamp(System.currentTimeMillis());
	}

	public static long getTimestampFromTimeZoneID(long milli, String timeZoneID) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", locale);
		format.setTimeZone(TimeZone.getTimeZone(timeZoneID));
		return convertTimestampToMilli(format.format(milli));
	}

	public static String getTimeZoneID(String dateTime, long milli) {
		String timeZoneID = null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", locale);
		format.setTimeZone(TimeZone.getTimeZone(TimeZone.getDefault().getID()));
		String timestampFromTimeZone = format.format(milli);
		dateTime = dateTime.substring(0, dateTime.length() - 2);
		if(timestampFromTimeZone.equals(dateTime)) {
			timeZoneID = TimeZone.getDefault().getID();
		}
		for(String availableTimeZoneID : TimeZone.getAvailableIDs()) {
			format = new SimpleDateFormat("yyyy-MM-dd HH:mm", locale);
			format.setTimeZone(TimeZone.getTimeZone(availableTimeZoneID));
			timestampFromTimeZone = format.format(milli);
			if(timestampFromTimeZone.equals(dateTime)) {
				timeZoneID = availableTimeZoneID;
				break;
			}
		}
		return timeZoneID;
	}

	public static String getDateFromTimestamp(String timestamp) {
		StringBuilder date = new StringBuilder();
		if(timestamp != null && timestamp.length() == 19) {
			date.append(timestamp.substring(0, 10));
		}
		return date.toString();
	}

	public static String getTimeFromTimestamp(String timestamp) {
		StringBuilder time = new StringBuilder();
		if(timestamp != null && timestamp.length() == 19) {
			time.append(timestamp.substring(11, 16));
		}
		return time.toString();
	}
}