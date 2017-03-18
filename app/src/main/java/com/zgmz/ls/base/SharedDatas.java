package com.zgmz.ls.base;

import java.util.HashMap;
import java.util.Map;

public class SharedDatas {

	private static SharedDatas sInstance;
	
	private boolean bRecordUpdated;
	
	private boolean bCheckUpdated;
	
	private Map<String, Object> mDatas;
	
	private SharedDatas () {
		mDatas = new HashMap<String, Object>();
	}
	
	public static SharedDatas getInstance() {
		if(sInstance == null) {
			synchronized (SharedDatas.class) {
				if(sInstance == null) {
					sInstance = new SharedDatas();
				}
			}
		}
		return sInstance;
	}
	
	public void put(String key, Object value) {
		mDatas.put(key, value);
	}
	
	private void remove(String key) {
		mDatas.remove(key);
	}
	
	public Object getValue(String key) {
		Object obj = null;
		if(mDatas.containsKey(key)) {
			obj = mDatas.get(key);
			remove(key);
		}
		return obj;
	}
	
	public String put(Object value) {
		String key = String.valueOf(System.currentTimeMillis());
		put(key, value);
		return key;
	}

	public boolean isRecordUpdated() {
		return bRecordUpdated;
	}

	public void recordUpdated() {
		this.bRecordUpdated = true;
	}
	
	public void clearRecordUpdated() {
		this.bRecordUpdated = false;
	}
	
	
	public boolean isCheckUpdated() {
		return bCheckUpdated;
	}
	
	public void checkUpdated() {
		this.bCheckUpdated = true;
	}
	
	public void clearCheckUpdated() {
		this.bCheckUpdated = false;
	}
	
}
