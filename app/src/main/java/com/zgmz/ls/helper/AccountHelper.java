package com.zgmz.ls.helper;

import java.util.ArrayList;

import com.zgmz.ls.AppContext;
import com.zgmz.ls.ui.LoginActivity;
import com.zgmz.ls.utils.PreferencesUtils;

import android.content.Intent;

public class AccountHelper {
	
	public interface LoginListener {
		
		public void onPreLogin();
		
		public void onPreLoginOut();
		
		public void onLogin();
		
		public void onLogout();
	}
	
	private ArrayList<LoginListener> mLoginListeners;
	
	private static AccountHelper sInstance;
	
	public static AccountHelper getInstance() {
		if(sInstance == null) {
			synchronized (AccountHelper.class) {
				if(sInstance == null) {
					sInstance = new AccountHelper();
				}
			}
		}
		return sInstance;
	}
	
	private AccountHelper() {
		mLoginListeners = new ArrayList<LoginListener>();
	}
	
	public void registerLoginListener(LoginListener listener) {
		mLoginListeners.add(listener);
	}
	
	public void unregisterLoginListener(LoginListener listener) {
		mLoginListeners.remove(listener);
	}
	
	public boolean isLogined() {
		//return !PreferencesUtils.getInstance().isEmptyToken();
		return false;
	}
	
	public String getToken() {
		return PreferencesUtils.getInstance().getToken();
	}
	
	public void login(String username, String token) {
		PreferencesUtils.getInstance().setToken(token);
		PreferencesUtils.getInstance().setUsername(username);
		
		for(LoginListener listener: mLoginListeners) {
			listener.onLogin();
		}
	}
	
	public void logout() {
		PreferencesUtils.getInstance().reset();
		
		for(LoginListener listener: mLoginListeners) {
			listener.onLogout();
		}
		
	}
	
	public void destroy() {
		sInstance = null;
		mLoginListeners.clear();
	}
	
	
	public static void startLoginUI() {
		Intent intent = new Intent();
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setClass(AppContext.getAppContext(), LoginActivity.class);
		AppContext.getAppContext().startActivity(intent);
	}
	
}
