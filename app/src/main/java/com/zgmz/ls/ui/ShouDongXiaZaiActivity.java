package com.zgmz.ls.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import com.zgmz.ls.AppContext;
import com.zgmz.ls.R;
import com.zgmz.ls.base.Const;
import com.zgmz.ls.base.SharedDatas;
import com.zgmz.ls.base.SubActivity;
import com.zgmz.ls.base.TitleBarActivity;
import com.zgmz.ls.db.DBHelper;
import com.zgmz.ls.model.DownloadTask;
import com.zgmz.ls.model.SimpleUserInfo;
import com.zgmz.ls.utils.CharUtils;
import com.zgmz.ls.utils.IDCardTools;
import com.zgmz.ls.utils.PreferencesUtils;
import com.zgmz.ls.utils.ToastUtils;

public class ShouDongXiaZaiActivity extends SubActivity {
	
	public static final String TEST_USERNAME = "test";
	private static final String TEST_PASSWORD = "123456";
	public static final String TEST_XZBM = "123456";
	private static final String TEST_TOKEN = "88sdh2e8dhh2sdf79sdf9";
	
	EditText mUsername;
	
	EditText mPassword;
	@Override
	protected void onConfigrationTitleBar() {
		// TODO Auto-generated method stub
		super.onConfigrationTitleBar();
		setTitleBarTitleText("新建用户");
	}
	CheckBox mOffline;
	
	ImageButton mBtnLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xin_jian_yong_hu_shou_dong);

		// TODO Auto-generated method stub
		mUsername = (EditText)this.findViewById(R.id.input_name);
		mPassword = (EditText)this.findViewById(R.id.input_shen_fen_zheng);
		mBtnLogin = (ImageButton)this.findViewById(R.id.wan_cheng_xin_zeng);

		mBtnLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				login();
			}
		});
	}
	
	
	private void login() {
		String name = mUsername.getText().toString().trim();
		String idNumber = mPassword.getText().toString().trim();

		if(TextUtils.isEmpty(name)) {
			ToastUtils.showLongToast("姓名不能为空");
			return;
		}

		if(!CharUtils.isChineseByREG(name)) {
			ToastUtils.showLongToast("姓名必须为中文");
			return;
		}

		if(TextUtils.isEmpty(idNumber)) {
			ToastUtils.showLongToast("身份证号不能为空");
			return;
		}

		String error = IDCardTools.IDCardValidate(idNumber);

		if(!TextUtils.isEmpty(error)) {
			ToastUtils.showLongToast(error);
			return;
		}


		Intent data = getIntent();
		SimpleUserInfo userInfo = new SimpleUserInfo();
		userInfo.setName(name);
		userInfo.setIdNumber(idNumber.toUpperCase());
		data.putExtra(Const.KEY_USER_INFO, userInfo);
		setResult(Activity.RESULT_OK, data);

		finish();
	}
	
	private void offlineLogin(String username, String password) {
		if(false) {
			ToastUtils.showLongToast("用户名或密码不正确！");
		}
		else {
			//PreferencesUtils.getInstance().setUsername(username);
			//PreferencesUtils.getInstance().setPassword(password);
			//PreferencesUtils.getInstance().setToken(TEST_TOKEN);
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
