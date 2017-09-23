package com.mobileoptima.data;

import android.os.SystemClock;

import com.android.library.Sqlite.SQLiteAdapter;
import com.android.library.Utils.Time;
import com.mobileoptima.constants.Modules;
import com.mobileoptima.constants.Settings;
import com.mobileoptima.constants.Table;
import com.mobileoptima.models.MasterCompany;
import com.mobileoptima.models.MasterEmployee;
import com.mobileoptima.models.Store;

import net.sqlcipher.Cursor;

public class Get {
	public static String apiKey(SQLiteAdapter db) {
		return db.getString("SELECT apiKey FROM " + Table.DEVICE.getName() + " WHERE ID = 1");
	}

	public static MasterCompany company(SQLiteAdapter db) {
		MasterCompany company = null;
		Cursor cursor = db.rawQuery("SELECT ID, name, deviceCode, address, mobile, landline, logoURL, expirationDate FROM " + Table.COMPANY.getName() + " LIMIT 1");
		while(cursor.moveToNext()) {
			company = new MasterCompany();
			company.ID = cursor.getString(0);
			company.name = cursor.getString(1);
			company.code = cursor.getString(2);
			company.address = cursor.getString(3);
			company.mobile = cursor.getString(4);
			company.landline = cursor.getString(5);
			company.logoURL = cursor.getString(6);
			company.expirationDate = cursor.getString(7);
		}
		cursor.close();
		return company;
	}

	public static String companyLogo(SQLiteAdapter db) {
		return db.getString("SELECT logoURL FROM " + Table.COMPANY.getName() + " LIMIT 1");
	}

	public static String companyName(SQLiteAdapter db) {
		return db.getString("SELECT name FROM " + Table.COMPANY.getName() + " LIMIT 1");
	}

	public static String conventionName(SQLiteAdapter db, String conventionID) {
		return db.getString("SELECT convention FROM " + Table.CONVENTION.getName() + " WHERE name = '" + conventionID + "'");
	}

	public static MasterEmployee employee(SQLiteAdapter db, String employeeID) {
		MasterEmployee employee = null;
		Cursor cursor = db.rawQuery("SELECT firstName, lastName, employeeNumber, email, mobile, photoURL, teamID, isApprover FROM " + Table.EMPLOYEES.getName() + " WHERE ID = '" + employeeID + "'");
		while(cursor.moveToNext()) {
			employee = new MasterEmployee();
			employee.ID = employeeID;
			employee.firstName = cursor.getString(0);
			employee.lastName = cursor.getString(1);
			employee.employeeNumber = cursor.getString(2);
			employee.email = cursor.getString(3);
			employee.mobile = cursor.getString(4);
			employee.photoURL = cursor.getString(5);
			employee.teamID = cursor.getString(6);
			employee.isApprover = cursor.getInt(7) == 1;
		}
		cursor.close();
		return employee;
	}

	public static String employeeID(SQLiteAdapter db) {
		return db.getString("SELECT employeeID FROM " + Table.ACCESS.getName() + " WHERE isLogOut = 0");
	}

	public static String employeeName(SQLiteAdapter db, String employeeID) {
		return employeeName(db, employeeID, Settings.LAST_NAME_FIRST);
	}

	public static String employeeName(SQLiteAdapter db, String employeeID, boolean isLastNameFirst) {
		return db.getString("SELECT " + (isLastNameFirst ? "lastName || ', ' || firstName" : "firstName || ' ' || lastName") + " FROM " + Table.EMPLOYEES.getName() + " WHERE ID = '" + employeeID + "'");
	}

	public static String employeeNumber(SQLiteAdapter db, String employeeID) {
		return db.getString("SELECT employeeNumber FROM " + Table.EMPLOYEES.getName() + " WHERE ID = '" + employeeID + "'");
	}

	public static String employeePhoto(SQLiteAdapter db, String employeeID) {
		return db.getString("SELECT photoURL FROM " + Table.EMPLOYEES.getName() + " WHERE ID = '" + employeeID + "'");
	}

	public static boolean isModuleEnabled(SQLiteAdapter db, String moduleID) {
		return moduleID.equals(Modules.ATTENDANCE.getID()) || db.getInt("SELECT isEnabled FROM " + Table.MODULES.getName() + " WHERE name = '" + moduleID + "'") == 1;
	}

	public static long serverTimestamp(SQLiteAdapter db) {
		long serverTimestamp = 0;
		Cursor cursor = db.rawQuery("SELECT timestamp, timeZoneID, elapsedTime FROM " + Table.TIME_SECURITY.getName() + " WHERE ID = 1");
		while(cursor.moveToNext()) {
			String timeZoneID = cursor.getString(1);
			if(timeZoneID != null) {
				serverTimestamp = Time.getTimestampFromTimeZoneID(cursor.getLong(0) + SystemClock.elapsedRealtime() - cursor.getLong(2), timeZoneID);
			}
		}
		cursor.close();
		return serverTimestamp;
	}

	public static String timeZoneID(SQLiteAdapter db) {
		return db.getString("SELECT timeZoneID FROM " + Table.TIME_SECURITY.getName() + " WHERE ID = '1'");
	}

	public static Store store(SQLiteAdapter db, String storeID) {
		Store store = null;
		Cursor cursor = db.rawQuery("");
		while(cursor.moveToNext()) {
			store = new Store();
			store.ID = "";
			store.name = "";
			store.address = "";
		}
		cursor.close();
		store = new Store();
		switch(storeID) {
			case "1":
				store.ID = "1";
				break;
			case "2":
				store.ID = "2";
				break;
			case "3":
				store.ID = "3";
				break;
		}
		store.name = "Store " + store.ID;
		store.address = "Address " + store.ID;
		return store;
		//TODO
	}

	public static String storeID(SQLiteAdapter db) {
//		return db.getString("SELECT storeID FROM " + Table.ACCESS.getName() + " WHERE isLogOut = 0");
		return null;
		//TODO
	}

	public static String syncBatchID(SQLiteAdapter db) {
		return db.getString("SELECT syncBatchID FROM " + Table.SYNC_BATCH.getName() + " WHERE ID = 1");
	}

	public static String timeInID(SQLiteAdapter db) {
//		return db.getString("SELECT ID FROM " + Table.ACCESS.getName() + " WHERE ID = '0'");
		return null;
		//TODO
	}

	public static String timeOutID(SQLiteAdapter db, String timeInID) {
//		return db.getString("SELECT ID FROM " + Table.ACCESS.getName() + " WHERE ID = '" + timeInID + "'");
		return null;
		//TODO
	}
}