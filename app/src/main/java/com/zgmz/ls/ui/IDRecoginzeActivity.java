package com.zgmz.ls.ui;

import com.zgmz.ls.R;
import com.zgmz.ls.base.Const;
import com.zgmz.ls.base.SharedDatas;
import com.zgmz.ls.base.SubActivity;
import com.zgmz.ls.db.DBHelper;
import com.zgmz.ls.model.IdCard;
import com.zgmz.ls.model.SimpleUserInfo;
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
import android.widget.ImageView;
import android.widget.TextView;

public class IDRecoginzeActivity extends SubActivity implements OnClickListener, OnIDReaderListener{
	
	private View mFrameRecognize;
	private View mFrameResult;
	
	private Button mBtnRecognize;
	private Button mBtnAgainRecognize;
	private Button mBtnOk;
    private ImageView fake_bg;
	private ImageView fake_head;
	
	private View mIdCardViewA;
	private View mIdCardViewB;
	
	private TextView mName;
	
	private TextView mSex;
	
	private TextView mNation;
	
	private TextView mYear;
	private TextView mMonth;
	private TextView mDay;
	
	private ImageView mAvatar;
	
	private TextView mAddress;
	
	private TextView mIDNumber;
	
	private TextView mAuthority;
	
	private TextView mPeriodOfValidity;
	
	private IDNumberReader mIdNumberReader;
	
	private IdCard mIdCard;
	
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
	
	private SimpleUserInfo mUserInfo;
	

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
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shen_fen_shi_bie);
		onNewIntent(getIntent());
        setupViews();
	}
	@Override
	protected void onConfigrationTitleBar() {
		// TODO Auto-generated method stub
		super.onConfigrationTitleBar();
		setTitleBarTitleText("身份证识别");
	}
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		mUserInfo = (SimpleUserInfo) intent.getSerializableExtra(Const.KEY_USER_INFO); 
		if(mUserInfo != null) {
			IdCard  card = DBHelper.getInstance().getIdCard(mUserInfo.getUserId());
			if(card != null) {
				updateIDCardViews(card);
				showFrameReuslt();
				mBtnOk.setEnabled(false);
			}
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		stopReconginze();
		super.onPause();
	}

	protected void setupViews() {
		// TODO Auto-generated method stub
		mFrameRecognize = this.findViewById(R.id.frame_recognize);
		mFrameResult = this.findViewById(R.id.frame_result);


		mBtnRecognize = (Button)mFrameRecognize.findViewById(R.id.btn_recognize);
		mBtnAgainRecognize = (Button)mFrameResult.findViewById(R.id.btn_again_recognize);
		mBtnOk = (Button)mFrameResult.findViewById(R.id.btn_ok);
		
		mBtnRecognize.setOnClickListener(this);
		mBtnAgainRecognize.setOnClickListener(this);
		mBtnOk.setOnClickListener(this);
        fake_bg = (ImageView) this.findViewById(R.id.fake_bg);
		fake_head = (ImageView) this.findViewById(R.id.fake_head);
		mIdCardViewA = mFrameResult.findViewById(R.id.card_a);
		mIdCardViewB = mFrameResult.findViewById(R.id.card_b);
		
		mName = (TextView)mIdCardViewA.findViewById(R.id.name);
		mSex = (TextView)mIdCardViewA.findViewById(R.id.sex);
		mNation = (TextView)mIdCardViewA.findViewById(R.id.nation);
		mYear = (TextView)mIdCardViewA.findViewById(R.id.year);
		mMonth = (TextView)mIdCardViewA.findViewById(R.id.month);
		mDay = (TextView)mIdCardViewA.findViewById(R.id.day);
		mAddress = (TextView)mIdCardViewA.findViewById(R.id.address);
		mIDNumber = (TextView)mIdCardViewA.findViewById(R.id.id_number);
		
		mAvatar = (ImageView)mIdCardViewA.findViewById(R.id.avatar);
		
		mAuthority = (TextView)mIdCardViewB.findViewById(R.id.authority);
		mPeriodOfValidity = (TextView)mIdCardViewB.findViewById(R.id.period_of_validity);
		showFrameRecognize();
//		showFrameReuslt();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
			case R.id.btn_recognize:
			case R.id.btn_again_recognize:
				startReconginze();
				break;
			case R.id.btn_ok:
				backResult();
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
		mFrameResult.setVisibility(View.GONE);
		mBtnRecognize.setText(R.string.start_reconginze);
	}
	
	private void showReconginzeFailure() {
		mBtnRecognize.setText(R.string.again_reconginze);
		mBtnRecognize.setEnabled(true);
		mState =  STATE_RECOGNIZED;
	}
	
	private void showFrameReuslt() {
		mFrameResult.setVisibility(View.VISIBLE);
		mBtnRecognize.setText(R.string.start_reconginze);
        mBtnRecognize.setVisibility(View.GONE);
        fake_bg.setVisibility(View.GONE);
		fake_head.setVisibility(View.GONE);
		mBtnOk.setEnabled(true);
		mState =  STATE_RECOGNIZED;
	}
	
	private void updateIDCardViews(IdCard card) {
		resetIDCardViews();
		mIdCard = card;
		mName.setText(card.getName());
		mSex.setText(card.getSex());
		mNation.setText(card.getNation());
		mYear.setText(card.getBirthYear());
		mMonth.setText(card.getBirthMonth());
		mDay.setText(card.getBirthDay());
		mAddress.setText(card.getAddress());
		mIDNumber.setText(card.getIdNumber());
		
		mAuthority.setText(card.getAuthority());
		mPeriodOfValidity.setText(card.getStartValidDate()+" - "+card.getEndValidDate());
		
		mAvatar.setImageBitmap(card.getAvatar());
	}
	
	private void resetIDCardViews() {
		mName.setText(null);
		mSex.setText(null);
		mNation.setText(null);
		mYear.setText(null);
		mMonth.setText(null);
		mDay.setText(null);
		mAddress.setText(null);
		mIDNumber.setText(null);
		mAuthority.setText(null);
		mPeriodOfValidity.setText(null);
		mAvatar.setImageBitmap(null);
	}
	
	private void backResult() {
		Intent data = getIntent();
		data.putExtra(Const.KEY_ID_CARD, SharedDatas.getInstance().put(mIdCard));
		setResult(Activity.RESULT_OK, data);
		SharedDatas.getInstance().recordUpdated();
		finish();
	}

	@Override
	public void onReadSuccess(IdCard idcard) {
		// TODO Auto-generated method stub
//		ToastUtils.showLongToast(idcard.toString());
		stopTimer();
		updateIDCardViews(idcard);
		showFrameReuslt();
		VibratorUtil.defaultVibrate();
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
//		ToastUtils.showShortToast("time = " + time + ", timeout = " + timeout);
		if(timeout<=time) {
			stopTimer();
		}
	}

}
