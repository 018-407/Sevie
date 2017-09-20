package com.mobileoptima.models;

import android.view.View;

import java.util.ArrayList;

public class ExpenseItem {
	public String dDate;
	public boolean isAdded;
	public boolean isOpen;
	public boolean isChecked;
	public boolean isSubmit;
	public float totalAmount;
	public ArrayList<View> childList;
}