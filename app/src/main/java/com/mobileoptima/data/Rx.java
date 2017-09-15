package com.mobileoptima.data;

import com.android.library.Connection.Http;
import com.android.library.Sqlite.SQLiteAdapter;
import com.android.library.Utils.Util;
import com.mobileoptima.Utils.Utils;
import com.mobileoptima.constants.App;
import com.mobileoptima.constants.Convention;
import com.mobileoptima.constants.Modules;
import com.mobileoptima.constants.Settings;
import com.mobileoptima.constants.Table;
import com.mobileoptima.models.Company;
import com.mobileoptima.models.Employee;
import com.mobileoptima.models.Team;

import org.json.JSONArray;
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
		Utils.log("company", url);
		Utils.log("company", params);
		Utils.log("company", response);
		JSONArray dataArray = Utils.getDataArray(params, response, errorCallback);
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

	public static boolean convention(SQLiteAdapter db, OnErrorCallback errorCallback) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("api_key", Get.apiKey(db));
		String url = App.WEB_API + "get-naming-convention";
		String params = Http.jasonObjectToURL(new JSONObject(map));
		String response = Http.get(url, params, Settings.HTTP_REQUEST_TIMEOUT_RX);
		Utils.log("convention", url);
		Utils.log("convention", params);
		Utils.log("convention", response);
		JSONArray dataArray = Utils.getDataArray(params, response, errorCallback);
		if(dataArray == null) {
			return false;
		}
		db.beginTransaction();
		for(int x = 0; x < dataArray.length(); x++) {
			JSONObject dataObj = dataArray.optJSONObject(x);
			for(Convention convention : Convention.values()) {
				String name = dataObj.optString(convention.getID());
				if(!name.isEmpty()) {
					if(name.equals("keep")) {
						switch(convention.getID()) {
							case "startday":
								name = "time in";
								break;
							case "endday":
								name = "time out";
								break;
							default:
								name = convention.getID();
								break;
						}
					}
					name = Util.camelCase(name);
					Save.convention(db, convention, name);
				}
			}
		}
		return db.endTransaction();
	}

	public static boolean employees(SQLiteAdapter db, OnErrorCallback errorCallback) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("api_key", Get.apiKey(db));
		String url = App.WEB_API + "get-employee-details";
		String params = Http.jasonObjectToURL(new JSONObject(map));
		String response = Http.get(url, params, Settings.HTTP_REQUEST_TIMEOUT_RX);
		Utils.log("employees", url);
		Utils.log("employees", params);
		Utils.log("employees", response);
		JSONArray dataArray = Utils.getDataArray(params, response, errorCallback);
		if(dataArray == null) {
			return false;
		}
		db.beginTransaction();
		db.rawQuery("UPDATE " + Table.EMPLOYEE.getName() + " SET isActive = 0");
		for(int x = 0; x < dataArray.length(); x++) {
			JSONObject dataObj = dataArray.optJSONObject(x);
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