package com.mobileoptima.tarkieattendance;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.library.Sqlite.SQLiteAdapter;
import com.android.library.Utils.UI;
import com.android.library.widgets.CustomTextView;
import com.mobileoptima.constants.Action;
import com.mobileoptima.data.Rx;
import com.mobileoptima.data.Tx;
import com.mobileoptima.interfaces.Callback.OnBackPressedCallback;
import com.mobileoptima.interfaces.Callback.OnErrorCallback;
import com.mobileoptima.interfaces.Callback.OnRefreshCallback;

public class LoadingDialogFragment extends Fragment implements OnBackPressedCallback, OnRefreshCallback, OnErrorCallback {
	private Action action;
	private AlertDialogFragment alert;
	private FragmentManager manager;
	private Handler handler;
	private MainActivity main;
	private OnRefreshCallback refreshCallback;
	private ProgressBar pbLoadingDialog;
	private SQLiteAdapter db;
	private String message;
	private CustomTextView tvProgressLoadingDialog, tvCompletionLoadingDialog;
	private Thread thread;
	private boolean isSaveInstanceState, result, isFinish;
	private int progress, max, percent;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		main = (MainActivity) getActivity();
		manager = main.getSupportFragmentManager();
		db = main.getSQLiteAdapater();
		final Bundle bundle = getArguments();
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					progress = 0;
					switch(action) {
						case AUTHORIZE_DEVICE:
							max = 4;
							result = Tx.authorizeDevice(db, bundle.getString("DEVICE_CODE"), bundle.getString("DEVICE_ID"), LoadingDialogFragment.this);
							Thread.sleep(250);
							handler.sendMessage(handler.obtainMessage());
							if(result) {
//								Rx.serverTime(db, LoadingDialogFragment.this);
//								Thread.sleep(250);
								handler.sendMessage(handler.obtainMessage());
								Rx.company(db, LoadingDialogFragment.this);
								Thread.sleep(250);
								handler.sendMessage(handler.obtainMessage());
								Rx.convention(db, LoadingDialogFragment.this);
								Thread.sleep(250);
								handler.sendMessage(handler.obtainMessage());
							}
							break;
						case LOGIN:
							max = 2;
							result = Tx.login(db, bundle.getString("USERNAME"), bundle.getString("PASSWORD"), LoadingDialogFragment.this);
							Thread.sleep(250);
							handler.sendMessage(handler.obtainMessage());
							if(result) {
								Rx.employees(db, LoadingDialogFragment.this);
								Thread.sleep(250);
								handler.sendMessage(handler.obtainMessage());
							}
							break;
						case VALIDATE_TIME:
							max = 1;
							result = Rx.serverTime(db, LoadingDialogFragment.this);
							Thread.sleep(250);
							handler.sendMessage(handler.obtainMessage());
							break;
						case UPDATE_MASTER_FILE:
							max = 9;
							result = Rx.syncBatchID(db, LoadingDialogFragment.this);
							Thread.sleep(250);
							handler.sendMessage(handler.obtainMessage());
							if(result) {
								result = Rx.company(db, LoadingDialogFragment.this);
								Thread.sleep(250);
								handler.sendMessage(handler.obtainMessage());
							}
							if(result) {
								result = Rx.convention(db, LoadingDialogFragment.this);
								Thread.sleep(250);
								handler.sendMessage(handler.obtainMessage());
							}
							if(result) {
								result = Rx.employees(db, LoadingDialogFragment.this);
								Thread.sleep(250);
								handler.sendMessage(handler.obtainMessage());
							}
							if(result) {
								result = Rx.alertTypes(db, LoadingDialogFragment.this);
								Thread.sleep(250);
								handler.sendMessage(handler.obtainMessage());
							}
							if(result) {
								result = Rx.breakTypes(db, LoadingDialogFragment.this);
								Thread.sleep(250);
								handler.sendMessage(handler.obtainMessage());
							}
							if(result) {
								result = Rx.expenseTypeCategories(db, LoadingDialogFragment.this);
								Thread.sleep(250);
								handler.sendMessage(handler.obtainMessage());
							}
							if(result) {
								result = Rx.scheduleTimes(db, LoadingDialogFragment.this);
								Thread.sleep(250);
								handler.sendMessage(handler.obtainMessage());
							}
							if(result) {
								result = Rx.overtimeReasons(db, LoadingDialogFragment.this);
								Thread.sleep(250);
								handler.sendMessage(handler.obtainMessage());
							}
							break;
						case SEND_BACK_UP:
							break;
						case SYNC_DATA:
							break;
					}
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
		});
		handler = new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message message) {
				progress++;
				percent = (int) Math.floor(((float) progress / (float) max) * 100f);
				pbLoadingDialog.setProgress(percent);
				tvProgressLoadingDialog.setText(percent + "%");
				tvCompletionLoadingDialog.setText(progress + "/" + max);
				if(progress == max) {
					isFinish = true;
					onRefresh();
				}
				return true;
			}
		});
		message = action.getName() + " successful.";
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.loading_dialog_layout, container, false);
		CustomTextView tvTitleLoadingDialog = view.findViewById(R.id.tvTitleLoadingDialog);
		pbLoadingDialog = view.findViewById(R.id.pbLoadingDialog);
		tvProgressLoadingDialog = view.findViewById(R.id.tvProgressLoadingDialog);
		tvCompletionLoadingDialog = view.findViewById(R.id.tvCompletionLoadingDialog);
		tvTitleLoadingDialog.setText(action.getTitle());
		pbLoadingDialog.setProgress(percent);
		tvProgressLoadingDialog.setText(percent + "%");
		tvCompletionLoadingDialog.setText(progress + "/" + max);
		thread.start();
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		isSaveInstanceState = false;
		onRefresh();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		isSaveInstanceState = true;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		thread.interrupt();
		refreshCallback.onRefresh();
	}

	@Override
	public void onBackPressed() {
		if(!isFinish) {
			alert = new AlertDialogFragment();
			alert.setOnRefreshCallback(this);
			alert.setTitle(action.getName());
			alert.setMessage("Are you sure you want to cancel " + action.getName() + "?");
			alert.setPositiveButton("Yes", new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					manager.popBackStack();
					manager.popBackStack();
				}
			});
			alert.setNegativeButton("No", new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					manager.popBackStack();
				}
			});
			UI.addFragment(manager, R.id.rlMain, alert);
			return;
		}
		onRefresh();
	}

	@Override
	public void onRefresh() {
		if(isSaveInstanceState) {
			return;
		}
		main.setOnBackPressedCallback(this);
		if(isFinish) {
			isFinish = false;
			if(alert != null) {
				alert = null;
				manager.popBackStack();
			}
			alert = new AlertDialogFragment();
			alert.setOnBackPressedCallback(new OnBackPressedCallback() {
				@Override
				public void onBackPressed() {
					manager.popBackStack();
					manager.popBackStack();
				}
			});
			alert.setOnRefreshCallback(this);
			alert.setTitle(action.getName());
			alert.setMessage(message);
			alert.setPositiveButton("OK", new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					manager.popBackStack();
					manager.popBackStack();
				}
			});
			UI.addFragment(manager, R.id.rlMain, alert);
		}
	}

	@Override
	public void onError(String params, String response, String error, boolean showError) {
		isFinish = true;
		message = "Failed to " + action.getName() + ".";
		if(showError) {
			message += " " + error;
		}
		onRefresh();
	}

	public void setOnRefreshCallback(OnRefreshCallback refreshCallback) {
		this.refreshCallback = refreshCallback;
	}

	public void setAction(Action action) {
		this.action = action;
	}
}