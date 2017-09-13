package com.android.library.Sqlite;

public class Field {
	public String field;
	public DataType dataType;

	public Field(String field) {
		this.field = field;
	}

	public Field(String field, DataType dataType) {
		this.field = field;
		this.dataType = dataType;
	}
}