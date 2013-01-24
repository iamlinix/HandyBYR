package com.lin.database;

public class DatabaseModel {
	
	public static final int version					= 1;
	
	public static final String DB_NAME 				= "com.lin.handybyr.database";
	public static final String TABLE_NAME			= "configtable";
	public static final String NAME_COL				= "name";
	public static final String VALUE_COL			= "value";
	
	public static class DataDictionary{
		public static final String REMEMBER_ME		= "remember_me";
		public static final String AUTO_LOGIN		= "auto_login";
		public static final String USER_NAME		= "user_name";
		public static final String PASSWORD			= "passwrod";
	}
}
