package com.mobileoptima.models;

public class Expense extends Transaction {
	public String name;
	public String origin;
	public String destination;
	public String notes;
	public Store store = new Store();
	public TimeIn timeIn = new TimeIn();
	public ExpenseType expenseType = new ExpenseType();
	public float amount;
	public boolean isReimbursable;
	public boolean isSubmit;
}