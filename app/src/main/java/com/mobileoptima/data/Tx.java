package com.mobileoptima.data;

import com.android.library.Connection.Http;
import com.android.library.Sqlite.SQLiteAdapter;
import com.mobileoptima.Utils.Utils;
import com.mobileoptima.constants.App;
import com.mobileoptima.constants.Settings;
import com.mobileoptima.interfaces.Callback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class Tx {
	public static boolean authorizeDevice(SQLiteAdapter db, String deviceCode, String deviceID, Callback.OnErrorCallback errorCallback) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("authorization_code", deviceCode);
		map.put("tablet_id", deviceID);
		map.put("api_key", "V0gu1964h5j762s7WiG52i45CMg1s9Xo8dbX565P20m3w7U7CA");
		map.put("device_type", "ANDROID");
		String url = App.WEB_API + "authorization-request";
		String params = new JSONObject(map).toString();
		String response = Http.post(url, params, Settings.HTTP_REQUEST_TIMEOUT_TX);
		Utils.log("authorize", url);
		Utils.log("authorize", params);
		Utils.log("authorize", response);
		JSONArray dataArray = Utils.getDataArray(params, response, errorCallback);
		if(dataArray == null) {
			return false;
		}
		db.beginTransaction();
		for(int x = 0; x < dataArray.length(); x++) {
			JSONObject dataObj = dataArray.optJSONObject(x);
			Save.apiKey(db, dataObj.optString("api_key"), deviceCode, deviceID);
		}
		return db.endTransaction();
	}

	public static boolean login(SQLiteAdapter db, String username, String password, Callback.OnErrorCallback errorCallback) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("api_key", Get.apiKey(db));
		map.put("employee_number", username);
		map.put("password", password);
		String url = App.WEB_API + "login";
		String params = new JSONObject(map).toString();
		String response = Http.post(url, params, Settings.HTTP_REQUEST_TIMEOUT_TX);
		Utils.log("login", url);
		Utils.log("login", params);
		Utils.log("login", response);
		JSONArray dataArray = Utils.getDataArray(params, response, errorCallback);
		if(dataArray == null) {
			return false;
		}
		db.beginTransaction();
		for(int x = 0; x < dataArray.length(); x++) {
			JSONObject dataObj = dataArray.optJSONObject(x);
			Save.login(db, dataObj.optString("employee_id"));
		}
		return db.endTransaction();
	}
}