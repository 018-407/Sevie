package com.mobileoptima.constants;

public enum Action {
	AUTHORIZE_DEVICE(
			"Authorize Device",
			"Authorizing Device"
	),
	LOGIN(
			"Login",
			"Validating Account"
	),
	VALIDATE_TIME(
			"Validate Time",
			"Validating Time"
	),
	UPDATE_MASTER_FILE(
			"Update Master File",
			"Updating Master File"
	),
	SEND_BACK_UP(
			"Send Back-up",
			"Sending Back-up"
	),
	SYNC_DATA(
			"Sync Data",
			"Syncing Data"
	);
	final String name, title;

	Action(String name, String title) {
		this.name = name;
		this.title = title;
	}

	public String getName() {
		return name;
	}

	public String getTitle() {
		return title;
	}
}