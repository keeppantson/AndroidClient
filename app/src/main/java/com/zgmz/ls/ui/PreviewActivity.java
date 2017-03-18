package com.zgmz.ls.ui;

import java.util.List;

import com.zgmz.ls.R;
import com.zgmz.ls.base.Const;
import com.zgmz.ls.base.SubActivity;
import com.zgmz.ls.db.DBHelper;
import com.zgmz.ls.model.Attachment;
import com.zgmz.ls.model.PreviewInfo;
import com.zgmz.ls.model.Signature;
import com.zgmz.ls.model.SimpleUserInfo;
import com.zgmz.ls.utils.BitmapHelper;
import com.zgmz.ls.utils.PreviewTools;
import com.zgmz.ls.utils.ToastUtils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class PreviewActivity extends SubActivity {

	private ImageView mAvatar;
	private ImageView mYearPhoto;
	private TextView mTextIdCard;
	private TextView mTextDistric;
	private TextView mTextFamilySituation;
	private TextView mTextFamilyProperty;
	private TextView mTextFamilyIncome;
	private TextView mTextFamilySpending;
	
	private ImageView mImageFingerprint;
	
	private SimpleUserInfo mUserInfo;
	
	private PreviewInfo mPreviewInfo;
	
	private ImageView mUserSignature;
	
	private ImageView mManagerSignature;
	
	private LinearLayout mAttachFrame;
	
	@Override
	protected void onConfigrationTitleBar() {
		// TODO Auto-generated method stub
		super.onConfigrationTitleBar();
		setTitleBarTitleText(R.string.title_preview);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preview);
		onNewIntent(getIntent());
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		mUserInfo = (SimpleUserInfo) intent.getSerializableExtra(Const.KEY_USER_INFO); 
		if(mUserInfo != null) {
			loadPreviewInfo();
		}
		else {
			ToastUtils.showLongToast("未知用户");
			finish();
		}
	}
	
	
	@Override
	protected void setupViews(View view) {
		// TODO Auto-generated method stub
		mImageFingerprint = (ImageView)view.findViewById(R.id.image_finger);
		mAvatar = (ImageView)view.findViewById(R.id.avatar);
		mYearPhoto = (ImageView)view.findViewById(R.id.year_photo);
		mAvatar.setVisibility(View.GONE);
		mTextIdCard = (TextView)view.findViewById(R.id.text_id_card);
		mTextDistric = (TextView)view.findViewById(R.id.text_district);
		mTextFamilySituation = (TextView)view.findViewById(R.id.text_family_situation);
		mTextFamilyProperty = (TextView)view.findViewById(R.id.text_family_property);
		mTextFamilyIncome = (TextView)view.findViewById(R.id.text_family_income);
		mTextFamilySpending = (TextView)view.findViewById(R.id.text_family_spending);
		mUserSignature = (ImageView)view.findViewById(R.id.user_signature);
		mManagerSignature = (ImageView)view.findViewById(R.id.manager_signature);
		
		mAttachFrame = (LinearLayout)view.findViewById(R.id.attachments);
	}
	
	private void loadPreviewInfo() {
		mPreviewInfo = DBHelper.getInstance().getPreviewInfo(mUserInfo.getUserId());
		updateViews();
	}
	
	private void updateViews() {
		if(mPreviewInfo == null) return;
		PreviewInfo info = mPreviewInfo;
		if(info.getIdCard() != null) {
			mTextIdCard.setText(PreviewTools.getIdCard(info.getIdCard()));
			if(info.getIdCard().getAvatar() != null) {
				mAvatar.setVisibility(View.VISIBLE);
				mAvatar.setImageBitmap(info.getIdCard().getAvatar());
			}
		}
		
		
		if(info.getFingerPrint() != null)
			mImageFingerprint.setImageBitmap(info.getFingerPrint().getCapture());
		
		if(info.getYearPhoto() != null && info.getYearPhoto().getPath() != null) {
			Bitmap bmp = BitmapHelper.getThumbBitmap(info.getYearPhoto().getPath(), 720, 720);
			if(bmp != null) {
				mYearPhoto.setImageBitmap(bmp);
			}
		}
		
		mTextDistric.setText(PreviewTools.getDistrict(info.getDistrict()));
		
		mTextFamilySituation.setText(PreviewTools.getFamilySituation(info.getFamilySituation()));
		mTextFamilyProperty.setText(PreviewTools.getFamilyProperty(info.getFamilyProperty()));
		mTextFamilyIncome.setText(PreviewTools.getFamilyIncome(info.getFamilyIncome()));
		mTextFamilySpending.setText(PreviewTools.getFamilySpending(info.getFamilySpending()));
		
		Signature signature = info.getSignature();
		if(signature != null) {
			mUserSignature.setImageBitmap(signature.getUserSignature());
			mManagerSignature.setImageBitmap(signature.getManagerSignature());
		}
		
		List<Attachment> attachments = info.getAttachments();
		updateAttchments(attachments);
	}
	
	
	private void updateAttchments(List<Attachment> attachments) {
		mAttachFrame.removeAllViews();
		if(attachments != null && attachments.size()>0) {
			LinearLayout.LayoutParams lp;
			ImageView image;
			Attachment attach;
			for(int i=0; i<attachments.size(); i++) {
				attach = attachments.get(i);
				if(!TextUtils.isEmpty(attach.getPath())) {
					image = new ImageView(this);
					lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
					lp.bottomMargin = 40;
					mAttachFrame.addView(image, lp);
					image.setScaleType(ScaleType.CENTER_INSIDE);
					Bitmap bmp = BitmapHelper.getThumbBitmap(attach.getPath(), 720, 720);
					if(bmp != null) {
						image.setImageBitmap(bmp);
					}
					else {
						image.setImageResource(R.drawable.photo_image);
					}
				}
			}
		}
	}
}
