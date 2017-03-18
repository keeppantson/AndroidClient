package com.zgmz.ls.dialog;

import com.zgmz.ls.R;

import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;

public class BottomDialog extends BaseDialog {
	
	
	View mRoot;
	
	FrameLayout mContent;
	
	View mButtonFrame;
	Button mPositiveButton;
	Button mNegativeButton;
	
	View.OnClickListener mPositiveOnClickListener;
	View.OnClickListener mNegativeOnClickListener;
	
	

	public BottomDialog(Context context) {
		super(context, R.style.Dialog_Bottom, Gravity.BOTTOM);
		// TODO Auto-generated constructor stub
		getWindow().setWindowAnimations(R.style.Animation_Bottom);
		setCanceledOnTouchOutside(true);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void setContentView(int layoutResID) {
		
		mRoot = mInflater.inflate(R.layout.dialog_bottom, null, false); 
		mContent = (FrameLayout)mRoot.findViewById(R.id.content);
		
		mButtonFrame = mRoot.findViewById(R.id.btn_frame);
		mPositiveButton = (Button)mButtonFrame.findViewById(R.id.positive);
		mNegativeButton = (Button)mButtonFrame.findViewById(R.id.negative);
		
		View content = mInflater.inflate(layoutResID, null);
		mContent.addView(content);
		mPositiveButton.setOnClickListener(listener);
		mNegativeButton.setOnClickListener(listener);
		Display display = getWindow().getWindowManager().getDefaultDisplay();
		super.setContentView(mRoot, new LayoutParams(display.getWidth(), LayoutParams.WRAP_CONTENT));
	}

	@Override
	public void setupViews() {
		// TODO Auto-generated method stub

	}
	
	public void setRootBackgroundColor(int color) {
		mRoot.setBackgroundColor(color);
	}
	
	public void setPositiveButton(int redId, View.OnClickListener listener){
		mButtonFrame.setVisibility(View.VISIBLE);
		mPositiveOnClickListener = listener;
		mPositiveButton.setText(redId);
		mPositiveButton.setVisibility(View.VISIBLE);
	}
	
	public void setNegativeButton(int redId, View.OnClickListener listener){
		mNegativeOnClickListener = listener;
		mButtonFrame.setVisibility(View.VISIBLE);
		mNegativeButton.setText(redId);
		mNegativeButton.setVisibility(View.VISIBLE);
	}
	
	
	private void onClickPositive(){
		if(mPositiveOnClickListener != null){
			mPositiveOnClickListener.onClick(mPositiveButton);
		}
	}
	
	private void onClickNegative(){
		if(mNegativeOnClickListener != null){
			mNegativeOnClickListener.onClick(mNegativeButton);
		}
	}
	
	View.OnClickListener listener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			int id = v.getId();
			if (id == R.id.positive) {
				onClickPositive();
				cancel();
			} else if (id == R.id.negative) {
				onClickNegative();
				cancel();
			}
			
		}
	};

}
