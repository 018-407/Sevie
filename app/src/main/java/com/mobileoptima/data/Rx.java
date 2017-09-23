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
import com.mobileoptima.models.MasterAlertType;
import com.mobileoptima.models.MasterBreakType;
import com.mobileoptima.models.MasterCompany;
import com.mobileoptima.models.MasterEmployee;
import com.mobileoptima.models.MasterExpenseType;
import com.mobileoptima.models.MasterExpenseTypeCategory;
import com.mobileoptima.models.MasterOvertimeReason;
import com.mobileoptima.models.MasterScheduleTime;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import static com.mobileoptima.interfaces.Callback.OnErrorCallback;

public class Rx {
	public static boolean alertTypes(SQLiteAdapter db, OnErrorCallback errorCallback) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("api_key", Get.apiKey(db));
		String url = App.WEB_API + "get-alert-types";
		String params = Http.jasonObjectToURL(new JSONObject(map));
		String response = Http.get(url, params, Settings.HTTP_REQUEST_TIMEOUT_RX);
		Utils.log("alertTypes", url);
		Utils.log("alertTypes", params);
		Utils.log("alertTypes", response);
		JSONArray dataArray = Utils.getDataArray(params, response, errorCallback);
		if(dataArray == null) {
			return false;
		}
		db.beginTransaction();
		db.rawQuery("UPDATE " + Table.ALERT_TYPES.getName() + " SET isActive = 0");
		for(int x = 0; x < dataArray.length(); x++) {
			JSONObject dataObj = dataArray.optJSONObject(x);
			MasterAlertType alertType = new MasterAlertType();
			alertType.ID = dataObj.optString("alert_type_id");
			alertType.name = dataObj.optString("alert_type");
			Save.alertType(db, alertType);
		}
		return db.endTransaction();
	}

	public static boolean breakTypes(SQLiteAdapter db, OnErrorCallback errorCallback) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("api_key", Get.apiKey(db));
		String url = App.WEB_API + "get-breaks";
		String params = Http.jasonObjectToURL(new JSONObject(map));
		String response = Http.get(url, params, Settings.HTTP_REQUEST_TIMEOUT_RX);
		Utils.log("breakTypes", url);
		Utils.log("breakTypes", params);
		Utils.log("breakTypes", response);
		JSONArray dataArray = Utils.getDataArray(params, response, errorCallback);
		if(dataArray == null) {
			return false;
		}
		db.beginTransaction();
		db.rawQuery("UPDATE " + Table.BREAK_TYPES.getName() + " SET isActive = 0");
		for(int x = 0; x < dataArray.length(); x++) {
			JSONObject dataObj = dataArray.optJSONObject(x);
			MasterBreakType breakType = new MasterBreakType();
			breakType.ID = dataObj.optString("break_id");
			breakType.name = dataObj.optString("break_name");
			breakType.duration = dataObj.optInt("duration");
			Save.breakType(db, breakType);
		}
		return db.endTransaction();
	}

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
			MasterCompany company = new MasterCompany();
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
		db.rawQuery("UPDATE " + Table.EMPLOYEES.getName() + " SET isActive = 0");
		for(int x = 0; x < dataArray.length(); x++) {
			JSONObject dataObj = dataArray.optJSONObject(x);
			MasterEmployee employee = new MasterEmployee();
			employee.ID = dataObj.optString("employee_id");
			employee.firstName = dataObj.optString("firstname");
			employee.lastName = dataObj.optString("lastname");
			employee.employeeNumber = dataObj.optString("employee_number");
			employee.email = dataObj.optString("email");
			employee.mobile = dataObj.optString("mobile");
			employee.photoURL = dataObj.optString("picture_url");
			employee.teamID = dataObj.optString("team_id");
			employee.isApprover = dataObj.optInt("is_approver") == 1;
			if(dataObj.optString("is_active").equals("yes")) {
				Save.employee(db, employee);
			}
		}
		return db.endTransaction();
	}

	public static boolean expenseTypeCategories(SQLiteAdapter db, OnErrorCallback errorCallback) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("api_key", Get.apiKey(db));
		String url = App.WEB_API + "get-expense-categories";
		String params = Http.jasonObjectToURL(new JSONObject(map));
		String response = Http.get(url, params, Settings.HTTP_REQUEST_TIMEOUT_RX);
		Utils.log("expenseTypeCategories", url);
		Utils.log("expenseTypeCategories", params);
		Utils.log("expenseTypeCategories", response);
		JSONArray dataArray = Utils.getDataArray(params, response, errorCallback);
		if(dataArray == null) {
			return false;
		}
		db.beginTransaction();
		db.rawQuery("UPDATE " + Table.EXPENSE_TYPE_CATEGORIES.getName() + " SET isActive = 0");
		for(int x = 0; x < dataArray.length(); x++) {
			JSONObject dataObj = dataArray.optJSONObject(x);
			MasterExpenseTypeCategory expenseTypeCategory = new MasterExpenseTypeCategory();
			expenseTypeCategory.ID = dataObj.optString("expense_category_id");
			expenseTypeCategory.name = dataObj.optString("expense_category_name");
			if(Save.expenseTypeCategory(db, expenseTypeCategory)) {
				expenseTypes(db, expenseTypeCategory.ID, errorCallback);
			}
		}
		return db.endTransaction();
	}

	public static boolean expenseTypes(SQLiteAdapter db, String expenseTypeCategoryID, OnErrorCallback errorCallback) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("api_key", Get.apiKey(db));
		map.put("expense_category_id", expenseTypeCategoryID);
		String url = App.WEB_API + "get-expense-types";
		String params = Http.jasonObjectToURL(new JSONObject(map));
		String response = Http.get(url, params, Settings.HTTP_REQUEST_TIMEOUT_RX);
		Utils.log("expenseTypes", url);
		Utils.log("expenseTypes", params);
		Utils.log("expenseTypes", response);
		JSONArray dataArray = Utils.getDataArray(params, response, errorCallback);
		if(dataArray == null) {
			return false;
		}
		db.rawQuery("UPDATE " + Table.EXPENSE_TYPES.getName() + " SET isActive = 0");
		for(int x = 0; x < dataArray.length(); x++) {
			JSONObject dataObj = dataArray.optJSONObject(x);
			MasterExpenseType expenseType = new MasterExpenseType();
			expenseType.ID = dataObj.optString("expense_type_id");
			expenseType.name = dataObj.optString("expense_type_name");
			MasterExpenseTypeCategory expenseTypeCategory = new MasterExpenseTypeCategory();
			expenseTypeCategory.ID = expenseTypeCategoryID;
			expenseType.expenseTypeCategory = expenseTypeCategory;
			expenseType.isRequired = dataObj.optString("is_required").equals("yes");
			Save.expenseType(db, expenseType);
		}
		return true;
	}

	public static boolean overtimeReasons(SQLiteAdapter db, OnErrorCallback errorCallback) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("api_key", Get.apiKey(db));
		String url = App.WEB_API + "get-overtime-reasons";
		String params = Http.jasonObjectToURL(new JSONObject(map));
		String response = Http.get(url, params, Settings.HTTP_REQUEST_TIMEOUT_RX);
		Utils.log("overtimeReasons", url);
		Utils.log("overtimeReasons", params);
		Utils.log("overtimeReasons", response);
		JSONArray dataArray = Utils.getDataArray(params, response, errorCallback);
		if(dataArray == null) {
			return false;
		}
		db.beginTransaction();
		db.rawQuery("UPDATE " + Table.OVERTIME_REASONS.getName() + " SET isActive = 0");
		for(int x = 0; x < dataArray.length(); x++) {
			JSONObject dataObj = dataArray.optJSONObject(x);
			MasterOvertimeReason overtimeReason = new MasterOvertimeReason();
			overtimeReason.ID = dataObj.optString("overtime_reason_id");
			overtimeReason.name = dataObj.optString("overtime_reason");
			Save.overtimeReason(db, overtimeReason);
		}
		return db.endTransaction();
	}

	public static boolean scheduleTimes(SQLiteAdapter db, OnErrorCallback errorCallback) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("api_key", Get.apiKey(db));
		String url = App.WEB_API + "get-schedule-time";
		String params = Http.jasonObjectToURL(new JSONObject(map));
		String response = Http.get(url, params, Settings.HTTP_REQUEST_TIMEOUT_RX);
		Utils.log("scheduleTimes", url);
		Utils.log("scheduleTimes", params);
		Utils.log("scheduleTimes", response);
		JSONArray dataArray = Utils.getDataArray(params, response, errorCallback);
		if(dataArray == null) {
			return false;
		}
		db.beginTransaction();
		db.rawQuery("UPDATE " + Table.SCHEDULE_TIMES.getName() + " SET isActive = 0");
		for(int x = 0; x < dataArray.length(); x++) {
			JSONObject dataObj = dataArray.optJSONObject(x);
			MasterScheduleTime scheduleTime = new MasterScheduleTime();
			scheduleTime.ID = dataObj.optString("time_schedule_id");
			scheduleTime.timeIn = dataObj.optString("time_in");
			scheduleTime.timeOut = dataObj.optString("time_out");
			scheduleTime.scheduleShiftID = dataObj.optString("shift_type_id");
			if(dataObj.optInt("is_active") == 1) {
				Save.scheduleTime(db, scheduleTime);
			}
		}
		return db.endTransaction();
	}

	public static boolean serverTime(SQLiteAdapter db, OnErrorCallback errorCallback) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("api_key", Get.apiKey(db));
		String url = App.WEB_API + "get-server-time";
		String params = Http.jasonObjectToURL(new JSONObject(map));
		String response = Http.get(url, params, Settings.HTTP_REQUEST_TIMEOUT_RX);
		Utils.log("serverTime", url);
		Utils.log("serverTime", params);
		Utils.log("serverTime", response);
		JSONArray dataArray = Utils.getDataArray(params, response, errorCallback);
		if(dataArray == null) {
			return false;
		}
		db.beginTransaction();
		for(int x = 0; x < dataArray.length(); x++) {
			JSONObject dataObj = dataArray.optJSONObject(x);
			Save.timeSecurity(db, dataObj.optString("date_time"), dataObj.optLong("timestamp") * 1000);
		}
		return db.endTransaction();
	}

	public static boolean syncBatchID(SQLiteAdapter db, OnErrorCallback errorCallback) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("api_key", Get.apiKey(db));
		String url = App.WEB_API + "get-sync-batch-id";
		String params = Http.jasonObjectToURL(new JSONObject(map));
		String response = Http.get(url, params, Settings.HTTP_REQUEST_TIMEOUT_RX);
		Utils.log("syncBatchID", url);
		Utils.log("syncBatchID", params);
		Utils.log("syncBatchID", response);
		JSONArray dataArray = Utils.getDataArray(params, response, errorCallback);
		if(dataArray == null) {
			return false;
		}
		db.beginTransaction();
		for(int x = 0; x < dataArray.length(); x++) {
			JSONObject dataObj = dataArray.optJSONObject(x);
			Save.syncBatchID(db, dataObj.optString("sync_batch_id"));
		}
		return db.endTransaction();
	}
}