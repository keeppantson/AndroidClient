/**
 * @Title: BaseActivity.java
 * @Package com.nnp.common.base
 * @Description: TODO
 * @author buddyyan
 * @date 2015年9月23日 上午11:45:20
 * @version V1.0
 */
package com.zgmz.ls.base;

import com.zgmz.ls.AppContext;
import com.zgmz.ls.AppManager;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

/**
 * @ClassName: BaseActivity
 * @Description: TODO
 * @author buddyyan
 * @date 2015年9月23日 上午11:45:20
 *
 */
public class BaseActivity extends Activity {

	
	public AppContext getAppContext() {
		return AppContext.getAppContext();
	}
	
	public AppManager getAppManager() {
		return AppManager.getAppManager();
	}
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
	}
}
