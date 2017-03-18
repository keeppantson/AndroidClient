package com.zgmz.ls.base;

import com.zgmz.ls.R;
import com.zgmz.ls.helper.SoundHelper;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

public abstract class TitleBarActivity extends BaseActivity {
	
	View mTitleBar;
	
	ImageButton mLeftImageBtn;
	ImageButton mRightImageBtn;
	
	Button mLeftBtn;
	Button mRightBtn;
	
	TextView mTitleText;
	
	FrameLayout mContentView;
	
	int mTitleBarHeight;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.title_bar_frame);
		initTitleBar();
	}
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(mTitleText != null) {
			mTitleText.requestFocus();
			mTitleText.setMarqueeRepeatLimit(5);
		}
	}
	
	
	private void initTitleBar() {
		mContentView = (FrameLayout) super.findViewById(R.id.content);
		mTitleBar = super.findViewById(R.id.title_bar);
		
		mLeftImageBtn = (ImageButton)mTitleBar.findViewById(R.id.title_bar_left_image_btn);
		mRightImageBtn = (ImageButton)mTitleBar.findViewById(R.id.title_bar_right_image_btn);
		
		mLeftBtn = (Button)mTitleBar.findViewById(R.id.title_bar_left_btn);
		mRightBtn = (Button)mTitleBar.findViewById(R.id.title_bar_right_btn);
		
		mTitleText = (TextView)mTitleBar.findViewById(R.id.title_bar_text);
		
		mTitleBarHeight = getResources().getDimensionPixelSize(R.dimen.title_bar_height);
		
		mLeftBtn.setOnClickListener(mTitleBarBtnOnClickListener);
		mRightBtn.setOnClickListener(mTitleBarBtnOnClickListener);
		mLeftImageBtn.setOnClickListener(mTitleBarBtnOnClickListener);
		mRightImageBtn.setOnClickListener(mTitleBarBtnOnClickListener);
		
		onConfigrationTitleBar();
	}
	
	public void setTitleBarTextOnClick(boolean click) {
		if(click) {
			mTitleText.setOnClickListener(mTitleBarBtnOnClickListener);
		}
		else {
			mTitleText.setOnClickListener(null);
		}
	}
	
	public TextView getTitleBarTextView() {
		return mTitleText;
	}
	
	abstract protected void onConfigrationTitleBar();
	
	
	public void setTitleBarSolid(boolean solid){
		if(solid) {
			mContentView.setPadding(0, mTitleBarHeight, 0, 0);
		}
		else {
			mContentView.setPadding(0, 0, 0, 0);
		}
	}
	
	
	@Override
	public void setContentView(int layoutResId) {
		View view = getLayoutInflater().inflate(layoutResId, null);
		setContentView(view);
	}
	
	@Override
	public void setContentView(View view) {
		// TODO Auto-generated method stub
		setContentView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}
	
	
	public void setContentView(View view, LayoutParams params) {
		if(view !=null && mContentView.getChildCount() == 0) {
			mContentView.addView(view);
			setupViews(view);
		}
	}
	
	@Override
	public View findViewById(int id) {
		// TODO Auto-generated method stub
		if(mContentView != null) {
			return mContentView.findViewById(id);
		}
		return null;
	}
	
	abstract protected void setupViews(View view);
	
	public void showTitleBar() {
		mTitleBar.setVisibility(View.VISIBLE);
	}
	
	public void hideTitleBar() {
		mTitleBar.setVisibility(View.GONE);
	}
	
	public void showTitleBarLeftButton() {
		mLeftBtn.setVisibility(View.VISIBLE);
		
		if(mLeftImageBtn.getVisibility() != View.GONE) {
			mLeftImageBtn.setVisibility(View.GONE);
		}
	}
	
	public void hideTitleBarLeftButton() {
		mLeftBtn.setVisibility(View.GONE);
	}
	
	public void showTitleBarRightButton() {
		mRightBtn.setVisibility(View.VISIBLE);
		
		if(mRightImageBtn.getVisibility() != View.GONE) {
			mRightImageBtn.setVisibility(View.GONE);
		}
	}
	
	public void hideTitleBarRightButton() {
		mRightBtn.setVisibility(View.GONE);
	}
	
	public void showTitleBarLeftImageButton() {
		mLeftImageBtn.setVisibility(View.VISIBLE);
		
		if(mLeftBtn.getVisibility() != View.GONE) {
			mLeftBtn.setVisibility(View.GONE);
		}
	}
	
	public void hideTitleBarLeftImageButton() {
		mLeftImageBtn.setVisibility(View.GONE);
	}
	
	public void showTitleBarRightImageButton() {
		mRightImageBtn.setVisibility(View.VISIBLE);
		
		if(mRightBtn.getVisibility() != View.GONE) {
			mRightBtn.setVisibility(View.GONE);
		}
	}
	
	public void hideTitleBarRightImageButton() {
		mRightImageBtn.setVisibility(View.GONE);
	}
	
	
	public void setTitleBarTitleText(CharSequence text) {
		mTitleText.setText(text);
	}
	
	public void setTitleBarTitleText(int resid) {
		mTitleText.setText(getResources().getText(resid));
	}
	
	
	public void setTitleBarLeftButtonText(int resid) {
		mLeftBtn.setText(resid);
		showTitleBarLeftButton();
	}
	
	public void setTitleBarRightButtonText(int resid) {
		mRightBtn.setText(resid);
		showTitleBarRightButton();
	}
	
	
	public void setTitleBarLeftButtonText(CharSequence text) {
		mLeftBtn.setText(text);
		
		if(!TextUtils.isEmpty(text)) {
			showTitleBarLeftButton();
		}
		else {
			hideTitleBarLeftButton();
		}
	}
	
	public void setTitleBarRightButtonText(CharSequence text) {
		mRightBtn.setText(text);
		if(!TextUtils.isEmpty(text)) {
			showTitleBarRightButton();
		}
		else {
			hideTitleBarRightButton();
		}
		
	}
	
	public void setTitleBarLeftButtonBackground(int resid) {
		mLeftBtn.setBackgroundResource(resid);
	}
	
	public void setTitleBarRightButtonBackground(int resid) {
		mRightBtn.setBackgroundResource(resid);
	}
	
	public void setTitleBarLeftImageButtonImageResource(int resId) {
		mLeftImageBtn.setImageResource(resId);
		showTitleBarLeftImageButton();
	}
	
	public void setTitleBarRightImageButtonImageResource(int resId) {
		mRightImageBtn.setImageResource(resId);
		showTitleBarRightImageButton();
	}
	
	public void setTitleBarLeftImageButtonBackground(int resid) {
		mLeftImageBtn.setBackgroundResource(resid);
	}
	
	public void setTitleBarRightImageButtonBackground(int resid) {
		mRightImageBtn.setBackgroundResource(resid);
	}
	
	public void onTitleBarLeftButtonOnClick(View v) {
		
	}
	
	public void onTitleBarRightButtonOnClick(View v) {
		
	}
	
	public void onTitleBarTextOnClick(View v) {
		
	}
	

	private OnClickListener mTitleBarBtnOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			SoundHelper.playClick();
			
			int id = v.getId();
			if (id == R.id.title_bar_left_btn
					|| id == R.id.title_bar_left_image_btn) {
				onTitleBarLeftButtonOnClick(v);
			} else if (id == R.id.title_bar_right_btn
					|| id == R.id.title_bar_right_image_btn) {
				onTitleBarRightButtonOnClick(v);
			} else if (id == R.id.title_bar_text) {
				onTitleBarTextOnClick(v);
			}
			
		}
	};
	
}
