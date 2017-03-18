package com.zgmz.ls.ui;

import com.zgmz.ls.R;
import com.zgmz.ls.base.Const;
import com.zgmz.ls.base.SharedDatas;
import com.zgmz.ls.base.SubActivity;
import com.zgmz.ls.db.DBHelper;
import com.zgmz.ls.model.FingerPrint;
import com.zgmz.ls.model.SimpleUserInfo;
import com.zgmz.ls.module.fp.Command;
import com.zgmz.ls.module.fp.TouchIdCallback;
import com.zgmz.ls.module.fp.TouchIdCommand.Response;
import com.zgmz.ls.module.fp.TouchIdController;
import com.zgmz.ls.utils.ToastUtils;
import com.zgmz.ls.utils.VibratorUtil;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class FingerprintActivitiy extends SubActivity implements OnClickListener{
	
//	private static final int FINGER_USER_ID = 202;
	
	private View mFrameFingerInput;
	
	private View mFrameResult;
	
	private TextView mPrompt;
	
	private ImageView mFingerDemoImage;
	
	private ImageView mFingerImage;
	
	private Button mBtnInput;
	private Button mBtnAgainInput;
	private Button mBtnOk;
	
	
	private static final int STATE_FIRST_INPUT = 1;
	private static final int STATE_SECOND_INPUT = 2;
	private static final int STATE_THIRD_INPUT = 3;
	private static final int STATE_FOURTH_INPUT = 4;
	private static final int STATE_INPUT_SUCCESS = 8;
	
	private int mState = STATE_FIRST_INPUT;
	
	private FingerPrint mFingerPrint;
	
	private SimpleUserInfo mUserInfo;
	
	private TouchIdController mTouchIdController;
	
	private int mUserId = 0;
	
	private static final long DELAY_TIME = 1500;
	
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
		setContentView(R.layout.fingerprint);
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
		mUserInfo = (SimpleUserInfo) intent.getSerializableExtra(Const.KEY_USER_INFO); 
		if(mUserInfo != null) {
			mUserId = mUserInfo.getUserId();
			mFingerPrint = DBHelper.getInstance().getFingerPrint(mUserInfo.getUserId());
			if(mFingerPrint != null) {
				showFrameReuslt();
				mBtnOk.setEnabled(false);
				mFingerImage.setImageBitmap(mFingerPrint.getCapture());
			}
			else {
				mFingerPrint = new FingerPrint();
				mFingerPrint.setUserId(mUserInfo.getUserId());
			}
			
		}
		else {
			ToastUtils.showLongToast("未知用户无法录入指纹");
			finish();
		}
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mTouchIdController.open();
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
		mFrameResult = view.findViewById(R.id.frame_result);
		
		mPrompt = (TextView)view.findViewById(R.id.prompt);
		mFingerDemoImage = (ImageView)view.findViewById(R.id.finger_demo_image);
		mFingerImage = (ImageView)view.findViewById(R.id.finger_image);
		
		mBtnInput = (Button)view.findViewById(R.id.btn_finger_input);
		mBtnAgainInput = (Button)view.findViewById(R.id.btn_again_finger_input);
		mBtnOk = (Button)view.findViewById(R.id.btn_ok);
		
		mBtnInput.setOnClickListener(this);
		mBtnAgainInput.setOnClickListener(this);
		mBtnOk.setOnClickListener(this);
		
		showFrameInput(STATE_FIRST_INPUT);
		mBtnInput.setEnabled(true);
//		showFrameReuslt();
		
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		final int state = mState;
		
		switch(v.getId()) {
			case R.id.btn_finger_input:
				if(state == STATE_FIRST_INPUT) {
					startInputFirst();
				}
				else if(state == STATE_SECOND_INPUT) {
					startInputSencond();
				}
				else if(state == STATE_THIRD_INPUT) {
					startInputThird();
				}
				else if(state == STATE_FOURTH_INPUT) {
					startInputFourth();
				}
				break;
			case R.id.btn_again_finger_input:
				removeUserTouchId();
				resetInput();
				break;
			case R.id.btn_ok:
				backResult();
				break;
		}
	}
	
	private void resetInput() {
		mFingerPrint.setCapture(null);
		mFingerPrint.setEigenValue(null);
		showFrameInput(STATE_FIRST_INPUT);
		startInputFirst();
	}
	
	private void showFrameInput(int state) {
		mFrameFingerInput.setVisibility(View.VISIBLE);
		mFrameResult.setVisibility(View.GONE);
		mBtnInput.setEnabled(false);
		switch(state) {
			case STATE_FIRST_INPUT:
				mFingerDemoImage.setBackgroundResource(R.drawable.fingerprint_a);
				mPrompt.setText(R.string.fingerprint_prompt);
				mBtnInput.setText(R.string.fingerprint_input_first);
				break;
			case STATE_SECOND_INPUT:
				mFingerDemoImage.setBackgroundResource(R.drawable.fingerprint_b);
				mPrompt.setText("已完成 25% \n");
				mBtnInput.setText(R.string.fingerprint_input_second);
				break;
			case STATE_THIRD_INPUT:
				mFingerDemoImage.setBackgroundResource(R.drawable.fingerprint_c);
				mPrompt.setText("已完成 60% \n");
				mBtnInput.setText(R.string.fingerprint_input_third);
				break;
			case STATE_FOURTH_INPUT:
				mFingerDemoImage.setBackgroundResource(R.drawable.fingerprint_d);
				mPrompt.setText("已完成 80% \n");
				mBtnInput.setText(R.string.fingerprint_input_fourth);
				break;
		}
		mState = state;
	}
	
	private void backResult() {
		Intent data = getIntent();
		data.putExtra(Const.KEY_FINGER_PRINT, SharedDatas.getInstance().put(mFingerPrint));
		setResult(Activity.RESULT_OK, data);
		finish();
	}
	
	private void showFrameReuslt() {
		mFrameFingerInput.setVisibility(View.GONE);
		mFrameResult.setVisibility(View.VISIBLE);
		mFingerImage.setImageBitmap(mFingerPrint.getCapture());
		mBtnOk.setEnabled(true);
//		mFingerImage.setRotation(180);
		mState = STATE_INPUT_SUCCESS;
	}
	
	private void startInputFirst() {
		mBtnInput.setEnabled(false);
		mBtnInput.setText(R.string.fingerprint_input_waiting);
		mPrompt.setText("已完成 0% \n等待第一次录入指纹");
		mTouchIdController.enroll1(mUserId);
	}
	
	private void startInputSencond() {
		mBtnInput.setEnabled(false);
		mBtnInput.setText(R.string.fingerprint_input_waiting);
		mPrompt.setText("已完成 25% \n等待第二次录入指纹");
		mTouchIdController.enroll2(mUserId);
	}
	
	private void startInputThird() {
		mBtnInput.setEnabled(false);
		mBtnInput.setText(R.string.fingerprint_input_waiting);
		mPrompt.setText("已完成 60% \n等待第三次录入指纹");
		mTouchIdController.enroll3(mUserId);
	}
	
	private void startInputFourth() {
		mBtnInput.setEnabled(false);
		mBtnInput.setText(R.string.fingerprint_input_waiting);
		mPrompt.setText("已完成 80% \n等待最后一次录入指纹");
		mTouchIdController.captureImage();
	}
	
	private void getUserEgienValue() {
		mTouchIdController.getUserEigenValue(mUserId);
	}
	
