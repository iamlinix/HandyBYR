package com.lin.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper{
	
	public SQLiteHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s ( %s VARCHAR(256), %s VARCHAR(256))",
				DatabaseModel.TABLE_NAME, DatabaseModel.NAME_COL, DatabaseModel.VALUE_COL));
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		if(oldVersion != newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + DatabaseModel.TABLE_NAME);
			onCreate(db);
		}
	}

}
