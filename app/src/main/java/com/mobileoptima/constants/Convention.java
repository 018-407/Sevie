package com.mobileoptima.constants;

public enum Convention {
	EMPLOYEES(
			"employees",
			"Employees"
	),
	STORES(
			"stores",
			"Stores"
	),
	TIME_IN(
			"startday",
			"Time In"
	),
	TIME_OUT(
			"endday",
			"Time Out"
	),
	VISITS(
			"visits",
			"Visit"
	),
	TEAMS(
			"teams",
			"Teams"
	);
	final String ID;
	String name;

	Convention(String ID, String name) {
		this.ID = ID;
		this.name = name;
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
}