/**
 * @Title: BaseApplication.java
 * @Package com.nnp.common.base
 * @Description: TODO
 * @author buddyyan
 * @date 2015年9月23日 上午11:17:10
 * @version V1.0
 */
package com.zgmz.ls.base;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;

/**
 * @ClassName: BaseApplication
 * @Description: TODO
 * @author buddyyan
 * @date 2015年9月23日 上午11:17:10
 *
 */
public abstract class BaseApplication extends Application {

	private Context mContext;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		mContext = this;
		super.onCreate();
		init();
	}
	
	abstract protected void init();

	/**
	 * @return the mContext
	 */
	public Context getContext() {
		return mContext;
	}
	
	public AssetManager getAssetManger() {
		return mContext.getAssets();
	}
	
	
}
