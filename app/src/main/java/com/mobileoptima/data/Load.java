package com.mobileoptima.data;

import com.android.library.Sqlite.SQLiteAdapter;
import com.mobileoptima.constants.Table;
import com.mobileoptima.models.Employee;
import com.mobileoptima.models.Expense;
import com.mobileoptima.models.Store;
import com.mobileoptima.models.Visit;

import net.sqlcipher.Cursor;

import java.util.ArrayList;

public class Load {
	public static ArrayList<Employee> employees(SQLiteAdapter db) {
		ArrayList<Employee> employees = new ArrayList<>();
		Employee employee;
		Cursor cursor = db.rawQuery("SELECT ID, firstName, lastName, employeeNumber, email, mobile, photoURL, teamID, isApprover FROM " + Table.EMPLOYEES.getName() + " WHERE isActive = 1");
		while(cursor.moveToNext()) {
			employee = new Employee();
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

	public static ArrayList<Store> stores(SQLiteAdapter db) {
		ArrayList<Store> stores = new ArrayList<>();
		Store store;
		Cursor cursor = db.rawQuery("SELECT name, address, contactNumber, class1ID, class2ID, class3ID, gpsLongitude, gpsLatitude, geoFenceRadius, ID, syncBatchID, webID, employeeID FROM " + Table.STORES.getName() + " WHERE isDelete = 0");
		while(cursor.moveToNext()) {
			store = new Store();
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
			store.employee.ID = cursor.getString(12);
		}
		cursor.close();
		return stores;
	}

	public static ArrayList<Visit> visits(SQLiteAdapter db, String dateStart, String dateEnd) {
		ArrayList<Visit> visits = new ArrayList<>();
		Visit visit;
		Cursor cursor = db.rawQuery("SELECT name, dateStart, dateEnd, deliveryFee, mappingCode, notes, status, storeID, checkInID, checkOutID, ID, dDate, dTime, syncBatchID, webID, employeeID, isFromWeb FROM " + Table.VISITS.getName() + " WHERE dateStart >= '" + dateStart + "' AND dateEnd <= '" + dateEnd + "' AND isDelete = 0");
		while(cursor.moveToNext()) {
			visit = new Visit();
			visit.name = cursor.getString(0);
			visit.dateStart = cursor.getString(1);
			visit.dateEnd = cursor.getString(2);
			visit.deliveryFee = cursor.getString(3);
			visit.mappingCode = cursor.getString(4);
			visit.notes = cursor.getString(5);
			visit.status = cursor.getString(6);
			visit.store.ID = cursor.getString(7);
			visit.checkIn.ID = cursor.getString(8);
			visit.checkOut.ID = cursor.getString(9);
			visit.ID = cursor.getString(10);
			visit.dDate = cursor.getString(11);
			visit.dTime = cursor.getString(12);
			visit.syncBatchID = cursor.getString(13);
			visit.webID = cursor.getString(14);
			visit.employee.ID = cursor.getString(15);
			visit.isFromWeb = cursor.getInt(16) == 1;
			visits.add(visit);
		}
		cursor.close();
		return visits;
	}

	public static ArrayList<Expense> expenses(SQLiteAdapter db, String date) {
		ArrayList<Expense> expenses = new ArrayList<>();
		Expense expense;
		Cursor cursor = db.rawQuery("SELECT name, origin, destination, notes, storeID, timeInID, expenseTypeID, amount, isReimbursable, isSubmit, ID, dDate, dTime, syncBatchID, webID, employeeID, gpsID FROM " + Table.EXPENSE.getName() + " WHERE isDelete = 0");
		while(cursor.moveToNext()) {
			expense = new Expense();
			expense.name = cursor.getString(0);
			expense.origin = cursor.getString(1);
			expense.destination = cursor.getString(2);
			expense.notes = cursor.getString(3);
			expense.store.ID = cursor.getString(4);
			expense.timeIn.ID = cursor.getString(5);
			expense.expenseType.ID = cursor.getString(6);
			expense.amount = cursor.getFloat(7);
			expense.isReimbursable = cursor.getInt(8) == 1;
			expense.isSubmit = cursor.getInt(9) == 1;
			expense.ID = cursor.getString(10);
			expense.dDate = cursor.getString(11);
			expense.dTime = cursor.getString(12);
			expense.syncBatchID = cursor.getString(13);
			expense.webID = cursor.getString(14);
			expense.employee.ID = cursor.getString(15);
			expense.gps.ID = cursor.getString(16);
			expenses.add(expense);
		}
		cursor.close();
		return expenses;
	}
}