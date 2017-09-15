package com.mobileoptima.Utils;

import com.mobileoptima.constants.Settings;
import com.mobileoptima.interfaces.Callback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Utils {
	public static void log(String msg) {
		log(Settings.LOG_TAG, msg);
	}

	public static void log(String tag, String msg) {
		com.android.library.Utils.Util.log(Settings.LOG_ENABLED, tag, msg);
	}

	public static JSONArray getDataArray(String params, String response, Callback.OnErrorCallback errorCallback) {
		JSONObject responseObj;
		try {
			responseObj = new JSONObject(response);
		}
		catch(JSONException e) {
			errorCallback.onError(params, response, e.getMessage(), false);
			return null;
		}
		JSONObject errorObj = responseObj.optJSONObject("error");
		if(errorObj != null) {
			String message = errorObj.optString("message");
			if(!message.isEmpty()) {
				errorCallback.onError(params, response, message, false);
			}
			return null;
		}
		JSONArray initArray = responseObj.optJSONArray("init");
		if(initArray != null) {
			for(int x = 0; x < initArray.length(); x++) {
				JSONObject initObj = initArray.optJSONObject(x);
				if(initObj != null) {
					String status = initObj.optString("status");
					int recNo = initObj.optInt("recno");
					if(status.isEmpty() || !status.equals("ok")) {
						String message = initObj.optString("message");
						if(!message.isEmpty()) {
							errorCallback.onError(params, response, message, true);
						}
						return null;
					}
				}
			}
		}
		return responseObj.optJSONArray("data");
	}
}