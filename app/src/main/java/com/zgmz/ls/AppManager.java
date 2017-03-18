/**
 * @Title: AppManager.java
 * @Package com.nnp.common
 * @Description: TODO
 * @author buddyyan
 * @date 2015年9月23日 上午11:18:00
 * @version V1.0
 */
package com.zgmz.ls;

import java.util.Stack;

import android.app.Activity;

/**
 * @ClassName: AppManager
 * @Description: 对Activity实现栈管理
 * @author buddyyan
 * @date 2015年9月23日 上午11:18:00
 *
 */
public class AppManager {

	
	/**
	 * @Fields sAppManager : 单例实例
	 */
	private static AppManager sAppManager;
	
	/**
	 * @Fields mActivityStack : Activity栈
	 */
	private Stack<Activity> mActivityStack;
	
	/**
	 * <p>Title: </p>
	 * <p>Description: </p>
	 */
	private AppManager() {
		mActivityStack = new Stack<Activity>();
	}
	
	
	/**
	 * @Title: getAppManager
	 * @Description: 单例获取方法
	 * 
	 * @return AppManager
	 */
	public static AppManager getAppManager() {
		if(sAppManager == null) {
			synchronized (AppManager.class) {
				if(sAppManager == null) {
					sAppManager = new AppManager();
				}
			}
		}
		return sAppManager;
	}
	
	/**
	 * @Title: addActivity
	 * @Description: 添加Activity到栈
	 * @param activity
	 */
	public void addActivity(Activity activity) {
		mActivityStack.add(activity);
	}
	

    /**
     * @Title: currentActivity
     * @Description: 获取当前栈顶Activity
     * @return Activity
     */
    public Activity currentActivity() {
    	return mActivityStack.lastElement();
    }
    
    /**
     * @Title: finishActivity
     * @Description: 结束栈顶Activity
     */
    public void finishActivity() {
        Activity activity = mActivityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * @Title: finishActivity
     * @Description: 结束Activity,并移除栈
     * @param  activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null && !activity.isFinishing()) {
        	mActivityStack.remove(activity);
            activity.finish();
        }
    }
    

    /**
     * @Title: finishActivity
     * @Description: 通过Class结束Activity
     * @param cls
     * @return void
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : mActivityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
                break;
            }
        }
    }
    
    /**
     * @Title: finishAllActivity
     * @Description: 结束所有Activity
     * @return void
     */
    public void finishAllActivity() {
    	Stack<Activity> stack = mActivityStack;
        for (int i = 0, size = stack.size(); i < size; i++) {
            if (null != stack.get(i)) {
                finishActivity(stack.get(i));
                break;
            }
        }
        stack.clear();
    }
    
    /**
     * @Title: AppExit
     * @Description: 退出Application
     * @return void
     */
    public void AppExit() {
        try {
            finishAllActivity();
            // 杀死该应用进程
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        } catch (Exception e) {
        }
    }
}
