package com.niw.kuttypanda;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class SQLiteDatabaseAdapter {

	private static SQLiteDatabaseHelper dbHelper = null;

	private static SQLiteDatabase mDB = null;

	private static Context contextm = null;

	public static void setContext(Context _context) {
		dbHelper = new SQLiteDatabaseHelper(_context);
		setContextm(_context);
	}

	/**
	 * Method open database.
	 * 
	 * @return the database
	 */
	public static SQLiteDatabase openDB() {
		return SQLiteDatabaseAdapter.dbHelper.getWritableDatabase();
	}

	public static void closeDB() {
		SQLiteDatabaseAdapter.dbHelper.close();
	}

	/**
	 * @return the mDB
	 */
	public static SQLiteDatabase getmDB() {
		return mDB;
	}

	/**
	 * @param mDB the mDB to set
	 */
	public static void setmDB(SQLiteDatabase mDB) {
		SQLiteDatabaseAdapter.mDB = mDB;
	}

	/**
	 * @return the contextm
	 */
	public static Context getContextm() {
		return contextm;
	}

	/**
	 * @param contextm the contextm to set
	 */
	public static void setContextm(Context contextm) {
		SQLiteDatabaseAdapter.contextm = contextm;
	}
}
