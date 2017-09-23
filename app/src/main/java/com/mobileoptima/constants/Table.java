package com.mobileoptima.constants;

public enum Table {
	DEVICE("device_tb", ", apiKey TEXT" +
			", deviceCode TEXT" +
			", deviceID TEXT"
	),
	ACCESS("access_tb", ", dDate TEXT" +
			", dTime TEXT" +
			", employeeID INTEGER" +
			", isLogOut INTEGER DEFAULT 0"
	),
	COMPANY("company_tb", ", name TEXT" +
			", code TEXT" +
			", address TEXT" +
			", mobile TEXT" +
			", landline TEXT" +
			", logoURL TEXT" +
			", expirationDate TEXT"
	),
	MODULES("modules_tb", ", name TEXT" +
			", isEnabled TEXT"
	),
	CONVENTION("convention_tb", ", name TEXT" +
			", convention TEXT"
	),
	EMPLOYEES("employees_tb", ", firstName TEXT" +
			", lastName TEXT" +
			", employeeNumber TEXT" +
			", email TEXT" +
			", mobile TEXT" +
			", photoURL TEXT" +
			", teamID INTEGER" +
			", isApprover INTEGER" +
			", isActive INTEGER DEFAULT 1"
	),
	TIME_SECURITY("time_security_tb", ", timestamp TEXT" +
			", timeZoneID TEXT" +
			", elapsedTime TEXT"
	),
	SYNC_BATCH("sync_batch_tb", ", dDate TEXT" +
			", dTime TEXT" +
			", syncBatchID TEXT"
	),
	ALERT_TYPES("alert_types_tb", ", name TEXT" +
			", isActive INTEGER DEFAULT 1"
	),
	BREAK_TYPES("break_types_tb", ", name TEXT" +
			", duration INTEGER" +
			", isActive INTEGER DEFAULT 1"
	),
	EXPENSE_TYPE_CATEGORIES("expense_type_categories_tb", ", name TEXT" +
			", isActive INTEGER DEFAULT 1"
	),
	EXPENSE_TYPES("expense_types_tb", ", name TEXT" +
			", expenseTypeCategoryID INTEGER" +
			", isRequired INTEGER DEFAULT 0" +
			", isActive INTEGER DEFAULT 1"
	),
	SCHEDULE_TIMES("schedule_times_tb", ", timeIn TEXT" +
			", timeOut TEXT" +
			", scheduleShiftID INTEGER" +
			", isActive INTEGER DEFAULT 1"
	),
	OVERTIME_REASONS("overtime_reasons_tb", ", name TEXT" +
			", isActive INTEGER DEFAULT 1"
	);
	final String name, fields;

	Table(String name, String fields) {
		this.name = name;
		this.fields = fields;
	}

	public String getName() {
		return name;
	}

	public String getFields() {
		return fields;
	}
}