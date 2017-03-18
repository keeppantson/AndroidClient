package com.zgmz.ls.model;

import java.io.Serializable;
import java.util.Arrays;

import android.graphics.Bitmap;

public class IdCard extends BaseData implements Serializable{
	
	/**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 3056512101185294404L;

	private String name;
	
	private String idNumber;

	private String sex;
	
	private String nation;
	
	private String birth;
	
	private String year;
	
	private String month;
	
	private String day;
	
	private String address;
	
	private String authority;
	
	private String startValidDate;
	
	private String endValidDate;
	
	private byte[] wlt;
	
	private Bitmap avatar;

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

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public String getStartValidDate() {
		return startValidDate;
	}

	public void setStartValidDate(String startValidDate) {
		this.startValidDate = startValidDate;
	}

	public String getEndValidDate() {
		return endValidDate;
	}

	public void setEndValidDate(String endValidDate) {
		this.endValidDate = endValidDate;
	}

	public byte[] getWlt() {
		return wlt;
	}

	public void setWlt(byte[] wlt) {
		this.wlt = wlt;
	}

	public Bitmap getAvatar() {
		return avatar;
	}

	public void setAvatar(Bitmap avatar) {
		this.avatar = avatar;
	}

	public String getBirth() {
		return birth;
	}

	public void setBirth(String birth) {
		this.birth = birth;
		this.year = getSubInfo(birth, 0, 4);
		this.month = getSubInfo(birth, 4, 6);
		this.day = getSubInfo(birth, 6, 8);
	}
	
	private String getSubInfo(String info,int start,int end){
		try {
			return info.substring(start, end);
		} catch (Exception e) {
			return "";
		}
	}
	
	public String getBirthYear() {
		return this.year;
	}
	
	public String getBirthMonth() {
		return this.month;
	}
	
	public String getBirthDay() {
		return this.day;
	}

	@Override
	public String toString() {
		return "IdCard [userId=" + getUserId() + ", name=" + name + ", idNumber=" + idNumber + ", sex=" + sex + ", nation="
				+ nation + ", address=" + address + ", authority=" + authority + ", startValidDate=" + startValidDate
				+ ", endValidDate=" + endValidDate + ", wlt=" + Arrays.toString(wlt) + ", avatar=" + avatar + "]";
	}
	
	
}
