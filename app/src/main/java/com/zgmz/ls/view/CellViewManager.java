/**
 * @Title: ICellView.java
 * @Package com.nnp.libview.celllayout
 * @Description: TODO
 * @author buddyyan
 * @date 2015年10月1日 上午9:35:30
 * @version V1.0
 */
package com.zgmz.ls.view;

/**
 * @ClassName: ICellView
 * @Description: TODO
 * @author buddyyan
 * @date 2015年10月1日 上午9:35:30
 *
 */
public interface CellViewManager {

	public void addCell(CellItemInfo info);
	
	public void removeCell();
	
	public CellItemInfo getCellItemInfo();
	
}
