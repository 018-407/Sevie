package com.mobileoptima.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.library.Utils.Time;
import com.android.library.widgets.CustomTextView;
import com.mobileoptima.models.ExpenseItem;
import com.mobileoptima.tarkieattendance.R;

import java.text.NumberFormat;
import java.util.ArrayList;

public class ExpenseItemsAdapter extends ArrayAdapter<ExpenseItem> {
	private ArrayList<ExpenseItem> items;
	private Context context;
	private NumberFormat nf;
	private int text_pri, theme_sec;

	public ExpenseItemsAdapter(Context context, ArrayList<ExpenseItem> items) {
		super(context, 0, items);
		this.context = context;
		this.items = items;
		nf = NumberFormat.getInstance();
		nf.setGroupingUsed(true);
		nf.setMinimumFractionDigits(2);
		nf.setMaximumFractionDigits(2);
		text_pri = ContextCompat.getColor(context, R.color.text_pri);
		theme_sec = ContextCompat.getColor(context, R.color.theme_sec);
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		if(view == null) {
			view = LayoutInflater.from(context).inflate(R.layout.expense_items_list_row, parent, false);
			ViewHolder holder = new ViewHolder();
			holder.tvDateExpenseItems = view.findViewById(R.id.tvDateExpenseItems);
			holder.tvTotalExpenseItems = view.findViewById(R.id.tvTotalExpenseItems);
			holder.ivCollapsibleExpenseItems = view.findViewById(R.id.ivCollapsibleExpenseItems);
			holder.llItemsExpenseItems = view.findViewById(R.id.llItemsExpenseItems);
			view.setTag(holder);
		}
		ExpenseItem obj = items.get(position);
		if(obj != null) {
			ViewHolder holder = (ViewHolder) view.getTag();
			if(obj.dDate != null) {
				holder.tvDateExpenseItems.setText(Time.formatDateTime(obj.dDate, "yyyy-MM-dd", "MMM dd, yyyy"));
			}
			holder.tvTotalExpenseItems.setText("PHP " + nf.format(obj.totalAmount));
			holder.llItemsExpenseItems.removeAllViews();
			if(obj.isAdded && obj.childList != null) {
				for(View child : obj.childList) {
					if(child != null && child.getParent() != null) {
						((ViewGroup) child.getParent()).removeView(child);
					}
					holder.llItemsExpenseItems.addView(child);
				}
			}
			holder.ivCollapsibleExpenseItems.setImageResource(obj.isOpen ? R.drawable.ic_user_placeholder : R.drawable.ic_user_placeholder);
			holder.llItemsExpenseItems.setVisibility(obj.isOpen ? View.VISIBLE : View.GONE);
			holder.tvDateExpenseItems.setTextColor(obj.isSubmit ? theme_sec : text_pri);
			holder.tvTotalExpenseItems.setTextColor(obj.isSubmit ? theme_sec : text_pri);
		}
		return view;
	}

	private class ViewHolder {
		private CustomTextView tvDateExpenseItems;
		private CustomTextView tvTotalExpenseItems;
		private ImageView ivCollapsibleExpenseItems;
		private LinearLayout llItemsExpenseItems;
	}
}