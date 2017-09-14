package com.mobileoptima.data;

import com.android.library.Sqlite.SQLiteAdapter;
import com.mobileoptima.constants.Modules;
import com.mobileoptima.constants.Settings;
import com.mobileoptima.constants.Table;
import com.mobileoptima.models.Company;
import com.mobileoptima.models.Employee;
import com.mobileoptima.models.Team;

import net.sqlcipher.Cursor;

public class Get {
	public static String apiKey(SQLiteAdapter db) {
		return db.getString("SELECT apiKey FROM " + Table.DEVICE.getName() + " WHERE ID = 1");
	}

	public static String employeeID(SQLiteAdapter db) {
		return db.getString("SELECT employeeID FROM " + Table.ACCESS.getName() + " WHERE isLogOut = 0");
	}

	public static Company company(SQLiteAdapter db) {
		Company company = new Company();
		Cursor cursor = db.rawQuery("SELECT ID, name, deviceCode, address, mobile, landline, logoURL, expirationDate FROM " + Table.COMPANY.getName() + " LIMIT 1");
		while(cursor.moveToNext()) {
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

	public static String companyName(SQLiteAdapter db) {
		return db.getString("SELECT name FROM " + Table.COMPANY.getName() + " LIMIT 1");
	}

	public static String companyLogo(SQLiteAdapter db) {
		return db.getString("SELECT logoURL FROM " + Table.COMPANY.getName() + " LIMIT 1");
	}

	public static Employee employee(SQLiteAdapter db, String employeeID) {
		Employee employee = new Employee();
		Cursor cursor = db.rawQuery("SELECT firstName, lastName, employeeNumber, email, mobile, photoURL, teamID, isApprover FROM " + Table.EMPLOYEE.getName() + " WHERE ID = '" + employeeID + "'");
		while(cursor.moveToNext()) {
			employee.ID = employeeID;
			employee.firstName = cursor.getString(0);
			employee.lastName = cursor.getString(1);
			employee.employeeNumber = cursor.getString(2);
			employee.email = cursor.getString(3);
			employee.mobile = cursor.getString(4);
			employee.photoURL = cursor.getString(5);
			Team team = new Team();
			team.ID = cursor.getString(6);
			employee.team = team;
			employee.isApprover = cursor.getInt(7) == 1;
		}
		cursor.close();
		return employee;
	}

	public static String employeeName(SQLiteAdapter db, String employeeID) {
		return employeeName(db, employeeID, Settings.LAST_NAME_FIRST);
	}

	public static String employeeName(SQLiteAdapter db, String employeeID, boolean isLastNameFirst) {
		return db.getString("SELECT " + (isLastNameFirst ? "lastName || ', ' || firstName" : "firstName || ' ' || lastName") + " FROM " + Table.EMPLOYEE.getName() + " WHERE ID = '" + employeeID + "'");
	}

	public static String employeeNumber(SQLiteAdapter db, String employeeID) {
		return db.getString("SELECT employeeNumber FROM " + Table.EMPLOYEE.getName() + " WHERE ID = '" + employeeID + "'");
	}

	public static String employeePhoto(SQLiteAdapter db, String employeeID) {
		return db.getString("SELECT photoURL FROM " + Table.EMPLOYEE.getName() + " WHERE ID = '" + employeeID + "'");
	}

	public static String conventionName(SQLiteAdapter db, String conventionID) {
		String name = null;
		return name;
	}

	public static boolean isModuleEnabled(SQLiteAdapter db, String moduleID) {
		return moduleID.equals(Modules.ATTENDANCE.getID()) || db.getInt("SELECT isEnabled FROM " + Table.MODULES.getName() + " WHERE name = '" + moduleID + "'") == 1;
	}

	public static String timeInID(SQLiteAdapter db) {
		String timeInID = null;
		return timeInID;
	}

	public static String timeOutID(SQLiteAdapter db, String timeInID) {
		String timeOutID = null;
		return timeOutID;
	}
}