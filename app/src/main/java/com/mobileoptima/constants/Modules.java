package com.mobileoptima.constants;

import android.support.v4.app.Fragment;

import com.mobileoptima.tarkieattendance.R;
import com.mobileoptima.tarkieattendance.attendance.AttendanceFragment;
import com.mobileoptima.tarkieattendance.expense.ExpenseFragment;
import com.mobileoptima.tarkieattendance.forms.FormsFragment;
import com.mobileoptima.tarkieattendance.inventory.InventoryFragment;
import com.mobileoptima.tarkieattendance.visits.VisitsFragment;

public enum Modules {
	VISITS("itinerary", Convention.VISITS.getName(), R.drawable.ic_module_visits, new VisitsFragment(), false),
	EXPENSE("expense", "Expense", R.drawable.ic_module_expense, new ExpenseFragment(), false),
	INVENTORY("inventory", "Inventory", R.drawable.ic_module_inventory, new InventoryFragment(), false),
	FORMS("forms", "Entries", R.string.fa_folder_open, new FormsFragment(), false),
	ATTENDANCE("attendance", "History", R.drawable.ic_module_attendance, new AttendanceFragment(), true);
	final Fragment fragment;
	final String ID;
	final int icon;
	String name;
	boolean isEnabled;

	Modules(String ID, String name, int icon, Fragment fragment, boolean isEnabled) {
		this.ID = ID;
		this.name = name;
		this.icon = icon;
		this.fragment = fragment;
		this.isEnabled = isEnabled;
	}

	public String getID() {
		return ID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIcon() {
		return icon;
	}

	public Fragment getFragment() {
		return fragment;
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
}