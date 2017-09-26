package com.mobileoptima.tarkieattendance;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.library.Sqlite.SQLiteAdapter;
import com.android.library.Utils.Cache;
import com.android.library.Utils.Time;
import com.android.library.Utils.UI;
import com.android.library.widgets.CustomTextView;
import com.android.library.widgets.ViewPagerAdapter;
import com.mobileoptima.constants.Action;
import com.mobileoptima.constants.App;
import com.mobileoptima.constants.Convention;
import com.mobileoptima.constants.Menu;
import com.mobileoptima.constants.Modules;
import com.mobileoptima.constants.Settings;
import com.mobileoptima.data.Get;
import com.mobileoptima.data.Save;
import com.mobileoptima.interfaces.Callback.OnBackPressedCallback;
import com.mobileoptima.interfaces.Callback.OnInitializeCallback;
import com.mobileoptima.interfaces.Callback.OnRefreshCallback;
import com.mobileoptima.widgets.ModulesSlidingTab;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnRefreshCallback, OnInitializeCallback, OnClickListener {
	private ArrayList<Fragment> vpFragments;
	private ArrayList<Integer> vpTabIcons;
	private ArrayList<String> vpTabTitles;
	private CustomTextView tvEmployeeNameHeaderDrawerMain, tvEmployeeNumberHeaderDrawerMain;
	private DisplayImageOptions ivEmployeePhotoHeaderDrawerMainOptions, ivCompanyLogoHeaderDrawerMainOptions;
	private DrawerLayout dlMain;
	private FragmentManager manager;
	private Handler handler;
	private ImageLoader imageLoader;
	private ImageView ivEmployeePhotoHeaderDrawerMain, ivCompanyLogoHeaderDrawerMain;
	private LinearLayout llMenuItemsDrawerMain;
	private ModulesSlidingTab stMain;
	private OnBackPressedCallback backPressedCallback;
	private Resources res;
	private SQLiteAdapter db;
	private String conventionTimeIn, conventionTimeOut;
	private Thread thread;
	private ViewPager vpMain;
	private ViewPagerAdapter vpAdapter;
	private Window window;
	private boolean isSaveInstanceState, isRefresh, showValidateTimeAlertDialog, showUpdateMasterFileAlertDialog;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		manager = getSupportFragmentManager();
		manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
		window = getWindow();
		res = getResources();
		dlMain = findViewById(R.id.dlMain);
		ivEmployeePhotoHeaderDrawerMain = findViewById(R.id.ivEmployeePhotoHeaderDrawerMain);
		ivCompanyLogoHeaderDrawerMain = findViewById(R.id.ivCompanyLogoHeaderDrawerMain);
		tvEmployeeNameHeaderDrawerMain = findViewById(R.id.tvEmployeeNameHeaderDrawerMain);
		tvEmployeeNumberHeaderDrawerMain = findViewById(R.id.tvEmployeeNumberHeaderDrawerMain);
		llMenuItemsDrawerMain = findViewById(R.id.llMenuItemsDrawerMain);
		LinearLayout llMenuAppBarMain = findViewById(R.id.llMenuAppBarMain);
		vpMain = findViewById(R.id.vpMain);
		stMain = findViewById(R.id.stMain);
		llMenuAppBarMain.setOnClickListener(this);
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while(!thread.isInterrupted()) {
						if(showValidateTimeAlertDialog) {
							Thread.sleep(1000);
							handler.sendMessage(handler.obtainMessage());
						}
					}
				}
				catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		handler = new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message message) {
				validateTime();
				return true;
			}
		});
		thread.start();
		vpFragments = new ArrayList<>();
		vpTabTitles = new ArrayList<>();
		vpTabIcons = new ArrayList<>();
		RoundedBitmapDrawable roundUserPlaceholder = RoundedBitmapDrawableFactory.create(res, BitmapFactory.decodeResource(res, R.drawable.ic_user_placeholder));
		roundUserPlaceholder.setCircular(true);
		ivEmployeePhotoHeaderDrawerMainOptions = new DisplayImageOptions.Builder()
				.displayer(new CircleBitmapDisplayer())
				.showImageForEmptyUri(roundUserPlaceholder)
				.showImageOnFail(roundUserPlaceholder)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.build();
		ivCompanyLogoHeaderDrawerMainOptions = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.ic_logo)
				.showImageOnFail(R.drawable.ic_logo)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.build();
		isRefresh = true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		isSaveInstanceState = false;
		if(isRefresh) {
			isRefresh = false;
			onRefresh();
		}
		validateTime();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		isSaveInstanceState = true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		thread.interrupt();
	}

	@Override
	public void onRefresh() {
		if(isSaveInstanceState) {
			isRefresh = true;
			return;
		}
		UI.hideSystemUi(window, false, false, false, false);
		dlMain.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		setOnBackPressedCallback(null);
		if(db == null || imageLoader == null) {
			SplashFragment splash = new SplashFragment();
			splash.setOnInitializeCallback(this);
			UI.addFragment(manager, R.id.rlMain, splash, 0, 0, 0, R.anim.fade_out);
			return;
		}
		String apiKey = Get.apiKey(db);
		if(apiKey == null || apiKey.isEmpty()) {
			AuthorizationFragment authorization = new AuthorizationFragment();
			authorization.setOnRefreshCallback(this);
			UI.addFragment(manager, R.id.rlMain, authorization, 0, 0, 0, R.anim.fade_out);
			return;
		}
		String employeeID = Get.employeeID(db);
		if(employeeID == null || employeeID.isEmpty()) {
			LoginFragment login = new LoginFragment();
			login.setOnRefreshCallback(this);
			UI.addFragment(manager, R.id.rlMain, login, 0, 0, 0, R.anim.fade_out);
			validateTime();
			return;
		}
		dlMain.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
		imageLoader.displayImage(Get.employeePhoto(db, employeeID), ivEmployeePhotoHeaderDrawerMain, ivEmployeePhotoHeaderDrawerMainOptions);
		imageLoader.displayImage(Get.companyLogo(db), ivCompanyLogoHeaderDrawerMain, ivCompanyLogoHeaderDrawerMainOptions);
		tvEmployeeNameHeaderDrawerMain.setText(Get.employeeName(db, employeeID));
		tvEmployeeNumberHeaderDrawerMain.setText(Get.employeeNumber(db, employeeID));
		for(Convention convention : Convention.values()) {
			String name = Get.conventionName(db, convention.getID());
			if(name != null && !name.isEmpty()) {
				convention.setName(name);
				if(convention == Convention.VISITS) {
					Modules.VISITS.setName(name);
				}
			}
		}
		conventionTimeIn = Convention.TIME_IN.getName();
		conventionTimeOut = Convention.TIME_OUT.getName();
		String timeInID = Get.timeInID(db);
		if(timeInID != null && !timeInID.isEmpty()) {
			Menu.TIME_IN_OUT.setIcon(R.drawable.ic_menu_time_out);
			Menu.TIME_IN_OUT.setName(conventionTimeOut);
		}
		else {
			Menu.TIME_IN_OUT.setIcon(R.drawable.ic_menu_time_in);
			Menu.TIME_IN_OUT.setName(conventionTimeIn);
		}
		for(Menu menu : Menu.values()) {
			updateMenuItems(llMenuItemsDrawerMain, menu);
		}
		vpFragments.clear();
		vpTabTitles.clear();
		vpTabIcons.clear();
		vpFragments.add(new HomeFragment());
		vpTabTitles.add("Home");
		vpTabIcons.add(R.string.fa_home);
		for(Modules module : Modules.values()) {
			boolean isEnabled = Get.isModuleEnabled(db, module.getID());
			if(module == Modules.ATTENDANCE) {
				isEnabled = true;
			}
			if(module == Modules.INVENTORY) {
				isEnabled = false;
			}
			if(module == Modules.FORMS) {
				isEnabled = false;
			}
			module.setEnabled(isEnabled);
			if(isEnabled) {
				vpFragments.add(module.getFragment());
				vpTabTitles.add(module.getName());
				vpTabIcons.add(module.getIcon());
			}
		}
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				vpAdapter = new ViewPagerAdapter(manager, vpFragments, vpTabTitles, vpTabIcons);
				vpMain.setOffscreenPageLimit(6);
				vpMain.setAdapter(vpAdapter);
				stMain.setViewPager(vpMain);
				stMain.setMaxScrollItems(6);
			}
		}, 0);
		validateTime();
		updateMasterFile();
	}

	@Override
	public void onInitialize(SQLiteAdapter db, ImageLoader imageLoader) {
		this.db = db;
		this.imageLoader = imageLoader;
		onRefresh();
	}

	@Override
	public void onBackPressed() {
		if(UI.closeDrawer(dlMain)) {
			return;
		}
		if(backPressedCallback != null) {
			backPressedCallback.onBackPressed();
			return;
		}
		if(vpMain.getCurrentItem() != 0) {
			vpMain.setCurrentItem(0, false);
			return;
		}
		if(manager.getBackStackEntryCount() == 0) {
			moveTaskToBack(true);
			return;
		}
		super.onBackPressed();
	}

	@Override
	public void onClick(View view) {
		UI.closeDrawer(dlMain);
		switch(view.getId()) {
			case R.id.llMenuAppBarMain:
				UI.openDrawer(dlMain);
				break;
			case 1://TIME_IN_OUT
				if(Menu.TIME_IN_OUT.getName().equals(conventionTimeIn)) {
					//TIME IN
				}
				else {
					//TIME OUT
				}
				break;
			case 2://BREAKS
				break;
			case 3://STORES
				break;
			case 4://UPDATE_MASTER_FILE
				LoadingDialogFragment updateMasterFile = new LoadingDialogFragment();
				updateMasterFile.setOnRefreshCallback(this);
				updateMasterFile.setAction(Action.UPDATE_MASTER_FILE);
				UI.addFragment(manager, R.id.rlMain, updateMasterFile);
				break;
			case 5://SEND_BACK_UP
				LoadingDialogFragment sendBackUp = new LoadingDialogFragment();
				sendBackUp.setOnRefreshCallback(this);
				sendBackUp.setAction(Action.SEND_BACK_UP);
				UI.addFragment(manager, R.id.rlMain, sendBackUp);
				break;
			case 6://BACK_UP
				break;
			case 7://ABOUT
				break;
			case 8://LOGOUT
				AlertDialogFragment logout = new AlertDialogFragment();
				logout.setOnRefreshCallback(this);
				logout.setTitle("Confirm Logout");
				logout.setMessage("Are you sure you want to Logout?");
				logout.setPositiveButton("Yes", new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						if(!Save.logout(db)) {
							return;
						}
						manager.popBackStack();
					}
				});
				logout.setNegativeButton("No", new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						manager.popBackStack();
					}
				});
				UI.addFragment(manager, R.id.rlMain, logout);
				break;
		}
	}

	public ImageLoader getImageLoader() {
		if(imageLoader == null) {
			imageLoader = Cache.getImageLoader(this);
		}
		return imageLoader;
	}

	public SQLiteAdapter getSQLiteAdapater() {
		if(db == null) {
			db = Cache.getSQLiteAdapter(this, App.DB_NAME, App.DB_VERSION);
		}
		return db;
	}

	public void setOnBackPressedCallback(OnBackPressedCallback backPressedCallback) {
		this.backPressedCallback = backPressedCallback;
	}

	public void updateMenuItems(LinearLayout container, Menu menu) {
		int index = menu.ordinal() + 1;
		View menuItem = container.getChildAt(index);
		if(menuItem == null) {
			menuItem = LayoutInflater.from(this).inflate(R.layout.menu_list_item, container, false);
			menuItem.setId(index);
			container.addView(menuItem, index);
		}
		menuItem = container.getChildAt(index);
		switch(res.getResourceTypeName(menu.getIcon())) {
			case "drawable":
				ImageView ivIconMenu = menuItem.findViewById(R.id.ivIconMenu);
				ivIconMenu.setBackgroundResource(menu.getIcon());
				ivIconMenu.setVisibility(View.VISIBLE);
				break;
			case "string":
				CustomTextView tvIconMenu = menuItem.findViewById(R.id.tvIconMenu);
				if(menu == Menu.UPDATE_MASTER_FILE) {
					tvIconMenu.setRotation(90f);
				}
				tvIconMenu.setText(menu.getIcon());
				tvIconMenu.setVisibility(View.VISIBLE);
				break;
		}
		((CustomTextView) menuItem.findViewById(R.id.tvTextMenu)).setText(menu.getName());
		menuItem.setOnClickListener(this);
	}

	public void validateTime() {
		if(db == null) {
			return;
		}
		String apiKey = Get.apiKey(db);
		if(apiKey == null || apiKey.isEmpty()) {
			return;
		}
		long serverTimestamp = Get.serverTimestamp(db);
		long timestamp = System.currentTimeMillis();
		if(serverTimestamp == 0 || !(serverTimestamp >= (timestamp - Settings.TIME_SECURITY_ALLOWANCE) && serverTimestamp <= (timestamp + Settings.TIME_SECURITY_ALLOWANCE))) {
			if(showValidateTimeAlertDialog) {
				return;
			}
			showValidateTimeAlertDialog = true;
			final AlertDialogFragment alert = new AlertDialogFragment();
			alert.setOnBackPressedCallback(new OnBackPressedCallback() {
				@Override
				public void onBackPressed() {
					moveTaskToBack(true);
				}
			});
			alert.setTitle("Date & Time Security");
			alert.setMessage("We have detected a discrepancy between server and your device time.\n\nServer Time:\n" + (serverTimestamp != 0 ? Time.formatDateTime(Time.convertMilliToTimestamp(serverTimestamp), "yyyy-MM-dd HH:mm:ss", "MMMM d, yyyy hh:mm a") : "N/A") + "\n\nPlease change the device time to \"Server Time\" to continue.");
			alert.setPositiveButton("Validate", new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					manager.popBackStack();
					LoadingDialogFragment validateTime = new LoadingDialogFragment();
					validateTime.setOnRefreshCallback(MainActivity.this);
					validateTime.setAction(Action.VALIDATE_TIME);
					UI.addFragment(manager, R.id.rlMain, validateTime);
				}
			});
			alert.setNegativeButton("Settings", new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Intent intent = new Intent();
					intent.setAction(android.provider.Settings.ACTION_DATE_SETTINGS);
					startActivity(intent);
				}
			});
			UI.addFragment(manager, R.id.rlMain, alert);
			return;
		}
		if(showValidateTimeAlertDialog) {
			showValidateTimeAlertDialog = false;
			manager.popBackStack();
		}
	}

	public void updateMasterFile() {
		if(db == null) {
			return;
		}
		String apiKey = Get.apiKey(db);
		if(apiKey == null || apiKey.isEmpty()) {
			return;
		}
		String employeeID = Get.employeeID(db);
		if(employeeID == null || employeeID.isEmpty()) {
			return;
		}
		String syncBatchID = Get.syncBatchID(db);
		if(syncBatchID == null || syncBatchID.isEmpty()) {
			if(showUpdateMasterFileAlertDialog) {
				return;
			}
			showUpdateMasterFileAlertDialog = true;
			final AlertDialogFragment alert = new AlertDialogFragment();
			alert.setOnBackPressedCallback(new OnBackPressedCallback() {
				@Override
				public void onBackPressed() {
					moveTaskToBack(true);
				}
			});
			alert.setTitle("Update Master File");
			alert.setMessage("Update Master File Required.");
			alert.setPositiveButton("Update", new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					manager.popBackStack();
					LoadingDialogFragment updateMasterFile = new LoadingDialogFragment();
					updateMasterFile.setOnRefreshCallback(MainActivity.this);
					updateMasterFile.setAction(Action.UPDATE_MASTER_FILE);
					UI.addFragment(manager, R.id.rlMain, updateMasterFile);
				}
			});
			UI.addFragment(manager, R.id.rlMain, alert);
			return;
		}
		if(showUpdateMasterFileAlertDialog) {
			showUpdateMasterFileAlertDialog = false;
			manager.popBackStack();
		}
	}
}