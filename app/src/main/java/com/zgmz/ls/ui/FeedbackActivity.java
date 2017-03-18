package com.zgmz.ls.ui;

import com.zgmz.ls.R;
import com.zgmz.ls.base.SubActivity;

import android.os.Bundle;
import android.view.View;

public class FeedbackActivity extends SubActivity {

	@Override
	protected void onConfigrationTitleBar() {
		// TODO Auto-generated method stub
		super.onConfigrationTitleBar();
		setTitleBarTitleText(R.string.title_feedback);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback);
	}
	
	@Override
	protected void setupViews(View view) {
		// TODO Auto-generated method stub
		super.setupViews(view);
	}
	
}
