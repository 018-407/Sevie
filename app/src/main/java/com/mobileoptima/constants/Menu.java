package com.mobileoptima.constants;

import com.mobileoptima.tarkieattendance.R;

public enum Menu {
	TIME_IN_OUT(
			R.drawable.ic_menu_time_in,
			Convention.TIME_IN.getName()
	),
	BREAKS(
			R.drawable.ic_menu_breaks,
			"Breaks"
	),
	STORES(
			R.drawable.ic_menu_stores,
			Convention.STORES.getName()
	),
	UPDATE_MASTER_FILE(
			R.drawable.ic_menu_update_master_file,
			"Update Master File"
	),
	SEND_BACK_UP(
			R.drawable.ic_menu_send_back_up,
			"Send Back-up Data"
	),
	BACK_UP(
			R.drawable.ic_menu_back_up,
			"Back-up Data"
	),
	ABOUT(
			R.drawable.ic_menu_about,
			"About"
	),
	LOGOUT(
			R.drawable.ic_menu_logout,
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