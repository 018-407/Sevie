package com.mobileoptima.constants;

public enum Table {
	DEVICE("device_tb", ", timestamp TEXT" +
			", apiKey TEXT" +
			", deviceCode TEXT" +
			", deviceID TEXT"
	),
	ACCESS("access_tb", ", timestamp TEXT" +
			", employeeID TEXT" +
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
	SYNC_BATCH("sync_batch_tb", ", timestamp TEXT" +
			", syncBatchID TEXT"
	),
	EMPLOYEE("employee_tb", ", firstName TEXT" +
			", lastName TEXT" +
			", employeeNumber TEXT" +
			", email TEXT" +
			", mobile TEXT" +
			", photoURL TEXT" +
			", teamID INTEGER" +
			", isApprover INTEGER" +
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