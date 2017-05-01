package com.zgmz.ls.ui;

import com.zgmz.ls.AppContext;
import com.zgmz.ls.R;
import com.zgmz.ls.base.TitleBarActivity;
import com.zgmz.ls.utils.ConfigClient;
import com.zgmz.ls.utils.PreferencesUtils;
import com.zgmz.ls.utils.RestClient;
import com.zgmz.ls.utils.ToastUtils;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import org.json.JSONException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class LoginActivity extends TitleBarActivity {
	
	public static final String TEST_USERNAME = "test";
	private static final String TEST_PASSWORD = "123456";
	public static final String TEST_XZBM = "123456";
	private static final String TEST_TOKEN = "88sdh2e8dhh2sdf79sdf9";
	public static final int PAGE_ITEM_NUM = 7;

	EditText mUsername;
	
	EditText mPassword;
	
	CheckBox mOffline;
	
	Button mBtnLogin;

	@Override
	protected void onConfigrationTitleBar() {
		// TODO Auto-generated method stub
		setTitleBarTitleText(R.string.title_login);
		hideTitleBar();
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
	}

	@Override
	protected void setupViews(View view) {
		// TODO Auto-generated method stub
		mUsername = (EditText)view.findViewById(R.id.username);
		mPassword = (EditText)view.findViewById(R.id.password);
		mOffline = (CheckBox)view.findViewById(R.id.offline);
		mBtnLogin = (Button)view.findViewById(R.id.btn_login);
		
		mOffline.setChecked(true);

		mBtnLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				login();
			}
		});
	}
	
	
	private void login() {
		String username = mUsername.getText().toString().trim();
		String password = mPassword.getText().toString().trim();

		{
			
			if(mOffline.isChecked()) {
				offlineLogin(username, password);
			}
			else {
				onlineLogin(username, password);
			}
		}
			
	}
	
	private void offlineLogin(String username, String password) {
		// TODO: 判断传入参数和本地存储的用户名密码
		if(false) {
			ToastUtils.showLongToast("用户名或密码不正确！");
		}
		else {
			PreferencesUtils.getInstance().setUsername(username);
			PreferencesUtils.getInstance().setPassword(password);
			PreferencesUtils.getInstance().setToken(TEST_TOKEN);
			gotoMainActivity();
			finish();
		}
		
	}

	private void onlineLogin(String username, String password) {
		AppContext.getAppContext().initTaijiClient(username, password);
		try {
			AppContext.getAppContext().getTaijiClient().LogIn();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		PreferencesUtils.getInstance().setUsername(username);
		PreferencesUtils.getInstance().setPassword(password);
		PreferencesUtils.getInstance().setToken(TEST_TOKEN);
		gotoMainActivity();
		finish();
	}
	
	private void gotoMainActivity() {
		Intent intent = new Intent();
		intent.setClass(this, DoorActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

}
