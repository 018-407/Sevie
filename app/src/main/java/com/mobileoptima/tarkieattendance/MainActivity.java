package com.mobileoptima.tarkieattendance;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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
import android.widget.TextView;

import com.android.library.Sqlite.SQLiteAdapter;
import com.android.library.Utils.Cache;
import com.android.library.Utils.UI;
import com.android.library.widgets.ViewPagerAdapter;
import com.mobileoptima.constants.Action;
import com.mobileoptima.constants.App;
import com.mobileoptima.constants.Convention;
import com.mobileoptima.constants.Menu;
import com.mobileoptima.constants.Modules;
import com.mobileoptima.data.Get;
import com.mobileoptima.data.Save;
import com.mobileoptima.interfaces.Callback.OnBackPressedCallback;
import com.mobileoptima.interfaces.Callback.OnInitializeCallback;
import com.mobileoptima.interfaces.Callback.OnRefreshCallback;
import com.mobileoptima.tarkieattendance.attendance.AttendanceFragment;
import com.mobileoptima.tarkieattendance.expense.ExpenseFragment;
import com.mobileoptima.tarkieattendance.forms.FormsFragment;
import com.mobileoptima.tarkieattendance.inventory.InventoryFragment;
import com.mobileoptima.tarkieattendance.visits.VisitsFragment;
import com.mobileoptima.widgets.ModulesSlidingTab;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnRefreshCallback, OnInitializeCallback, OnClickListener {
	private ArrayList<Fragment> vpFragments;
	private ArrayList<String> vpTabs;
	private DisplayImageOptions ivEmployeePhotoHeaderDrawerMainOptions, ivCompanyLogoHeaderDrawerMainOptions;
	private DrawerLayout dlMain;
	private FragmentManager manager;
	private ImageLoader imageLoader;
	private ImageView ivEmployeePhotoHeaderDrawerMain, ivCompanyLogoHeaderDrawerMain;
	private LayoutInflater inflater;
	private LinearLayout llMenuItemsDrawerMain;
	private OnBackPressedCallback backPressedCallback;
	private Resources res;
	private ModulesSlidingTab stMain;
	private SQLiteAdapter db;
	private TextView tvEmployeeNameHeaderDrawerMain, tvEmployeeNumberHeaderDrawerMain;
	private ViewPager vpMain;
	private ViewPagerAdapter vpAdapter;
	private Window window;
	private boolean isSaveInstanceState, isRefresh;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		manager = getSupportFragmentManager();
		manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
		window = getWindow();
		inflater = getLayoutInflater();
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
		dlMain.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		llMenuAppBarMain.setOnClickListener(this);
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
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		isSaveInstanceState = true;
	}

	@Override
	public void onRefresh() {
		if(isSaveInstanceState) {
			isRefresh = true;
			return;
		}
		UI.hideSystemUi(window, false, false, false, false);
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
			}
		}
		String timeInID = Get.timeInID(db);
		if(timeInID != null && !timeInID.isEmpty()) {
			Menu.TIME_IN_OUT.setIcon(R.drawable.ic_menu_time_out);
			Menu.TIME_IN_OUT.setName(Convention.TIME_OUT.getName());
		}
		else {
			Menu.TIME_IN_OUT.setIcon(R.drawable.ic_menu_time_in);
			Menu.TIME_IN_OUT.setName(Convention.TIME_IN.getName());
		}
		for(Menu menu : Menu.values()) {
			updateMenuItem(inflater, llMenuItemsDrawerMain, menu);
		}
		vpFragments = new ArrayList<>();
		vpTabs = new ArrayList<>();
		vpFragments.add(new HomeFragment());
		vpTabs.add("Home");
		vpFragments.add(new VisitsFragment());
		vpTabs.add(Modules.VISITS.getName());
		vpFragments.add(new ExpenseFragment());
		vpTabs.add(Modules.EXPENSE.getName());
		vpFragments.add(new InventoryFragment());
		vpTabs.add(Modules.INVENTORY.getName());
		vpFragments.add(new FormsFragment());
		vpTabs.add(Modules.FORMS.getName());
		vpFragments.add(new AttendanceFragment());
		vpTabs.add("History");
		vpAdapter = new ViewPagerAdapter(manager, vpFragments, vpTabs);
		vpMain.setOffscreenPageLimit(6);
		vpMain.setAdapter(vpAdapter);
		stMain.setViewPager(vpMain);
		stMain.setMaxScrollItems(6);
		stMain.init();
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

	public void updateMain() {
	}

	public void updateMenuItem(LayoutInflater inflater, LinearLayout container, Menu menu) {
		int index = menu.ordinal() + 1;
		View menuItem = container.getChildAt(index);
		if(menuItem == null) {
			menuItem = inflater.inflate(R.layout.menu_list_row, null, false);
			menuItem.setId(index);
			container.addView(menuItem, index);
		}
		menuItem = container.getChildAt(index);
		((ImageView) menuItem.findViewById(R.id.ivIconMenu)).setImageResource(menu.getIcon());
		((TextView) menuItem.findViewById(R.id.tvTextMenu)).setText(menu.getName());
		menuItem.setOnClickListener(this);
	}
}