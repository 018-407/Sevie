package com.mobileoptima.data;

import android.os.SystemClock;

import com.android.library.Sqlite.Condition;
import com.android.library.Sqlite.FieldValue;
import com.android.library.Sqlite.SQLiteAdapter;
import com.android.library.Utils.Time;
import com.mobileoptima.constants.Convention;
import com.mobileoptima.constants.Table;
import com.mobileoptima.models.AlertType;
import com.mobileoptima.models.BreakType;
import com.mobileoptima.models.Company;
import com.mobileoptima.models.Employee;
import com.mobileoptima.models.Expense;
import com.mobileoptima.models.ExpenseReport;
import com.mobileoptima.models.ExpenseType;
import com.mobileoptima.models.ExpenseTypeCategory;
import com.mobileoptima.models.OvertimeReason;
import com.mobileoptima.models.ScheduleTime;
import com.mobileoptima.models.Store;
import com.mobileoptima.models.Visit;

import java.util.ArrayList;

public class Save {
	public static boolean alertType(SQLiteAdapter db, AlertType alertType) {
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

	public static boolean breakType(SQLiteAdapter db, BreakType breakType) {
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

	public static boolean company(SQLiteAdapter db, Company company) {
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

	public static boolean employee(SQLiteAdapter db, Employee employee) {
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

	public static boolean expense(SQLiteAdapter db, Expense expense) {
		String table = Table.EXPENSE.getName();
		ArrayList<FieldValue> fieldValues = new ArrayList<>();
		fieldValues.add(new FieldValue("name", expense.name));
		fieldValues.add(new FieldValue("origin", expense.origin));
		fieldValues.add(new FieldValue("destination", expense.destination));
		fieldValues.add(new FieldValue("notes", expense.notes));
		fieldValues.add(new FieldValue("storeID", expense.store.ID));
		fieldValues.add(new FieldValue("timeInID", expense.timeIn.ID));
		fieldValues.add(new FieldValue("expenseTypeID", expense.expenseType.ID));
		fieldValues.add(new FieldValue("amount", expense.amount));
		fieldValues.add(new FieldValue("isReimbursable", expense.isReimbursable));
		fieldValues.add(new FieldValue("isSubmit", expense.isSubmit));
		fieldValues.add(new FieldValue("syncBatchID", expense.syncBatchID));
		fieldValues.add(new FieldValue("webID", expense.webID));
		fieldValues.add(new FieldValue("employeeID", expense.employee.ID));
		fieldValues.add(new FieldValue("gpsID", expense.gps.ID));
		fieldValues.add(new FieldValue("isSync", expense.isSync));
		fieldValues.add(new FieldValue("isUpdate", expense.isUpdate));
		fieldValues.add(new FieldValue("isWebUpdate", expense.isWebUpdate));
		fieldValues.add(new FieldValue("isDelete", expense.isDelete));
		fieldValues.add(new FieldValue("isWebDelete", expense.isWebDelete));
		if(db.getCount("SELECT ID FROM " + table + " WHERE ID = '" + expense.ID + "'") == 0) {
			fieldValues.add(new FieldValue("dDate", expense.dDate));
			fieldValues.add(new FieldValue("dTime", expense.dTime));
			return db.insert(table, fieldValues) > 0;
		}
		ArrayList<Condition> conditions = new ArrayList<>();
		conditions.add(new Condition(new FieldValue("ID", expense.ID)));
		return db.update(table, fieldValues, conditions);
	}

	public static boolean expenseReport(SQLiteAdapter db, ExpenseReport expenseReport) {
		return false;
	}

	public static boolean expenseType(SQLiteAdapter db, ExpenseType expenseType) {
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

	public static boolean expenseTypeCategory(SQLiteAdapter db, ExpenseTypeCategory expenseTypeCategory) {
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

	public static boolean isDelete(SQLiteAdapter db, String table, String ID) {
		ArrayList<FieldValue> fieldValues = new ArrayList<>();
		fieldValues.add(new FieldValue("isDelete", true));
		ArrayList<Condition> conditions = new ArrayList<>();
		conditions.add(new Condition(new FieldValue("ID", ID)));
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

	public static boolean overtimeReason(SQLiteAdapter db, OvertimeReason overtimeReason) {
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

	public static boolean scheduleTime(SQLiteAdapter db, ScheduleTime scheduleTime) {
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
		String table = Table.STORES.getName();
		ArrayList<FieldValue> fieldValues = new ArrayList<>();
		fieldValues.add(new FieldValue("name", store.name));
		fieldValues.add(new FieldValue("address", store.address));
		fieldValues.add(new FieldValue("contactNo", store.contactNumber));
		fieldValues.add(new FieldValue("class1ID", store.class1ID));
		fieldValues.add(new FieldValue("class2ID", store.class2ID));
		fieldValues.add(new FieldValue("class3ID", store.class3ID));
		fieldValues.add(new FieldValue("gpsLongitude", store.gpsLongitude));
		fieldValues.add(new FieldValue("gpsLatitude", store.gpsLatitude));
		fieldValues.add(new FieldValue("geoFenceRadius", store.geoFenceRadius));
		fieldValues.add(new FieldValue("isTag", store.isTag));
		fieldValues.add(new FieldValue("isFromVisit", store.isFromVisit));
		fieldValues.add(new FieldValue("syncBatchID", store.syncBatchID));
		fieldValues.add(new FieldValue("webID", store.webID));
		fieldValues.add(new FieldValue("employeeID", store.employee.ID));
		fieldValues.add(new FieldValue("isSync", store.isSync));
		fieldValues.add(new FieldValue("isUpdate", store.isUpdate));
		fieldValues.add(new FieldValue("isWebUpdate", store.isWebUpdate));
		fieldValues.add(new FieldValue("isDelete", store.isDelete));
		fieldValues.add(new FieldValue("isWebDelete", store.isWebDelete));
		if(db.getCount("SELECT ID FROM " + table + " WHERE webID = '" + store.webID + "'") == 0) {
			String timestamp = Time.getDeviceTimestamp();
			fieldValues.add(new FieldValue("dDate", Time.getDateFromTimestamp(timestamp)));
			fieldValues.add(new FieldValue("dTime", Time.getTimeFromTimestamp(timestamp)));
			fieldValues.add(new FieldValue("isFromWeb", store.isFromWeb));
			return db.insert(table, fieldValues) > 0;
		}
		ArrayList<Condition> conditions = new ArrayList<>();
		conditions.add(new Condition(new FieldValue("webID", store.webID)));
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

	public static boolean visit(SQLiteAdapter db, Visit visit) {
		String table = Table.VISITS.getName();
		ArrayList<FieldValue> fieldValues = new ArrayList<>();
		fieldValues.add(new FieldValue("name", visit.name));
		fieldValues.add(new FieldValue("dateStart", visit.dateStart));
		fieldValues.add(new FieldValue("dateEnd", visit.dateEnd));
		fieldValues.add(new FieldValue("deliveryFee", visit.deliveryFee));
		fieldValues.add(new FieldValue("mappingCode", visit.mappingCode));
		fieldValues.add(new FieldValue("notes", visit.notes));
		fieldValues.add(new FieldValue("status", visit.status));
		fieldValues.add(new FieldValue("storeID", visit.store.ID));
		fieldValues.add(new FieldValue("checkInID", visit.checkIn.ID));
		fieldValues.add(new FieldValue("checkOutID", visit.checkOut.ID));
		fieldValues.add(new FieldValue("syncBatchID", visit.syncBatchID));
		fieldValues.add(new FieldValue("webID", visit.webID));
		fieldValues.add(new FieldValue("employeeID", visit.employee.ID));
		fieldValues.add(new FieldValue("isSync", visit.isSync));
		fieldValues.add(new FieldValue("isUpdate", visit.isUpdate));
		fieldValues.add(new FieldValue("isWebUpdate", visit.isWebUpdate));
		fieldValues.add(new FieldValue("isDelete", visit.isDelete));
		fieldValues.add(new FieldValue("isWebDelete", visit.isWebDelete));
		if(db.getCount("SELECT ID FROM " + table + " WHERE webID = '" + visit.webID + "'") == 0) {
			fieldValues.add(new FieldValue("dDate", visit.dDate));
			fieldValues.add(new FieldValue("dTime", visit.dTime));
			fieldValues.add(new FieldValue("isFromWeb", visit.isFromWeb));
			return db.insert(table, fieldValues) > 0;
		}
		ArrayList<Condition> conditions = new ArrayList<>();
		conditions.add(new Condition(new FieldValue("webID", visit.webID)));
		return db.update(table, fieldValues, conditions);
	}
}