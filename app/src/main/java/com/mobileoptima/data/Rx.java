package com.mobileoptima.data;

import com.android.library.Connection.Http;
import com.android.library.Sqlite.SQLiteAdapter;
import com.mobileoptima.Utils.Util;
import com.mobileoptima.constants.App;
import com.mobileoptima.constants.Modules;
import com.mobileoptima.constants.Settings;
import com.mobileoptima.constants.Table;
import com.mobileoptima.models.Company;
import com.mobileoptima.models.Employee;
import com.mobileoptima.models.Team;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.mobileoptima.interfaces.Callback.OnErrorCallback;

public class Rx {
	public static boolean company(SQLiteAdapter db, OnErrorCallback errorCallback) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("api_key", Get.apiKey(db));
		String url = App.WEB_API + "get-company";
		String params = Http.jasonObjectToURL(new JSONObject(map));
		String response = Http.get(url, params, Settings.HTTP_REQUEST_TIMEOUT_RX);
		Util.log("company", url);
		Util.log("company", params);
		Util.log("company", response);
		JSONObject responseObj;
		try {
			responseObj = new JSONObject(response);
		}
		catch(JSONException e) {
			errorCallback.onError(params, response, e.getMessage(), false);
			return false;
		}
		JSONObject errorObj = responseObj.optJSONObject("error");
		if(errorObj != null) {
			String message = errorObj.optString("message");
			if(!message.isEmpty()) {
				errorCallback.onError(params, response, message, false);
			}
			return false;
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
						return false;
					}
					if(recNo == 0) {
						return true;
					}
				}
			}
		}
		JSONArray dataArray = responseObj.optJSONArray("data");
		if(dataArray == null) {
			return false;
		}
		db.beginTransaction();
		for(int x = 0; x < dataArray.length(); x++) {
			JSONObject dataObj = dataArray.optJSONObject(x);
			Company company = new Company();
			company.ID = dataObj.optString("company_id");
			company.name = dataObj.optString("company_name");
			company.code = dataObj.optString("company_code");
			company.address = dataObj.optString("address");
			company.mobile = dataObj.optString("mobile");
			company.landline = dataObj.optString("landline");
			company.logoURL = dataObj.optString("company_logo");
			company.expirationDate = dataObj.optString("expiration_date");
			Save.company(db, company);
			JSONArray modulesArray = dataObj.optJSONArray("modules");
			db.rawQuery("UPDATE " + Table.MODULES.getName() + " SET isEnabled = 0 WHERE name = '" + Modules.ATTENDANCE.getID() + "'");
			for(int y = 0; y < modulesArray.length(); y++) {
				Save.moduleEnabled(db, modulesArray.optString(y));
			}
		}
		return db.endTransaction();
	}

	public static boolean employee(SQLiteAdapter db, OnErrorCallback errorCallback) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("api_key", Get.apiKey(db));
		String url = App.WEB_API + "get-employee-details";
		String params = Http.jasonObjectToURL(new JSONObject(map));
		String response = Http.get(url, params, Settings.HTTP_REQUEST_TIMEOUT_RX);
		Util.log("employees", url);
		Util.log("employees", params);
		Util.log("employees", response);
		JSONObject responseObj;
		try {
			responseObj = new JSONObject(response);
		}
		catch(JSONException e) {
			errorCallback.onError(params, response, e.getMessage(), false);
			return false;
		}
		JSONObject errorObj = responseObj.optJSONObject("error");
		if(errorObj != null) {
			String message = errorObj.optString("message");
			if(!message.isEmpty()) {
				errorCallback.onError(params, response, message, false);
			}
			return false;
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
						return false;
					}
					if(recNo == 0) {
						return true;
					}
				}
			}
		}
		JSONArray dataArray = responseObj.optJSONArray("data");
		if(dataArray == null) {
			return false;
		}
		db.beginTransaction();
		db.rawQuery("UPDATE " + Table.EMPLOYEE.getName() + " SET isActive = 0");
		for(int y = 0; y < dataArray.length(); y++) {
			JSONObject dataObj = dataArray.optJSONObject(y);
			Employee employee = new Employee();
			employee.ID = dataObj.optString("employee_id");
			employee.firstName = dataObj.optString("firstname");
			employee.lastName = dataObj.optString("lastname");
			employee.employeeNumber = dataObj.optString("employee_number");
			employee.email = dataObj.optString("email");
			employee.mobile = dataObj.optString("mobile");
			employee.photoURL = dataObj.optString("picture_url");
			Team team = new Team();
			team.ID = dataObj.optString("team_id");
			employee.team = team;
			employee.isApprover = dataObj.optInt("is_approver") == 1;
			Save.employee(db, employee);
		}
		return db.endTransaction();
	}
}