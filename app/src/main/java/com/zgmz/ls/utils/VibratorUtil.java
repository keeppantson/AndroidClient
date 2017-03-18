package com.zgmz.ls.utils;

import com.zgmz.ls.AppContext;

import android.app.Service;
import android.os.Vibrator;

public class VibratorUtil {

	private static final long DEFAULT_VIBRATOR = 100;
	
//	private static final long[] FINGERPRINT_VIBRATOR = {50,50,50,50};
	
	private static Vibrator sVibrator;
	

	private static Vibrator getVibrator() {
		if (sVibrator == null) {
			sVibrator = (Vibrator) AppContext.getAppContext().getSystemService(Service.VIBRATOR_SERVICE);
		}
		return sVibrator;
	}
	
	
	public static void defaultVibrate() {
		vibrate(DEFAULT_VIBRATOR);
	}
	
	public static void fingerPrintVibrate() {
		defaultVibrate();
//		vibrate(FINGERPRINT_VIBRATOR, false);
	}

	/**
	 * long milliseconds ：震动的时长，单位是毫秒
	 */
	public static void vibrate(long milliseconds) {
		Vibrator vib = getVibrator();
		if (vib != null)
			vib.vibrate(milliseconds);
	}

	/**
	 * long[] pattern ：自定义震动模式 。数组中数字的含义依次是[静止时长，震动时长，静止时长，震动时长。。。]时长的单位是毫秒
	 * boolean isRepeat ： 是否反复震动，如果是true，反复震动，如果是false，只震动一次
	 */
	public static void vibrate(long[] pattern, boolean isRepeat) {
		Vibrator vib = getVibrator();
		if (vib != null)
			vib.vibrate(pattern, isRepeat ? 1 : -1);
	}

}
