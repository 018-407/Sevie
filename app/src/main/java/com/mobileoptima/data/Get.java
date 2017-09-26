package com.mobileoptima.data;

import android.os.SystemClock;

import com.android.library.Sqlite.SQLiteAdapter;
import com.android.library.Utils.Time;
import com.mobileoptima.constants.Settings;
import com.mobileoptima.constants.Table;
import com.mobileoptima.models.CheckIn;
import com.mobileoptima.models.CheckOut;
import com.mobileoptima.models.Company;
import com.mobileoptima.models.Employee;
import com.mobileoptima.models.Store;

import net.sqlcipher.Cursor;

public class Get {
	public static String apiKey(SQLiteAdapter db) {
		return db.getString("SELECT apiKey FROM " + Table.DEVICE.getName() + " WHERE ID = 1");
	}

	public static CheckIn checkIn(SQLiteAdapter db, String checkInID) {
		CheckIn checkIn = new CheckIn();
		return checkIn;
	}

	public static CheckOut checkOut(SQLiteAdapter db, String checkOutID) {
		CheckOut checkOut = new CheckOut();
		return checkOut;
	}

	public static Company company(SQLiteAdapter db) {
		Company company = null;
		Cursor cursor = db.rawQuery("SELECT ID, name, deviceCode, address, mobile, landline, logoURL, expirationDate FROM " + Table.COMPANY.getName() + " LIMIT 1");
		while(cursor.moveToNext()) {
			company = new Company();
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

	public static Employee employee(SQLiteAdapter db, String employeeID) {
		Employee employee = new Employee();
		Cursor cursor = db.rawQuery("SELECT firstName, lastName, employeeNumber, email, mobile, photoURL, teamID, isApprover FROM " + Table.EMPLOYEES.getName() + " WHERE ID = '" + employeeID + "'");
		while(cursor.moveToNext()) {
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
		return db.getInt("SELECT isEnabled FROM " + Table.MODULES.getName() + " WHERE name = '" + moduleID + "'") == 1;
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

	public static Store store(SQLiteAdapter db, String storeID) {
		Store store = new Store();
		Cursor cursor = db.rawQuery("SELECT name, address, contactNumber, class1ID, class2ID, class3ID, gpsLongitude, gpsLatitude, geoFenceRadius, ID, syncBatchID, webID, employeeID FROM " + Table.STORES.getName() + " WHERE ID = '" + storeID + "'");
		while(cursor.moveToNext()) {
			store.name = cursor.getString(0);
			store.address = cursor.getString(1);
			store.contactNumber = cursor.getString(2);
			store.class1ID = cursor.getString(3);
			store.class2ID = cursor.getString(4);
			store.class3ID = cursor.getString(5);
			store.gpsLongitude = cursor.getDouble(6);
			store.gpsLatitude = cursor.getDouble(7);
			store.geoFenceRadius = cursor.getInt(8);
			store.ID = cursor.getString(9);
			store.syncBatchID = cursor.getString(10);
			store.webID = cursor.getString(11);
			store.employee = employee(db, cursor.getString(12));
		}
		cursor.close();
		return store;
	}

	public static String storeID(SQLiteAdapter db) {
//		return db.getString("SELECT storeID FROM " + Table.ACCESS.getName() + " WHERE isLogOut = 0");
		return null;
		//TODO
	}

	public static String storeIDFromWebID(SQLiteAdapter db, String webID) {
		return db.getString("SELECT ID FROM " + Table.STORES.getName() + " WHERE webID = '" + webID + "'");
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

	public static int visitTodayCount(SQLiteAdapter db) {
		String date = Time.getDateFromTimestamp(Time.getDeviceTimestamp());
		return db.getInt("SELECT COUNT(ID) FROM " + Table.VISITS.getName() + " WHERE dateStart >= '" + date + "' AND dateEnd <= '" + date + "' AND isFromWeb = 0");
	}
}