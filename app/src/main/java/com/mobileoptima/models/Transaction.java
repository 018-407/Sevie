package com.mobileoptima.models;

public class Transaction {
	public String ID;
	public String dDate;
	public String dTime;
	public String syncBatchID;
	public String webID;
	public Employee employee = new Employee();
	public GPS gps = new GPS();
	public int batteryLevel;
	public boolean isFromWeb;
	public boolean isSync;
	public boolean isUpdate;
	public boolean isWebUpdate;
	public boolean isDelete;
	public boolean isWebDelete;
}