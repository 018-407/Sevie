package com.mobileoptima.models;

public class Visit extends Transaction {
	public String name;
	public String dateStart;
	public String dateEnd;
	public String deliveryFee;
	public String mappingCode;
	public String notes;
	public String status;
	public Store store = new Store();
	public CheckIn checkIn = new CheckIn();
	public CheckOut checkOut = new CheckOut();
}