package com.mobileoptima.tarkieattendance;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.library.widgets.CustomTextView;
import com.mobileoptima.interfaces.Callback.OnBackPressedCallback;
import com.mobileoptima.interfaces.Callback.OnRefreshCallback;

public class AlertDialogFragment extends Fragment implements OnBackPressedCallback {
	private FragmentManager manager;
	private MainActivity main;
	private OnClickListener positiveButtonOnClick, negativeButtonOnClick;
	private OnBackPressedCallback backPressedCallback;
	private OnRefreshCallback refreshCallback;
	private String title, message, positiveButtonLabel, negativeButtonLabel;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		main = (MainActivity) getActivity();
		manager = main.getSupportFragmentManager();
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.alert_dialog_layout, container, false);
		CustomTextView tvTitleAlertDialog = view.findViewById(R.id.tvTitleAlertDialog);
		CustomTextView tvMessageAlertDialog = view.findViewById(R.id.tvMessageAlertDialog);
		Button btnPositiveAlertDialog = view.findViewById(R.id.btnPositiveAlertDialog);
		Button btnNegativeAlertDialog = view.findViewById(R.id.btnNegativeAlertDialog);
		if(title != null) {
			tvTitleAlertDialog.setText(title);
			tvTitleAlertDialog.setVisibility(View.VISIBLE);
		}
		else {
			tvTitleAlertDialog.setVisibility(View.GONE);
		}
		if(message != null) {
			tvMessageAlertDialog.setText(message);
			tvMessageAlertDialog.setVisibility(View.VISIBLE);
		}
		else {
			tvMessageAlertDialog.setVisibility(View.GONE);
		}
		if(positiveButtonLabel != null && positiveButtonOnClick != null) {
			btnPositiveAlertDialog.setText(positiveButtonLabel);
			btnPositiveAlertDialog.setOnClickListener(positiveButtonOnClick);
			btnPositiveAlertDialog.setVisibility(View.VISIBLE);
		}
		else {
			btnPositiveAlertDialog.setVisibility(View.GONE);
		}
		if(negativeButtonLabel != null && negativeButtonOnClick != null) {
			btnNegativeAlertDialog.setText(negativeButtonLabel);
			btnNegativeAlertDialog.setOnClickListener(negativeButtonOnClick);
			btnNegativeAlertDialog.setVisibility(View.VISIBLE);
		}
		else {
			btnNegativeAlertDialog.setVisibility(View.GONE);
		}
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		main.setOnBackPressedCallback(this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		refreshCallback.onRefresh();
	}

	@Override
	public void onBackPressed() {
		if(backPressedCallback != null) {
			backPressedCallback.onBackPressed();
			return;
		}
		manager.popBackStack();
	}

	public void setOnBackPressedCallback(OnBackPressedCallback backPressedCallback) {
		this.backPressedCallback = backPressedCallback;
	}

	public void setOnRefreshCallback(OnRefreshCallback refreshCallback) {
		this.refreshCallback = refreshCallback;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setPositiveButton(String label, OnClickListener onClick) {
		positiveButtonLabel = label;
		positiveButtonOnClick = onClick;
	}

	public void setNegativeButton(String label, OnClickListener onClick) {
		negativeButtonLabel = label;
		negativeButtonOnClick = onClick;
	}
}