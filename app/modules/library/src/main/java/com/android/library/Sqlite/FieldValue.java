package com.android.library.Sqlite;

public class FieldValue {
	public String field;
	public String value;

	public FieldValue(String field, String value) {
		this.field = field;
		this.value = value;
	}

	public FieldValue(String field, int value) {
		this.field = field;
		this.value = String.valueOf(value);
	}

	public FieldValue(String field, long value) {
		this.field = field;
		this.value = String.valueOf(value);
	}

	public FieldValue(String field, float value) {
		this.field = field;
		this.value = String.valueOf(value);
	}

	public FieldValue(String field, double value) {
		this.field = field;
		this.value = String.valueOf(value);
	}

	public FieldValue(String field, boolean value) {
		this.field = field;
		this.value = value ? "1" : "0";
	}
}