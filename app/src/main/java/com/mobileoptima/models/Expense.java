package com.mobileoptima.models;

public class Expense extends Transaction {
	public String notes;
	public String origin;
	public String destination;
	public Store store;
	public MasterExpenseType type;
	public boolean isReimbursable, isUpdate, isSubmit;
	public float amount;
}