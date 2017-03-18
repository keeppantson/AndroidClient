package com.zgmz.ls.model;

import java.util.List;

public class PreviewInfo {

	private UserInfo userInfo;
	
	private IdCard idCard;
	
	private FingerPrint fingerPrint;
	
	private Attachment yearPhoto;
	
	private District district;
	
	private FamilySituation familySituation;
	
	private FamilyProperty familyProperty;
	
	private FamilyIncome familyIncome;
	
	private FamilySpending familySpending;
	
	private Signature signature;
	
	private List<Attachment> attachments;

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public IdCard getIdCard() {
		return idCard;
	}

	public void setIdCard(IdCard idCard) {
		this.idCard = idCard;
	}

	public FingerPrint getFingerPrint() {
		return fingerPrint;
	}

	public void setFingerPrint(FingerPrint fingerPrint) {
		this.fingerPrint = fingerPrint;
	}

	public District getDistrict() {
		return district;
	}

	public void setDistrict(District district) {
		this.district = district;
	}

	public FamilySituation getFamilySituation() {
		return familySituation;
	}

	public void setFamilySituation(FamilySituation familySituation) {
		this.familySituation = familySituation;
	}

	public FamilyProperty getFamilyProperty() {
		return familyProperty;
	}

	public void setFamilyProperty(FamilyProperty familyProperty) {
		this.familyProperty = familyProperty;
	}

	public FamilyIncome getFamilyIncome() {
		return familyIncome;
	}

	public void setFamilyIncome(FamilyIncome familyIncome) {
		this.familyIncome = familyIncome;
	}

	public FamilySpending getFamilySpending() {
		return familySpending;
	}

	public void setFamilySpending(FamilySpending familySpending) {
		this.familySpending = familySpending;
	}

	public Signature getSignature() {
		return signature;
	}

	public void setSignature(Signature signature) {
		this.signature = signature;
	}

	public List<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}

	public Attachment getYearPhoto() {
		return yearPhoto;
	}

	public void setYearPhoto(Attachment yearPhoto) {
		this.yearPhoto = yearPhoto;
	}
	
}
