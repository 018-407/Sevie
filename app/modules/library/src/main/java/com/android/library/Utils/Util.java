package com.android.library.Utils;

import android.util.Log;

public class Util {
	public static void log(boolean isEnabled, String tag, String msg) {
		if(isEnabled) {
			Log.e(tag, msg);
		}
	}

	public static String camelCase(String words) {
		StringBuilder output = new StringBuilder();
		for(String word : words.split(" ")) {
			if(output.length() != 0) {
				output.append(" ");
			}
			output.append(word.substring(0, 1).toUpperCase() + word.substring(1));
		}
		return output.toString();
	}
}