package com.mobileoptima.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.library.Utils.Cache;
import com.android.library.widgets.CustomTextView;
import com.android.library.widgets.ViewPagerAdapter;
import com.mobileoptima.tarkieattendance.R;

public class PageSlidingTab extends HorizontalScrollView {
	private Context context;
	private ViewPager viewPager;
	private int maxScrollItems = 2;
	private Typeface light, semiBold;

	public PageSlidingTab(Context context) {
		this(context, null);
	}

	public PageSlidingTab(Context context, AttributeSet attrs) {
		this(context, attrs, android.R.attr.horizontalScrollViewStyle);
	}

	public PageSlidingTab(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = context;
		light = Cache.getTypeface(context.getAssets(), context.getString(R.string.proxima_nova_light));
		semiBold = Cache.getTypeface(context.getAssets(), context.getString(R.string.proxima_nova_semi_bold));
	}

	public void setViewPager(ViewPager viewPager) {
		this.viewPager = viewPager;
		init();
	}

	public void setMaxScrollItems(int maxScrollItems) {
		if(maxScrollItems <= 2) {
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
					View tabItem = LayoutInflater.from(context).inflate(R.layout.page_sliding_tab_item, container, false);
					CustomTextView tvTitlePageSlidingTab = tabItem.findViewById(R.id.tvTitlePageSlidingTab);
					ImageView ivIndicatorPageSlidingTab = tabItem.findViewById(R.id.ivIndicatorPageSlidingTab);
					tabItem.getLayoutParams().width = context.getResources().getDisplayMetrics().widthPixels / maxScrollItems;
					tabItem.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View view) {
							viewPager.setCurrentItem(position, false);
						}
					});
					CharSequence title = adapter.getPageTitle(position);
					if(title != null) {
						tvTitlePageSlidingTab.setText(title);
					}
					else {
						tvTitlePageSlidingTab.setVisibility(GONE);
					}
					if(x == 0) {
						tvTitlePageSlidingTab.setTypeface(semiBold);
						ivIndicatorPageSlidingTab.setVisibility(VISIBLE);
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
								((CustomTextView) container.getChildAt(x).findViewById(R.id.tvTitlePageSlidingTab)).setTypeface(position == x ? semiBold : light);
								container.getChildAt(x).findViewById(R.id.ivIndicatorPageSlidingTab).setVisibility(position == x ? VISIBLE : INVISIBLE);
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