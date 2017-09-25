package com.mobileoptima.data;

import android.os.SystemClock;

import com.android.library.Sqlite.Condition;
import com.android.library.Sqlite.FieldValue;
import com.android.library.Sqlite.SQLiteAdapter;
import com.android.library.Utils.Time;
import com.mobileoptima.constants.Convention;
import com.mobileoptima.constants.Table;
import com.mobileoptima.models.MasterAlertType;
import com.mobileoptima.models.MasterBreakType;
import com.mobileoptima.models.MasterCompany;
import com.mobileoptima.models.MasterEmployee;
import com.mobileoptima.models.MasterExpenseType;
import com.mobileoptima.models.MasterExpenseTypeCategory;
import com.mobileoptima.models.MasterOvertimeReason;
import com.mobileoptima.models.MasterScheduleTime;
import com.mobileoptima.models.Store;

import java.util.ArrayList;

public class Save {
	public static boolean alertType(SQLiteAdapter db, MasterAlertType alertType) {
		String table = Table.ALERT_TYPES.getName();
		ArrayList<FieldValue> fieldValues = new ArrayList<>();
		fieldValues.add(new FieldValue("ID", alertType.ID));
		fieldValues.add(new FieldValue("name", alertType.name));
		fieldValues.add(new FieldValue("isActive", true));
		if(db.getCount("SELECT ID FROM " + table + " WHERE ID = '" + alertType.ID + "'") == 0) {
			return db.insert(table, fieldValues) > 0;
		}
		ArrayList<Condition> conditions = new ArrayList<>();
		conditions.add(new Condition(new FieldValue("ID", alertType.ID)));
		return db.update(table, fieldValues, conditions);
	}

	public static boolean apiKey(SQLiteAdapter db, String apiKey, String deviceCode, String deviceID) {
		String table = Table.DEVICE.getName();
		ArrayList<FieldValue> fieldValues = new ArrayList<>();
		fieldValues.add(new FieldValue("apiKey", apiKey));
		fieldValues.add(new FieldValue("deviceCode", deviceCode));
		fieldValues.add(new FieldValue("deviceID", deviceID));
		if(db.getCount("SELECT ID FROM " + table + " WHERE ID = '1'") == 0) {
			return db.insert(table, fieldValues) > 0;
		}
		ArrayList<Condition> conditions = new ArrayList<>();
		conditions.add(new Condition(new FieldValue("ID", "1")));
		return db.update(table, fieldValues, conditions);
	}

	public static boolean breakType(SQLiteAdapter db, MasterBreakType breakType) {
		String table = Table.BREAK_TYPES.getName();
		ArrayList<FieldValue> fieldValues = new ArrayList<>();
		fieldValues.add(new FieldValue("ID", breakType.ID));
		fieldValues.add(new FieldValue("name", breakType.name));
		fieldValues.add(new FieldValue("duration", breakType.duration));
		fieldValues.add(new FieldValue("isActive", true));
		if(db.getCount("SELECT ID FROM " + table + " WHERE ID = '" + breakType.ID + "'") == 0) {
			return db.insert(table, fieldValues) > 0;
		}
		ArrayList<Condition> conditions = new ArrayList<>();
		conditions.add(new Condition(new FieldValue("ID", breakType.ID)));
		return db.update(table, fieldValues, conditions);
	}

	public static boolean company(SQLiteAdapter db, MasterCompany company) {
		String table = Table.COMPANY.getName();
		ArrayList<FieldValue> fieldValues = new ArrayList<>();
		fieldValues.add(new FieldValue("ID", company.ID));
		fieldValues.add(new FieldValue("name", company.name));
		fieldValues.add(new FieldValue("code", company.code));
		fieldValues.add(new FieldValue("address", company.address));
		fieldValues.add(new FieldValue("mobile", company.mobile));
		fieldValues.add(new FieldValue("landline", company.landline));
		fieldValues.add(new FieldValue("logoURL", company.logoURL));
		fieldValues.add(new FieldValue("expirationDate", company.expirationDate));
		if(db.getCount("SELECT ID FROM " + table + " WHERE ID = '" + company.ID + "'") == 0) {
			return db.insert(table, fieldValues) > 0;
		}
		ArrayList<Condition> conditions = new ArrayList<>();
		conditions.add(new Condition(new FieldValue("ID", company.ID)));
		return db.update(table, fieldValues, conditions);
	}

	public static boolean convention(SQLiteAdapter db, Convention convention, String name) {
		String table = Table.CONVENTION.getName();
		ArrayList<FieldValue> fieldValues = new ArrayList<>();
		fieldValues.add(new FieldValue("name", convention.getID()));
		fieldValues.add(new FieldValue("convention", name));
		if(db.getCount("SELECT ID FROM " + table + " WHERE name = '" + convention.getID() + "'") == 0) {
			return db.insert(table, fieldValues) > 0;
		}
		ArrayList<Condition> conditions = new ArrayList<>();
		conditions.add(new Condition(new FieldValue("name", convention.getID())));
		return db.update(table, fieldValues, conditions);
	}

