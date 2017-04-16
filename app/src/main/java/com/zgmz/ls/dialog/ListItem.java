
/**
 * Project Name:NiuNiuCommon
 * File Name:LIstItem.java
 * Package Name:com.niuniuparking.dialog
 * Date:2015年6月28日下午2:19:29
 * Copyright (c) 2015, buddyyan@126.com All Rights Reserved.
 *
 */
package com.zgmz.ls.dialog;


/**
 * ClassName: LIstItem 
 * Function: TODO 
 * Reason: TODO 
 * date: 2015年6月28日 下午2:19:29 
 *
 * @author buddyyan
 * @version 
 * @since JDK 1.6
 */
public class ListItem {

	public int id;
	
	public int icon;
	
	public String name;
	
	public Object data;

	public boolean selected;
	
	public static ListItem create() {
		return new ListItem();
	}
	
}
