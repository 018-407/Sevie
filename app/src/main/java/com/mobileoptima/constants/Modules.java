package com.mobileoptima.constants;

public enum Modules {
	ATTENDANCE("attendance", "Attendance"),
	VISITS("itinerary", Convention.VISITS.getName()),
	FORMS("forms", "Forms"),
	EXPENSE("expense", "Expense"),
	INVENTORY("inventory", "Inventory");
	final String ID;
	String name;

	Modules(String ID, String name) {
		this.ID = ID;
		this.name = name;
	}

	public String getID() {
		return ID;
	}

	public String getName() {
		return name;
	}
}