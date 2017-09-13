package com.android.library.Sqlite;

public class Condition {
	public String field, value;
	public Operator operator;

	public Condition(FieldValue fieldValue) {
		this.field = fieldValue.field;
		this.value = fieldValue.value;
		this.operator = Operator.EQUALS;
	}

	public Condition(FieldValue fieldValue, Operator operator) {
		this.field = fieldValue.field;
		this.value = fieldValue.value;
		this.operator = operator;
	}

	public Condition(Field field, Operator operator) {
		this.field = field.field;
		this.operator = operator;
	}
}