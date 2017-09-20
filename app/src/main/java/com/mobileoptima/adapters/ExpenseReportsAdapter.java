package com.mobileoptima.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.android.library.Utils.Time;
import com.android.library.widgets.CustomTextView;
import com.mobileoptima.models.ExpenseReport;
import com.mobileoptima.tarkieattendance.R;

import java.text.NumberFormat;
import java.util.ArrayList;

public class ExpenseReportsAdapter extends ArrayAdapter<ExpenseReport> {
	private ArrayList<ExpenseReport> items;
	private Context context;
	private NumberFormat nf;
	private int orange_500, theme_sec;

	public ExpenseReportsAdapter(Context context, ArrayList<ExpenseReport> items) {
		super(context, 0, items);
		this.context = context;
		this.items = items;
		nf = NumberFormat.getInstance();
		nf.setGroupingUsed(true);
		nf.setMinimumFractionDigits(2);
		nf.setMaximumFractionDigits(2);
		orange_500 = ContextCompat.getColor(context, R.color.orange_500);
		theme_sec = ContextCompat.getColor(context, R.color.theme_sec);
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		if(view == null) {
			view = LayoutInflater.from(context).inflate(R.layout.expense_reports_list_row, parent, false);
			ViewHolder holder = new ViewHolder();
			holder.tvNameExpenseReports = view.findViewById(R.id.tvNameExpenseReports);
			holder.tvStatusExpenseReports = view.findViewById(R.id.tvStatusExpenseReports);
			holder.tvTotalExpenseReports = view.findViewById(R.id.tvTotalExpenseReports);
			holder.tvDateExpenseReports = view.findViewById(R.id.tvDateExpenseReports);
			view.setTag(holder);
		}
		ExpenseReport obj = items.get(position);
		if(obj != null) {
			ViewHolder holder = (ViewHolder) view.getTag();
			holder.tvNameExpenseReports.setText(obj.name);
			holder.tvStatusExpenseReports.setText(obj.isSubmit ? "Submitted" : "Draft");
			holder.tvStatusExpenseReports.setTextColor(obj.isSubmit ? theme_sec : orange_500);
			holder.tvTotalExpenseReports.setText("PHP " + nf.format(obj.totalAmount));
			if(obj.dDate != null) {
				holder.tvDateExpenseReports.setText(Time.formatDateTime(obj.dDate, "yyyy-MM-dd", "MMM dd, yyyy"));
			}
		}
		return view;
	}

	private class ViewHolder {
		private CustomTextView tvNameExpenseReports;
		private CustomTextView tvStatusExpenseReports;
		private CustomTextView tvTotalExpenseReports;
		private CustomTextView tvDateExpenseReports;
	}
}