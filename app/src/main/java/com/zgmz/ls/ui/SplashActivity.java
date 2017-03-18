package com.zgmz.ls.ui;

import com.zgmz.ls.R;
import com.zgmz.ls.base.BaseSplashActivity;
import com.zgmz.ls.helper.AccountHelper;

import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends BaseSplashActivity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void initSplash() {
		// TODO Auto-generated method stub
		setDuration(3000);
		setLayoutId(R.layout.splash);
	}

	@Override
	public void setupViews() {
		// TODO Auto-generated method stub
//		TextView view = (TextView) findViewById(R.id.name);

	}
	
	
	@Override
	protected void gotoTargetActivity() {
		// TODO Auto-generated method stub
		
//		super.gotoTargetActivity();
//		startTargetActivity();
		
		if(AccountHelper.getInstance().isLogined()) {
			super.gotoTargetActivity();
		}
		else {
			AccountHelper.startLoginUI();
		}
	}
	
	protected void startTargetActivity() {
		Intent intent = new Intent();
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setClass(this, SearchActivity.class);
		startActivity(intent);
	}
	

}
