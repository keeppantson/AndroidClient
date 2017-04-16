package com.zgmz.ls.utils;

import com.zgmz.ls.AppContext;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

public class PreferencesUtils {

	public static final int NEW_ORDER_TYPE_TEXT = 4;
	
	public static final String FILE_NAME = "config";
	
	private static final String TOKEN = "token";
	
	private static final String SOUNDS_SWITCH = "sounds_switch";
	
	private static final String USERNAME = "username";
	private static final String QHM = "qhm";
	
	private static final String PASSWORD = "password";

	private static final String QUHUAMA_LAST_UPDATE_TIME = "quhuama";
	
	private static final boolean DEFAULT_SOURNDS = true;
	
	
	private SharedPreferences.Editor mEditor;
	
	private SharedPreferences mPref;
	
	
	
	private static PreferencesUtils sInstance;

	public static PreferencesUtils getInstance() {
		if (sInstance == null) {
			synchronized (PreferencesUtils.class) {
				if(sInstance == null) {
					sInstance = new PreferencesUtils(AppContext.getAppContext());
				}
			}
		}
		return sInstance;
	}
	
	
	public PreferencesUtils(Context context) {
		this.mPref = context.getSharedPreferences(FILE_NAME, 0);
		this.mEditor = this.mPref.edit();
	}
	

	public boolean getSoundsSwtich() {
		return this.mPref.getBoolean(SOUNDS_SWITCH, DEFAULT_SOURNDS);
	}

	public String getToken() {
		return this.mPref.getString(TOKEN, null);
	}

	public boolean isEmptyToken() {
		return TextUtils.isEmpty(getToken());
	}

	public void reset() {
		setPassword(null);
		setUsername(null);
		setToken(null);
	}
	
	public void clear() {
		this.mEditor.clear().commit();
	}


	public void setSoundsSwitch(boolean value) {
		this.mEditor.putBoolean(SOUNDS_SWITCH, value);
		this.mEditor.commit();
	}

	public void setToken(String value) {
		this.mEditor.putString(TOKEN, value);
		this.mEditor.commit();
	}

	public void setQHM(String qhm) {
		putString(QHM, qhm);
	}


	public String getQHM() {
		return getString(QHM, null);
	}

	public void setQuhuamaLastUpdateTime(long time) {
		putLong(QUHUAMA_LAST_UPDATE_TIME, time);
	}

	public Long getQuhuamaLastUpdateTime() {
		return getLong(QUHUAMA_LAST_UPDATE_TIME, 0);
	}

	public void setUsername(String username) {
		putString(USERNAME, username);
	}


	public String getUsername() {
		return getString(USERNAME, null);
	}

	public void setPassword(String password) {
		putString(PASSWORD, password);
	}
	
	public String getPassword() {
		return getString(PASSWORD, null);
	}
	
	public void putString(String key, String value) {
		this.mEditor.putString(key, value);
		this.mEditor.commit();
	}
	
	public String getString(String key, String defValue) {
		return this.mPref.getString(key, defValue);
	}
	
	
	public void putInt(String key, int value) {
		this.mEditor.putInt(key, value);
		this.mEditor.commit();
	}
	
	public int getInt(String key, int defValue) {
		return this.mPref.getInt(key, defValue);
	}
	
	
	public void putLong(String key, long value) {
		this.mEditor.putLong(key, value);
		this.mEditor.commit();
	}
	
	public long getLong(String key, long defValue) {
		return this.mPref.getLong(key, defValue);
	}
	
	
	public void putFloat(String key, float value) {
		this.mEditor.putFloat(key, value);
		this.mEditor.commit();
	}
	
	public float getFloat(String key, float defValue) {
		return this.mPref.getFloat(key, defValue);
	}
	
	
	public void putBoolean(String key, boolean value) {
		this.mEditor.putBoolean(key, value);
		this.mEditor.commit();
	}
	
	public boolean getBoolean(String key, boolean defValue) {
		return this.mPref.getBoolean(key, defValue);
	}


}