	public static boolean employee(SQLiteAdapter db, MasterEmployee employee) {
		String table = Table.EMPLOYEES.getName();
		ArrayList<FieldValue> fieldValues = new ArrayList<>();
		fieldValues.add(new FieldValue("ID", employee.ID));
		fieldValues.add(new FieldValue("firstName", employee.firstName));
		fieldValues.add(new FieldValue("lastName", employee.lastName));
		fieldValues.add(new FieldValue("employeeNumber", employee.employeeNumber));
		fieldValues.add(new FieldValue("email", employee.email));
		fieldValues.add(new FieldValue("mobile", employee.mobile));
		fieldValues.add(new FieldValue("photoURL", employee.photoURL));
		fieldValues.add(new FieldValue("teamID", employee.teamID));
		fieldValues.add(new FieldValue("isApprover", employee.isApprover));
		fieldValues.add(new FieldValue("isActive", true));
		if(db.getCount("SELECT ID FROM " + table + " WHERE ID = '" + employee.ID + "'") == 0) {
			return db.insert(table, fieldValues) > 0;
		}
		ArrayList<Condition> conditions = new ArrayList<>();
		conditions.add(new Condition(new FieldValue("ID", employee.ID)));
		return db.update(table, fieldValues, conditions);
	}

	public static boolean expenseType(SQLiteAdapter db, MasterExpenseType expenseType) {
		String table = Table.EXPENSE_TYPES.getName();
		ArrayList<FieldValue> fieldValues = new ArrayList<>();
		fieldValues.add(new FieldValue("ID", expenseType.ID));
		fieldValues.add(new FieldValue("name", expenseType.name));
		fieldValues.add(new FieldValue("expenseTypeCategoryID", expenseType.expenseTypeCategory.ID));
		fieldValues.add(new FieldValue("isRequired", expenseType.isRequired));
		fieldValues.add(new FieldValue("isActive", true));
		if(db.getCount("SELECT ID FROM " + table + " WHERE ID = '" + expenseType.ID + "'") == 0) {
			return db.insert(table, fieldValues) > 0;
		}
		ArrayList<Condition> conditions = new ArrayList<>();
		conditions.add(new Condition(new FieldValue("ID", expenseType.ID)));
		return db.update(table, fieldValues, conditions);
	}

	public static boolean expenseTypeCategory(SQLiteAdapter db, MasterExpenseTypeCategory expenseTypeCategory) {
		String table = Table.EXPENSE_TYPE_CATEGORIES.getName();
		ArrayList<FieldValue> fieldValues = new ArrayList<>();
		fieldValues.add(new FieldValue("ID", expenseTypeCategory.ID));
		fieldValues.add(new FieldValue("name", expenseTypeCategory.name));
		fieldValues.add(new FieldValue("isActive", true));
		if(db.getCount("SELECT ID FROM " + table + " WHERE ID = '" + expenseTypeCategory.ID + "'") == 0) {
			return db.insert(table, fieldValues) > 0;
		}
		ArrayList<Condition> conditions = new ArrayList<>();
		conditions.add(new Condition(new FieldValue("ID", expenseTypeCategory.ID)));
		return db.update(table, fieldValues, conditions);
	}

	public static boolean login(SQLiteAdapter db, String employeeID) {
		ArrayList<FieldValue> fieldValues = new ArrayList<>();
		String timestamp = Time.getDeviceTimestamp();
		fieldValues.add(new FieldValue("dDate", Time.getDateFromTimestamp(timestamp)));
		fieldValues.add(new FieldValue("dTime", Time.getTimeFromTimestamp(timestamp)));
		fieldValues.add(new FieldValue("employeeID", employeeID));
		return db.insert(Table.ACCESS.getName(), fieldValues) > 0;
	}

	public static boolean logout(SQLiteAdapter db) {
		ArrayList<FieldValue> fieldValues = new ArrayList<>();
		fieldValues.add(new FieldValue("isLogOut", true));
		ArrayList<Condition> conditions = new ArrayList<>();
		conditions.add(new Condition(new FieldValue("employeeID", Get.employeeID(db))));
		conditions.add(new Condition(new FieldValue("isLogOut", false)));
		return db.update(Table.ACCESS.getName(), fieldValues, conditions);
	}

	public static boolean moduleEnabled(SQLiteAdapter db, String module) {
		String table = Table.MODULES.getName();
		ArrayList<FieldValue> fieldValues = new ArrayList<>();
		fieldValues.add(new FieldValue("name", module));
		fieldValues.add(new FieldValue("isEnabled", true));
		if(db.getCount("SELECT ID FROM " + table + " WHERE name = '" + module + "'") == 0) {
			return db.insert(table, fieldValues) > 0;
		}
		ArrayList<Condition> conditions = new ArrayList<>();
		conditions.add(new Condition(new FieldValue("name", module)));
		return db.update(table, fieldValues, conditions);
	}

