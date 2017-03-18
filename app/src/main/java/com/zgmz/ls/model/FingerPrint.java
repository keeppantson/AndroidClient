package com.zgmz.ls.model;

import android.graphics.Bitmap;

public class FingerPrint extends BaseData{

	private byte[] eigenValue;
	
	private Bitmap capture;

	public byte[] getEigenValue() {
		return eigenValue;
	}

	public void setEigenValue(byte[] eigenValue) {
		this.eigenValue = eigenValue;
	}

	public Bitmap getCapture() {
		return capture;
	}

	public void setCapture(Bitmap capture) {
		this.capture = capture;
	}
	
	
}
