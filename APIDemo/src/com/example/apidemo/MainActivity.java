package com.example.apidemo;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ShareActionProvider;
import android.widget.Toast;

import com.example.apidemo.dbhelper.UserInfoDbHelper;
import com.example.apidemo.module.UserInfo;
import com.example.apidemo.module.UserInfoContract.UserInfoEntry;

public class MainActivity extends ActionBarActivity {
	
	private static final int CONTACT_PICK_REQUEST = 1;
	private ShareActionProvider shareActionProvider;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
    	MenuInflater mi = getMenuInflater();
    	mi.inflate(R.menu.sharelist, menu);
    	MenuItem item = menu.findItem(R.id.menu_item_share);
    	shareActionProvider = (ShareActionProvider) item.getActionProvider();
        return true;
    }
    
    private void setShareIntent(Intent intent) {
    	if(shareActionProvider != null) {
    		shareActionProvider.setShareIntent(intent);
    	}
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
    	if(item.getItemId() == R.id.menu_item_share) {
    		Intent sendIntent = new Intent();
    		sendIntent.setAction(Intent.ACTION_SEND);
        	sendIntent.putExtra(Intent.EXTRA_TEXT, "welcome to api demo");
        	sendIntent.setType("text/plain");
        	setShareIntent(sendIntent);
    	}
        return true;
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
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if(requestCode == CONTACT_PICK_REQUEST) {
    		if(resultCode == RESULT_OK) {
    			Uri contactUri = data.getData();
    			String[] projection = {Phone.NUMBER};
    			Cursor cur = getContentResolver().query(contactUri, projection, null, null, null);
    			cur.moveToFirst();
    			String number = cur.getString(cur.getColumnIndexOrThrow(Phone.NUMBER));
    			Toast.makeText(this, number, Toast.LENGTH_SHORT).show();
    		}
    	}
    }

    public void doLogin(View view) {
    	EditText userNameView = (EditText) findViewById(R.id.user_name);
		String userName = userNameView.getText().toString();
		EditText passView = (EditText) findViewById(R.id.user_pwd);
		String pass = passView.getText().toString();
		
		UserInfo user = loginBySqlite();
		
		if(userName.equals(user.getUserName()) && pass.equals(user.getPass())) {
			Toast.makeText(this, getString(R.string.login_success), Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(this, getString(R.string.login_invalid), Toast.LENGTH_SHORT).show();
		}
    }
    
    public void doRegister(View view) {
    	Intent intent = new Intent(this, RegisterActivity.class);
    	startActivity(intent);
    }
    
    private UserInfo loginByPreference() {
    	SharedPreferences sp = getSharedPreferences("logininfo", Context.MODE_PRIVATE);
		String userNameStore = sp.getString("userId", "admin");
		String passStore = sp.getString("password", "123");
		
		UserInfo user = new UserInfo();
		user.setUserName(userNameStore);
		user.setPass(passStore);
		
		return user;
    }
    
    private UserInfo loginBySqlite() {
    	UserInfoDbHelper dbHelper = new UserInfoDbHelper(getBaseContext());
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		
		String[] projection = {
			    UserInfoEntry.COLUMN_USER_NAME,
			    UserInfoEntry.COLUMN_USER_PASS
			    };
		Cursor cur = db.query(UserInfoEntry.TABLE_NAME, projection, null, null, null, null, null);
		cur.moveToFirst();
		String userName = cur.getString(cur.getColumnIndexOrThrow(UserInfoEntry.COLUMN_USER_NAME));
		String pass = cur.getString(cur.getColumnIndexOrThrow(UserInfoEntry.COLUMN_USER_PASS));
		
		UserInfo user = new UserInfo();
		user.setUserName(userName);
		user.setPass(pass);
		
		return user;
    }
    
    public void doCall(View view) {
    	Uri uri = Uri.parse("tel:13918949525");
    	Intent callIntent = new Intent(Intent.ACTION_DIAL, uri);
    	PackageManager pm = getPackageManager();
    	List<ResolveInfo> resolveInfo = pm.queryIntentActivities(callIntent, 0);
    	if(resolveInfo.size()> 0) {
    		startActivity(callIntent);
    	}
    }
    
    public void doContact(View view) {
    	Intent contactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
    	contactIntent.setType(Phone.CONTENT_TYPE);
    	startActivityForResult(contactIntent, CONTACT_PICK_REQUEST);
    }
    
    public void doSendTxt(View view) {
    	Intent sendIntent = new Intent();
    	sendIntent.setAction(Intent.ACTION_SEND);
    	sendIntent.putExtra(Intent.EXTRA_TEXT, "welcome to api demo");
    	sendIntent.setType("text/plain");
    	startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.chooser_title)));
    }
}
