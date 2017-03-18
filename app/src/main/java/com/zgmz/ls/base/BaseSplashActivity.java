package com.zgmz.ls.base;

import com.zgmz.ls.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

public abstract class BaseSplashActivity extends BaseActivity {
	
	private static final long DURATION = 3500;
	
	
	private long mDuration = DURATION;
	
	private String mActionMain;
	
	private int mLayoutId = R.layout.splash;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setActionMain(getResources().getString(R.string.action_main));
		initSplash();
		View view = View.inflate(this, mLayoutId, null);
		setContentView(view);
	    setupViews();
		AlphaAnimation animation = new AlphaAnimation(0.6f, 1.0f);
		animation.setDuration(mDuration);
		view.startAnimation(animation);
		animation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {}
			
			@Override
			public void onAnimationRepeat(Animation animation) {}
			
			@Override
			public void onAnimationEnd(Animation animation) { 
				gotoTargetActivity();
				finish();
			}
		});
		
	}
	
	public void initSplash() {
		
	}
	
	abstract public void setupViews();
	
	final public void setLayoutId(int layoutId) {
		mLayoutId = layoutId;
	}
	
	final public void setDuration(int duration) {
		mDuration = duration;
	}
	
	final public void setActionMain(String action) {
		mActionMain = action;
	}
	
	
	private void gotoMainActivity() {
		Intent intent = new Intent(mActionMain);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}
	
	protected void gotoTargetActivity() {
		gotoMainActivity();
	}
	
}
