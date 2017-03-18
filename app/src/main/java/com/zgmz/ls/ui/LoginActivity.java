package com.zgmz.ls.ui;

import com.zgmz.ls.R;
import com.zgmz.ls.base.TitleBarActivity;
import com.zgmz.ls.utils.PreferencesUtils;
import com.zgmz.ls.utils.ToastUtils;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class LoginActivity extends TitleBarActivity {
	
	private static final String TEST_USERNAME = "test";
	private static final String TEST_PASSWORD = "123456";
	private static final String TEST_TOKEN = "88sdh2e8dhh2sdf79sdf9";
	
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
		
		boolean offline = mOffline.isChecked();
		
		if(TextUtils.isEmpty(username)) {
			ToastUtils.showLongToast("用户名不能为空！");
		}
		else if(TextUtils.isEmpty(password)) {
			ToastUtils.showLongToast("密码不能为空！");
		}
		else {
			
			if(offline) {
				offlineLogin(username, password);
			}
			else {
				onlineLogin(username, password);
			}
		}
			
	}
	
	private void offlineLogin(String username, String password) {
		if(!TEST_USERNAME.equals(username) || !TEST_PASSWORD.equals(password)) {
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
		ToastUtils.showLongToast("暂时只支持离线登录");
	}
	
	private void gotoMainActivity() {
		Intent intent = new Intent();
		intent.setClass(this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

}
