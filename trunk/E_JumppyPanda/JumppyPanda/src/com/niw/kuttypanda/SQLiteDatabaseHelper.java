package com.niw.kuttypanda;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.niw.kuttypanda.base.DebugLog;
import com.niw.kuttypanda.common.Constants;

public class SQLiteDatabaseHelper extends SQLiteOpenHelper {

	private static String DB_NAME = "KUTTYPANDA.DB";
	private static int DB_VERSION = 4;
	
	public static final String TABLE_GAME = "TBL_GAME";
	public static final String COLUMN_BEST_SCORE_EASY = "bestScoreEasy";
	public static final String COLUMN_BEST_SCORE_HARD = "bestScoreHary";
	public static final String COLUMN_SETTINGS = "settings";
	
	public SQLiteDatabaseHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String scriptCreateDb =
				"CREATE TABLE TBL_GAME( "
				+ "bestScoreEasy text, "
				+ "bestScoreHary text,"
				+ "settings text"
				+ ")";
		
		DebugLog.loge(scriptCreateDb);
		
		db.execSQL(scriptCreateDb);
		
		ContentValues value = new ContentValues();
		value.put(COLUMN_BEST_SCORE_EASY, "0");
		value.put(COLUMN_BEST_SCORE_HARD, "0");
		value.put(COLUMN_SETTINGS, Constants.easy);
		db.insert(TABLE_GAME, null, value);
		
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
				db.execSQL("drop table if exists " + TABLE_GAME);
		        onCreate(db);
	}
}
