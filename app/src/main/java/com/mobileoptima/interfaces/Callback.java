package com.mobileoptima.interfaces;

import com.android.library.Sqlite.SQLiteAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

public class Callback {
	public interface OnInitializeCallback {
		void onInitialize(SQLiteAdapter db, ImageLoader imageLoader);
	}

	public interface OnBackPressedCallback {
		void onBackPressed();
	}

	public interface OnRefreshCallback {
		void onRefresh();
	}

	public interface OnErrorCallback {
		void onError(String params, String response, String error, boolean showError);
	}
}