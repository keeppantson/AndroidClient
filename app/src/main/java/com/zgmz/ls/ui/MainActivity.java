package com.zgmz.ls.ui;

import com.zgmz.ls.R;
import com.zgmz.ls.base.BottomTabActivity;
import com.zgmz.ls.ui.fragment.TabCheckFragment;
import com.zgmz.ls.ui.fragment.TabRecordFragment;
import com.zgmz.ls.ui.fragment.TabSettingsFragment;
import com.zgmz.ls.utils.ToastUtils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class MainActivity extends BottomTabActivity {
	
	private static final int CANCAL_QUIT_TIME = 2000;
	
	private static final int MSG_CANCAL_QUIT = 0x0001;
	
	boolean bCanQiut = false;
	
	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch(msg.what) {
				case MSG_CANCAL_QUIT:
					bCanQiut = false;
					break;
			}
			super.handleMessage(msg);
		}
		
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		showTab(0);
	}

	@Override
	protected void initTabs() {
		// TODO Auto-generated method stub
		addTab(R.string.tab_check, R.drawable.tab_check, new TabCheckFragment());
		addTab(R.string.tab_record, R.drawable.tab_record, new TabRecordFragment());
		addTab(R.string.tab_settings, R.drawable.tab_settings, new TabSettingsFragment());
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(bCanQiut) {
			super.onBackPressed();
		}
		else {
			bCanQiut = true;
			mHandler.sendEmptyMessageDelayed(MSG_CANCAL_QUIT, CANCAL_QUIT_TIME);
			ToastUtils.showShortToast(R.string.prompt_pressed_back_key_quit);
		}
		
	}

}
