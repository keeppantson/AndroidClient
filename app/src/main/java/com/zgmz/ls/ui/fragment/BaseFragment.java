package com.zgmz.ls.ui.fragment;

import com.zgmz.ls.dialog.ProgressDialog;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {
	
	View mRoot;
	
	ProgressDialog mProgressDialog;
	
	boolean bFirstBoot = true;
	
	private static final int MSG_BTN_CLICK = 0x2001;
	
	boolean bViewCanClick = true;
	
	Handler mHandler = new Handler() {
		
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what) {
				case MSG_BTN_CLICK:
					bViewCanClick = true;
					break;
			}
		}
	};
	

	
	public void checkAppUpgrade(boolean show) {

	}
	
	
	public View getRootView() {
		return mRoot;
	}
	
	public boolean isViewCanClick() {
		return bViewCanClick;
	}
	
	public void viewClick() {
		bViewCanClick = false;
		mHandler.removeMessages(MSG_BTN_CLICK);
		mHandler.sendEmptyMessageDelayed(MSG_BTN_CLICK, 500);
	}
	
	
	public boolean isFirstBoot() {
		return bFirstBoot;
	}
	
	private void setFirstBootFalse() {
		bFirstBoot = false;
	}
	
	abstract public View onCreateFragmentView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) ;
	
	public void onCreated(Bundle savedInstanceState) {
		
	}
	
	
	@Override
	final public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if(mRoot == null) {
			mRoot = onCreateFragmentView(inflater, container, savedInstanceState);
		}
		else {
			ViewGroup parent = (ViewGroup)mRoot.getParent();
			if(parent != null) {
				parent.removeView(mRoot);
			}
		}
		return mRoot;
	}
	
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
	}
	
	@Override
	final public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		if(isFirstBoot()) {
			setFirstBootFalse();
			onCreated(savedInstanceState);
		}
		
	}
	
	/**
	 * TODO 
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		viewClick();
		super.onResume();
	}
	
	// fragment回调返回数据
	public void fragmentCallback(Bundle bundle) {
		
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		mRoot = null;
		super.onDestroy();
	}
	
	public View findViewById(int id) {
		if(mRoot != null) {
			return mRoot.findViewById(id);
		}
		return null;
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
        // TODO Auto-generated method stub
		
        if (keyCode == KeyEvent.KEYCODE_BACK && keyEvent.getRepeatCount() == 0) {
        	return onBackPressed();
        }
        return false;
    }
	
	
	protected boolean onBackPressed() {
		return false;
	}
	
	
	public boolean shouldBeAddedToBackStack() {
		return true;
	}
	
	
	protected void showProgressDialog(String text) {
		if(mProgressDialog == null) {
			mProgressDialog = new ProgressDialog(getActivity());
		}
		mProgressDialog.setCancelKeyBack();
		mProgressDialog.setMessege(text);
		mProgressDialog.show();
	}
	
	protected void showProgressDialog(int resId) {
		if(mProgressDialog == null) {
			mProgressDialog = new ProgressDialog(getActivity());
		}
		mProgressDialog.setMessege(resId);
		mProgressDialog.show();
	}
	
	protected void dismissProgressDialog() {
		if(mProgressDialog != null) {
			mProgressDialog.dismiss();
		}
	}
	
	
}
