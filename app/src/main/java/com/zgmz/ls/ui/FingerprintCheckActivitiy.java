package com.zgmz.ls.ui;

import com.android.charger.EmGpio;
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
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.zz.jni.mxComFingerDriver;
import org.zz.jni.zzFingerAlg;

import finger.BMP;

import static java.lang.Thread.sleep;

public class FingerprintCheckActivitiy extends SubActivity implements OnClickListener{
	
//	private static final int FINGER_USER_ID = 202;
	
	private View mFrameFingerInput;
	
	private View mFrameResult;
	
	private TextView mPrompt;
	
	private ImageView mFingerDemoImage;
	
	private ImageView mFingerImage;
	
	private Button mBtnInput;
	private Button mBtnAgainInput;
	private Button mBtnOk;
	
	private TextView mResultText;
	
	
	
	private static final int STATE_FIRST_INPUT = 1;
	private static final int STATE_SECOND_INPUT = 2;
	private static final int STATE_THIRD_INPUT = 3;
	private static final int STATE_FOURTH_INPUT = 4;
	private static final int STATE_INPUT_SUCCESS = 8;
	
	private static final int STATE_CHECK_INPUT = 0x101;
	private static final int STATE_CHECK_SUCCESS = 0x102;
	
	private int mState = STATE_FIRST_INPUT;
	
	private FingerPrint mFingerPrint;
	
	private SimpleUserInfo mUserInfo;

	
	private int mUserId = 0;
	
	private static final long DELAY_TIME = 1500;
	
	Handler mHandler = new Handler();

	mxComFingerDriver devDriver;
	zzFingerAlg algDriver;
	@Override
	protected void onConfigrationTitleBar() {
		// TODO Auto-generated method stub
		super.onConfigrationTitleBar();
		setTitleBarTitleText(R.string.title_fingerprint_check);
	}
	void show_image()
	{

		if(m_bitmap != null){
			if (!m_bitmap.isRecycled()) {
				m_bitmap.recycle();
			}
		}
		m_bitmap = BMP.Raw2Bimap(m_bImgBuf,IMAGE_X,IMAGE_Y);
		if(m_bitmap!=null){
			ImageView image_open = (ImageView) findViewById(R.id.finger_demo_image);
			image_open.setImageBitmap(m_bitmap);
		}
	}

