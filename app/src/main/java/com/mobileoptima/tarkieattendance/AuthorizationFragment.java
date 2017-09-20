package com.mobileoptima.tarkieattendance;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

import com.android.library.Sqlite.SQLiteAdapter;
import com.android.library.Utils.Device;
import com.android.library.Utils.UI;
import com.android.library.widgets.CustomEditText;
import com.mobileoptima.constants.Action;
import com.mobileoptima.data.Get;
import com.mobileoptima.interfaces.Callback.OnBackPressedCallback;
import com.mobileoptima.interfaces.Callback.OnRefreshCallback;

public class AuthorizationFragment extends Fragment implements OnBackPressedCallback, OnRefreshCallback, OnClickListener {
	private CustomEditText etDeviceCodeAuthorization;
	private FragmentManager manager;
	private MainActivity main;
	private OnRefreshCallback refreshCallback;
	private SQLiteAdapter db;
	private Window window;
	private boolean isSaveInstanceState, isPermissionDenied, showPermissionAlertDialog;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		main = (MainActivity) getActivity();
		manager = main.getSupportFragmentManager();
		window = main.getWindow();
		db = main.getSQLiteAdapater();
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.authorization_layout, container, false);
		etDeviceCodeAuthorization = view.findViewById(R.id.etDeviceCodeAuthorization);
		final Button btnAuthorization = view.findViewById(R.id.btnAuthorization);
		etDeviceCodeAuthorization.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId == EditorInfo.IME_ACTION_DONE) {
					btnAuthorization.performClick();
				}
				return false;
			}
		});
		btnAuthorization.setOnClickListener(this);
		return view;
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
	public void onBackPressed() {
		main.finish();
	}

	@Override
	public void onRefresh() {
		if(isSaveInstanceState) {
			return;
		}
		UI.hideSystemUi(window, true, false, false, false);
		main.setOnBackPressedCallback(this);
		String apiKey = Get.apiKey(db);
		if(apiKey != null && !apiKey.isEmpty()) {
			manager.popBackStack();
			refreshCallback.onRefresh();
			return;
		}
		if(ActivityCompat.checkSelfPermission(main, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
			if(isPermissionDenied) {
				if(showPermissionAlertDialog) {
					return;
				}
				showPermissionAlertDialog = true;
				AlertDialogFragment alert = new AlertDialogFragment();
				alert.setOnBackPressedCallback(new OnBackPressedCallback() {
					@Override
					public void onBackPressed() {
						main.finish();
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
						main.finish();
					}
				});
				UI.addFragment(manager, R.id.rlMain, alert);
				return;
			}
			requestPermissions(new String[] {android.Manifest.permission.READ_PHONE_STATE}, 2);
			return;
		}
		if(showPermissionAlertDialog) {
			showPermissionAlertDialog = false;
			manager.popBackStack();
		}
	}

	@Override
	public void onClick(View view) {
		UI.hideKeyboard(main, view);
		switch(view.getId()) {
			case R.id.btnAuthorization:
				String deviceCode = etDeviceCodeAuthorization.getText().toString();
				if(deviceCode.isEmpty()) {
					break;
				}
				String deviceID = Device.getDeviceID(main);
				if(deviceID == null || deviceID.isEmpty()) {
					break;
				}
				Bundle bundle = new Bundle();
				bundle.putString("DEVICE_CODE", deviceCode);
				bundle.putString("DEVICE_ID", deviceID);
				LoadingDialogFragment loading = new LoadingDialogFragment();
				loading.setArguments(bundle);
				loading.setOnRefreshCallback(this);
				loading.setAction(Action.AUTHORIZE_DEVICE);
				UI.addFragment(manager, R.id.rlMain, loading);
				break;
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if(requestCode == 2 && grantResults.length != 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
			isPermissionDenied = true;
			onRefresh();
		}
	}

	public void setOnRefreshCallback(OnRefreshCallback refreshCallback) {
		this.refreshCallback = refreshCallback;
	}
}