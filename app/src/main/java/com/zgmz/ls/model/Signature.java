package com.zgmz.ls.model;

import android.graphics.Bitmap;

public class Signature extends BaseData {

	private Bitmap userSignature;
	
	private Bitmap managerSignature;

	public Bitmap getUserSignature() {
		return userSignature;
	}

	public void setUserSignature(Bitmap userSignature) {
		this.userSignature = userSignature;
	}

	public Bitmap getManagerSignature() {
		return managerSignature;
	}

	public void setManagerSignature(Bitmap managerSignature) {
		this.managerSignature = managerSignature;
	}

}
