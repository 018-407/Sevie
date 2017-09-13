package com.mobileoptima.data;

import com.android.library.Connection.Http;
import com.android.library.Sqlite.SQLiteAdapter;
import com.mobileoptima.Utils.Util;
import com.mobileoptima.constants.App;
import com.mobileoptima.constants.Settings;
import com.mobileoptima.interfaces.Callback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Tx {
	public static boolean authorizeDevice(SQLiteAdapter db, String deviceCode, String deviceID, Callback.OnErrorCallback errorCallback) {
		boolean result = false;
		HashMap<String, Object> map = new HashMap<>();
		map.put("authorization_code", deviceCode);
		map.put("tablet_id", deviceID);
		map.put("api_key", "V0gu1964h5j762s7WiG52i45CMg1s9Xo8dbX565P20m3w7U7CA");
		map.put("device_type", "ANDROID");
		String url = App.WEB_API + "authorization-request";
		String params = new JSONObject(map).toString();
		String response = Http.post(url, params, Settings.HTTP_REQUEST_TIMEOUT_TX);
		Util.log("authorize", url);
		Util.log("authorize", params);
		Util.log("authorize", response);
		try {
			JSONObject responseObj = new JSONObject(response);
			try {
				JSONObject errorObj = responseObj.getJSONObject("error");
				String message = errorObj.getString("message");
				errorCallback.onError(params, response, message, false);
				return false;
			}
			catch(JSONException e) {
				e.printStackTrace();
			}
			JSONArray initArray = responseObj.getJSONArray("init");
			for(int x = 0; x < initArray.length(); x++) {
				JSONObject initObj = initArray.getJSONObject(x);
				String status = initObj.getString("status");
				if(!status.equals("ok")) {
					String message = initObj.getString("message");
					errorCallback.onError(params, response, message, true);
					return false;
				}
				JSONArray dataArray = responseObj.getJSONArray("data");
				for(int y = 0; y < dataArray.length(); y++) {
					JSONObject dataObj = dataArray.getJSONObject(y);
					String apiKey = dataObj.getString("api_key");
					result = Save.apiKey(db, apiKey, deviceCode, deviceID);
				}
			}
		}
		catch(JSONException e) {
			e.printStackTrace();
			errorCallback.onError(params, response, e.getMessage(), false);
		}
		return result;
	}

	public static boolean login(SQLiteAdapter db, String username, String password, Callback.OnErrorCallback errorCallback) {
		boolean result = false;
		HashMap<String, Object> map = new HashMap<>();
		map.put("api_key", Get.apiKey(db));
		map.put("employee_number", username);
		map.put("password", password);
		String url = App.WEB_API + "login";
		String params = new JSONObject(map).toString();
		String response = Http.post(url, params, Settings.HTTP_REQUEST_TIMEOUT_TX);
		Util.log("login", url);
		Util.log("login", params);
		Util.log("login", response);
		try {
			JSONObject responseObj = new JSONObject(response);
			try {
				JSONObject errorObj = responseObj.getJSONObject("error");
				String message = errorObj.getString("message");
				errorCallback.onError(params, response, message, false);
				return false;
			}
			catch(JSONException e) {
				e.printStackTrace();
			}
			JSONArray initArray = responseObj.getJSONArray("init");
			for(int x = 0; x < initArray.length(); x++) {
				JSONObject initObj = initArray.getJSONObject(x);
				String status = initObj.getString("status");
				if(!status.equals("ok")) {
					String message = initObj.getString("message");
					errorCallback.onError(params, response, message, true);
					return false;
				}
				JSONArray dataArray = responseObj.getJSONArray("data");
				for(int y = 0; y < dataArray.length(); y++) {
					JSONObject dataObj = dataArray.getJSONObject(y);
					String employeeID = dataObj.getString("employee_id");
					result = Save.login(db, employeeID);
				}
			}
		}
		catch(JSONException e) {
			e.printStackTrace();
			errorCallback.onError(params, response, e.getMessage(), false);
		}
		return result;
	}
}