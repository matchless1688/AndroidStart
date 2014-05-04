package com.example.apidemo.dbhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.apidemo.module.UserInfoContract.UserInfoEntry;

public class UserInfoDbHelper extends SQLiteOpenHelper {

	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "UserReader.db";

	private static final String TEXT_TYPE = " TEXT";
	private static final String COMMA_SEP = ",";
	private static final String SQL_CREATE_ENTRIES = "CREATE TABLE "
			+ UserInfoEntry.TABLE_NAME + " (" + UserInfoEntry._ID
			+ " INTEGER PRIMARY KEY," + UserInfoEntry.COLUMN_USER_NAME
			+ TEXT_TYPE + COMMA_SEP + UserInfoEntry.COLUMN_USER_PASS
			+ TEXT_TYPE + " )";

	private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "
			+ UserInfoEntry.TABLE_NAME;

	public UserInfoDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_ENTRIES);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(SQL_DELETE_ENTRIES);
		onCreate(db);
	}

}
