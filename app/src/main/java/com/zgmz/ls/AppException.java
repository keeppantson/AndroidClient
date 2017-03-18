/**
 * @Title: AppException.java
 * @Package com.nnp.applib
 * @Description: TODO
 * @author buddyyan
 * @date 2015年9月28日 上午8:47:09
 * @version V1.0
 */
package com.zgmz.ls;

import java.lang.Thread.UncaughtExceptionHandler;

/**
 * @ClassName: AppException
 * @Description: TODO
 * @author buddyyan
 * @date 2015年9月28日 上午8:47:09
 *
 */
public class AppException extends Exception implements UncaughtExceptionHandler {

	/**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 1523203570853336355L;

	/* (non-Javadoc)
	 * <p>Title: uncaughtException</p>
	 * <p>Description: </p>
	 * @param thread
	 * @param ex
	 * @see java.lang.Thread.UncaughtExceptionHandler#uncaughtException(java.lang.Thread, java.lang.Throwable)
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		// TODO Auto-generated method stub

	}

}
