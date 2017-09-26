package com.mobileoptima.tarkieattendance;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.android.library.Sqlite.SQLiteAdapter;
import com.android.library.Utils.UI;
import com.mobileoptima.constants.Settings;
import com.mobileoptima.constants.Table;
import com.mobileoptima.interfaces.Callback.OnBackPressedCallback;
import com.mobileoptima.interfaces.Callback.OnInitializeCallback;
import com.mobileoptima.interfaces.Callback.OnRefreshCallback;
import com.nostra13.universalimageloader.core.ImageLoader;

public class SplashFragment extends Fragment implements OnBackPressedCallback, OnRefreshCallback {
	private FragmentManager manager;
	private Handler handler;
	private ImageLoader imageLoader;
	private MainActivity main;
	private OnInitializeCallback initializeCallback;
	private SQLiteAdapter db;
	private Thread thread;
	private Window window;
	private boolean isSaveInstanceState, isInitialized, isPermissionDenied, showAlertDialog;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		main = (MainActivity) getActivity();
		manager = main.getSupportFragmentManager();
		window = main.getWindow();
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					db = main.getSQLiteAdapater();
					imageLoader = main.getImageLoader();
					db.beginTransaction();
					for(Table table : Table.values()) {
						db.createTable(table.getName(), table.getFields());
					}
					db.endTransaction();
					Thread.sleep(2000);
					handler.sendMessage(handler.obtainMessage());
				}
				catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		handler = new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message message) {
				isInitialized = true;
				onRefresh();
				return true;
			}
		});
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.splash_layout, container, false);
	}

	@Override
	public void onResume() {
		super.onResume();
		isSaveInstanceState = false;
		onRefresh();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		isSaveInstanceState = true;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		thread.interrupt();
	}

	@Override
	public void onBackPressed() {
		main.moveTaskToBack(true);
	}

	@Override
	public void onRefresh() {
		if(isSaveInstanceState) {
			return;
		}
		UI.hideSystemUi(window, true, true, true, true);
		main.setOnBackPressedCallback(this);
		if(isInitialized) {
			manager.popBackStack();
			initializeCallback.onInitialize(db, imageLoader);
			return;
		}
		if(Settings.EXTERNAL_STORAGE && ActivityCompat.checkSelfPermission(main, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
			if(isPermissionDenied) {
				if(showAlertDialog) {
					return;
				}
				showAlertDialog = true;
				AlertDialogFragment alert = new AlertDialogFragment();
				alert.setOnBackPressedCallback(new OnBackPressedCallback() {
					@Override
					public void onBackPressed() {
						main.moveTaskToBack(true);
					}
				});
				alert.setOnRefreshCallback(this);
				alert.setTitle("Permission");
				alert.setMessage("Allow Permission");
				alert.setPositiveButton("Settings", new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Intent intent = new Intent();
						intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
						intent.setData(Uri.fromParts("package", main.getPackageName(), null));
						main.startActivity(intent);
					}
				});
				alert.setNegativeButton("Cancel", new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						main.moveTaskToBack(true);
					}
				});
				UI.addFragment(manager, R.id.rlMain, alert);
				return;
			}
			requestPermissions(new String[] {android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
			return;
		}
		if(showAlertDialog) {
			showAlertDialog = false;
			manager.popBackStack();
			return;
		}
		thread.start();
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if(requestCode == 1 && grantResults.length != 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
			isPermissionDenied = true;
			onRefresh();
		}
	}

	public void setOnInitializeCallback(OnInitializeCallback initializeCallback) {
		this.initializeCallback = initializeCallback;
	}
}