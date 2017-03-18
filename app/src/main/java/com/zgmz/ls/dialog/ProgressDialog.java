package com.zgmz.ls.dialog;

import com.zgmz.ls.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ProgressDialog extends BaseDialog {
	
	TextView mMessage;
	
	ProgressBar mProgressBar;

	public ProgressDialog(Context context) {
		super(context, R.style.Dialog, Gravity.CENTER);
		setContentView(R.layout.dialog_progress);
	}

	@Override
	public void setupViews() {
		mProgressBar = (ProgressBar)findViewById(R.id.progress);
		mMessage = (TextView)findViewById(R.id.message);
	}
	
	
	public void setProgressIndeterminateDrawable(int resId){
		if(resId > 0){
			Drawable d = getContext().getResources().getDrawable(resId);
			mProgressBar.setIndeterminateDrawable(d);
		}
	}
	
	public void setMessege(int resId){
		if(mMessage != null){
			mMessage.setText(resId);
		}
	}
	
	public void setMessege(String text){
		if(mMessage != null){
			mMessage.setText(text);
		}
	}

}
