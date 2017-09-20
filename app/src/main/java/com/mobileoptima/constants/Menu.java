package com.mobileoptima.constants;

import com.mobileoptima.tarkieattendance.R;

public enum Menu {
	TIME_IN_OUT(
			R.drawable.ic_menu_time_in,
			Convention.TIME_IN.getName()
	),
	BREAKS(
			R.string.fa_coffee,
			"Breaks"
	),
	STORES(
			R.drawable.ic_menu_stores,
			Convention.STORES.getName()
	),
	UPDATE_MASTER_FILE(
			R.string.fa_refresh,
			"Update Master File"
	),
	SEND_BACK_UP(
			R.string.fa_upload,
			"Send Back-up Data"
	),
	BACK_UP(
			R.string.fa_download,
			"Back-up Data"
	),
	ABOUT(
			R.string.fa_info_circle,
			"About"
	),
	LOGOUT(
			R.string.fa_power_off,
			"Log-out"
	);
	int icon;
	String name;

	Menu(int icon, String name) {
		this.icon = icon;
		this.name = name;
	}

	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}