	public static boolean overtimeReason(SQLiteAdapter db, MasterOvertimeReason overtimeReason) {
		String table = Table.OVERTIME_REASONS.getName();
		ArrayList<FieldValue> fieldValues = new ArrayList<>();
		fieldValues.add(new FieldValue("ID", overtimeReason.ID));
		fieldValues.add(new FieldValue("name", overtimeReason.name));
		fieldValues.add(new FieldValue("isActive", true));
		if(db.getCount("SELECT ID FROM " + table + " WHERE ID = '" + overtimeReason.ID + "'") == 0) {
			return db.insert(table, fieldValues) > 0;
		}
		ArrayList<Condition> conditions = new ArrayList<>();
		conditions.add(new Condition(new FieldValue("ID", overtimeReason.ID)));
		return db.update(table, fieldValues, conditions);
	}

	public static boolean scheduleTime(SQLiteAdapter db, MasterScheduleTime scheduleTime) {
		String table = Table.SCHEDULE_TIMES.getName();
		ArrayList<FieldValue> fieldValues = new ArrayList<>();
		fieldValues.add(new FieldValue("ID", scheduleTime.ID));
		fieldValues.add(new FieldValue("timeIn", scheduleTime.timeIn));
		fieldValues.add(new FieldValue("timeOut", scheduleTime.timeOut));
		fieldValues.add(new FieldValue("scheduleShiftID", scheduleTime.scheduleShiftID));
		fieldValues.add(new FieldValue("isActive", true));
		if(db.getCount("SELECT ID FROM " + table + " WHERE ID = '" + scheduleTime.ID + "'") == 0) {
			return db.insert(table, fieldValues) > 0;
		}
		ArrayList<Condition> conditions = new ArrayList<>();
		conditions.add(new Condition(new FieldValue("ID", scheduleTime.ID)));
		return db.update(table, fieldValues, conditions);
	}

	public static boolean store(SQLiteAdapter db, Store store) {
		String table = Table.BREAK_TYPES.getName();
		ArrayList<FieldValue> fieldValues = new ArrayList<>();
		fieldValues.add(new FieldValue("ID", store.ID));
		fieldValues.add(new FieldValue("name", breakType.name));
		fieldValues.add(new FieldValue("duration", breakType.duration));
		fieldValues.add(new FieldValue("isActive", true));
		fieldValues.add(new FieldValue("ID", store.ID));
		query.add(new FieldValue("name", name));
		query.add(new FieldValue("empID", empID));
		query.add(new FieldValue("address", address));
		query.add(new FieldValue("webStoreID", webStoreID));
		query.add(new FieldValue("contactNo", dataObj.getString("contact_number")));
		query.add(new FieldValue("gpsLongitude", dataObj.getDouble("longitude")));
		query.add(new FieldValue("gpsLatitude", dataObj.getDouble("latitude")));
		query.add(new FieldValue("radius", dataObj.getInt("geo_fence_radius")));
		query.add(new FieldValue("isActive", dataObj.getInt("is_active")));
		query.add(new FieldValue("isWebUpdate", true));
		query.add(new FieldValue("isSync", true));
		query.add(new FieldValue("isTag", true));
		if(db.getCount("SELECT ID FROM " + table + " WHERE ID = '" + breakType.ID + "'") == 0) {
			return db.insert(table, fieldValues) > 0;
		}
		ArrayList<Condition> conditions = new ArrayList<>();
		conditions.add(new Condition(new FieldValue("ID", breakType.ID)));
		return db.update(table, fieldValues, conditions);
	}

	public static boolean syncBatchID(SQLiteAdapter db, String syncBatchID) {
		String table = Table.SYNC_BATCH.getName();
		ArrayList<FieldValue> fieldValues = new ArrayList<>();
		String timestamp = Time.getDeviceTimestamp();
		fieldValues.add(new FieldValue("dDate", Time.getDateFromTimestamp(timestamp)));
		fieldValues.add(new FieldValue("dTime", Time.getTimeFromTimestamp(timestamp)));
		fieldValues.add(new FieldValue("syncBatchID", syncBatchID));
		if(db.getCount("SELECT ID FROM " + table + " WHERE ID = '1'") == 0) {
			return db.insert(table, fieldValues) > 0;
		}
		ArrayList<Condition> conditions = new ArrayList<>();
		conditions.add(new Condition(new FieldValue("ID", "1")));
		return db.update(table, fieldValues, conditions);
	}

	public static boolean timeSecurity(SQLiteAdapter db, String dateTime, long timestamp) {
		String table = Table.TIME_SECURITY.getName();
		ArrayList<FieldValue> fieldValues = new ArrayList<>();
		fieldValues.add(new FieldValue("timestamp", timestamp));
		fieldValues.add(new FieldValue("timeZoneID", Time.getTimeZoneID(dateTime, timestamp)));
		fieldValues.add(new FieldValue("elapsedTime", SystemClock.elapsedRealtime()));
		if(db.getCount("SELECT ID FROM " + table + " WHERE ID = '1'") == 0) {
			return db.insert(table, fieldValues) > 0;
		}
		ArrayList<Condition> conditions = new ArrayList<>();
		conditions.add(new Condition(new FieldValue("ID", "1")));
		return db.update(table, fieldValues, conditions);
	}
}