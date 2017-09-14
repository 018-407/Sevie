package com.mobileoptima.constants;

import android.support.v4.app.Fragment;

import com.mobileoptima.tarkieattendance.attendance.AttendanceFragment;
import com.mobileoptima.tarkieattendance.expense.ExpenseFragment;
import com.mobileoptima.tarkieattendance.forms.FormsFragment;
import com.mobileoptima.tarkieattendance.inventory.InventoryFragment;
import com.mobileoptima.tarkieattendance.visits.VisitsFragment;

public enum Modules {
	VISITS("itinerary", Convention.VISITS.getName(), new VisitsFragment()),
	EXPENSE("expense", "Expense", new ExpenseFragment()),
	INVENTORY("inventory", "Inventory", new InventoryFragment()),
	FORMS("forms", "Forms", new FormsFragment()),
	ATTENDANCE("attendance", "History", new AttendanceFragment());
	final Fragment fragment;
	final String ID, name;

	Modules(String ID, String name, Fragment fragment) {
		this.ID = ID;
		this.name = name;
		this.fragment = fragment;
	}

	public String getID() {
		return ID;
	}

	public String getName() {
		return name;
	}

	public Fragment getFragment() {
		return fragment;
	}
}