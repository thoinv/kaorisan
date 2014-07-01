package com.kaorisan.dataLayer;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteDatabaseHelper extends SQLiteOpenHelper {

	private static String DB_NAME = "Kaorisan_DB";
	private static int DB_VERSION = 1;
	
	public static String TABLE_CACHE_DATA = "TBL_CACHE_DATA";
	public static String TABLE_CACHE_DATA_COLUMN_PUSHABLE = "Pushable";
	public static String TABLE_CACHE_DATA_COLUMN_KAORISAN_TOKEN = "KaorisanToken";
	public static String TABLE_CACHE_DATA_COLUMN_AVATAR = "Avatar";
	public static String TABLE_CACHE_DATA_COLUMN_NAME = "Name";
	public static String TABLE_CACHE_DATA_COLUMN_CURRENT_TASK_PUSH_ID = "CurrentTaskPushId";

	public SQLiteDatabaseHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// Table cache user
		String scriptCreateDb = "CREATE TABLE "
				+ "TBL_CACHE_DATA( "
				+ "KaorisanToken text, "
				+ "Pushable text,"
				+ "Avatar text,"
				+ "Name text,"
				+ "CurrentTaskPushId integer"
				+ ");";
		db.execSQL(scriptCreateDb);
		
		ContentValues value = new ContentValues();
		value.put(TABLE_CACHE_DATA_COLUMN_KAORISAN_TOKEN,"");
		value.put(TABLE_CACHE_DATA_COLUMN_PUSHABLE, "");
		value.put(TABLE_CACHE_DATA_COLUMN_AVATAR, "");
		value.put(TABLE_CACHE_DATA_COLUMN_NAME, "");
		value.put(TABLE_CACHE_DATA_COLUMN_CURRENT_TASK_PUSH_ID, 0);
		db.insert(TABLE_CACHE_DATA, null, value);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		this.onCreate(db);
	}
}
