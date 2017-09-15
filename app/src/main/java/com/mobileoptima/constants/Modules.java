package com.mobileoptima.constants;

import android.support.v4.app.Fragment;

import com.mobileoptima.tarkieattendance.R;
import com.mobileoptima.tarkieattendance.attendance.AttendanceFragment;
import com.mobileoptima.tarkieattendance.expense.ExpenseFragment;
import com.mobileoptima.tarkieattendance.forms.FormsFragment;
import com.mobileoptima.tarkieattendance.inventory.InventoryFragment;
import com.mobileoptima.tarkieattendance.visits.VisitsFragment;

public enum Modules {
	VISITS("itinerary", Convention.VISITS.getName(), R.drawable.ic_module_visits, new VisitsFragment()),
	EXPENSE("expense", "Expense", R.drawable.ic_module_expense, new ExpenseFragment()),
	INVENTORY("inventory", "Inventory", R.drawable.ic_module_inventory, new InventoryFragment()),
	FORMS("forms", "Entries", R.drawable.ic_module_forms, new FormsFragment()),
	ATTENDANCE("attendance", "History", R.drawable.ic_module_attendance, new AttendanceFragment());
	final Fragment fragment;
	final String ID;
	final int icon;
	String name;

	Modules(String ID, String name, int icon, Fragment fragment) {
		this.ID = ID;
		this.name = name;
		this.icon = icon;
		this.fragment = fragment;
	}

	public String getID() {
		return ID;
	}

	public String getName() {
		return name;
	}

	public int getIcon() {
		return icon;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Fragment getFragment() {
		return fragment;
	}
}