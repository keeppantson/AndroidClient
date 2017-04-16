package com.zgmz.ls.view;

import com.zgmz.ls.R;
import com.zgmz.ls.model.Attachment;
import com.zgmz.ls.utils.BitmapHelper;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageButton;

public class AttachImageButton extends ImageButton implements CellViewManager{

	
	CellItemInfo mCellItemInfo;
	
	public AttachImageButton(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public AttachImageButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public AttachImageButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void addCell(CellItemInfo info) {
		// TODO Auto-generated method stub
		mCellItemInfo = info;
		
		if(info != null && info.getObj() != null) {
			Attachment attach = (Attachment) info.getObj();
			if(attach.getType() == Attachment.TYPE_ADD)
				setBackgroundResource(attach.getResId());
			else {
				setBackground(null);
				//Bitmap bmp = BitmapHelper.getThumbBitmap(attach.getPath(), 150, 150);
				Bitmap bmp = attach.getContent();
				if(bmp != null)
					setImageBitmap(bmp);
				else 
					setBackgroundResource(R.drawable.photo_image);
			}
		}
	}

	@Override
	public void removeCell() {
		// TODO Auto-generated method stub
		setBackground(null);
		setImageDrawable(null);
		mCellItemInfo= null;
	}

	@Override
	public CellItemInfo getCellItemInfo() {
		// TODO Auto-generated method stub
		return mCellItemInfo;
	}

	
	
}
