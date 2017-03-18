package com.zgmz.ls.model;

import java.io.Serializable;

public class SimpleUserInfo implements Serializable{
	
	/**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 4282757673861014761L;

	private int userId;
	
	private String name;
	
	private String idNumber;
	
	private boolean checked;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}
	
	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	@Override
	public String toString() {
		return "SimpleUserInfo [userId=" + userId + ", name=" + name + ", idNumber=" + idNumber + ", checked=" + checked
				+ "]";
	}

	public UserInfo toUserInfo() {
		UserInfo info = new UserInfo();
		info.setUserId(this.userId);
		info.setName(this.name);
		info.setIdNumber(this.idNumber);
		return info;
	}
	
}
