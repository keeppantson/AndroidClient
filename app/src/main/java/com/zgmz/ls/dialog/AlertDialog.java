package com.zgmz.ls.dialog;

import com.zgmz.ls.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class AlertDialog extends BaseDialog {

	TextView mTitle;
	ImageView mIcon;
	FrameLayout mContent;

	View mButtonFrame;
	Button mPositiveButton;
	Button mNegativeButton;
	
	View.OnClickListener mPositiveOnClickListener;
	View.OnClickListener mNegativeOnClickListener;
	
	boolean bClickDismiss = true;
	
	int mContentPandingBottom = 0;
	
	
	public void setClickNotDismiss() {
		bClickDismiss = false;
	}
	
	public AlertDialog(Context context) {
		super(context, R.style.Dialog_Alert, Gravity.CENTER);
	}
	public AlertDialog(Context context, int theam) {
		super(context, theam, Gravity.CENTER);
	}
	@Override
	public void setupViews() {
	}

	@SuppressWarnings("deprecation")
	@Override
	public void setContentView(int layoutResID) {
		
		View root = mInflater.inflate(R.layout.dialog_alert, null);
		
		//mTitle = (TextView)root.findViewById(R.id.title);
		mIcon = (ImageView)root.findViewById(R.id.icon);
		mContent = (FrameLayout)root.findViewById(R.id.content);
		
		mButtonFrame = root.findViewById(R.id.btn_frame);
		mPositiveButton = (Button)mButtonFrame.findViewById(R.id.positive);
		mNegativeButton = (Button)mButtonFrame.findViewById(R.id.negative);
		
		View content = mInflater.inflate(layoutResID, null);
		mContent.addView(content);
		mPositiveButton.setOnClickListener(listener);
		mNegativeButton.setOnClickListener(listener);
		Display display = getWindow().getWindowManager().getDefaultDisplay();
		super.setContentView(root, new LayoutParams(display.getWidth(), LayoutParams.WRAP_CONTENT));
	}
	
	public View findViewById(int resId){
		if(mContent != null){
			return mContent.findViewById(resId);
		}
		return null;
	}
	
	public void setTitle(int resId){
		//mTitle.setText(resId);
	}
	
	/*public void setTitle(CharSequence text){
		mTitle.setText(text);
	}*/
	
	public void setIcon(int resId){
		mIcon.setImageResource(resId);
		mIcon.setVisibility(View.VISIBLE);
	}
	
	public void setIcon(Drawable drawable){
		mIcon.setImageDrawable(drawable);
		mIcon.setVisibility(View.VISIBLE);
	}
	
	protected void setContentPaddingBottom(){
		final int left = mContent.getPaddingLeft();
		final int right = mContent.getPaddingRight();
		final int top = mContent.getPaddingTop();
		mContent.setPadding(left, top, right, mContentPandingBottom);
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
	
	public void setPositiveButton(View.OnClickListener listener){
		setPositiveButton(R.string.ok, listener);
	}
	
	public void setNegativeButton(View.OnClickListener listener){
		setNegativeButton(R.string.cancel, listener);
	}
	
	final public void hidePositiveButton() {
		mPositiveButton.setVisibility(View.GONE);
	}
	
	final public void hideNegativeButton() {
		mPositiveButton.setVisibility(View.GONE);
	}
	
	final public void hideButtons() {
		mButtonFrame.setVisibility(View.GONE);
		hidePositiveButton();
		hideNegativeButton();
	}
	
	
	
	protected void onClickPositive(){
		if(mPositiveOnClickListener != null){
			mPositiveOnClickListener.onClick(mPositiveButton);
		}
	}
	
	protected void onClickNegative(){
		if(mNegativeOnClickListener != null){
			mNegativeOnClickListener.onClick(mNegativeButton);
		}
	}
	
	
	View.OnClickListener listener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			int id = v.getId();
			if (id == R.id.positive) {
				onClickPositive();
			} else if (id == R.id.negative) {
				onClickNegative();
			}
		
			if(bClickDismiss) {
				dismiss();
			}
		}
	};
	

}
