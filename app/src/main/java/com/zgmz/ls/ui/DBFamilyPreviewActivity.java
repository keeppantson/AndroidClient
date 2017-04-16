package com.zgmz.ls.ui;

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

import com.zgmz.ls.R;
import com.zgmz.ls.base.Const;
import com.zgmz.ls.base.SubActivity;
import com.zgmz.ls.db.DBHelper;
import com.zgmz.ls.model.Attachment;
import com.zgmz.ls.model.CheckTask;
import com.zgmz.ls.model.FamilyBase;
import com.zgmz.ls.model.PreviewInfo;
import com.zgmz.ls.model.Signature;
import com.zgmz.ls.model.SimpleUserInfo;
import com.zgmz.ls.model.UserInfo;
import com.zgmz.ls.utils.BitmapHelper;
import com.zgmz.ls.utils.PreviewTools;
import com.zgmz.ls.utils.ToastUtils;

import java.util.List;

public class DBFamilyPreviewActivity extends SubActivity {
	private ImageView mAvatar;
	private ImageView mYearPhoto;
	private TextView mTextIdCard;
	private TextView mTextNd;
	private TextView mTextRq;
	private TextView mTextFzr;
	private TextView mTextLx;
	private TextView mTextXzqhdm;
	private TextView mTextApplyTime;
	private TextView mTextJzlx;
	private TextView mTextZpyy;

	private ImageView mImageFingerprint;

	private SimpleUserInfo local_user_info;

	private ImageView mUserSignature;

	private ImageView mManagerSignature;

	private LinearLayout mAttachFrame;


	private UserInfo mUserInfo;
	private FamilyBase mFamilyDBInfo;
	private CheckTask mTaskInfo;
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
		setContentView(R.layout.dbfamilypreview);
		onNewIntent(getIntent());
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		local_user_info = (SimpleUserInfo) intent.getSerializableExtra(Const.KEY_USER_INFO);
		if(local_user_info != null) {
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
		mTextNd = (TextView)view.findViewById(R.id.preview_nd);
		mTextRq = (TextView)view.findViewById(R.id.preview_rq);
		mTextFzr = (TextView)view.findViewById(R.id.preview_fzr);
		mTextLx = (TextView)view.findViewById(R.id.preview_lx);
		mTextXzqhdm = (TextView)view.findViewById(R.id.preview_xzqhdm);
		mTextApplyTime = (TextView)view.findViewById(R.id.preview_apply_time);
		mTextJzlx = (TextView)view.findViewById(R.id.preview_jzlx);
		mTextZpyy = (TextView)view.findViewById(R.id.preview_zpyy);
		mUserSignature = (ImageView)view.findViewById(R.id.user_signature);
		mManagerSignature = (ImageView)view.findViewById(R.id.manager_signature);

		mAttachFrame = (LinearLayout)view.findViewById(R.id.attachments);
	}

	private void loadPreviewInfo() {
		mFamilyDBInfo = DBHelper.getInstance().getFamilyWithTaskID(local_user_info.getCheck_task_id());
		if(mFamilyDBInfo == null) {
			mFamilyDBInfo = new FamilyBase();
			mFamilyDBInfo.setCheck_task_id(local_user_info.getCheck_task_id());
		}
		mTaskInfo = DBHelper.getInstance().get_check_task(local_user_info.getCheck_task_id());
		if(mTaskInfo == null) {
			mTaskInfo = new CheckTask();
			mTaskInfo.setCheck_task_id(local_user_info.getCheck_task_id());
		}
		mUserInfo = DBHelper.getInstance().getOneUncheckedMember(local_user_info.getIdNumber(),
				local_user_info.getCheck_task_id(), true);
        DBHelper.getInstance().getFingerAndPhoto(mUserInfo);
		updateViews();
	}

	private void updateViews() {
		if(mFamilyDBInfo.getSqrsfzh() != null) {
			mTextIdCard.setText(mFamilyDBInfo.getSqrsfzh());
			if(mUserInfo.getAvatar() != null) {
				mAvatar.setVisibility(View.VISIBLE);
				mAvatar.setImageBitmap(mUserInfo.getAvatar());
			}
		}


		if(mUserInfo.getFinger() != null)
			mImageFingerprint.setImageBitmap(mUserInfo.getFinger());

		if(mUserInfo.getNian_du_she_xiang() != null) {
			mYearPhoto.setImageBitmap(mUserInfo.getNian_du_she_xiang());
		}
		mTextIdCard.setText(mFamilyDBInfo.getSqrsfzh());
		mTextNd.setText(mTaskInfo.getNd());
		mTextRq.setText(mTaskInfo.getDate());
		mTextFzr.setText(mTaskInfo.getFzr());
		mTextLx.setText(mTaskInfo.getLx());
		mTextXzqhdm.setText(mFamilyDBInfo.getXzqhdm());
		mTextApplyTime.setText(mFamilyDBInfo.getSqrq());
        if (mFamilyDBInfo.getJzywlx().equals("110")) {
            mTextJzlx.setText("最低生活保障");
        } else {
            mTextJzlx.setText("特困人员供养");
        }
        if (mFamilyDBInfo.getZyzpyy().equals("01")) {
            mTextJzlx.setText("疾病");
        } else if(mFamilyDBInfo.getZyzpyy().equals("02")) {
            mTextJzlx.setText("灾害");
        } else if(mFamilyDBInfo.getZyzpyy().equals("03")) {
            mTextJzlx.setText("残疾");
        } else if(mFamilyDBInfo.getZyzpyy().equals("04")) {
            mTextJzlx.setText("缺乏劳动力(老弱)");
        } else if(mFamilyDBInfo.getZyzpyy().equals("05")) {
            mTextJzlx.setText("失业");
        } else if(mFamilyDBInfo.getZyzpyy().equals("06")) {
            mTextJzlx.setText("失地");
        } else if(mFamilyDBInfo.getZyzpyy().equals("99")) {
            mTextJzlx.setText("其他");
        }

		if(mUserInfo.getManager_signture() != null && mUserInfo.getUser_signture() != null) {
			mUserSignature.setImageBitmap(mUserInfo.getManager_signture());
			mManagerSignature.setImageBitmap(mUserInfo.getManager_signture());
		}

	}


	private void updateAttchments(List<Attachment> attachments) {
		mAttachFrame.removeAllViews();
		if(attachments != null && attachments.size()>0) {
			LayoutParams lp;
			ImageView image;
			Attachment attach;
			for(int i=0; i<attachments.size(); i++) {
				attach = attachments.get(i);
				if(!TextUtils.isEmpty(attach.getPath())) {
					image = new ImageView(this);
					lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
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
