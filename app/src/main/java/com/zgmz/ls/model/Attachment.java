package com.zgmz.ls.model;

public class Attachment extends BaseData {
	
	public static final int TYPE_IMAGE = 0;
	
	public static final int TYPE_ADD = 1001;
	
	private int type = 0;
	
	private String name;
	
	private String path;
	
	private int resId;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getResId() {
		return resId;
	}

	public void setResId(int resId) {
		this.resId = resId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
