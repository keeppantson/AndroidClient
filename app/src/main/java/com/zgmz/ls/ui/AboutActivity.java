package com.zgmz.ls.ui;

import com.zgmz.ls.R;
import com.zgmz.ls.base.SubActivity;

import android.os.Bundle;
import android.view.View;

public class AboutActivity extends SubActivity {

	@Override
	protected void onConfigrationTitleBar() {
		// TODO Auto-generated method stub
		super.onConfigrationTitleBar();
		setTitleBarTitleText(R.string.title_about);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
	}
	
	@Override
	protected void setupViews(View view) {
		// TODO Auto-generated method stub
		super.setupViews(view);
	}
	
}
