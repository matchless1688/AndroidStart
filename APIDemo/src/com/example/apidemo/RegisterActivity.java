package com.example.apidemo;

import java.io.FileOutputStream;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.apidemo.dbhelper.UserInfoDbHelper;
import com.example.apidemo.module.UserInfo;
import com.example.apidemo.module.UserInfoContract.UserInfoEntry;

public class RegisterActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 setContentView(R.layout.activity_register);
		
		if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.containerRegister, new PlaceholderFragment())
                    .commit();
        }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_register,
					container, false);
			return rootView;
		}
	}

	public void doSubmit(View view) {
		EditText userNameView = (EditText) findViewById(R.id.user_name_reg);
		String userName = userNameView.getText().toString();
		EditText passView = (EditText) findViewById(R.id.user_pwd_reg);
		String pass = passView.getText().toString();
		EditText passConfirmView = (EditText) findViewById(R.id.user_pwd_reg_confirm);
		String passConfirm = passConfirmView.getText().toString();
		if(!pass.equals(passConfirm)) {
			Toast.makeText(this, getString(R.string.pass_not_match), Toast.LENGTH_SHORT).show();
		} else{
			UserInfo user = new UserInfo();
			user.setUserName(userName);
			user.setPass(pass);
			
			long id = saveBySqlite(user);
			
			Toast.makeText(this, getString(R.string.reg_success) + ":" + id, Toast.LENGTH_SHORT).show();
			
			Intent loginIntent = new Intent(this, MainActivity.class);
			startActivity(loginIntent);
		}
    }
	
	private void saveByPreference(UserInfo user) {
		SharedPreferences sp = getSharedPreferences("logininfo", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("userId", user.getUserName());
		editor.putString("password", user.getPass());
		editor.commit();
	}
	
	private void saveByFile(UserInfo user) {
		try {
			String info = user.getUserName() + ":" + user.getPass();
			FileOutputStream fos = openFileOutput("profile", Context.MODE_PRIVATE);
			fos.write(info.getBytes());
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private long saveBySqlite(UserInfo user) {
		UserInfoDbHelper dbHelper = new UserInfoDbHelper(getBaseContext());
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(UserInfoEntry.COLUMN_USER_NAME, user.getUserName());
		values.put(UserInfoEntry.COLUMN_USER_PASS, user.getPass());
		
		long rowId = db.insert(UserInfoEntry.TABLE_NAME, null, values);
		return rowId;
	}
}
