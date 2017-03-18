package com.zgmz.ls.utils;

import com.zgmz.ls.AppContext;

import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

public class ToastUtils {

	public static final String TAG = "ToastUtils";
	
	static final int GRAVITY = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
	
	
	public static void showShortToast(int resId){
		makeText(resId, Toast.LENGTH_SHORT).show();
	}
	
	public static void showShortToast(CharSequence text){
		if(TextUtils.isEmpty(text)) return;
		makeText(text, Toast.LENGTH_SHORT).show();
	}
	
	public static void showLongToast(int resId){
		if(resId<=0) return;
		makeText(resId, Toast.LENGTH_LONG).show();
	}
	
	public static void showLongToast(CharSequence text){
		if(TextUtils.isEmpty(text)) return;
		makeText(text, Toast.LENGTH_LONG).show();
	}
	
	public static Toast makeText(CharSequence text, int duration) {
		return Toast.makeText(AppContext.getAppContext(), text, duration);
    }
	
	public static Toast makeText(int resId, int duration) {
		return Toast.makeText(AppContext.getAppContext(), resId, duration);
    }
	
}
