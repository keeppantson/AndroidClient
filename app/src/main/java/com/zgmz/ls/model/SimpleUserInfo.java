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

	public String getAttachment_path() {
		return attachment_path;
	}

	public void setAttachment_path(String attachment_path) {
		this.attachment_path = attachment_path;
	}

	private String attachment_path;

	public String getFather_card_id() {
		return father_card_id;
	}

	public void setFather_card_id(String father_card_id) {
		this.father_card_id = father_card_id;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getXzqhbm() {
		return xzqhbm;
	}

	public void setXzqhbm(String xzqhbm) {
		this.xzqhbm = xzqhbm;
	}

	private String father_card_id;
	private String time; // 年 月 日
    private String xzqhbm;// 行政区划编码

	private boolean checked;

	public String getCheck_task_id() {
		return check_task_id;
	}

	public void setCheck_task_id(String check_task_id) {
		this.check_task_id = check_task_id;
	}

	private String check_task_id;


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
				+ ", task_id=" + check_task_id + ", path=" + attachment_path + "]";
	}

	public UserInfo toUserInfo() {
		UserInfo info = new UserInfo();
		info.setUserId(this.userId);
		info.setName(this.name);
		info.setIdNumber(this.idNumber);
		return info;
	}
	
}
