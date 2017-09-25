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
import com.android.library.widgets.CustomButton;
import com.android.library.widgets.CustomTextView;
import com.mobileoptima.constants.Convention;
import com.mobileoptima.constants.Modules;
import com.mobileoptima.data.Get;
import com.mobileoptima.data.Load;
import com.mobileoptima.models.Store;
import com.mobileoptima.models.Visit;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class HomeFragment extends Fragment implements OnClickListener {
	private CustomTextView tvTimeHome, tvAmPmHome, tvDateHome;
	private DisplayImageOptions ivLogoHomeOptions;
	private FragmentManager manager;
	private Handler handler;
	private ImageLoader imageLoader;
	private MainActivity main;
	private SQLiteAdapter db;
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
						handler.sendMessage(handler.obtainMessage());
						Thread.sleep(1000);
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
		LinearLayout llMenuItemsDrawerMain = main.findViewById(R.id.llMenuItemsDrawerMain);
		final ScrollView svContentHome = view.findViewById(R.id.svContentHome);
		final LinearLayout llContentHome = view.findViewById(R.id.llContentHome);
		ImageView ivLogoHome = view.findViewById(R.id.ivLogoHome);
		RelativeLayout rlAttendanceHome = view.findViewById(R.id.rlAttendanceHome);
		tvTimeHome = view.findViewById(R.id.tvTimeHome);
		tvAmPmHome = view.findViewById(R.id.tvAmPmHome);
		tvDateHome = view.findViewById(R.id.tvDateHome);
		CustomButton btnStoreHome = view.findViewById(R.id.btnStoreHome);
		CustomButton btnTimeInOutHome = view.findViewById(R.id.btnTimeInOutHome);
		LinearLayout llVisitsHome = view.findViewById(R.id.llVisitsHome);
		LinearLayout llTodayVisitsHome = view.findViewById(R.id.llTodayVisitsHome);
		CustomButton btnNewVisitsHome = view.findViewById(R.id.btnNewVisitsHome);
		LinearLayout llFormsHome = view.findViewById(R.id.llFormsHome);
		LinearLayout llInventoryHome = view.findViewById(R.id.llInventoryHome);
		CustomButton btnAddExpenseHome = view.findViewById(R.id.btnAddExpenseHome);
		imageLoader.displayImage(Get.companyLogo(db), ivLogoHome, ivLogoHomeOptions);
		btnStoreHome.setAllCaps(false);
		btnStoreHome.setGravity(Gravity.START);
		btnStoreHome.setOnClickListener(this);
		btnTimeInOutHome.setOnClickListener(this);
		btnAddExpenseHome.setOnClickListener(this);
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
			thread.start();
			String storeID = Get.storeID(db);
			if(storeID != null) {
				Store store = Get.store(db, storeID);
				btnStoreHome.setText(store.name);
				btnStoreHome.setTextColor(ContextCompat.getColor(main, R.color.text_pri));
			}
			else {
				btnStoreHome.setText("Select " + Convention.STORES.getName());
				btnStoreHome.setTextColor(ContextCompat.getColor(main, R.color.text_sec));
			}
			menuTimeInOut = llMenuItemsDrawerMain.getChildAt(1);
			btnTimeInOutHome.setText(((CustomTextView) menuTimeInOut.findViewById(R.id.tvTextMenu)).getText());
		}
		if(Modules.VISITS.isEnabled()) {
			llVisitsHome.setVisibility(View.VISIBLE);
			llTodayVisitsHome.removeAllViews();
			for(Visit visit : Load.visits(db)) {
				updateTodayVisits(llTodayVisitsHome, visit);
			}
			btnNewVisitsHome.setText("New " + Convention.VISITS.getName());
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
		return view;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		thread.interrupt();
	}

	@Override
	public void onClick(View view) {
		switch(view.getId()) {
			case R.id.btnStoreHome:
				break;
			case R.id.btnTimeInOutHome:
				menuTimeInOut.performClick();
				break;
			case R.id.btnAddExpenseHome:
				break;
		}
	}

	public void updateTodayVisits(LinearLayout container, Visit visit) {
		View todayVisitsItem = LayoutInflater.from(main).inflate(R.layout.today_visit_list_item, container, false);
		((CustomTextView) todayVisitsItem.findViewById(R.id.tvNameTodayVisit)).setText(visit.name);
		((CustomTextView) todayVisitsItem.findViewById(R.id.tvAddressTodayVisit)).setText(visit.store.address);
		todayVisitsItem.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
			}
		});
		container.addView(todayVisitsItem);
	}
}