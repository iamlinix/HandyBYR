package com.lin.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseOperator {

	public static long insert(SQLiteHelper helper, ContentValues cv) {
		SQLiteDatabase db = helper.getWritableDatabase();
		return db.insert(DatabaseModel.TABLE_NAME, null, cv);
	}
	
	public static void executeWrite(SQLiteHelper helper, String sql) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL(sql);
	}
	
	public static void executeRead(SQLiteHelper helper, String sql) {
		SQLiteDatabase db = helper.getReadableDatabase();
		db.execSQL(sql);
	}
	
	public static Cursor query(SQLiteHelper helper, String sql, String[] whereParams) {
		SQLiteDatabase db = helper.getReadableDatabase();
		return db.rawQuery(sql, whereParams);
	}
	
	public static Cursor select(SQLiteHelper helper, String configItem) {
		SQLiteDatabase db = helper.getReadableDatabase();
		return db.rawQuery("SELECT " + DatabaseModel.VALUE_COL + " from " + 
				DatabaseModel.TABLE_NAME + " where " + DatabaseModel.NAME_COL + "=?", 
				new String[] {configItem});
	}
	
	public static Cursor selectAndInsertIfNotExist(SQLiteHelper helper, String configItem) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor csr = db.rawQuery("SELECT " + DatabaseModel.VALUE_COL + " from " + 
				DatabaseModel.TABLE_NAME + " where " + DatabaseModel.NAME_COL + "=?", 
				new String[] {configItem});
		if(csr.moveToNext()) {
			csr.moveToPrevious();
		} else {
			//not exist
			db.execSQL("INSERT INTO " + DatabaseModel.TABLE_NAME + " values ('" + configItem +
					"', '')");
			csr = db.rawQuery("SELECT " + DatabaseModel.VALUE_COL + " from " + 
					DatabaseModel.TABLE_NAME + " where " + DatabaseModel.NAME_COL + "=?", 
					new String[] {configItem});
		}
		return csr;
	}
	
	public static void updateAndInsertIfNotExist(SQLiteHelper helper, String configItem, String value) {
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor csr = db.rawQuery(String.format("SELECT %s FROM %s WHERE %s=?", DatabaseModel.VALUE_COL, 
				DatabaseModel.TABLE_NAME, DatabaseModel.NAME_COL), new String[] {configItem});
		if(csr.moveToNext()) {
			db.execSQL(String.format("UPDATE %s SET %s = '%s' WHERE %s = '%s'", 
					DatabaseModel.TABLE_NAME, DatabaseModel.VALUE_COL, value, 
					DatabaseModel.NAME_COL, configItem));
		} else {
			//no such item, insert it
			db.execSQL(String.format("INSERT INTO %s VALUES ('%s', '%s')", DatabaseModel.TABLE_NAME,
					configItem, value));
		}
	}
	
}
