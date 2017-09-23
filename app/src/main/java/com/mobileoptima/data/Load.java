package com.mobileoptima.data;

import com.android.library.Sqlite.SQLiteAdapter;
import com.mobileoptima.constants.Table;
import com.mobileoptima.models.MasterEmployee;
import com.mobileoptima.models.Visit;

import net.sqlcipher.Cursor;

import java.util.ArrayList;

public class Load {
	public static ArrayList<MasterEmployee> employees(SQLiteAdapter db) {
		ArrayList<MasterEmployee> employees = new ArrayList<>();
		MasterEmployee employee;
		Cursor cursor = db.rawQuery("SELECT ID, firstName, lastName, employeeNumber, email, mobile, photoURL, teamID, isApprover FROM " + Table.EMPLOYEES.getName() + " WHERE isActive = 1");
		while(cursor.moveToNext()) {
			employee = new MasterEmployee();
			employee.ID = cursor.getString(0);
			employee.firstName = cursor.getString(1);
			employee.lastName = cursor.getString(2);
			employee.employeeNumber = cursor.getString(3);
			employee.email = cursor.getString(4);
			employee.mobile = cursor.getString(5);
			employee.photoURL = cursor.getString(6);
			employee.teamID = cursor.getString(7);
			employee.isApprover = cursor.getInt(8) == 1;
			employees.add(employee);
		}
		cursor.close();
		return employees;
	}

	public static ArrayList<Visit> visits(SQLiteAdapter db) {
		ArrayList<Visit> visits = new ArrayList<>();
		Visit visit;
		Cursor cursor = db.rawQuery("");
		while(cursor.moveToNext()) {
			visit = new Visit();
			visit.ID = cursor.getString(0);
			visit.name = cursor.getString(1);
			visit.store = Get.store(db, cursor.getString(2));
			visits.add(visit);
		}
		cursor.close();
		visit = new Visit();
		visit.ID = "1";
		visit.name = "Task 1";
		visit.store = Get.store(db, "1");
		visits.add(visit);
		visit = new Visit();
		visit.ID = "2";
		visit.name = "Task 2";
		visit.store = Get.store(db, "2");
		visits.add(visit);
		return visits;
	}
}