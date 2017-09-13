package com.android.library.Sqlite;

public enum DataType {
	TEXT("TEXT"),
	INTEGER("INTEGER");
	final String value;

	DataType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}