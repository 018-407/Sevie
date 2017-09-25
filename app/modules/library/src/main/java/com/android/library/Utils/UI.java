package com.android.library.Utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

public class UI {
	public static void addFragment(FragmentManager manager, int containerViewID, Fragment fragment) {
		addFragment(manager, containerViewID, fragment, 0, 0, 0, 0);
	}

	public static void addFragment(FragmentManager manager, int containerViewID, Fragment fragment, int enter, int exit) {
		addFragment(manager, containerViewID, fragment, enter, exit, 0, 0);
	}

	public static void addFragment(FragmentManager manager, int containerViewID, Fragment fragment, int enter, int exit, int popEnter, int popExit) {
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.setCustomAnimations(enter, exit, popEnter, popExit);
		transaction.add(containerViewID, fragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}

	public static boolean closeDrawer(DrawerLayout drawer) {
		boolean isDrawerOpen = drawer.isDrawerOpen(GravityCompat.START);
		if(isDrawerOpen) {
			drawer.closeDrawer(GravityCompat.START);
		}
		return isDrawerOpen;
	}

	public static void hideKeyboard(Context context, View view) {
		((InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	public static void hideSystemUi(Window window, boolean fullScreen, boolean hideStatusBar, boolean hideNavigationBar, boolean immersive) {
		View decorView = window.getDecorView();
		window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
		int flag = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
		if(fullScreen) {
			if(hideStatusBar) {
				flag |= View.SYSTEM_UI_FLAG_FULLSCREEN;
			}
			else {
				if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
					window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
				}
			}
			if(hideNavigationBar) {
				flag |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
			}
			else {
				if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
					window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
				}
			}
			if(hideStatusBar || hideNavigationBar) {
				flag |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
				if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && immersive) {
					flag |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
				}
				window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			}
		}
		else {
			window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		}
		decorView.setSystemUiVisibility(flag);
	}

	public static boolean openDrawer(DrawerLayout drawer) {
		boolean isDrawerClosed = !drawer.isDrawerOpen(GravityCompat.START);
		if(isDrawerClosed) {
			drawer.openDrawer(GravityCompat.START);
		}
		return isDrawerClosed;
	}
}