package com.mobileoptima.models;

import java.util.ArrayList;

public class ExpenseReport extends Transaction {
	public String name;
	public String subject;
	public String message;
	public String signature;
	public String reportNo;
	public String approverID;
	public String dateSubmitted;
	public String timeSubmitted;
	public boolean isSubmit;
	public float totalAmount;
	public float totalReimbursable;
	public ArrayList<String> taggedDates;
}