package com.mobileoptima.data;

import com.android.library.Connection.Http;
import com.android.library.Sqlite.SQLiteAdapter;
import com.mobileoptima.Utils.Util;
import com.mobileoptima.constants.App;
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
		boolean result = false;
		HashMap<String, Object> map = new HashMap<>();
		map.put("api_key", Get.apiKey(db));
		String url = App.WEB_API + "get-company";
		String params = Http.jasonObjectToURL(new JSONObject(map));
		String response = Http.get(url, params, Settings.HTTP_REQUEST_TIMEOUT_RX);
		Util.log("company", url);
		Util.log("company", params);
		Util.log("company", response);
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
				db.beginTransaction();
				for(int y = 0; y < dataArray.length(); y++) {
					JSONObject dataObj = dataArray.getJSONObject(y);
					Company company = new Company();
					company.ID = dataObj.getString("company_id");
					company.name = dataObj.getString("company_name");
					company.code = dataObj.getString("company_code");
					company.address = dataObj.getString("address");
					company.mobile = dataObj.getString("mobile");
					company.landline = dataObj.getString("landline");
					company.logoURL = dataObj.getString("company_logo");
					company.expirationDate = dataObj.getString("expiration_date");
					Save.company(db, company);
				}
				result = db.endTransaction();
			}
		}
		catch(JSONException e) {
			e.printStackTrace();
			errorCallback.onError(params, response, e.getMessage(), false);
		}
		return result;
	}

	public static boolean employee(SQLiteAdapter db, OnErrorCallback errorCallback) {
		boolean result = false;
		HashMap<String, Object> map = new HashMap<>();
		map.put("api_key", Get.apiKey(db));
		String url = App.WEB_API + "get-employee-details";
		String params = Http.jasonObjectToURL(new JSONObject(map));
		String response = Http.get(url, params, Settings.HTTP_REQUEST_TIMEOUT_RX);
		Util.log("employees", url);
		Util.log("employees", params);
		Util.log("employees", response);
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
				db.beginTransaction();
				db.rawQuery("UPDATE " + Table.EMPLOYEE.getName() + " SET isActive = 0");
				for(int y = 0; y < dataArray.length(); y++) {
					JSONObject dataObj = dataArray.getJSONObject(y);
					Employee employee = new Employee();
					employee.ID = dataObj.getString("employee_id");
					employee.firstName = dataObj.getString("firstname");
					employee.lastName = dataObj.getString("lastname");
					employee.employeeNumber = dataObj.getString("employee_number");
					employee.email = dataObj.getString("email");
					employee.mobile = dataObj.getString("mobile");
					employee.photoURL = dataObj.getString("picture_url");
					Team team = new Team();
					team.ID = dataObj.getString("team_id");
					employee.team = team;
					employee.isApprover = dataObj.getInt("is_approver") == 1;
					Save.employee(db, employee);
				}
				result = db.endTransaction();
			}
		}
		catch(JSONException e) {
			e.printStackTrace();
			errorCallback.onError(params, response, e.getMessage(), false);
		}
		return result;
	}
}