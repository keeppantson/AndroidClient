package com.zgmz.ls.base;

import android.net.Uri;

public interface Const {

	public static final int INT_INVALID = -1;
	public static final int INT_YES = 1;
	public static final int INT_NO = 2;
	
	public static final String KEY = "key";
	
	public static final String KEY_TYPE = "type";
	
	public static final String KEY_BITMAP = "bmp";
	
	public static final String KEY_USER_ID = "userId";
	
	public static final String KEY_USER_INFO = "userInfo";
	
	public static final String KEY_ID_CARD = "idcard";
	
	public static final String KEY_FINGER_PRINT = "fingerprint";
	
	public static final String KEY_ID_NUMBER = "idnumber";
	
	
	public static final String IMAGE_BASE_LOCATION = "/sdcard/.ls/image/";
	
	public static final String IMAGE_TEMP_FILE_LOCATION = "/sdcard/.ls/image/temp.jpg";//temp file
	
	public static final Uri IMAGE_URI = Uri.parse("file://"+IMAGE_TEMP_FILE_LOCATION);//The Uri to store the big bitmap
	
	
	public interface HousingStructure {
		// 木
		public static final int WOOD = 1;
		// 砖木
		public static final int BRICK_AND_WOOD = 2;
		// 砖混
		public static final int BRICK_AND_CONCRETE = 3;
		// 茅草
		public static final int THATCH = 4;
	}
	
	
	public interface InfoType {
		
		public static final int INPUT = 0;
		
		public static final int CHECK = 1002;
		
	}
	
	
}
