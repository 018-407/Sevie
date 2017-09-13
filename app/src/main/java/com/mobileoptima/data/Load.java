package com.mobileoptima.data;

import com.android.library.Sqlite.SQLiteAdapter;
import com.mobileoptima.constants.Table;
import com.mobileoptima.models.Employee;
import com.mobileoptima.models.Team;

import net.sqlcipher.Cursor;

import java.util.ArrayList;

public class Load {
	public ArrayList<Employee> employees(SQLiteAdapter db) {
		ArrayList<Employee> employees = new ArrayList<>();
		Cursor cursor = db.rawQuery("SELECT ID, firstName, lastName, employeeNumber, email, mobile, photoURL, teamID, isApprover FROM " + Table.EMPLOYEE.getName() + " WHERE isActive = 1");
		while(cursor.moveToNext()) {
			Employee employee = new Employee();
			employee.ID = cursor.getString(0);
			employee.firstName = cursor.getString(1);
			employee.lastName = cursor.getString(2);
			employee.employeeNumber = cursor.getString(3);
			employee.email = cursor.getString(4);
			employee.mobile = cursor.getString(5);
			employee.photoURL = cursor.getString(6);
			Team team = new Team();
			team.ID = cursor.getString(7);
			employee.team = team;
			employee.isApprover = cursor.getInt(8) == 1;
			employees.add(employee);
		}
		cursor.close();
		return employees;
	}
}