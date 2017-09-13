package com.android.library.Sqlite;

import android.content.Context;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;
import net.sqlcipher.database.SQLiteStatement;

import java.io.File;
import java.util.ArrayList;

public class SQLiteAdapter {
	static {
		System.loadLibrary("ndkLib");
	}

	private SQLiteDatabase db;
	private SQLiteOpenHelper helper;
	private String password;

	public SQLiteAdapter(Context context, String database, int version) {
		password = getCipherKey();
		SQLiteDatabase.loadLibs(context);
		File file = context.getDatabasePath(database);
		if(!file.exists()) {
			file.mkdirs();
			file.delete();
		}
		try {
			File tempFile = context.getDatabasePath("temp.db");
			tempFile.mkdirs();
			String tempPath = tempFile.getAbsolutePath();
			tempFile.delete();
			db = SQLiteDatabase.openOrCreateDatabase(file, "", null);
			db.rawExecSQL(String.format("ATTACH DATABASE '%s' AS encrypted KEY '%s'", tempPath, password));
			db.rawExecSQL("SELECT sqlcipher_export('encrypted');");
			db.rawExecSQL("DETACH DATABASE encrypted;");
			db.close();
			if(file.delete()) {
				tempFile.renameTo(file);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		helper = new SQLiteHelper(context, database, version);
	}

	private static native String getCipherKey();

	private void getWritableDatabase() {
		db = helper.getWritableDatabase(password);
	}

	public void createTable(String table, String fields) {
		execSQL("CREATE TABLE IF NOT EXISTS " + table + " (ID " + DataType.INTEGER.getValue() + " PRIMARY KEY AUTOINCREMENT NOT NULL" + fields + ")");
	}

	public void dropTable(String table) {
		execSQL("DROP TABLE IF EXISTS " + table);
	}

	public void addColumn(String table, String column, DataType dataType) {
		execSQL("ALTER TABLE " + table + " ADD " + column + " " + dataType.getValue());
	}

	public void addColumn(String table, String column, String defaultValue) {
		execSQL("ALTER TABLE " + table + " ADD " + column + " " + DataType.TEXT.getValue() + " DEFAULT " + (defaultValue != null ? defaultValue : "NULL"));
	}

	public void addColumn(String table, String column, Integer defaultValue) {
		execSQL("ALTER TABLE " + table + " ADD " + column + " " + DataType.INTEGER.getValue() + " DEFAULT " + (defaultValue != null ? defaultValue : 0));
	}

	public long insert(String table, ArrayList<FieldValue> fieldValues) {
		if(fieldValues.isEmpty()) {
			return 0;
		}
		StringBuilder fields = new StringBuilder();
		StringBuilder values = new StringBuilder();
		for(FieldValue obj : fieldValues) {
			if(fields.length() > 0) {
				fields.append(", ");
			}
			fields.append(obj.field);
			if(values.length() > 0) {
				values.append(", ");
			}
			values.append("'" + obj.value + "'");
		}
		getWritableDatabase();
		SQLiteStatement statement = db.compileStatement("INSERT INTO " + table + "(" + fields.toString() + ") VALUES(" + values.toString() + ")");
		long result = statement.executeInsert();
		statement.close();
		return result;
	}

	public boolean update(String table, ArrayList<FieldValue> fieldValues, String ID) {
		if(fieldValues.isEmpty() || ID == null || ID.isEmpty()) {
			return false;
		}
		getWritableDatabase();
		SQLiteStatement statement = db.compileStatement("UPDATE " + table + " SET " + fieldValuesToUpdateString(fieldValues) + " WHERE ID = '" + ID + "'");
		int result = statement.executeUpdateDelete();
		statement.close();
		return result > 0;
	}

	public boolean update(String table, ArrayList<FieldValue> fieldValues, ArrayList<Condition> conditions) {
		if(fieldValues.isEmpty() || conditions == null || conditions.isEmpty()) {
			return false;
		}
		getWritableDatabase();
		SQLiteStatement statement = db.compileStatement("UPDATE " + table + " SET " + fieldValuesToUpdateString(fieldValues) + " WHERE " + conditionsToString(conditions));
		int result = statement.executeUpdateDelete();
		statement.close();
		return result > 0;
	}

	public boolean delete(String table, String ID) {
		if(ID == null || ID.isEmpty()) {
			return false;
		}
		getWritableDatabase();
		SQLiteStatement statement = db.compileStatement("DELETE FROM " + table + " WHERE ID = '" + ID + "'");
		int result = statement.executeUpdateDelete();
		statement.close();
		return result > 0;
	}

	public boolean delete(String table, ArrayList<Condition> conditions) {
		if(conditions == null || conditions.isEmpty()) {
			return false;
		}
		getWritableDatabase();
		SQLiteStatement statement = db.compileStatement("DELETE FROM " + table + " WHERE " + conditionsToString(conditions));
		int result = statement.executeUpdateDelete();
		statement.close();
		return result > 0;
	}

	private String fieldValuesToUpdateString(ArrayList<FieldValue> list) {
		StringBuilder fieldValue = new StringBuilder();
		for(FieldValue obj : list) {
			if(fieldValue.length() != 0) {
				fieldValue.append(", ");
			}
			fieldValue.append(obj.field + " " + Operator.EQUALS.getValue() + " '" + obj.value + "'");
		}
		return fieldValue.toString();
	}

	private String conditionsToString(ArrayList<Condition> conditions) {
		StringBuilder condition = new StringBuilder();
		for(Condition obj : conditions) {
			if(condition.length() != 0) {
				condition.append(" AND ");
			}
			if(obj.operator == Operator.IS_NULL || obj.operator == Operator.IS_NOT_NULL || obj.operator == Operator.IS_EMPTY || obj.operator == Operator.IS_NOT_EMPTY) {
				condition.append(obj.field + " " + obj.operator.getValue());
			}
			else {
				condition.append(obj.field + " " + obj.operator.getValue() + " '" + obj.value + "'");
			}
		}
		return condition.toString();
	}

	public void beginTransaction() {
		getWritableDatabase();
		db.beginTransaction();
	}

	public boolean endTransaction() {
		getWritableDatabase();
		boolean result = false;
		try {
			db.setTransactionSuccessful();
			result = true;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			db.endTransaction();
		}
		return result;
	}

	private void execSQL(String query) {
		getWritableDatabase();
		SQLiteStatement statement = db.compileStatement(query);
		statement.execute();
		statement.close();
	}

	public Cursor rawQuery(String query) {
		getWritableDatabase();
		return db.rawQuery(query, null);
	}

	public int getCount(String query) {
		Cursor cursor = rawQuery(query);
		if(cursor == null) {
			return 0;
		}
		int value = cursor.getCount();
		cursor.close();
		return value;
	}

	public String getString(String query) {
		Cursor cursor = rawQuery(query);
		if(cursor == null) {
			return null;
		}
		if(!cursor.moveToLast()) {
			cursor.close();
			return null;
		}
		String value = cursor.getString(0);
		cursor.close();
		return value;
	}

	public int getInt(String query) {
		Cursor cursor = rawQuery(query);
		if(cursor == null) {
			return 0;
		}
		if(!cursor.moveToLast()) {
			cursor.close();
			return 0;
		}
		int value = cursor.getInt(0);
		cursor.close();
		return value;
	}

	public long getLong(String query) {
		Cursor cursor = rawQuery(query);
		if(cursor == null) {
			return 0;
		}
		if(!cursor.moveToLast()) {
			cursor.close();
			return 0;
		}
		long value = cursor.getLong(0);
		cursor.close();
		return value;
	}

	public float getFloat(String query) {
		Cursor cursor = rawQuery(query);
		if(cursor == null) {
			return 0;
		}
		if(!cursor.moveToLast()) {
			cursor.close();
			return 0;
		}
		float value = cursor.getFloat(0);
		cursor.close();
		return value;
	}

	public double getDouble(String query) {
		Cursor cursor = rawQuery(query);
		if(cursor == null) {
			return 0;
		}
		if(!cursor.moveToLast()) {
			cursor.close();
			return 0;
		}
		double value = cursor.getDouble(0);
		cursor.close();
		return value;
	}

	private class SQLiteHelper extends SQLiteOpenHelper {
		private SQLiteHelper(Context context, String database, int version) {
			super(context, database, null, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		}
	}
}