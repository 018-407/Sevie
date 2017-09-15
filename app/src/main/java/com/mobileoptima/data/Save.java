package com.mobileoptima.data;

import com.android.library.Sqlite.Condition;
import com.android.library.Sqlite.FieldValue;
import com.android.library.Sqlite.SQLiteAdapter;
import com.android.library.Utils.Time;
import com.mobileoptima.constants.Convention;
import com.mobileoptima.constants.Table;
import com.mobileoptima.models.Company;
import com.mobileoptima.models.Employee;

import java.util.ArrayList;

public class Save {
	public static boolean apiKey(SQLiteAdapter db, String apiKey, String deviceCode, String deviceID) {
		String table = Table.DEVICE.getName();
		ArrayList<FieldValue> fieldValues = new ArrayList<>();
		fieldValues.add(new FieldValue("timestamp", Time.getTimestamp()));
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

	public static boolean login(SQLiteAdapter db, String employeeID) {
		ArrayList<FieldValue> fieldValues = new ArrayList<>();
		fieldValues.add(new FieldValue("timestamp", Time.getTimestamp()));
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

	public static boolean employee(SQLiteAdapter db, Employee employee) {
		String table = Table.EMPLOYEE.getName();
		ArrayList<FieldValue> fieldValues = new ArrayList<>();
		fieldValues.add(new FieldValue("ID", employee.ID));
		fieldValues.add(new FieldValue("firstName", employee.firstName));
		fieldValues.add(new FieldValue("lastName", employee.lastName));
		fieldValues.add(new FieldValue("employeeNumber", employee.employeeNumber));
		fieldValues.add(new FieldValue("email", employee.email));
		fieldValues.add(new FieldValue("mobile", employee.mobile));
		fieldValues.add(new FieldValue("photoURL", employee.photoURL));
		fieldValues.add(new FieldValue("teamID", employee.team.ID));
		fieldValues.add(new FieldValue("isApprover", employee.isApprover));
		fieldValues.add(new FieldValue("isActive", true));
		if(db.getCount("SELECT ID FROM " + table + " WHERE ID = '" + employee.ID + "'") == 0) {
			return db.insert(table, fieldValues) > 0;
		}
		ArrayList<Condition> conditions = new ArrayList<>();
		conditions.add(new Condition(new FieldValue("ID", employee.ID)));
		return db.update(table, fieldValues, conditions);
	}
}