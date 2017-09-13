package com.mobileoptima.tarkieattendance.expense;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.library.widgets.ViewPagerAdapter;
import com.mobileoptima.tarkieattendance.MainActivity;
import com.mobileoptima.tarkieattendance.R;
import com.mobileoptima.widgets.PageSlidingTab;

import java.util.ArrayList;

public class ExpenseFragment extends Fragment {
	private ArrayList<Fragment> vpFragments;
	private ArrayList<String> vpTabs;
	private FragmentManager manager;
	private MainActivity main;
	private PageSlidingTab stExpense;
	private ViewPager vpExpense;
	private ViewPagerAdapter vpAdapter;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		main = (MainActivity) getActivity();
		manager = main.getSupportFragmentManager();
		vpFragments = new ArrayList<>();
		vpTabs = new ArrayList<>();
		vpFragments.add(new ExpenseItemsFragment());
		vpTabs.add("Expense Items");
		vpFragments.add(new ExpenseReportsFragment());
		vpTabs.add("Expense Reports");
		vpFragments.add(new ExpenseItemsFragment());
		vpTabs.add("Expense Items");
		vpFragments.add(new ExpenseReportsFragment());
		vpTabs.add("Expense Reports");
//		vpFragments.add(new ExpenseItemsFragment());
//		vpTabs.add("Expense Items");
//		vpFragments.add(new ExpenseReportsFragment());
//		vpTabs.add("Expense Reports");
//		vpFragments.add(new ExpenseItemsFragment());
//		vpTabs.add("Expense Items");
//		vpFragments.add(new ExpenseReportsFragment());
//		vpTabs.add("Expense Reports");
//		vpFragments.add(new ExpenseItemsFragment());
//		vpTabs.add("Expense Items");
//		vpFragments.add(new ExpenseReportsFragment());
//		vpTabs.add("Expense Reports");
//		vpFragments.add(new ExpenseItemsFragment());
//		vpTabs.add("Expense Items");
//		vpFragments.add(new ExpenseReportsFragment());
//		vpTabs.add("Expense Reports");
		vpAdapter = new ViewPagerAdapter(manager, vpFragments, vpTabs);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.expense_layout, container, false);
		vpExpense = view.findViewById(R.id.vpExpense);
		stExpense = view.findViewById(R.id.stExpense);
//		vpExpense.setAdapter(vpAdapter);
//		stExpense.setViewPager(vpExpense);
//		stExpense.setMaxScrollItems(1);
//		vpAdapter.notifyDataSetChanged();
//		vpExpense.invalidate();
//		stExpense.init();
		return view;
	}
}