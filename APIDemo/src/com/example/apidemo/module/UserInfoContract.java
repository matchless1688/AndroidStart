package com.example.apidemo.module;

import android.provider.BaseColumns;

public final class UserInfoContract {

	public static abstract class UserInfoEntry implements BaseColumns {
		public static final String TABLE_NAME = "user_info";
		public static final String COLUMN_USER_NAME = "uname";
		public static final String COLUMN_USER_PASS = "upass";
	}
}
