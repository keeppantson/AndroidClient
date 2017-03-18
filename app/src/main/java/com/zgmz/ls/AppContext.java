/**
 * @Title: AppContext.java
 * @Package com.nnp.common
 * @Description: TODO
 * @author buddyyan
 * @date 2015年9月23日 上午11:39:39
 * @version V1.0
 */
package com.zgmz.ls;

import com.zgmz.ls.base.BaseApplication;
import com.zgmz.ls.db.DBHelper;
import com.zgmz.ls.utils.FileUtils;

import android.media.AudioManager;
import android.media.SoundPool;
import android.util.SparseIntArray;

/**
 * @ClassName: AppContext
 * @Description: TODO
 * @author buddyyan
 * @date 2015年9月23日 上午11:39:39
 *
 */
public class AppContext extends BaseApplication{

	private static AppContext sAppContext;
	
	
	private static SoundPool mSoundPool;
	
	private static SparseIntArray mSounds;
	
	private static boolean bSoundPoolLoaded = false;
	
	private static int[] sRaws = {R.raw.click};
	
	public static AppContext getAppContext() {
		return sAppContext;
	}
	

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		sAppContext = this;
		DBHelper.getInstance();
		super.onCreate();
	}
	
	@Override
	protected void init() {
		// TODO Auto-generated method stub
		FileUtils.ensureImageLocation();
		initSoundPool();
	}
	
	
	public void initSoundPool() {
		if(!bSoundPoolLoaded) {
			mSoundPool = new SoundPool(2, AudioManager.STREAM_MUSIC,0 ) ;
			mSounds = new SparseIntArray();
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					int n = sRaws.length;
					for(int i=0; i<n; i++) {
						mSounds.put(sRaws[i], mSoundPool.load(getApplicationContext(), sRaws[i], 1));
					}
					bSoundPoolLoaded = true;
				}
			}).start();
		  	
		}
		 
	}
	
	public static void playSound(int redId) {
		if(getAppContext() != null && bSoundPoolLoaded
				) {
			AudioManager am = (AudioManager) getAppContext().getSystemService(AUDIO_SERVICE);
			float volume = am.getStreamVolume(AudioManager.STREAM_MUSIC)
					/(float)am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
			mSoundPool.play(mSounds.get(redId), volume, volume, 1, 0, 1);
		}
	}
}
