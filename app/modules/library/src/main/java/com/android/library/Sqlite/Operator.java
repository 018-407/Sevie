package com.android.library.Sqlite;

public enum Operator {
	EQUALS("="),
	NOT_EQUALS("!="),
	GREATER_THAN(">"),
	GREATER_THAN_OR_EQUALS(">="),
	LESS_THAN("<"),
	LESS_THAN_OR_EQUALS("<="),
	IS_NULL("IS NULL"),
	IS_NOT_NULL("IS NOT NULL"),
	IS_EMPTY("= ''"),
	IS_NOT_EMPTY("!= ''");
	String value;

	Operator(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}