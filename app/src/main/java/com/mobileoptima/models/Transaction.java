package com.mobileoptima.models;

public class Transaction {
	public String ID;
	public String dDate;
	public String dTime;
	public String syncBatchID;
	public String webID;
	public MasterEmployee employee;
	public GPS gps;
	public boolean isFromWeb;
	public boolean isSync;
	public int batteryLevel;
}