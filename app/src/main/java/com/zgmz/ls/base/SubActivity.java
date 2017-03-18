package com.zgmz.ls.base;

import com.zgmz.ls.R;

import android.view.View;

public class SubActivity extends TitleBarActivity {

	@Override
	protected void onConfigrationTitleBar() {
		// TODO Auto-generated method stub
		setTitleBarLeftImageButtonImageResource(R.drawable.title_back);
	}

	@Override
	protected void setupViews(View view) {
		// TODO Auto-generated method stub
		
	}
	

	@Override
	public void onTitleBarLeftButtonOnClick(View v) {
		onBackPressed();
	}
	
	
}
