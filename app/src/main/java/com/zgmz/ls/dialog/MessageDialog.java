package com.zgmz.ls.dialog;

import com.zgmz.ls.R;

import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class MessageDialog extends AlertDialog {

	TextView mMessage;
	
	public MessageDialog(Context context) {
		super(context);
		setContentView(R.layout.dialog_message);
	}

	@Override
	public void setupViews() {
		super.setupViews();
		mMessage = (TextView)findViewById(R.id.message);
		mMessage.setMovementMethod(LinkMovementMethod.getInstance());
	}
	
	public void setMessage(int resId){
		if(mMessage != null){
			mMessage.setText(resId);
		}
	}
	
	public void setMessage(CharSequence text){
		if(mMessage != null){
			mMessage.setText(text);
		}
	}
	
	public void setMessageHtml(int resId){
		if(mMessage != null){
			setMessageHtml(mContext.getResources().getString(resId));
		}
	}
	
	public void setMessageHtml(String text){
		if(mMessage != null){
			mMessage.setText(Html.fromHtml(text));
		}
	}

}
