package com.mobileoptima.widgets;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.library.widgets.ViewPagerAdapter;
import com.mobileoptima.tarkieattendance.R;

public class ModulesSlidingTab extends HorizontalScrollView {
	private Context context;
	private ViewPager viewPager;
	private int maxScrollItems = 6;

	public ModulesSlidingTab(Context context) {
		super(context);
		this.context = context;
	}

	public ModulesSlidingTab(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public ModulesSlidingTab(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = context;
	}

	public void setViewPager(final ViewPager viewPager) {
		this.viewPager = viewPager;
		init();
	}

	public void setMaxScrollItems(int maxScrollItems) {
		if(maxScrollItems <= 6) {
			this.maxScrollItems = maxScrollItems;
			init();
		}
	}

	public void init() {
		setFillViewport(true);
		setHorizontalScrollBarEnabled(false);
		if(getChildCount() != 0) {
			removeAllViews();
		}
		final LinearLayout container = new LinearLayout(context);
		container.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		container.setOrientation(LinearLayout.HORIZONTAL);
		if(viewPager != null) {
			ViewPagerAdapter adapter = (ViewPagerAdapter) viewPager.getAdapter();
			if(adapter != null && adapter.getCount() != 0) {
				for(int x = 0; x < adapter.getCount(); x++) {
					final int position = x;
					View tabItem = LayoutInflater.from(context).inflate(R.layout.modules_sliding_tab_item, container, false);
					ImageView ivIconModulesSlingTab = tabItem.findViewById(R.id.ivIconModulesSlingTab);
					TextView tvTitleModulesSlidingTab = tabItem.findViewById(R.id.tvTitleModulesSlidingTab);
					ImageView ivIndicatorModulesSlidingTab = tabItem.findViewById(R.id.ivIndicatorModulesSlidingTab);
					tabItem.getLayoutParams().width = context.getResources().getDisplayMetrics().widthPixels / maxScrollItems;
					tabItem.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View view) {
							viewPager.setCurrentItem(position, false);
						}
					});
					Integer icon = adapter.getTabIcons(position);
					if(icon != null) {
						ivIconModulesSlingTab.setImageResource(icon);
					}
					else {
						ivIconModulesSlingTab.setVisibility(GONE);
					}
					CharSequence title = adapter.getPageTitle(position);
					if(title != null) {
						tvTitleModulesSlidingTab.setText(title);
					}
					else {
						tvTitleModulesSlidingTab.setVisibility(GONE);
					}
					if(x == 0) {
						ivIndicatorModulesSlidingTab.setVisibility(VISIBLE);
					}
					container.addView(tabItem);
				}
				if(container.getChildCount() != 0) {
					viewPager.addOnPageChangeListener(new OnPageChangeListener() {
						@Override
						public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
						}

						@Override
						public void onPageSelected(int position) {
							for(int x = 0; x < container.getChildCount(); x++) {
								container.getChildAt(x).findViewById(R.id.ivIndicatorModulesSlidingTab).setVisibility(position == x ? VISIBLE : INVISIBLE);
							}
							container.requestChildFocus(container.getChildAt(position), container.getChildAt(position));
							((ViewPagerAdapter) viewPager.getAdapter()).getItem(position).onResume();
						}

						@Override
						public void onPageScrollStateChanged(int state) {
						}
					});
				}
			}
		}
		addView(container);
	}
}