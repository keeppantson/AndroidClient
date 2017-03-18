package com.zgmz.ls.ui;

import com.zgmz.ls.R;
import com.zgmz.ls.base.Const;
import com.zgmz.ls.base.Const.InfoType;
import com.zgmz.ls.base.SharedDatas;
import com.zgmz.ls.base.SubActivity;
import com.zgmz.ls.db.DBHelper;
import com.zgmz.ls.model.FingerPrint;
import com.zgmz.ls.model.IdCard;
import com.zgmz.ls.model.SimpleUserInfo;
import com.zgmz.ls.model.UserInfo;
import com.zgmz.ls.utils.ToastUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class CheckUserInfoActivity extends SubActivity implements OnClickListener{
	
	private View mBaseInfo;
	
	private TextView mName;
	
	private TextView mIDNumber;
	
	private ImageView mAvatar;
	
	private ImageView mImageID;
	private ImageView mImageFingerPrint;
	private ImageView mImageYearPhoto;
	private ImageView mImageDistrict;
	
	private ImageView mImageFamilySituation;
	private ImageView mImageFamilyProperty;
	private ImageView mImageFamilyIncome;
	private ImageView mImageFamilySpending;
	
	private ImageView mImageSign;
	private ImageView mImageAttachment;
	
	
	private UserInfo mUserInfo;
	
	private int mUserId = 0;
	
	private int mType = InfoType.CHECK;
	
	@Override
	protected void onConfigrationTitleBar() {
		// TODO Auto-generated method stub
		super.onConfigrationTitleBar();
		setTitleBarTitleText(R.string.title_user_info);
	}
	
	
	@Override
	public void onTitleBarRightButtonOnClick(View v) {
		// TODO Auto-generated method stub
		if(mType == InfoType.CHECK) {
			if (mUserInfo != null && !mUserInfo.isChecked()) {
				
				if(!mUserInfo.isCompleted()) {
					ToastUtils.showLongToast("有必要项未填写，无法确认");
					return ;
				}
				if(DBHelper.getInstance().updateUserChecked(mUserId)) {
					updateUserInfo();
					SharedDatas.getInstance().checkUpdated();
					ToastUtils.showLongToast("已确认核查");
				}
			}
		}
	}
	
	private void updateTitle() {
		if(mType == InfoType.INPUT) {
			setTitleBarRightButtonText(null);
		}
		else if(mType == InfoType.CHECK) {
			if(mUserInfo != null) {
				if(mUserInfo.isChecked()) {
					setTitleBarTitleText(R.string.checked);
				}
				else {
					setTitleBarTitleText(R.string.uncheck);
					setTitleBarRightButtonText("确认");
				}
			}
		}
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.check_user_info);
		onNewIntent(getIntent());
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		updateUserInfo();
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		SimpleUserInfo info = (SimpleUserInfo) intent.getSerializableExtra(Const.KEY_USER_INFO);
		if(info == null) {
			ToastUtils.showLongToast("用户基本信息不能为空！");
			finish();
		}
		else {
			mUserId = info.getUserId();
			mType = intent.getIntExtra(Const.KEY_TYPE, InfoType.INPUT);
		}
	}
	
	private void updateUserInfo() {
		if(mUserId == 0) return;
		mUserInfo = DBHelper.getInstance().getUserInfo(mUserId, true);
		updateUserBaseInfo(mUserInfo);
		updateAllInfosState();
		updateTitle();
	}
	
	private void updateUserBaseInfo(UserInfo info) {
		if(info == null) return;
		mName.setText(info.getName());
		mIDNumber.setText(info.getIdNumber());
		if(info.isFlagId()) {
			mAvatar.setImageBitmap(info.getAvatar());
		}
	}
	
	
	private void updateAllInfosState() {
		UserInfo info = mUserInfo;
		updateIdCardState(info.isFlagId());
		updateFingerState(info.isFlagFinger());
		updateYearPhotoState(info.isFlagYearPhoto());
		updateDistrictState(info.isFlagDistrict());
		
		updateFamilySituationState(info.isFlagFmSituation());
		updateFamilyPropertyState(info.isFlagFmProperty());
		updateFamilyIncomeState(info.isFlagFmIncome());
		updateFamilySpendingState(info.isFlagFmSpending());
		
		updateSignState(info.isFlagSignature());
		updateAttachmentState(info.isFlagAttachment());
	}
	
	
	private void updateIdCardState(boolean flag) {
		if(flag) {
			mImageID.setImageResource(R.drawable.icon_card_on);
		}
		else {
			mImageID.setImageResource(R.drawable.icon_card_off);
		}
	}
	
	private void updateFingerState(boolean flag) {
		if(flag) {
			mImageFingerPrint.setImageResource(R.drawable.icon_fingerprint_on);
		}
		else {
			mImageFingerPrint.setImageResource(R.drawable.icon_fingerprint_off);
		}
	}
	
	private void updateYearPhotoState(boolean flag) {
		if(flag) {
			mImageYearPhoto.setImageResource(R.drawable.icon_camera_on);
		}
		else {
			mImageYearPhoto.setImageResource(R.drawable.icon_camera_off);
		}
	}
	
	private void updateDistrictState(boolean flag) {
		if(flag) {
			mImageDistrict.setImageResource(R.drawable.icon_position_on);
		}
		else {
			mImageDistrict.setImageResource(R.drawable.icon_position_off);
		}
	}
	
	private void updateFamilySituationState(boolean flag) {
		if(flag) {
			mImageFamilySituation.setImageResource(R.drawable.icon_family_on);
		}
		else {
			mImageFamilySituation.setImageResource(R.drawable.icon_family_off);
		}
	}
	
	private void updateFamilyPropertyState(boolean flag) {
		if(flag) {
			mImageFamilyProperty.setImageResource(R.drawable.icon_property_on);
		}
		else {
			mImageFamilyProperty.setImageResource(R.drawable.icon_property_off);
		}
	}
	
	private void updateFamilyIncomeState(boolean flag) {
		if(flag) {
			mImageFamilyIncome.setImageResource(R.drawable.icon_income_on);
		}
		else {
			mImageFamilyIncome.setImageResource(R.drawable.icon_income_off);
		}
	}
	
	private void updateFamilySpendingState(boolean flag) {
		if(flag) {
			mImageFamilySpending.setImageResource(R.drawable.icon_expenditure_on);
		}
		else {
			mImageFamilySpending.setImageResource(R.drawable.icon_expenditure_off);
		}
	}
	
	private void updateSignState(boolean flag) {
		if(flag) {
			mImageSign.setImageResource(R.drawable.icon_sign_on);
		}
		else {
			mImageSign.setImageResource(R.drawable.icon_sign_off);
		}
	}
	
	private void updateAttachmentState(boolean flag) {
		if(flag) {
			mImageAttachment.setImageResource(R.drawable.icon_attachment_on);
		}
		else {
			mImageAttachment.setImageResource(R.drawable.icon_attachment_off);
		}
	}
	
	@Override
	protected void setupViews(View view) {
		// TODO Auto-generated method stub
		mBaseInfo = view.findViewById(R.id.base_info);
		mAvatar = (ImageView) mBaseInfo.findViewById(R.id.avatar);
		mName = (TextView) mBaseInfo.findViewById(R.id.name);
		mIDNumber = (TextView) mBaseInfo.findViewById(R.id.id_number);
		mBaseInfo.setOnClickListener(this);
		
		TextView text;
		View item = view.findViewById(R.id.id_card);
		item.setOnClickListener(this);
		mImageID = (ImageView) item.findViewById(R.id.icon);
		mImageID.setImageResource(R.drawable.icon_card_off);
		text = (TextView) item.findViewById(R.id.text);
		text.setText(R.string.check_id_recognize);
		
		
		item = view.findViewById(R.id.fingerprint);
		item.setOnClickListener(this);
		mImageFingerPrint = (ImageView) item.findViewById(R.id.icon);
		mImageFingerPrint.setImageResource(R.drawable.icon_fingerprint_off);
		text = (TextView) item.findViewById(R.id.text);
		text.setText(R.string.check_fingerprint_input);
		
		item = view.findViewById(R.id.year_photo);
		item.setOnClickListener(this);
		mImageYearPhoto = (ImageView) item.findViewById(R.id.icon);
		mImageYearPhoto.setImageResource(R.drawable.icon_position_off);
		text = (TextView) item.findViewById(R.id.text);
		text.setText(R.string.check_year_photo);
		
		item = view.findViewById(R.id.district);
		item.setOnClickListener(this);
		mImageDistrict = (ImageView) item.findViewById(R.id.icon);
		mImageDistrict.setImageResource(R.drawable.icon_position_off);
		text = (TextView) item.findViewById(R.id.text);
		text.setText(R.string.check_district_area);
		
		
		item = view.findViewById(R.id.family_situation);
		item.setOnClickListener(this);
		mImageFamilySituation = (ImageView) item.findViewById(R.id.icon);
		mImageFamilySituation.setImageResource(R.drawable.icon_family_off);
		text = (TextView) item.findViewById(R.id.text);
		text.setText(R.string.check_family_situation);
		
		item = view.findViewById(R.id.family_property);
		item.setOnClickListener(this);
		mImageFamilyProperty = (ImageView) item.findViewById(R.id.icon);
		mImageFamilyProperty.setImageResource(R.drawable.icon_property_off);
		text = (TextView) item.findViewById(R.id.text);
		text.setText(R.string.check_family_property);
		
		item = view.findViewById(R.id.family_income);
		item.setOnClickListener(this);
		mImageFamilyIncome = (ImageView) item.findViewById(R.id.icon);
		mImageFamilyIncome.setImageResource(R.drawable.icon_income_off);
		text = (TextView) item.findViewById(R.id.text);
		text.setText(R.string.check_family_income);
		
		item = view.findViewById(R.id.family_spending);
		item.setOnClickListener(this);
		mImageFamilySpending = (ImageView) item.findViewById(R.id.icon);
		mImageFamilySpending.setImageResource(R.drawable.icon_expenditure_off);
		text = (TextView) item.findViewById(R.id.text);
		text.setText(R.string.check_family_spending);
		
		
		item = view.findViewById(R.id.signature);
		item.setOnClickListener(this);
		mImageSign = (ImageView) item.findViewById(R.id.icon);
		mImageSign.setImageResource(R.drawable.icon_sign_off);
		text = (TextView) item.findViewById(R.id.text);
		text.setText(R.string.check_signature);
		
		item = view.findViewById(R.id.attachment);
		item.setOnClickListener(this);
		mImageAttachment = (ImageView) item.findViewById(R.id.icon);
		mImageAttachment.setImageResource(R.drawable.icon_attachment_off);
		text = (TextView) item.findViewById(R.id.text);
		text.setText(R.string.check_attachment);
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
			case R.id.base_info:
				preview();
				break;
			case R.id.id_card:
				recognizeIdCard();
				break;
			case R.id.fingerprint:
				inputFingerPring();
				break;
			case R.id.year_photo:
				inputYearPhoto();
				break;
			case R.id.district:
				inputDistrict();
				break;
			case R.id.family_situation:
				inputFamilySituation();
				break;
			case R.id.family_property:
				inputFamilyProperty();
				break;
			case R.id.family_income:
				inputFamilyIncome();
				break;
			case R.id.family_spending:
				inputFamilySpending();
				break;
			case R.id.signature:
				inputSignature();
				break;
			case R.id.attachment:
				addAttachment();
				break;
		}
	}
	
	
	private static final int REQUEST_CODE_RECONGNIZE = 2001;
	
	private static final int REQUEST_CODE_FINGERPRINT = 2002;
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode == REQUEST_CODE_RECONGNIZE) {
			if(resultCode == Activity.RESULT_OK) {
				String key = data.getStringExtra(Const.KEY_ID_CARD);
				if(key != null) {
					IdCard idcard = (IdCard) SharedDatas.getInstance().getValue(key);
					if(idcard != null) {
						if(idcard.getIdNumber().equals(mUserInfo.getIdNumber())) {
							idcard.setUserId(mUserInfo.getUserId());
							saveIdCard(idcard);
						}
						else {
							ToastUtils.showLongToast("身份证号不一致，无法保存");
						}
					}
						
				}
			}
		}
		else if(requestCode == REQUEST_CODE_FINGERPRINT) {
			if(resultCode == Activity.RESULT_OK) {
				String key = data.getStringExtra(Const.KEY_FINGER_PRINT);
				if(key != null) {
					FingerPrint finger = (FingerPrint) SharedDatas.getInstance().getValue(key);
					if(finger != null) {
						finger.setUserId(mUserInfo.getUserId());
						saveFingerPrint(finger);
					}
						
				}
			}
		}
	}
	
	private void saveIdCard(IdCard idCard) {
		if(!DBHelper.getInstance().insertOrUpdateIdCard(idCard)) {
			updateUserInfo();
		}
	}
	
	private void saveFingerPrint(FingerPrint finger) {
		if(DBHelper.getInstance().insertOrUpdateFingerPrint(finger)) {
			mUserInfo.setFlagFinger(true);
			updateFingerState(true);
		}
	}
	
	public void updateItemIcon(ImageView image, int resid) {
		image.setImageResource(resid);
	}
	
	private void preview() {
		Intent intent = new Intent();
		intent.putExtra(Const.KEY_USER_INFO, UserInfo.getSimpleUserInfo(mUserInfo));
		intent.setClass(this,PreviewActivity.class);
		startActivity(intent);
	}
	
	private void recognizeIdCard() {
		Intent intent = new Intent();
		intent.putExtra(Const.KEY_USER_INFO, UserInfo.getSimpleUserInfo(mUserInfo));
		intent.setClass(this,IDRecoginzeActivity.class);
		startActivityForResult(intent, REQUEST_CODE_RECONGNIZE);
	}
	
	private void inputFingerPring() {
		Intent intent = new Intent();
		intent.putExtra(Const.KEY_USER_INFO, UserInfo.getSimpleUserInfo(mUserInfo));
		intent.setClass(this,FingerprintCheckActivitiy.class);
		startActivityForResult(intent, REQUEST_CODE_FINGERPRINT);
	}
	
	private void inputYearPhoto() {
		Intent intent = new Intent();
		intent.putExtra(Const.KEY_USER_INFO, UserInfo.getSimpleUserInfo(mUserInfo));
		intent.setClass(this,YearPhotoActivity.class);
		startActivity(intent);
	}
	
	private void inputDistrict() {
		Intent intent = new Intent();
		intent.putExtra(Const.KEY_USER_INFO, UserInfo.getSimpleUserInfo(mUserInfo));
		intent.setClass(this,DistrictActivity.class);
		startActivity(intent);
	}

	private void inputFamilySituation() {
		Intent intent = new Intent();
		intent.putExtra(Const.KEY_USER_INFO, UserInfo.getSimpleUserInfo(mUserInfo));
		intent.setClass(this,FamilySituationActivity.class);
		startActivity(intent);
	}

	private void inputFamilyProperty() {
		Intent intent = new Intent();
		intent.putExtra(Const.KEY_USER_INFO, UserInfo.getSimpleUserInfo(mUserInfo));
		intent.setClass(this,FamilyPropertyActivity.class);
		startActivity(intent);
	}
	
	
	
	private void inputFamilyIncome() {
		Intent intent = new Intent();
		intent.putExtra(Const.KEY_USER_INFO, UserInfo.getSimpleUserInfo(mUserInfo));
		intent.setClass(this,FamilyIncomeActivity.class);
		startActivity(intent);
	}
	
	private void inputFamilySpending() {
		Intent intent = new Intent();
		intent.putExtra(Const.KEY_USER_INFO, UserInfo.getSimpleUserInfo(mUserInfo));
		intent.setClass(this,FamilySpendingActivity.class);
		startActivity(intent);
	}
	
	private void inputSignature() {
		Intent intent = new Intent();
		intent.putExtra(Const.KEY_USER_INFO, UserInfo.getSimpleUserInfo(mUserInfo));
		intent.setClass(this,SignatureActivity.class);
		startActivity(intent);
	}
	
	private void addAttachment() {
		Intent intent = new Intent();
		intent.putExtra(Const.KEY_USER_INFO, UserInfo.getSimpleUserInfo(mUserInfo));
		intent.setClass(this,AttachmentActivity.class);
		startActivity(intent);
	}
}
