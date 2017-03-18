package com.zgmz.ls.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;

public abstract class BaseDialog extends Dialog {
	
	protected Resources mRes;
	protected LayoutInflater mInflater;
	protected Context mContext;

	private boolean bCancelKeyBack = false;
	
	public void setCancelKeyBack() {
		bCancelKeyBack = true;
	}
	
	public BaseDialog(Context context, int theme, int gravity) {
		super(context, theme);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setGravity(gravity);
		setCanceledOnTouchOutside(false);
		mContext = context;
		mRes = context.getResources();
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		setupViews();
	}
	
	
	@Override
	public void setContentView(View view) {
		// TODO Auto-generated method stub
		super.setContentView(view);
		setupViews();
	}
	
	@Override
	public void setContentView(View view, LayoutParams params) {
		// TODO Auto-generated method stub
		super.setContentView(view, params);
		setupViews();
	}
	
	
	public abstract void setupViews();
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (bCancelKeyBack && keyCode == KeyEvent.KEYCODE_BACK) {
        	return true;
        }
		
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void dismiss() {
		if(getActivity()!= null && !getActivity().isFinishing()){
			super.dismiss();
		}
	}
	
	
	@Override
	public void show() {
		if(getActivity()!= null && !getActivity().isFinishing()){
			super.show();
		}
	}
	

	public Activity getActivity(){
		if(mContext instanceof Activity)
			return (Activity)mContext;
		return null;
	}
	
	
}
