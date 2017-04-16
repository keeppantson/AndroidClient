package com.zgmz.ls.ui;

import com.zgmz.ls.AppContext;
import com.zgmz.ls.R;
import com.zgmz.ls.base.SubActivity;
import com.zgmz.ls.utils.PreferencesUtils;
import com.zgmz.ls.utils.ToastUtils;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class PasswordActivity extends SubActivity {
	EditText mUsername;

	EditText mPassword;

	Button mBtnYanZheng;

	Button mBtnChongZhi;
	@Override
	protected void onConfigrationTitleBar() {
		// TODO Auto-generated method stub
		super.onConfigrationTitleBar();
		setTitleBarTitleText("系统用户名密码修改");
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.password);
	}
	
	@Override
	protected void setupViews(View view) {
		// TODO Auto-generated method stub
		super.setupViews(view);

		mUsername = (EditText)view.findViewById(R.id.username);
		mPassword = (EditText)view.findViewById(R.id.password);
		mBtnYanZheng = (Button)view.findViewById(R.id.btn_yanzheng);
		mBtnChongZhi = (Button)view.findViewById(R.id.btn_chongzhi);

		mBtnYanZheng.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				yanzheng();
			}
		});

		mBtnChongZhi.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				chongzhi();
			}
		});

	}
	boolean exit = false;
	protected  void yanzheng() {
		String username = mUsername.getText().toString();
		String password = mPassword.getText().toString();
		AppContext.getAppContext().initTaijiClient(username, password);
		Thread thread=new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try {
					AppContext.getAppContext().getTaijiClient().LogIn();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
		// TODO: yanzheng
		boolean ret = true;
		if (ret) {
			PreferencesUtils.getInstance().setUsername(username);
			PreferencesUtils.getInstance().setPassword(password);
			mBtnChongZhi.setText("成功验证返回");
			exit = true;
		}
	}

	protected  void chongzhi() {
		if (exit) {
			finish();
		}
		mUsername.setText("");
		mUsername.setText("");
	}

	
}
