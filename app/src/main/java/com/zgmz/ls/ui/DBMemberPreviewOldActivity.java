package com.zgmz.ls.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
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
import com.zgmz.ls.model.CheckTask;
import com.zgmz.ls.model.FamilyBase;
import com.zgmz.ls.model.SimpleUserInfo;
import com.zgmz.ls.model.UserInfo;
import com.zgmz.ls.utils.ToastUtils;

public class DBMemberPreviewOldActivity extends SubActivity {
	private ImageView mAvatar;
	private ImageView mYearPhoto;
	private TextView mTextIdCard;
	private TextView mTextCYXB;
	private TextView mTextCYNL;
	private TextView mTextCYLDNL;
	private TextView mTextCYJKQK;
	private TextView mTextCYCJLB;
	private TextView mTextCYCJDJ;
	private TextView mTextCYWHCD;
	private TextView mTextCYSFZX;
	private TextView mTextCYYSQRGX;
	private TextView mTextCYSFYTX;
	private TextView mTextCYSFHCZP;
	private TextView mTextCYSFHCSFZ;
	private TextView mTextCYSFHCHKB;

	private ImageView mImageFingerprint;

	private SimpleUserInfo local_user_info;


    private FamilyBase.member mFamilyMemberInfo;
    private UserInfo mUserInfo;
    private FamilyBase mFamilyDBInfo;
    private CheckTask mTaskInfo;
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
		setContentView(R.layout.dbmemberpreview);
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
        mTextCYXB = (TextView)view.findViewById(R.id.preview_CYXB);
        mTextCYNL = (TextView)view.findViewById(R.id.preview_CYNL);
        mTextCYLDNL = (TextView)view.findViewById(R.id.preview_CYLDNL);
        mTextCYJKQK = (TextView)view.findViewById(R.id.preview_CYJKQK);
        mTextCYCJLB = (TextView)view.findViewById(R.id.preview_CYCJLB);
        mTextCYCJDJ = (TextView)view.findViewById(R.id.preview_CYCJDJ);
        mTextCYWHCD = (TextView)view.findViewById(R.id.preview_CYWHCD);
        mTextCYSFZX = (TextView)view.findViewById(R.id.preview_CYSFZX);
        mTextCYYSQRGX = (TextView)view.findViewById(R.id.preview_CYYSQRGX);
        mTextCYSFYTX = (TextView)view.findViewById(R.id.preview_CYSFYTX);
        mTextCYSFHCZP = (TextView)view.findViewById(R.id.preview_CYSFHCZP);
        mTextCYSFHCSFZ = (TextView)view.findViewById(R.id.preview_CYSFHCSFZ);
        mTextCYSFHCHKB = (TextView)view.findViewById(R.id.preview_CYSFHCHKB);
        mAttachFrame = (LinearLayout)view.findViewById(R.id.attachments);
	}

	private void loadPreviewInfo() {
        mFamilyMemberInfo = DBHelper.getInstance().getOneUncheckedMember(local_user_info.getIdNumber(), local_user_info.getCheck_task_id());
        if(mFamilyMemberInfo == null) {
            FamilyBase base = new FamilyBase();
            mFamilyMemberInfo = base.new member();
            mFamilyMemberInfo.setCheck_task_id(local_user_info.getCheck_task_id());
        }
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

        if (mUserInfo.getIdNumber() != null) {
            mTextIdCard.setText(mUserInfo.getIdNumber());
            if (mUserInfo.getAvatar() != null) {
                mAvatar.setVisibility(View.VISIBLE);
                mAvatar.setImageBitmap(mUserInfo.getAvatar());
            }
        }


        if (mUserInfo.getFinger() != null)
            mImageFingerprint.setImageBitmap(mUserInfo.getFinger());

        if (mUserInfo.getNian_du_she_xiang() != null) {
            mYearPhoto.setImageBitmap(mUserInfo.getNian_du_she_xiang());
        }


        if (mFamilyMemberInfo.getXb() != null) {
            if (mFamilyMemberInfo.getXb().equals("1")) {
                mTextCYXB.setText("男");
            } else if (mFamilyMemberInfo.getXb().equals("2")) {
                mTextCYXB.setText("女");
            }
        }

        mTextCYNL.setText(mFamilyMemberInfo.getNl());

        if (mFamilyMemberInfo.getLdnl() != null) {
            if (mFamilyMemberInfo.getLdnl().equals("01")) {
                mTextCYLDNL.setText("有劳动能力");
            } else if (mFamilyMemberInfo.getLdnl().equals("02")) {
                mTextCYLDNL.setText("部分丧失劳动能力");
            } else if (mFamilyMemberInfo.getLdnl().equals("03")) {
                mTextCYLDNL.setText("完全丧失劳动能力");
            } else if (mFamilyMemberInfo.getLdnl().equals("04")) {
                mTextCYLDNL.setText("无劳动能力");
            }
        }

        if (mFamilyMemberInfo.getJkzk() != null) {
            if (mFamilyMemberInfo.getJkzk().equals("10")) {
                mTextCYJKQK.setText("健康或良好");
            } else if (mFamilyMemberInfo.getJkzk().equals("20")) {
                mTextCYJKQK.setText("一般或较弱");
            } else if (mFamilyMemberInfo.getJkzk().equals("40")) {
                mTextCYJKQK.setText("重病");
            }
        }

        if (mFamilyMemberInfo.getCjlb() != null) {
            if (mFamilyMemberInfo.getCjlb().equals("61")) {
                mTextCYCJLB.setText("视力残疾");
            } else if (mFamilyMemberInfo.getCjlb().equals("62")) {
                mTextCYCJLB.setText("听力残疾");
            } else if (mFamilyMemberInfo.getCjlb().equals("63")) {
                mTextCYCJLB.setText("言语残疾");
            } else if (mFamilyMemberInfo.getCjlb().equals("64")) {
                mTextCYCJLB.setText("肢体残疾");
            } else if (mFamilyMemberInfo.getCjlb().equals("65")) {
                mTextCYCJLB.setText("智力残疾");
            } else if (mFamilyMemberInfo.getCjlb().equals("66")) {
                mTextCYCJLB.setText("精神残疾");
            } else if (mFamilyMemberInfo.getCjlb().equals("67")) {
                mTextCYCJLB.setText("多重残疾");
            } else if (mFamilyMemberInfo.getCjlb().equals("69")) {
                mTextCYCJLB.setText("其他残疾");
            }
        }

        if (mFamilyMemberInfo.getCjdj() != null) {
            if (mFamilyMemberInfo.getCjdj().equals("01")) {
                mTextCYCJDJ.setText("一级残疾");
            } else if (mFamilyMemberInfo.getCjdj().equals("02")) {
                mTextCYCJDJ.setText("二级残疾");
            } else if (mFamilyMemberInfo.getCjdj().equals("03")) {
                mTextCYCJDJ.setText("三级残疾");
            } else if (mFamilyMemberInfo.getCjdj().equals("04")) {
                mTextCYCJDJ.setText("四级残疾");
            }
        }

        if (mFamilyMemberInfo.getWhcd() != null) {
            if (mFamilyMemberInfo.getWhcd().equals("10")) {
                mTextCYWHCD.setText("研究生");
            } else if (mFamilyMemberInfo.getWhcd().equals("20")) {
                mTextCYWHCD.setText("大学本科");
            } else if (mFamilyMemberInfo.getWhcd().equals("30")) {
                mTextCYWHCD.setText("专科");
            } else if (mFamilyMemberInfo.getWhcd().equals("40")) {
                mTextCYWHCD.setText("中等职业");
            } else if (mFamilyMemberInfo.getWhcd().equals("50")) {
                mTextCYWHCD.setText("普通高级中学");
            } else if (mFamilyMemberInfo.getWhcd().equals("60")) {
                mTextCYWHCD.setText("初级中学");
            } else if (mFamilyMemberInfo.getWhcd().equals("70")) {
                mTextCYWHCD.setText("小学");
            } else if (mFamilyMemberInfo.getWhcd().equals("90")) {
                mTextCYWHCD.setText("其他");
            }
        }

        if (mFamilyMemberInfo.getSfzx() != null) {
            if (mFamilyMemberInfo.getSfzx().equals("01")) {
                mTextCYSFZX.setText("是");
            } else if (mFamilyMemberInfo.getSfzx().equals("02")) {
                mTextCYSFZX.setText("否");
            }
        }

        if (mFamilyMemberInfo.getYsqrgx() != null) {
            if (mFamilyMemberInfo.getYsqrgx().equals("01")) {
                mTextCYYSQRGX.setText("本人");
            } else if (mFamilyMemberInfo.getYsqrgx().equals("10")) {
                mTextCYYSQRGX.setText("配偶");
            } else if (mFamilyMemberInfo.getYsqrgx().equals("20")) {
                mTextCYYSQRGX.setText("子");
            } else if (mFamilyMemberInfo.getYsqrgx().equals("30")) {
                mTextCYYSQRGX.setText("女");
            } else if (mFamilyMemberInfo.getYsqrgx().equals("40")) {
                mTextCYYSQRGX.setText("孙子、孙女或外孙子、外孙女");
            } else if (mFamilyMemberInfo.getYsqrgx().equals("50")) {
                mTextCYYSQRGX.setText("父母");
            } else if (mFamilyMemberInfo.getYsqrgx().equals("60")) {
                mTextCYYSQRGX.setText("祖父母或外祖父母");
            } else if (mFamilyMemberInfo.getYsqrgx().equals("70")) {
                mTextCYYSQRGX.setText("兄弟姐妹");
            } else if (mFamilyMemberInfo.getYsqrgx().equals("99")) {
                mTextCYYSQRGX.setText("其他");
            }
        }

        if (mFamilyMemberInfo.getSfzzp() != null) {
            if (mFamilyMemberInfo.getSfzzp().equals("1")) {
                mTextCYSFYTX.setText("是");
            } else if (mFamilyMemberInfo.getSfzzp().equals("0")) {
                mTextCYSFYTX.setText("否");
            }
        }

        if (mFamilyMemberInfo.getHcdxzp() != null) {
            if (mFamilyMemberInfo.getHcdxzp().equals("01")) {
                mTextCYSFHCZP.setText("是");
            } else if (mFamilyMemberInfo.getHcdxzp().equals("02")) {
                mTextCYSFHCZP.setText("否");
            }
        }

        if (mFamilyMemberInfo.getHcsfz() != null) {
            if (mFamilyMemberInfo.getHcsfz().equals("01")) {
                mTextCYSFHCSFZ.setText("是");
            } else if (mFamilyMemberInfo.getHcsfz().equals("02")) {
                mTextCYSFHCSFZ.setText("否");
            }
        }

        if (mFamilyMemberInfo.getHchkb() != null) {
            if (mFamilyMemberInfo.getHchkb().equals("01")) {
                mTextCYSFHCHKB.setText("是");
            } else if (mFamilyMemberInfo.getHchkb().equals("02")) {
                mTextCYSFHCHKB.setText("否");
            }
        }

        DBHelper.getInstance().getFingerAndPhoto(mUserInfo);
        updateAttchments();
	}


	private void updateAttchments() {
		mAttachFrame.removeAllViews();
		if(mUserInfo.getFinger() != null || mUserInfo.getNian_du_she_xiang() != null) {
			LayoutParams lp;
			ImageView image;
			while(true) {
                if(mUserInfo.getFinger() != null) {
                    Bitmap bmp = mUserInfo.getFinger();
                    image = new ImageView(this);
                    lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                    lp.bottomMargin = 40;
                    mAttachFrame.addView(image, lp);
                    image.setScaleType(ScaleType.CENTER_INSIDE);
                    if(bmp != null) {
                        image.setImageBitmap(bmp);
                    }
                    else {
                        image.setImageResource(R.drawable.photo_image);
                    }
                }

                if(mUserInfo.getFinger() != null) {
                    Bitmap bmp = mUserInfo.getNian_du_she_xiang();
                    image = new ImageView(this);
                    lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                    lp.bottomMargin = 40;
                    mAttachFrame.addView(image, lp);
                    image.setScaleType(ScaleType.CENTER_INSIDE);
                    if(bmp != null) {
                        image.setImageBitmap(bmp);
                    }
                    else {
                        image.setImageResource(R.drawable.photo_image);
                    }
                }
                break;
			}
		}
	}
}
