package com.mobileoptima.tarkieattendance;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.android.library.Sqlite.SQLiteAdapter;
import com.android.library.Utils.Time;
import com.android.library.Utils.UI;
import com.android.library.widgets.CustomButton;
import com.android.library.widgets.CustomTextView;
import com.mobileoptima.constants.Convention;
import com.mobileoptima.constants.Modules;
import com.mobileoptima.constants.Table;
import com.mobileoptima.data.Get;
import com.mobileoptima.data.Load;
import com.mobileoptima.data.Save;
import com.mobileoptima.interfaces.Callback.OnRefreshCallback;
import com.mobileoptima.models.Expense;
import com.mobileoptima.models.Store;
import com.mobileoptima.models.Visit;
import com.mobileoptima.tarkieattendance.visits.VisitDetailsFragment;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class HomeFragment extends Fragment implements OnRefreshCallback, OnClickListener {
	private CustomButton btnTimeInOutHome, btnStoreHome, btnAddExpenseHome, btnNewVisitsHome;
	private CustomTextView tvTimeHome, tvAmPmHome, tvDateHome;
	private DisplayImageOptions ivLogoHomeOptions;
	private FragmentManager manager;
	private Handler handler;
	private ImageLoader imageLoader;
	private LinearLayout llMenuItemsDrawerMain, llContentHome, llVisitsHome, llTodayVisitsHome, llFormsHome, llInventoryHome;
	private MainActivity main;
	private RelativeLayout rlAttendanceHome;
	private ScrollView svContentHome;
	private SQLiteAdapter db;
	private String conventionStores, conventionVisits;
	private Thread thread;
	private View menuTimeInOut;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		main = (MainActivity) getActivity();
		manager = main.getSupportFragmentManager();
		db = main.getSQLiteAdapater();
		imageLoader = main.getImageLoader();
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while(!thread.isInterrupted()) {
						if(rlAttendanceHome.getVisibility() == View.VISIBLE) {
							handler.sendMessage(handler.obtainMessage());
							Thread.sleep(1000);
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
				String timestamp = Time.getDeviceTimestamp();
				tvTimeHome.setText(Time.formatDateTime(timestamp, "yyyy-MM-dd HH:mm:ss", "hh:mm"));
				tvAmPmHome.setText(Time.formatDateTime(timestamp, "yyyy-MM-dd HH:mm:ss", "a"));
				tvDateHome.setText(Time.formatDateTime(timestamp, "yyyy-MM-dd HH:mm:ss", "EEEE MMM d, yyyy"));
				return true;
			}
		});
		ivLogoHomeOptions = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.ic_logo)
				.showImageOnFail(R.drawable.ic_logo)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.build();
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home_layout, container, false);
		llMenuItemsDrawerMain = main.findViewById(R.id.llMenuItemsDrawerMain);
		svContentHome = view.findViewById(R.id.svContentHome);
		llContentHome = view.findViewById(R.id.llContentHome);
		ImageView ivLogoHome = view.findViewById(R.id.ivLogoHome);
		rlAttendanceHome = view.findViewById(R.id.rlAttendanceHome);
		tvTimeHome = view.findViewById(R.id.tvTimeHome);
		tvAmPmHome = view.findViewById(R.id.tvAmPmHome);
		tvDateHome = view.findViewById(R.id.tvDateHome);
		btnStoreHome = view.findViewById(R.id.btnStoreHome);
		btnTimeInOutHome = view.findViewById(R.id.btnTimeInOutHome);
		llVisitsHome = view.findViewById(R.id.llVisitsHome);
		llTodayVisitsHome = view.findViewById(R.id.llTodayVisitsHome);
		btnNewVisitsHome = view.findViewById(R.id.btnNewVisitsHome);
		llFormsHome = view.findViewById(R.id.llFormsHome);
		llInventoryHome = view.findViewById(R.id.llInventoryHome);
		btnAddExpenseHome = view.findViewById(R.id.btnAddExpenseHome);
		imageLoader.displayImage(Get.companyLogo(db), ivLogoHome, ivLogoHomeOptions);
		btnStoreHome.setAllCaps(false);
		btnStoreHome.setGravity(Gravity.START);
		btnStoreHome.setOnClickListener(this);
		btnTimeInOutHome.setOnClickListener(this);
		btnNewVisitsHome.setOnClickListener(this);
		btnAddExpenseHome.setOnClickListener(this);
		thread.start();
		onRefresh();
		return view;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		thread.interrupt();
	}

	@Override
	public void onRefresh() {
		main.setOnBackPressedCallback(null);
		conventionStores = Convention.STORES.getName();
		conventionVisits = Convention.VISITS.getName();
		if(true || (!Modules.VISITS.isEnabled() && !Modules.INVENTORY.isEnabled() && !Modules.FORMS.isEnabled())) {
			rlAttendanceHome.setVisibility(View.VISIBLE);
			btnTimeInOutHome.setVisibility(View.VISIBLE);
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					FrameLayout.LayoutParams params;
					if(llContentHome.getHeight() >= svContentHome.getHeight()) {
						params = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, Gravity.TOP | Gravity.CENTER_HORIZONTAL);
					}
					else {
						params = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
					}
					llContentHome.setLayoutParams(params);
				}
			}, 0);
			String storeID = Get.storeID(db);
			if(storeID != null) {
				Store store = Get.store(db, storeID);
				btnStoreHome.setText(store.name);
				btnStoreHome.setTextColor(ContextCompat.getColor(main, R.color.text_pri));
			}
			else {
				btnStoreHome.setText("Select " + conventionStores);
				btnStoreHome.setTextColor(ContextCompat.getColor(main, R.color.text_sec));
			}
			menuTimeInOut = llMenuItemsDrawerMain.getChildAt(1);
			btnTimeInOutHome.setText(((CustomTextView) menuTimeInOut.findViewById(R.id.tvTextMenu)).getText());
		}
		if(Modules.VISITS.isEnabled()) {
			llVisitsHome.setVisibility(View.VISIBLE);
			llTodayVisitsHome.removeAllViews();
			String today = Time.getDateFromTimestamp(Time.getDeviceTimestamp());
			for(Visit visit : Load.visits(db, today, today)) {
				updateTodayVisits(llTodayVisitsHome, visit);
			}
			btnNewVisitsHome.setText("New " + conventionVisits);
		}
		if(Modules.FORMS.isEnabled()) {
			llFormsHome.setVisibility(View.VISIBLE);
		}
		if(Modules.INVENTORY.isEnabled()) {
			llInventoryHome.setVisibility(View.VISIBLE);
		}
		if(Modules.EXPENSE.isEnabled()) {
			btnAddExpenseHome.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onClick(View view) {
		switch(view.getId()) {
			case R.id.btnStoreHome:
				break;
			case R.id.btnTimeInOutHome:
				menuTimeInOut.performClick();
				break;
			case R.id.btnNewVisitsHome:
				AlertDialogFragment alert = new AlertDialogFragment();
				alert.setOnRefreshCallback(this);
				alert.setTitle("Add " + conventionVisits);
				alert.setMessage("Are you sure you want to add new " + conventionVisits + "?");
				alert.setPositiveButton("Yes", new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						String timestamp = Time.getDeviceTimestamp();
						String date = Time.getDateFromTimestamp(timestamp);
						String time = Time.getTimeFromTimestamp(timestamp);
						Visit visit = new Visit();
						visit.name = "New Visit " + (Get.visitsTodayCount(db) + 1);
						visit.dateStart = date;
						visit.dateEnd = date;
						visit.dDate = date;
						visit.dTime = time;
						visit.employee = Get.employee(db, Get.employeeID(db));
						if(Save.visit(db, visit)) {
							manager.popBackStack();
						}
					}
				});
				alert.setNegativeButton("No", new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						manager.popBackStack();
					}
				});
				UI.addFragment(manager, R.id.rlMain, alert);
				break;
			case R.id.btnAddExpenseHome:
				String timestamp = Time.getDeviceTimestamp();
				Expense expense = new Expense();
				expense.name = "Expense " + (Get.expensesTodayCount(db) + 1);
				expense.dDate = Time.getDateFromTimestamp(timestamp);
				expense.dTime = Time.getTimeFromTimestamp(timestamp);
				expense.employee = Get.employee(db, Get.employeeID(db));
				if(Save.expense(db, expense)) {
					UI.showToast(main, (RelativeLayout) main.findViewById(R.id.rlMain), "Expense " + Get.expensesTodayCount(db)+ " has been added. You may enter more details later.");
				}
				break;
		}
	}

	public void updateTodayVisits(LinearLayout container, final Visit visit) {
		String storeAddress = visit.store.address;
		View todayVisitsItem = LayoutInflater.from(main).inflate(R.layout.today_visit_list_item, container, false);
		CustomTextView tvNameTodayVisit = todayVisitsItem.findViewById(R.id.tvNameTodayVisit);
		CustomTextView tvAddressTodayVisit = todayVisitsItem.findViewById(R.id.tvAddressTodayVisit);
		tvNameTodayVisit.setText(visit.name);
		if(storeAddress != null && !storeAddress.isEmpty()) {
			tvAddressTodayVisit.setText(storeAddress);
		}
		else {
			tvAddressTodayVisit.setVisibility(View.GONE);
		}
		todayVisitsItem.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				VisitDetailsFragment visitDetails = new VisitDetailsFragment();
				UI.addFragment(manager, R.id.rlMain, visitDetails, R.anim.slide_in_rtl, R.anim.slide_out_rtl, R.anim.slide_in_ltr, R.anim.slide_out_ltr);
			}
		});
		todayVisitsItem.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View view) {
				if(visit.isFromWeb || visit.checkIn.ID != null) {
					AlertDialogFragment alert = new AlertDialogFragment();
					alert.setOnRefreshCallback(HomeFragment.this);
					alert.setTitle("Delete " + conventionVisits);
					alert.setMessage("You are not allowed to delete this " + conventionVisits + ".");
					alert.setPositiveButton("OK", new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							manager.popBackStack();
						}
					});
					UI.addFragment(manager, R.id.rlMain, alert);
					return true;
				}
				AlertDialogFragment alert = new AlertDialogFragment();
				alert.setOnRefreshCallback(HomeFragment.this);
				alert.setTitle("Delete " + conventionVisits);
				alert.setMessage("Are you sure you want to delete this " + conventionVisits + "?");
				alert.setPositiveButton("Yes", new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Save.isDelete(db, Table.VISITS.getName(), visit.ID);
						manager.popBackStack();
					}
				});
				alert.setNegativeButton("No", new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						manager.popBackStack();
					}
				});
				UI.addFragment(manager, R.id.rlMain, alert);
				return true;
			}
		});
		container.addView(todayVisitsItem);
	}
}