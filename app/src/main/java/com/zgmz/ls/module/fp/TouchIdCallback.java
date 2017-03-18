package com.zgmz.ls.module.fp;

import android.graphics.Bitmap;

public interface TouchIdCallback {

	public void onResponseEnrollFirst(Command cmd, int state);
	
	public void onResponseEnrollSecond(Command cmd, int state);
	
	public void onResponseEnrollThird(Command cmd, int state);
	
	public void onResponseRemoveUser(Command cmd, int state);
	
	public void onResponseRemoveAll(Command cmd, int state);
	
	public void onResponseSearch(Command cmd, int state);
	
	public void onResponseUploadAndCompare(Command cmd, int state);
	
	public void onCaptureImage(Command cmd, int state, Bitmap bmp);
	
	public void onCollectAndExtract(Command cmd, int state, byte[] data);
	
	public void onReponseUserEigenValue(Command cmd, int state, byte[] data);
	
	
	public void onTimeout(Command cmd);
	
	public void onError(Command cmd);
	
	
}
