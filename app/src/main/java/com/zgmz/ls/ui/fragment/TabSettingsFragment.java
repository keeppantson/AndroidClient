package com.zgmz.ls.ui.fragment;

import com.zgmz.ls.R;
import com.zgmz.ls.ui.AboutActivity;
import com.zgmz.ls.ui.FeedbackActivity;
import com.zgmz.ls.ui.PasswordActivity;
import com.zgmz.ls.ui.ProfileActivity;
import com.zgmz.ls.ui.TechnicalSupportActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class TabSettingsFragment extends TitleBarFragment implements OnClickListener{
	
	@Override
	protected void onConfigrationTitleBar() {
		// TODO Auto-generated method stub
		setTitleBarTitleText(R.string.title_tab_settings);

		setTitleBarLeftImageButtonImageResource(R.drawable.title_back);
	}

	@Override
	public void onTitleBarLeftButtonOnClick(View v) {
		getActivity().finish();
	}

	@Override
	public void onCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreated(savedInstanceState);
		setContentView(R.layout.tab_settings);
	}
	
	
	@Override
	protected void setupViews(View root) {
		// TODO Auto-generated method stub
		View item = root.findViewById(R.id.profile);
		TextView name = (TextView) item.findViewById(R.id.name);
		item.setOnClickListener(this);
		name.setText(R.string.setting_item_profile);
		
		item = root.findViewById(R.id.password);
		name = (TextView) item.findViewById(R.id.name);
		item.setOnClickListener(this);
		name.setText(R.string.setting_item_password);
		
		item = root.findViewById(R.id.feedback);
		name = (TextView) item.findViewById(R.id.name);
		item.setOnClickListener(this);
		name.setText(R.string.setting_item_feedback);
		
		item = root.findViewById(R.id.technical_support);
		name = (TextView) item.findViewById(R.id.name);
		item.setOnClickListener(this);
		name.setText(R.string.setting_item_technical_support);
		
		item = root.findViewById(R.id.about);
		name = (TextView) item.findViewById(R.id.name);
		item.setOnClickListener(this);
		name.setText(R.string.setting_item_about);
	}
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
			case R.id.profile:
				showProfile();
				break;
			case R.id.password:
				showChangePassword();
				break;
			case R.id.feedback:
				showFeedback();
				break;
			case R.id.technical_support:
				showTechnicalSupport();
				break;
			case R.id.about:
				showAbout();
				break;
		}
	}
	
	private void showProfile() {
		Intent intent = new Intent();
		intent.setClass(getActivity(),ProfileActivity.class);
		startActivity(intent);
	}
	
	private void showChangePassword() {
		Intent intent = new Intent();
		intent.setClass(getActivity(),PasswordActivity.class);
		startActivity(intent);
	}
	
	private void showFeedback() {
		Intent intent = new Intent();
		intent.setClass(getActivity(),FeedbackActivity.class);
		startActivity(intent);
	}
	
	private void showTechnicalSupport() {
		Intent intent = new Intent();
		intent.setClass(getActivity(),TechnicalSupportActivity.class);
		startActivity(intent);
	}
	
	private void showAbout() {
		Intent intent = new Intent();
		intent.setClass(getActivity(),AboutActivity.class);
		startActivity(intent);
	}


	
	
	
}
