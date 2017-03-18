package com.zgmz.ls.ui;

import com.zgmz.ls.R;
import com.zgmz.ls.base.Const;
import com.zgmz.ls.base.SubActivity;
import com.zgmz.ls.module.fp.Command;
import com.zgmz.ls.module.fp.TouchIdCallback;
import com.zgmz.ls.module.fp.TouchIdController;
import com.zgmz.ls.utils.ToastUtils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class FingerprintSimpleActivitiy extends SubActivity implements OnClickListener{
	
//	private static final int FINGER_USER_ID = 202;
	
	private View mFrameFingerInput;
	
	private ImageView mFingerDemoImage;
	
	private Button mBtnInput;
	
	
//	private FingerPrint mFingerPrint;
	
	private TouchIdController mTouchIdController;
	
	Handler mHandler = new Handler();

	@Override
	protected void onConfigrationTitleBar() {
		// TODO Auto-generated method stub
		super.onConfigrationTitleBar();
		setTitleBarTitleText(R.string.title_fingerprint);
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fingerprint_simple);
		onNewIntent(getIntent());
		initController();
	}
	
	private void initController() {
		mTouchIdController = new TouchIdController();
		mTouchIdController.setCallBack(mTouchIdCallback);
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mTouchIdController.open();
	}
	
	@Override
	protected void onPostResume() {
		// TODO Auto-generated method stub
		super.onPostResume();
		mHandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				startInput();
			}
		}, 500);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mTouchIdController.close();
	}
	
	
	@Override
	protected void setupViews(View view) {
		// TODO Auto-generated method stub
		super.setupViews(view);
		
		mFrameFingerInput = view.findViewById(R.id.frame_finger_input);
		
		mFingerDemoImage = (ImageView)view.findViewById(R.id.finger_demo_image);
		
		mBtnInput = (Button)view.findViewById(R.id.btn_finger_input);
		
		mBtnInput.setOnClickListener(this);
		
		showFrameInput();
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch(v.getId()) {
			case R.id.btn_finger_input:
				startInput();
				break;
		}
	}
	
	private void resetInput() {
		showFrameInput();
	}
	
	private void showFrameInput() {
		mFrameFingerInput.setVisibility(View.VISIBLE);
		mBtnInput.setEnabled(true);
		mFingerDemoImage.setBackgroundResource(R.drawable.fingerprint_a);
		mBtnInput.setText(R.string.fingerprint_input);
	}
	
	private void backResult(int userId) {
		Intent data = getIntent();
		data.putExtra(Const.KEY_USER_ID, userId);
		setResult(Activity.RESULT_OK, data);
		finish();
	}
	
	
	private void startInput() {
		mBtnInput.setEnabled(false);
		mBtnInput.setText(R.string.fingerprint_input_waiting);
		search();
	}
	
	
	private void search() {
		mTouchIdController.search();
	}
	
	
	TouchIdCallback mTouchIdCallback = new TouchIdCallback() {
		
		@Override
		public void onTimeout(Command cmd) {
			// TODO Auto-generated method stub
			resetInput();
			ToastUtils.showShortToast("录入超时");
		}
		
		@Override
		public void onResponseUploadAndCompare(Command cmd, int state) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onResponseRemoveAll(Command cmd, int state) {
			// TODO Auto-generated method stub
		}
		
		@Override
		public void onResponseRemoveUser(Command cmd, int state) {
			// TODO Auto-generated method stub
		}
		
		@Override
		public void onResponseSearch(Command cmd, int state) {
			backResult(cmd.getUserId());
		};
		
		@Override
		public void onResponseEnrollThird(Command cmd, int state) {
			// TODO Auto-generated method stub
		}
		
		@Override
		public void onResponseEnrollSecond(Command cmd, int state) {
			// TODO Auto-generated method stub
		}
		
		@Override
		public void onResponseEnrollFirst(Command cmd, int state) {
			// TODO Auto-generated method stub
		}
		
		@Override
		public void onReponseUserEigenValue(Command cmd, int state, byte[] data) {
			// TODO Auto-generated method stub
		}
		
		@Override
		public void onError(Command cmd) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onCollectAndExtract(Command cmd, int state, byte[] data) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onCaptureImage(Command cmd, int state, Bitmap bmp) {
			// TODO Auto-generated method stub
		}
	};
}
