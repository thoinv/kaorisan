package com.kaorisan.dataLayer;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserDao {
	public static boolean isExist(String kaorisanToken) {
		SQLiteDatabase db = SQLiteDatabaseAdapter.openDB();
		Cursor mCursor = null;
		try {
			mCursor = db.query(SQLiteDatabaseHelper.TABLE_CACHE_DATA, new String[] { SQLiteDatabaseHelper.TABLE_CACHE_DATA_COLUMN_KAORISAN_TOKEN, },
							SQLiteDatabaseHelper.TABLE_CACHE_DATA_COLUMN_KAORISAN_TOKEN + "=?", new String[] { kaorisanToken }, null, null, null);
			if (mCursor != null && mCursor.getCount() > 0) {
				return true;
			}
			return false;
		} finally {
			if (mCursor != null) {
				mCursor.close();
			}
			SQLiteDatabaseAdapter.closeDB();
		}
	}
	
	public static void setCurrentUser(String kaorisanToken, String pushable, String avatar, String fullName) {
		SQLiteDatabase db = SQLiteDatabaseAdapter.openDB();

		try {
			ContentValues value = new ContentValues();
			value.put(SQLiteDatabaseHelper.TABLE_CACHE_DATA_COLUMN_KAORISAN_TOKEN, kaorisanToken.trim());
			value.put(SQLiteDatabaseHelper.TABLE_CACHE_DATA_COLUMN_PUSHABLE, pushable.trim());
			value.put(SQLiteDatabaseHelper.TABLE_CACHE_DATA_COLUMN_AVATAR, avatar.trim());
			value.put(SQLiteDatabaseHelper.TABLE_CACHE_DATA_COLUMN_NAME, fullName.trim());
			db.update(SQLiteDatabaseHelper.TABLE_CACHE_DATA, value, null, null);
		} finally {
			SQLiteDatabaseAdapter.closeDB();
		}
	}
	
	public static void setCurrentTaskPushId(int taskId) {
		SQLiteDatabase db = SQLiteDatabaseAdapter.openDB();

		try {
			ContentValues value = new ContentValues();
			value.put(SQLiteDatabaseHelper.TABLE_CACHE_DATA_COLUMN_CURRENT_TASK_PUSH_ID, taskId);
			db.update(SQLiteDatabaseHelper.TABLE_CACHE_DATA, value, null, null);
		} finally {
			SQLiteDatabaseAdapter.closeDB();
		}
	}
	
	
	public static int getCurrentTaskPushId() {
		SQLiteDatabase db = SQLiteDatabaseAdapter.openDB();
		Cursor mCursor = null;

		try {
			mCursor = db.query(SQLiteDatabaseHelper.TABLE_CACHE_DATA, null, null, null, null, null, null);
			if (mCursor != null) {
				mCursor.moveToFirst();
				return Integer.parseInt(mCursor.getString(4));
			} else {
				return 0;
			}

		} finally {
			if (mCursor != null) {
				mCursor.close();
			}
			SQLiteDatabaseAdapter.closeDB();
		}
	}
	
	public static String getKaorisanToken(){
		SQLiteDatabase db = SQLiteDatabaseAdapter.openDB();
		Cursor mCursor = null;

		try {
			mCursor = db.query(SQLiteDatabaseHelper.TABLE_CACHE_DATA, null, null, null, null, null, null);
			if (mCursor != null) {
				mCursor.moveToFirst();
				return mCursor.getString(0);
			} else {
				return "";
			}

		} finally {
			if (mCursor != null) {
				mCursor.close();
			}
			SQLiteDatabaseAdapter.closeDB();
		}
	}
	
	public static String getPushable(){
		SQLiteDatabase db = SQLiteDatabaseAdapter.openDB();
		Cursor mCursor = null;

		try {
			mCursor = db.query(SQLiteDatabaseHelper.TABLE_CACHE_DATA, null, null, null, null, null, null);
			if (mCursor != null) {
				mCursor.moveToLast();
				return mCursor.getString(1);
			} else {
				return "";
			}

		} finally {
			if (mCursor != null) {
				mCursor.close();
			}
			SQLiteDatabaseAdapter.closeDB();
		}
	}
	
	
	public static String getAvatar(){
		SQLiteDatabase db = SQLiteDatabaseAdapter.openDB();
		Cursor mCursor = null;

		try {
			mCursor = db.query(SQLiteDatabaseHelper.TABLE_CACHE_DATA, null, null, null, null, null, null);
			if (mCursor != null) {
				mCursor.moveToLast();
				return mCursor.getString(2);
			} else {
				return "";
			}

		} finally {
			if (mCursor != null) {
				mCursor.close();
			}
			SQLiteDatabaseAdapter.closeDB();
		}
	}
	
	
	public static String getName(){
		SQLiteDatabase db = SQLiteDatabaseAdapter.openDB();
		Cursor mCursor = null;

		try {
			mCursor = db.query(SQLiteDatabaseHelper.TABLE_CACHE_DATA, null, null, null, null, null, null);
			if (mCursor != null) {
				mCursor.moveToLast();
				return mCursor.getString(3);
			} else {
				return "";
			}

		} finally {
			if (mCursor != null) {
				mCursor.close();
			}
			SQLiteDatabaseAdapter.closeDB();
		}
	}
}
