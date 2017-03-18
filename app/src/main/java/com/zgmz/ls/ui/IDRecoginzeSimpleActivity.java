package com.zgmz.ls.ui;

import com.zgmz.ls.R;
import com.zgmz.ls.base.Const;
import com.zgmz.ls.base.SharedDatas;
import com.zgmz.ls.base.SubActivity;
import com.zgmz.ls.model.IdCard;
import com.zgmz.ls.module.IDNumberReader;
import com.zgmz.ls.module.IDNumberReader.OnIDReaderListener;
import com.zgmz.ls.utils.VibratorUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class IDRecoginzeSimpleActivity extends SubActivity implements OnClickListener, OnIDReaderListener{
	
	private View mFrameRecognize;
	private TextView mPrompt;
	
	private Button mBtnRecognize;
	private IDNumberReader mIdNumberReader;
	
	// 等待识别
	private static final int STATE_WAIT_RECOGNIZE = 1;
	// 正在识别
	private static final int STATE_RECOGNIZING = 2;
	// 识别结束
	private static final int STATE_RECOGNIZED = 3;
	
	
	
	private static final long DEFAULT_TIME_OUT = 10000;
	
	private int mState =  STATE_WAIT_RECOGNIZE;
	
	private long mTimeOut = DEFAULT_TIME_OUT;
	
	private int mTimeCount = 0;
	
	private static final int MSG_TIME = 0x2001;
	
	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch(msg.what) {
			case MSG_TIME:
				if(mTimeCount>0) {
					sendEmptyMessageDelayed(MSG_TIME, 1000);
					mBtnRecognize.setText("识别倒计时 "+ mTimeCount);
					mTimeCount--;
				}
				else {
					mTimeCount = 0;
					showReconginzeFailure();
				}
				break;
			}
		};
	};
	
	private void startTimer() {
		mTimeCount = (int) (mTimeOut/1000);
		mHandler.sendEmptyMessage(MSG_TIME);
	}
	
	private void stopTimer() {
		mTimeCount = 0;
		mHandler.removeMessages(MSG_TIME);
	}
	
	@Override
	protected void onConfigrationTitleBar() {
		// TODO Auto-generated method stub
		super.onConfigrationTitleBar();
		setTitleBarTitleText(R.string.title_id_recognize);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.id_recognize_simple);
		onNewIntent(getIntent());
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
		startReconginze();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		stopReconginze();
		super.onPause();
	}
	
	@Override
	protected void setupViews(View view) {
		// TODO Auto-generated method stub
		mFrameRecognize = view.findViewById(R.id.frame_recognize);
		
		
		mPrompt = (TextView) mFrameRecognize.findViewById(R.id.prompt);
		mBtnRecognize = (Button)mFrameRecognize.findViewById(R.id.btn_recognize);
		
		mBtnRecognize.setOnClickListener(this);
		
		showFrameRecognize();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
			case R.id.btn_recognize:
				startReconginze();
				break;
		}
	}
	
	
	private void startReconginze() {
		if(mState != STATE_RECOGNIZING) {
			showFrameRecognize();
			startTimer();
			mState =  STATE_RECOGNIZING;
			mBtnRecognize.setEnabled(false);
			openIdNumberReader();
		}
	}
	
	private void stopReconginze() {
		if(mState == STATE_RECOGNIZING) {
			showFrameRecognize();
			stopTimer();
			mState =  STATE_WAIT_RECOGNIZE;
			mBtnRecognize.setEnabled(true);
			closeIdNumberReader();
		}
	}
	
	private void openIdNumberReader() {
		if(mIdNumberReader == null) {
			mIdNumberReader = new IDNumberReader(this);
			mIdNumberReader.setTimeout(mTimeOut);
			mIdNumberReader.setOnIDReaderListener(this);
		}
		mIdNumberReader.OpenReader();
	}
	
	private void closeIdNumberReader() {
		if(mIdNumberReader != null) {
			mIdNumberReader.stopReader();
		}
	}
	
	
	private void showFrameRecognize() {
		mPrompt.setText(R.string.prompt_reconginze_id_ready);
		mBtnRecognize.setText(R.string.start_reconginze);
	}
	
	private void showReconginzeFailure() {
		mPrompt.setText(R.string.prompt_reconginze_id_failure);
		mBtnRecognize.setText(R.string.again_reconginze);
		mBtnRecognize.setEnabled(true);
		mState =  STATE_RECOGNIZED;
	}
	
	
	private void backResult(IdCard idcard) {
		Intent data = getIntent();
		data.putExtra(Const.KEY_ID_CARD, SharedDatas.getInstance().put(idcard));
		setResult(Activity.RESULT_OK, data);
		finish();
	}

	@Override
	public void onReadSuccess(IdCard idcard) {
		// TODO Auto-generated method stub
//		ToastUtils.showLongToast(idcard.toString());
		stopTimer();
		VibratorUtil.defaultVibrate();
		backResult(idcard);
	}

	@Override
	public void onReadFailure(int status) {
		// TODO Auto-generated method stub
		stopTimer();
		showReconginzeFailure();
	}

	@Override
	public void onReadTime(long time, long timeout) {
		// TODO Auto-generated method stub
		if(timeout<=time) {
			stopTimer();
		}
	}

}
