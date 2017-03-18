package com.zgmz.ls.helper;

import com.zgmz.ls.AppContext;
import com.zgmz.ls.R;

public class SoundHelper {
	
	public static void play(int id) {
		AppContext.playSound(id);
	}
	
	public static void playQrscan() {
		play(R.raw.qr_sacn);
	}
	
	public static void playClick() {
		play(R.raw.click);
	}
	
}