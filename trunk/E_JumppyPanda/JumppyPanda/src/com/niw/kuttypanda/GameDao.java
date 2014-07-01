package com.niw.kuttypanda;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class GameDao {
	public static boolean isExist(String email) {
		SQLiteDatabase db = SQLiteDatabaseAdapter.openDB();
		Cursor mCursor = null;
		try {
			mCursor = db.query(SQLiteDatabaseHelper.TABLE_GAME, new String[] { SQLiteDatabaseHelper.COLUMN_SETTINGS, },
							SQLiteDatabaseHelper.COLUMN_SETTINGS + "=?", new String[] { email }, null, null, null);
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
	
	public static void setBestScoreEasy(int bestScoreEasy) {
		SQLiteDatabase db = SQLiteDatabaseAdapter.openDB();

		try {
			ContentValues value = new ContentValues();
			value.put(SQLiteDatabaseHelper.COLUMN_BEST_SCORE_EASY, String.valueOf(bestScoreEasy));
			db.update(SQLiteDatabaseHelper.TABLE_GAME, value, null, null);
		} finally {
			SQLiteDatabaseAdapter.closeDB();
		}
	}
	
	public static void setBestScoreHard(int bestScoreHard) {
		SQLiteDatabase db = SQLiteDatabaseAdapter.openDB();

		try {
			ContentValues value = new ContentValues();
			value.put(SQLiteDatabaseHelper.COLUMN_BEST_SCORE_HARD, String.valueOf(bestScoreHard));
			db.update(SQLiteDatabaseHelper.TABLE_GAME, value, null, null);
		} finally {
			SQLiteDatabaseAdapter.closeDB();
		}
	}
	
	public static void setSettings(String settings) {
		SQLiteDatabase db = SQLiteDatabaseAdapter.openDB();

		try {
			ContentValues value = new ContentValues();
			value.put(SQLiteDatabaseHelper.COLUMN_SETTINGS, settings);
			db.update(SQLiteDatabaseHelper.TABLE_GAME, value, null, null);
		} finally {
			SQLiteDatabaseAdapter.closeDB();
		}
	}
	
	public static int getBestScoreEasy() {
		int bestScoreEasy;
		SQLiteDatabase db = SQLiteDatabaseAdapter.openDB();
		Cursor mCursor = null;

		try {
			mCursor = db.query(SQLiteDatabaseHelper.TABLE_GAME, null, null, null, null, null, null);
			if (mCursor != null) {
				mCursor.moveToFirst();
				bestScoreEasy = Integer.parseInt(mCursor.getString(mCursor.getColumnIndex(SQLiteDatabaseHelper.COLUMN_BEST_SCORE_EASY)));
				
				return bestScoreEasy;
			}
			return 0;

		} finally {
			if (mCursor != null) {
				mCursor.close();
			}
			SQLiteDatabaseAdapter.closeDB();
		}
	}
	
	public static int getBestScoreHard() {
		int bestScoreHard;
		SQLiteDatabase db = SQLiteDatabaseAdapter.openDB();
		Cursor mCursor = null;

		try {
			mCursor = db.query(SQLiteDatabaseHelper.TABLE_GAME, null, null, null, null, null, null);
			if (mCursor != null) {
				mCursor.moveToFirst();
				bestScoreHard = Integer.parseInt(mCursor.getString(mCursor.getColumnIndex(SQLiteDatabaseHelper.COLUMN_BEST_SCORE_HARD)));
				
				return bestScoreHard;
			}
			return 0;

		} finally {
			if (mCursor != null) {
				mCursor.close();
			}
			SQLiteDatabaseAdapter.closeDB();
		}
	}
	
	public static String getSettings() {
		String settings;
		SQLiteDatabase db = SQLiteDatabaseAdapter.openDB();
		Cursor mCursor = null;

		try {
			mCursor = db.query(SQLiteDatabaseHelper.TABLE_GAME, null, null, null, null, null, null);
			if (mCursor != null) {
				mCursor.moveToFirst();
				settings = mCursor.getString(mCursor.getColumnIndex(SQLiteDatabaseHelper.COLUMN_SETTINGS));
				
				return settings;
			}
			return null;

		} finally {
			if (mCursor != null) {
				mCursor.close();
			}
			SQLiteDatabaseAdapter.closeDB();
		}
	}
}

