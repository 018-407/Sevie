package com.mobileoptima.tarkieattendance.attendance;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.library.widgets.ViewPagerAdapter;
import com.mobileoptima.tarkieattendance.MainActivity;
import com.mobileoptima.tarkieattendance.R;
import com.mobileoptima.widgets.PageSlidingTab;

import java.util.ArrayList;

public class AttendanceFragment extends Fragment {
	private ArrayList<Fragment> vpFragments;
	private ArrayList<String> vpTabs;
	private FragmentManager manager;
	private MainActivity main;
	private PageSlidingTab stAttendance;
	private ViewPager vpAttendance;
	private ViewPagerAdapter vpAdapter;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e("paul", "onCreate");
		main = (MainActivity) getActivity();
		manager = main.getSupportFragmentManager();
		vpFragments = new ArrayList<>();
		vpFragments.clear();
		vpTabs = new ArrayList<>();
		vpTabs.clear();
		vpFragments.add(new AttendanceReportFragment());
		vpTabs.add("Attendance");
		vpFragments.add(new ActivitiesFragment());
		vpTabs.add("Activities");
		vpAdapter = new ViewPagerAdapter(manager, vpFragments, vpTabs);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		Log.e("paul", "onCreateView");
		View view = inflater.inflate(R.layout.attendance_layout, container, false);
		vpAttendance = view.findViewById(R.id.vpAttendance);
		stAttendance = view.findViewById(R.id.stAttendance);
		vpAttendance.setAdapter(vpAdapter);
		stAttendance.setViewPager(vpAttendance);
		stAttendance.setMaxScrollItems(1);
		vpAdapter.notifyDataSetChanged();
		vpAttendance.invalidate();
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.e("paul", "onResume");
		stAttendance.init();
	}
}