package com.android.library.widgets;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
	private ArrayList<Fragment> fragments;
	private ArrayList<String> tabTitles;
	private ArrayList<Integer> tabIcons;

	public ViewPagerAdapter(FragmentManager manager, ArrayList<Fragment> fragments) {
		this(manager, fragments, null);
	}

	public ViewPagerAdapter(FragmentManager manager, ArrayList<Fragment> fragments, ArrayList<String> tabTitles) {
		this(manager, fragments, tabTitles, null);
	}

	public ViewPagerAdapter(FragmentManager manager, ArrayList<Fragment> fragments, ArrayList<String> tabTitles, ArrayList<Integer> tabIcons) {
		super(manager);
		this.fragments = fragments;
		this.tabTitles = tabTitles;
		this.tabIcons = tabIcons;
	}

	@Override
	public Fragment getItem(int position) {
		if(fragments == null || fragments.isEmpty()) {
			return null;
		}
		return this.fragments.get(position);
	}

	@Override
	public CharSequence getPageTitle(int position) {
		if(tabTitles == null || tabTitles.isEmpty()) {
			return null;
		}
		return tabTitles.get(position);
	}

	public Integer getTabIcons(int position) {
		if(tabIcons == null || tabIcons.isEmpty()) {
			return null;
		}
		return tabIcons.get(position);
	}

	@Override
	public int getCount() {
		return this.fragments.size();
	}
}