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

public class PageSlidingTab extends HorizontalScrollView {
	private Context context;
	private LinearLayout container;
	private ViewPager viewPager;
	private int maxScrollItems = 2;

	public PageSlidingTab(Context context) {
		super(context);
		this.context = context;
	}

	public PageSlidingTab(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public PageSlidingTab(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = context;
	}

	public void setViewPager(ViewPager viewPager) {
		this.viewPager = viewPager;
		init();
	}

	public void setMaxScrollItems(int maxScrollItems) {
		if(maxScrollItems <= 2) {
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
					viewPager.addOnPageChangeListener(new OnPageChangeListener() {
						@Override
						public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
							if(container.getChildCount() != 0) {
								if(position >= 0 && (position + 1) < container.getChildCount()) {
									ImageView ivIndicatorModulesSlidingTab1 = container.getChildAt(position).findViewById(R.id.ivIndicatorPageSlidingTab);
									int width1 = ivIndicatorModulesSlidingTab1.getWidth();
									ivIndicatorModulesSlidingTab1.setX(positionOffset * width1);
									ivIndicatorModulesSlidingTab1.setVisibility(VISIBLE);
									ImageView ivIndicatorModulesSlidingTab2 = container.getChildAt(position + 1).findViewById(R.id.ivIndicatorPageSlidingTab);
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
							((ViewPagerAdapter) viewPager.getAdapter()).getItem(position).onResume();
						}

						@Override
						public void onPageScrollStateChanged(int state) {
							for(int x = 0; x < container.getChildCount(); x++) {
								if(viewPager.getCurrentItem() != x) {
									container.getChildAt(x).findViewById(R.id.ivIndicatorPageSlidingTab).setVisibility(INVISIBLE);
								}
							}
						}
					});
				}
				Log.e("paul", "page width: " + container.getWidth());
				for(int x = 0; x < adapter.getCount(); x++) {
					final int position = x;
					View tabItem = LayoutInflater.from(context).inflate(R.layout.page_sliding_tab_item, container, false);
					TextView tvTitlePageSlidingTab = tabItem.findViewById(R.id.tvTitlePageSlidingTab);
					ImageView ivIndicatorPageSlidingTab = tabItem.findViewById(R.id.ivIndicatorPageSlidingTab);
					tabItem.getLayoutParams().width = getWidth() / maxScrollItems;
					tabItem.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View view) {
							viewPager.setCurrentItem(position, true);
						}
					});
					CharSequence title = adapter.getPageTitle(position);
					if(title != null) {
						tvTitlePageSlidingTab.setText(title);
					}
					else {
						tvTitlePageSlidingTab.setVisibility(GONE);
					}
					container.addView(tabItem);
				}
			}
		}
	}
}