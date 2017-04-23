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
import com.zgmz.ls.utils.PreferencesUtils;
import com.zgmz.ls.utils.RestClient;
import com.zgmz.ls.utils.RestClientOld;
import com.zgmz.ls.utils.TaijiClient;
import com.zgmz.ls.utils.TaijiClientLocal;
import com.zgmz.ls.utils.ToastUtils;
import com.zgmz.ls.utils.WorkerThread;

import android.media.AudioManager;
import android.media.SoundPool;
import android.telephony.TelephonyManager;
import android.util.SparseIntArray;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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

	private WorkerThread mWorkerThread = null;
	private TaijiClient taijiClient = null;
	private RestClient restClient = null;


	public synchronized TaijiClient getTaijiClient() {
		return taijiClient;
	}
	public synchronized boolean initTaijiClient() {
        if (taijiClient != null) {
			taijiClient = null;
		}
        TelephonyManager tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
        String imei = tm.getDeviceId();
		// TODO Fake IMEI & user name & password
		imei = "12345";
        String userName = PreferencesUtils.getInstance().getUsername();
        if (userName == null || userName.equals("")) {
            userName = "quxian1";
        }
        String userPassword= PreferencesUtils.getInstance().getPassword();
        if (userPassword == null || userPassword.equals("")) {
            userPassword = "abcd1234";
        }
		taijiClient = new TaijiClient(userName, userPassword, imei);

        Thread thread=new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try {
                    AppContext.getAppContext().getTaijiClient().LogIn();
                    getWorkerThread().initialized = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        return true;
	}
    public synchronized boolean initTaijiClient(String userName, String userPassword) {
        if (taijiClient != null) {
            taijiClient = null;
        }
        TelephonyManager tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
        String imei = tm.getDeviceId();
		// TODO Fake IMEI
		imei = "12345";
        taijiClient = new TaijiClient(userName, userPassword, imei);

        return true;
    }
    public synchronized RestClient getRestClient() {
        return restClient;
    }
    public synchronized boolean initRestClient() {
        if (restClient != null) {
            restClient = null;
        }
        TelephonyManager tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
        String imei = tm.getDeviceId();
		// TODO Fake IMEI
		imei = "12345";
        String userName = PreferencesUtils.getInstance().getUsername();
        String userPassword= PreferencesUtils.getInstance().getPassword();
        userName = "quxian1";
        userPassword = "abcd1234";
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		md.update(userPassword.getBytes());
		userPassword = new BigInteger(1, md.digest()).toString(16);
        String serverurl = "115.28.185.19";
        restClient = new RestClient(serverurl, userName, userPassword, imei);

        return true;
    }

	public synchronized WorkerThread getWorkerThread() {
		return mWorkerThread;
	}
	public synchronized void deInitWorkerThread() {
		mWorkerThread.exit();
		try {
			mWorkerThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		mWorkerThread = null;
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
		FileUtils.ensureVideoLocation();
		initSoundPool();
		initTaijiClient();
		initRestClient();
		if (mWorkerThread == null) {
			mWorkerThread = new WorkerThread(getApplicationContext());
			mWorkerThread.start();
		}

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
