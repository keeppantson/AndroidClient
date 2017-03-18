package com.zgmz.ls.ui;

import com.zgmz.ls.R;
import com.zgmz.ls.base.Const;
import com.zgmz.ls.base.SharedDatas;
import com.zgmz.ls.base.SubActivity;
import com.zgmz.ls.db.DBHelper;
import com.zgmz.ls.model.Signature;
import com.zgmz.ls.model.SimpleUserInfo;
import com.zgmz.ls.utils.ToastUtils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

public class SignatureActivity extends SubActivity implements OnClickListener{
	
	ImageButton mUserSign;
	
	ImageButton mManagerSign;
	
	Button mBtnComplete;
	
	Signature mSignature = new Signature();
	
	int mUserId = 0;

	@Override
	protected void onConfigrationTitleBar() {
		// TODO Auto-generated method stub
		super.onConfigrationTitleBar();
		setTitleBarTitleText(R.string.title_signature);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signature);
		onNewIntent(getIntent());
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		SimpleUserInfo info = (SimpleUserInfo) intent.getSerializableExtra(Const.KEY_USER_INFO);
		if(info != null) {
			mUserId = info.getUserId();
			loadSignature();
		}
		else {
			ToastUtils.showLongToast("用户不存在！");
			finish();
		}
	}
	
	private void loadSignature() {
		mSignature = DBHelper.getInstance().getSignature(mUserId);
		if(mSignature == null) {
			mSignature = new Signature();
			mSignature.setUserId(mUserId);
		}
		updateViews();
	}
	
	private void updateViews() {
		updateUserSignature();
		updateManagerSignature();
	}
	
	private void updateUserSignature() {
		Signature sign = mSignature;
		if(sign != null && sign.getUserSignature() != null) {
			mUserSign.setImageBitmap(sign.getUserSignature());
		}
	}
	
	private void updateManagerSignature() {
		Signature sign = mSignature;
		if(sign != null && sign.getManagerSignature() != null) {
			mManagerSign.setImageBitmap(sign.getManagerSignature());
		}
	}
	
	@Override
	protected void setupViews(View view) {
		// TODO Auto-generated method stub
		mUserSign = (ImageButton)view.findViewById(R.id.user_sign);
		mManagerSign = (ImageButton)view.findViewById(R.id.manager_sign);
		
		mBtnComplete = (Button)view.findViewById(R.id.btn_complete);
		
		
		mUserSign.setOnClickListener(this);
		mManagerSign.setOnClickListener(this);
		mBtnComplete.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
		case R.id.user_sign:
			startSignaturePanelActivity(REQUEST_CODE_USER);
			break;
		case R.id.manager_sign:
			startSignaturePanelActivity(REQUEST_CODE_MANAGER);
			break;
		case R.id.btn_complete:
			save();
			break;
		}
		
	}
	
	
	private static final int REQUEST_CODE_USER = 0x3001;
	
	private static final int REQUEST_CODE_MANAGER = 0x3002;
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode == REQUEST_CODE_USER) {
			if(resultCode == Activity.RESULT_OK) {
				String key = data.getStringExtra(Const.KEY_BITMAP);
				if(key != null) {
					Bitmap bm = (Bitmap) SharedDatas.getInstance().getValue(key);
					if(bm != null) {
						mSignature.setUserSignature(bm);
						updateUserSignature();
					}
				}
			}
		}
		else if(requestCode == REQUEST_CODE_MANAGER) {
			if(resultCode == Activity.RESULT_OK) {

				String key = data.getStringExtra(Const.KEY_BITMAP);
				
				if(key != null) {
					Bitmap bm = (Bitmap) SharedDatas.getInstance().getValue(key);
					mSignature.setManagerSignature(bm);
					updateManagerSignature();
				}
				
			}
		}
	}
	
	private void startSignaturePanelActivity(int requestCode) {
		Intent intent = new Intent();
		intent.setClass(this, SignaturePanelActivity.class);
		intent.putExtra(Const.KEY_USER_ID, mUserId);
		startActivityForResult(intent, requestCode);
	}
	
	
	private void save() {
		mSignature.setCompleted(checkSignatureComplete());
		if(DBHelper.getInstance().insertOrUpdatSignature(mSignature)){
			ToastUtils.showLongToast("保存成功");
			finish();
		}
		else {
			ToastUtils.showLongToast("保存失败");
		}
	}
	
	private boolean checkSignatureComplete() {
		Signature sign = mSignature;
		if(sign.getUserSignature()==null || sign.getManagerSignature() == null) return false;
		return true;
	}
	
}
