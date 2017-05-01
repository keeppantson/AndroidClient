package com.zgmz.ls.model;

import android.graphics.Bitmap;

import java.util.List;

public class AttachmentTime extends BaseData {

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	String time;
	public List<Attachment> attachments;
}
