package com.zgmz.ls.model;

import java.io.Serializable;

import android.graphics.Bitmap;

public class UserInfo extends BaseData implements Serializable{

	/**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = -7471351552332815903L;

	private String name;
	
	private String idNumber;
	
	private int type;
	
	private boolean checked;
	
	private Bitmap avatar;
	
	private boolean flagId;
	
	private boolean flagFinger;
	
	private boolean flagYearPhoto;
	
	private boolean flagDistrict;
	
	private boolean flagFmSituation;
	
	private boolean flagFmProperty;
	
	private boolean flagFmIncome;
	
	private boolean flagFmSpending;
	
	private boolean flagSignature;
	
	private boolean flagAttachment;
	
	private int checkStatus;

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

	public boolean isFlagId() {
		return flagId;
	}

	public void setFlagId(boolean flagId) {
		this.flagId = flagId;
	}

	public boolean isFlagFinger() {
		return flagFinger;
	}

	public void setFlagFinger(boolean flagFinger) {
		this.flagFinger = flagFinger;
	}

	public boolean isFlagDistrict() {
		return flagDistrict;
	}

	public void setFlagDistrict(boolean flagDistrict) {
		this.flagDistrict = flagDistrict;
	}

	public boolean isFlagFmSituation() {
		return flagFmSituation;
	}

	public void setFlagFmSituation(boolean flagFmSituation) {
		this.flagFmSituation = flagFmSituation;
	}

	public boolean isFlagFmProperty() {
		return flagFmProperty;
	}

	public void setFlagFmProperty(boolean flagFmProperty) {
		this.flagFmProperty = flagFmProperty;
	}

	public boolean isFlagFmIncome() {
		return flagFmIncome;
	}

	public void setFlagFmIncome(boolean flagFmIncome) {
		this.flagFmIncome = flagFmIncome;
	}

	public boolean isFlagFmSpending() {
		return flagFmSpending;
	}

	public void setFlagFmSpending(boolean flagFmSpending) {
		this.flagFmSpending = flagFmSpending;
	}

	public boolean isFlagSignature() {
		return flagSignature;
	}

	public void setFlagSignature(boolean flagSignature) {
		this.flagSignature = flagSignature;
	}

	public boolean isFlagAttachment() {
		return flagAttachment;
	}

	public void setFlagAttachment(boolean flagAttachment) {
		this.flagAttachment = flagAttachment;
	}
	

	public int getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(int checkStatus) {
		this.checkStatus = checkStatus;
	}
	

	public Bitmap getAvatar() {
		return avatar;
	}

	public void setAvatar(Bitmap avatar) {
		this.avatar = avatar;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	
	public boolean isFlagYearPhoto() {
		return flagYearPhoto;
	}

	public void setFlagYearPhoto(boolean flagYearPhoto) {
		this.flagYearPhoto = flagYearPhoto;
	}

	@Override
	public boolean isCompleted() {
		// TODO Auto-generated method stub
		return (isFlagFinger() && isFlagFinger() && isFlagFmSituation() && isFlagFmProperty() && isFlagFmIncome() && isFlagFmSpending() && isFlagSignature());
	}

	@Override
	public String toString() {
		return "UserInfo [userId=" + getUserId() + ", name=" + name + ", idNumber=" + idNumber + ", flagId=" + flagId
				+ ", flagFinger=" + flagFinger + ", flagDistrict=" + flagDistrict + ", flagFmSituation="
				+ flagFmSituation + ", flagFmProperty=" + flagFmProperty + ", flagFmIncome=" + flagFmIncome
				+ ", flagFmSpending=" + flagFmSpending + ", flagSignature=" + flagSignature + ", flagAttachment="
				+ flagAttachment + "]";
	}

	public static SimpleUserInfo getSimpleUserInfo(UserInfo info) {
		SimpleUserInfo userInfo = new SimpleUserInfo();
		if(info != null) {
			userInfo.setUserId(info.getUserId());
			userInfo.setName(info.getName());
			userInfo.setIdNumber(info.getIdNumber());
			userInfo.setChecked(info.isChecked());
		}
		
		return userInfo;
	}
	
	
	public SimpleUserInfo toSimpleUserInfo() {
		SimpleUserInfo userInfo = new SimpleUserInfo();
		userInfo.setUserId(this.getUserId());
		userInfo.setName(this.getName());
		userInfo.setIdNumber(this.getIdNumber());
		userInfo.setChecked(this.isChecked());
		return userInfo;
	}
	
}