//	private void removeTouchIdAllUser() {
//		mTouchIdController.removeAll();
//	}
	
	private void removeUserTouchId() {
		mTouchIdController.removeUser(mUserId);
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
//			if(state == Response.ACK_SUCCESS) {
//				
//			}
//			else {
//				resetInput();
//			}
		}
		
		@Override
		public void onResponseRemoveUser(Command cmd, int state) {
			// TODO Auto-generated method stub
			if(state == Response.ACK_SUCCESS) {
				ToastUtils.showLongToast("用户指纹已清除，重新录入");
			}
		}
		
		@Override
		public void onResponseSearch(Command cmd, int state) {
			
		};
		
		@Override
		public void onResponseEnrollThird(Command cmd, int state) {
			// TODO Auto-generated method stub
			if(state == Response.ACK_SUCCESS) {
				getUserEgienValue();
			}
			else {
				resetInput();
			}
		}
		
		@Override
		public void onResponseEnrollSecond(Command cmd, int state) {
			// TODO Auto-generated method stub
			if(state == Response.ACK_SUCCESS) {
				VibratorUtil.fingerPrintVibrate();
				showFrameInput(STATE_THIRD_INPUT);
				mHandler.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						startInputThird();
					}
				}, DELAY_TIME);
			}
			else {
				resetInput();
			}
		}
		
		@Override
		public void onResponseEnrollFirst(Command cmd, int state) {
			// TODO Auto-generated method stub
			if(state == Response.ACK_SUCCESS) {
				VibratorUtil.fingerPrintVibrate();
				showFrameInput(STATE_SECOND_INPUT);
				mHandler.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						startInputSencond();
					}
				}, DELAY_TIME);
				
			}
			else {
				removeUserTouchId();
				resetInput();
			}
		}
		
		@Override
		public void onReponseUserEigenValue(Command cmd, int state, byte[] data) {
			// TODO Auto-generated method stub
			if(state == Response.ACK_SUCCESS) {
				mFingerPrint.setEigenValue(data);
				VibratorUtil.fingerPrintVibrate();
				showFrameInput(STATE_FOURTH_INPUT);
				mHandler.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						startInputFourth();
					}
				}, DELAY_TIME);
//				ToastUtils.showShortToast("成功获取特征值");
			}
			else {
				resetInput();
			}
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
			if(state == Response.ACK_SUCCESS) {
				VibratorUtil.fingerPrintVibrate();
				mFingerPrint.setCapture(bmp);
				mFingerPrint.setCompleted(true);
				showFrameReuslt();
			}
			else {
				showFrameInput(STATE_FOURTH_INPUT);
			}
		}
	};
}
