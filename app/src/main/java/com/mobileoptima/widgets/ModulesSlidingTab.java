package com.mobileoptima.widgets;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
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
	private LinearLayout container;
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
		}
	}

	public void init() {
		if(viewPager != null) {
			ViewPagerAdapter adapter = (ViewPagerAdapter) viewPager.getAdapter();
			if(adapter != null && adapter.getCount() != 0) {
				setFillViewport(true);
				setWillNotDraw(false);
				setHorizontalScrollBarEnabled(false);
				if(container == null) {
					removeAllViews();
					container = new LinearLayout(context);
					container.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
					container.setOrientation(LinearLayout.HORIZONTAL);
					addView(container);
					Log.e("paul", "haha");
					viewPager.addOnPageChangeListener(new OnPageChangeListener() {
						@Override
						public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
							Log.e("paul", "onPageScrolled");
							if(container.getChildCount() != 0) {
								if(position >= 0 && (position + 1) < container.getChildCount()) {
									ImageView ivIndicatorModulesSlidingTab1 = container.getChildAt(position).findViewById(R.id.ivIndicatorModulesSlidingTab);
									int width1 = ivIndicatorModulesSlidingTab1.getWidth();
									ivIndicatorModulesSlidingTab1.setX(positionOffset * width1);
									ivIndicatorModulesSlidingTab1.setVisibility(VISIBLE);
									ImageView ivIndicatorModulesSlidingTab2 = container.getChildAt(position + 1).findViewById(R.id.ivIndicatorModulesSlidingTab);
									int width2 = ivIndicatorModulesSlidingTab2.getWidth();
									ivIndicatorModulesSlidingTab2.setX((positionOffset * width2) - width2);
									ivIndicatorModulesSlidingTab2.setVisibility(VISIBLE);
									if((position + 1) != viewPager.getCurrentItem()) {
										if(position < viewPager.getCurrentItem()) {
											ivIndicatorModulesSlidingTab1.setVisibility(INVISIBLE);
										}
										if(position > viewPager.getCurrentItem()) {
											ivIndicatorModulesSlidingTab2.setVisibility(INVISIBLE);
										}
									}
								}
							}
						}

						@Override
						public void onPageSelected(int position) {
							Log.e("paul", "onPageSelected");
							((ViewPagerAdapter) viewPager.getAdapter()).getItem(position).onResume();
						}

						@Override
						public void onPageScrollStateChanged(int state) {
							Log.e("paul", "onPageScrollStateChanged");
							for(int x = 0; x < container.getChildCount(); x++) {
								if(viewPager.getCurrentItem() != x) {
									container.getChildAt(x).findViewById(R.id.ivIndicatorModulesSlidingTab).setVisibility(INVISIBLE);
								}
							}
						}
					});
					Log.e("paul", "hehe");
				}
				Log.e("paul", "modules width: " + getWidth());
				for(int x = 0; x < adapter.getCount(); x++) {
					final int position = x;
					View tabItem = LayoutInflater.from(context).inflate(R.layout.modules_sliding_tab_item, container, false);
					ImageView ivIconModulesSlingTab = tabItem.findViewById(R.id.ivIconModulesSlingTab);
					TextView tvTitleModulesSlidingTab = tabItem.findViewById(R.id.tvTitleModulesSlidingTab);
					ImageView ivIndicatorModulesSlidingTab = tabItem.findViewById(R.id.ivIndicatorModulesSlidingTab);
					tabItem.getLayoutParams().width = getWidth() / maxScrollItems;
					tabItem.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View view) {
							viewPager.setCurrentItem(position, true);
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
					container.addView(tabItem);
				}
			}
		}
	}
}