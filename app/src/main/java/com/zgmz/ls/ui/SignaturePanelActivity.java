package com.zgmz.ls.ui;

import com.zgmz.ls.R;
import com.zgmz.ls.base.Const;
import com.zgmz.ls.base.SharedDatas;
import com.zgmz.ls.base.SubActivity;
import com.zgmz.ls.view.SignaturePad;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SignaturePanelActivity extends SubActivity implements OnClickListener{
	
	SignaturePad mSignaturePad;
	
	Button mBtnClear;
	
	Button mBtnOk;
	
	@Override
	protected void onConfigrationTitleBar() {
		// TODO Auto-generated method stub
		super.onConfigrationTitleBar();
		setTitleBarTitleText(R.string.title_signature_panel);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signature_panel);
		
	}
	
	@Override
	protected void setupViews(View view) {
		// TODO Auto-generated method stub
		mSignaturePad = (SignaturePad) view.findViewById(R.id.panel);
		
		mBtnClear = (Button) view.findViewById(R.id.btn_clear);
		mBtnOk = (Button) view.findViewById(R.id.btn_ok);
		
		mBtnClear.setOnClickListener(this);
		mBtnOk.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
			case R.id.btn_clear:
				clear();
				break;
			case R.id.btn_ok:
				save();
				break;
		}
	}
	
	
	public void clear() {
		mSignaturePad.clear();
	}
	
	public void save() {
//		Bitmap bmp = mSignaturePad.getTransparentSignatureBitmap();
		Bitmap bmp = mSignaturePad.getSignatureBitmap();
		
		if(bmp != null) {
			Intent data = getIntent();
			String key = SharedDatas.getInstance().put(bmp);
			data.putExtra(Const.KEY_BITMAP, key);
			setResult(Activity.RESULT_OK, data);
			finish();
		}
		
	}
	
}