    private void ShowDlg(String strMsg) {
        new AlertDialog.Builder(FingerprintCheckActivitiy.this).setTitle("提示信息")
                .setMessage(strMsg)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialoginterface, int i) {
                    }
                }).show();
    }
	private static final int TZ_SIZE =512;
	private byte[] m_mbBuf = new byte[TZ_SIZE*3];
	int m_iTimeout      = 15*1000;

	Bitmap m_bitmap = null;
	private static final  int IMAGE_X     = 208;
	private static final  int IMAGE_Y     = 288;
	private static final  int IMAGE_SIZE  = IMAGE_X*IMAGE_Y;
	private byte[] m_bImgBuf = new byte[IMAGE_SIZE];
	public void Enroll() {
		int nRet = 0;
		byte[] tzBuf1 = new byte[TZ_SIZE];
		byte[] tzBuf2 = new byte[TZ_SIZE];
		byte[] tzBuf3 = new byte[TZ_SIZE];
		String strDevName  = "/dev/ttyMT3";
		String strBaudRate = "115200";
		int iBaudRate = Integer.parseInt(strBaudRate);
        ShowDlg("0=======================");
		nRet = devDriver.mxGetComIdCardTz(strDevName, iBaudRate, m_iTimeout,tzBuf1);
		if (nRet != 0) {
			ToastUtils.showLongToast("注册指纹模板失败,nRet=" + nRet);
			return ;
		}

        ShowDlg("1=======================");
		nRet = devDriver.mxGetImageDY(strDevName, iBaudRate, m_iTimeout,m_bImgBuf);
		if (nRet != 0) {
            ShowDlg("1=======================:" + nRet);
			return;
		}
        ShowDlg("2=======================:" + nRet);
		show_image();
		ToastUtils.showLongToast("请第2次手指...");
		nRet = devDriver.mxGetComIdCardTz(strDevName, iBaudRate, m_iTimeout,tzBuf2);
		if (nRet != 0) {
			ToastUtils.showLongToast("注册指纹模板失败,nRet=" + nRet);
			return ;
		}
		nRet = devDriver.mxGetImageDY(strDevName, iBaudRate, m_iTimeout,m_bImgBuf);
		if (nRet != 0) {
			ToastUtils.showLongToast("获取指纹失败,nRet=" + nRet);
			return;
		}
		show_image();

		nRet = algDriver.mxFingerMatch512(tzBuf1,tzBuf2,3);
		if(nRet!=0)
		{
			ToastUtils.showLongToast("注册指纹模板失败,nRet=" + nRet);
			return;
		}

		ToastUtils.showLongToast("请第3次手指...");
		nRet = devDriver.mxGetComIdCardTz(strDevName, iBaudRate, m_iTimeout,tzBuf3);
		if (nRet != 0) {
			ToastUtils.showLongToast("注册指纹模板失败,nRet=" + nRet);
			return ;
		}
		nRet = devDriver.mxGetImageDY(strDevName, iBaudRate, m_iTimeout,m_bImgBuf);
		if (nRet != 0) {
			ToastUtils.showLongToast("获取指纹失败,nRet=" + nRet);
			return;
		}
		show_image();

		nRet = algDriver.mxFingerMatch512(tzBuf1,tzBuf3,3);
		if(nRet!=0)
		{
			ToastUtils.showLongToast("注册指纹模板失败,nRet=" + nRet);
			return;
		}

		for(int j=0;j<512;j++)
		{
			m_mbBuf[j] = tzBuf1[j];
		}
		for(int j=0;j<512;j++)
		{
			m_mbBuf[512+j] = tzBuf2[j];
		}
		for(int j=0;j<512;j++)
		{
			m_mbBuf[1024+j] = tzBuf3[j];
		}

		ToastUtils.showLongToast("请第3次手指...");
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fingerprint);
		onNewIntent(getIntent());

		mtSetGPIOValue(4, true);
		sleep(188);
	}


	//liuwei add for power ctrl
	private void sleep(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
		}
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
//				showFrameReuslt();
//				mBtnOk.setEnabled(false);
//				mFingerImage.setImageBitmap(mFingerPrint.getCapture());
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
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
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
		
		mResultText = (TextView)view.findViewById(R.id.result_text);
		mBtnAgainInput.setText(R.string.fingerprint_re_check);
		mResultText.setText(R.string.fingerprint_check_success);
		
		mBtnInput.setOnClickListener(this);
		mBtnAgainInput.setOnClickListener(this);
		mBtnOk.setOnClickListener(this);
		
		showFrameInput(STATE_CHECK_INPUT);
		mBtnInput.setEnabled(true);


		devDriver = new mxComFingerDriver();
		algDriver = new zzFingerAlg();
		
	}


    private class EnrollThread extends Thread {
        public void run() {
            Looper.prepare();
            try {
                Enroll();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                Looper.loop();
            }
        }
    };
    private EnrollThread   m_enrollThread = null;
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		final int state = mState;
		
		switch(v.getId()) {
			case R.id.btn_finger_input:
                if (m_enrollThread != null) {
                    m_enrollThread.interrupt();
                    m_enrollThread = null;
                }
                m_enrollThread = new EnrollThread();
                m_enrollThread.start();
				/*if (state == STATE_CHECK_INPUT) {
					startCheckFringer();
				}
				else if (state == STATE_FIRST_INPUT) {
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
				}*/
				break;
			case R.id.btn_again_finger_input:
//				removeUserTouchId();
//				resetInput();
				resetCheck();
				startCheckFringer();
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
	
	private void resetCheck() {
		mFingerPrint.setCapture(null);
		mFingerPrint.setEigenValue(null);
		showFrameInput(STATE_CHECK_INPUT);
		mBtnInput.setEnabled(true);
	}
	
	private void showFrameInput(int state) {
		mFrameFingerInput.setVisibility(View.VISIBLE);
		mFrameResult.setVisibility(View.GONE);
		mBtnInput.setEnabled(false);
		switch(state) {
			case STATE_CHECK_INPUT:
				mFingerDemoImage.setBackgroundResource(R.drawable.fingerprint_a);
				mPrompt.setText(R.string.fingerprint_check_prompt);
				mBtnInput.setText(R.string.fingerprint_check_input_first);
				break;
			case STATE_FIRST_INPUT:
				mFingerDemoImage.setBackgroundResource(R.drawable.fingerprint_a);
				mPrompt.setText(R.string.fingerprint_check_prompt);
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

		mtSetGPIOValue(4, false);
		finish();
	}
	public void mtSetGPIOValue(int pin, boolean bHigh)
	{
		if (pin < 0) {
			return;
		}
		EmGpio.gpioInit();
		EmGpio.setGpioMode(pin);
		if (bHigh)
		{
			EmGpio.setGpioOutput(pin);
			EmGpio.setGpioDataHigh(pin);
		}
		else
		{
			EmGpio.setGpioOutput(pin);
			EmGpio.setGpioDataLow(pin);
		}
		EmGpio.gpioUnInit();
	}
	private void showFrameReuslt() {
		mFrameFingerInput.setVisibility(View.GONE);
		mFrameResult.setVisibility(View.VISIBLE);
		mFingerImage.setImageBitmap(mFingerPrint.getCapture());
		mBtnOk.setEnabled(true);
//		mFingerImage.setRotation(180);
		mState = STATE_INPUT_SUCCESS;
	}
	
	private void shwoUserFinger(int userId) {
		FingerPrint fp = DBHelper.getInstance().getFingerPrint(userId);
		 
		
		if(fp != null && mFingerPrint.getUserId() == userId) {
			mFrameFingerInput.setVisibility(View.GONE);
			mFrameResult.setVisibility(View.VISIBLE);
			mFingerImage.setImageBitmap(fp.getCapture());
			mBtnOk.setEnabled(false);
			mState = STATE_CHECK_SUCCESS;
			ToastUtils.showLongToast("比对正确");
		}
		else {
			ToastUtils.showLongToast("比对失败");
			resetCheck();
		}
	}
	
	private void startCheckFringer() {
		mBtnInput.setEnabled(false);
	}
	
	private void startInputFirst() {
		mBtnInput.setEnabled(false);
		mBtnInput.setText(R.string.fingerprint_input_waiting);
		mPrompt.setText("已完成 0% \n等待第一次录入指纹");
	}
	
	private void startInputSencond() {
		mBtnInput.setEnabled(false);
		mBtnInput.setText(R.string.fingerprint_input_waiting);
		mPrompt.setText("已完成 25% \n等待第二次录入指纹");
	}
	
	private void startInputThird() {
		mBtnInput.setEnabled(false);
		mBtnInput.setText(R.string.fingerprint_input_waiting);
		mPrompt.setText("已完成 60% \n等待第三次录入指纹");
	}
	
	private void startInputFourth() {
		mBtnInput.setEnabled(false);
		mBtnInput.setText(R.string.fingerprint_input_waiting);
		mPrompt.setText("已完成 80% \n等待最后一次录入指纹");
	}

}
