package com.mobileoptima.tarkieattendance;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.library.Sqlite.SQLiteAdapter;
import com.android.library.Utils.UI;
import com.android.library.widgets.CustomEditText;
import com.mobileoptima.constants.Action;
import com.mobileoptima.data.Get;
import com.mobileoptima.interfaces.Callback.OnBackPressedCallback;
import com.mobileoptima.interfaces.Callback.OnRefreshCallback;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class LoginFragment extends Fragment implements OnBackPressedCallback, OnRefreshCallback, OnClickListener {
	private CustomEditText etUsernameLogin, etPasswordLogin;
	private FragmentManager manager;
	private MainActivity main;
	private OnRefreshCallback refreshCallback;
	private SQLiteAdapter db;
	private Window window;
	private boolean isSaveInstanceState;

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
		View view = inflater.inflate(R.layout.login_layout, container, false);
		ImageView ivLogoLogin = view.findViewById(R.id.ivLogoLogin);
		etUsernameLogin = view.findViewById(R.id.etUsernameLogin);
		etPasswordLogin = view.findViewById(R.id.etPasswordLogin);
		final Button btnLogin = view.findViewById(R.id.btnLogin);
		ImageView ivPoweredByLogin = view.findViewById(R.id.ivPoweredByLogin);
		Resources res = main.getResources();
		int hasNavID = res.getIdentifier("config_showNavigationBar", "bool", "android");
		int navHeightID = res.getIdentifier("navigation_bar_height", "dimen", "android");
		if(hasNavID > 0 && res.getBoolean(hasNavID) && navHeightID > 0) {
			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(res.getDimensionPixelSize(R.dimen.ninety_five), res.getDimensionPixelSize(R.dimen.forty_five));
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
			layoutParams.setMargins(0, 0, 0, res.getDimensionPixelSize(navHeightID) + res.getDimensionPixelOffset(R.dimen.sixteen));
			ivPoweredByLogin.setLayoutParams(layoutParams);
		}
		ImageLoader imageLoader = main.getImageLoader();
		DisplayImageOptions ivLogoLoginOptions = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.ic_logo)
				.showImageOnFail(R.drawable.ic_logo)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.build();
		imageLoader.displayImage(Get.companyLogo(db), ivLogoLogin, ivLogoLoginOptions);
		etPasswordLogin.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId == EditorInfo.IME_ACTION_DONE) {
					btnLogin.performClick();
				}
				return false;
			}
		});
		btnLogin.setOnClickListener(this);
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
		String employeeID = Get.employeeID(db);
		if(employeeID != null && !employeeID.isEmpty()) {
			manager.popBackStack();
			refreshCallback.onRefresh();
		}
	}

	@Override
	public void onClick(View view) {
		UI.hideKeyboard(main, view);
		switch(view.getId()) {
			case R.id.btnLogin:
				String username = etUsernameLogin.getText().toString();
				String password = etPasswordLogin.getText().toString();
				if(username.isEmpty() || password.isEmpty()) {
					break;
				}
				Bundle bundle = new Bundle();
				bundle.putString("USERNAME", username);
				bundle.putString("PASSWORD", password);
				LoadingDialogFragment loading = new LoadingDialogFragment();
				loading.setArguments(bundle);
				loading.setOnRefreshCallback(this);
				loading.setAction(Action.LOGIN);
				UI.addFragment(manager, R.id.rlMain, loading);
				break;
		}
	}

	public void setOnRefreshCallback(OnRefreshCallback refreshCallback) {
		this.refreshCallback = refreshCallback;
	}
}