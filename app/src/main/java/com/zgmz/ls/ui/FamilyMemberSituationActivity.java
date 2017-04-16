package com.zgmz.ls.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.zgmz.ls.R;
import com.zgmz.ls.base.Const;
import com.zgmz.ls.base.SharedDatas;
import com.zgmz.ls.base.SubActivity;
import com.zgmz.ls.db.DBHelper;
import com.zgmz.ls.model.Attachment;
import com.zgmz.ls.model.FamilyBase;
import com.zgmz.ls.model.FamilyBase.member;
import com.zgmz.ls.model.IdCard;
import com.zgmz.ls.model.SimpleUserInfo;
import com.zgmz.ls.utils.BitmapUtils;
import com.zgmz.ls.utils.ToastUtils;

import static com.zgmz.ls.model.Attachment.TYPE_IMAGE_SHENGFENZHEN;
import static com.zgmz.ls.ui.LoginActivity.TEST_XZBM;

public class FamilyMemberSituationActivity extends SubActivity implements OnClickListener {

	private TextView CYXM;
	private EditText CYXM_EDIT;

	private TextView CYSFZ;
	private EditText CYSFZ_EDIT;

	private TextView CYXB;
	private RadioButton CYXB_MAN;
    private RadioButton CYXB_WOMEN;

    private TextView CYNL;
    private EditText CYNL_EDIT;

    private TextView CYLDNL;
    private RadioButton CYLDNL_1;
    private RadioButton CYLDNL_2;
    private RadioButton CYLDNL_3;
    private RadioButton CYLDNL_4;

    private TextView CYJKQK;
    private RadioButton CYJKQK_1;
    private RadioButton CYJKQK_2;
    private RadioButton CYJKQK_3;

    private TextView CYCJLB;
    private RadioButton CYCJLB_1;
    private RadioButton CYCJLB_2;
    private RadioButton CYCJLB_3;
    private RadioButton CYCJLB_4;
    private RadioButton CYCJLB_5;
    private RadioButton CYCJLB_6;
    private RadioButton CYCJLB_7;
    private RadioButton CYCJLB_8;

    private TextView CYCJDJ;
    private RadioButton CYCJDJ_1;
    private RadioButton CYCJDJ_2;
    private RadioButton CYCJDJ_3;
    private RadioButton CYCJDJ_4;

    private TextView CYWHCD;
    private RadioButton CYWHCD_1;
    private RadioButton CYWHCD_2;
    private RadioButton CYWHCD_3;
    private RadioButton CYWHCD_4;
    private RadioButton CYWHCD_5;
    private RadioButton CYWHCD_6;
    private RadioButton CYWHCD_7;
    private RadioButton CYWHCD_8;

    private TextView CYSFZX;
    private RadioButton CYSFZX_YES;
    private RadioButton CYSFZX_NO;

    private TextView CYYSQRGX;
    private RadioButton CYYSQRGX_1;
    private RadioButton CYYSQRGX_2;
    private RadioButton CYYSQRGX_3;
    private RadioButton CYYSQRGX_4;
    private RadioButton CYYSQRGX_5;
    private RadioButton CYYSQRGX_6;
    private RadioButton CYYSQRGX_7;
    private RadioButton CYYSQRGX_8;
    private RadioButton CYYSQRGX_9;

    private TextView CYSFYTX;
    private RadioButton CYSFYTX_YES;
    private RadioButton CYSFYTX_NO;

    private TextView CYSFHCZP;
    private RadioButton CYSFHCZP_YES;
    private RadioButton CYSFHCZP_NO;

    private TextView CYSFHCSFZ;
    private RadioButton CYSFHCSFZ_YES;
    private RadioButton CYSFHCSFZ_NO;

    private TextView CYSFHCHKB;
    private RadioButton CYSFHCHKB_YES;
    private RadioButton CYSFHCHKB_NO;

    private TextView CYSFZZT;
    private RadioButton CYSFZZT_YES;
    private RadioButton CYSFZZT_NO;

    private TextView CYRYZT;
    private RadioButton CYRYZT_1;
    private RadioButton CYRYZT_2;
    private RadioButton CYRYZT_3;
    private RadioButton CYRYZT_4;
    private RadioButton CYRYZT_5;
    private RadioButton CYRYZT_6;
    private RadioButton CYRYZT_7;

    private FamilyBase.member mFamilyMemberInfo;
	boolean newAdd = false;

    Button mBtnRecognizeInput;
    Attachment attachment;
    Bitmap pic;
    @Override
	protected void onConfigrationTitleBar() {
		// TODO Auto-generated method stub
		super.onConfigrationTitleBar();
		setTitleBarTitleText(R.string.title_family_member_situation);
		setTitleBarRightButtonText(R.string.save);
	}
	
	@Override
	public void onTitleBarRightButtonOnClick(View v) {
		// TODO Auto-generated method stub
		saveMemberSituation();
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.member_base_situation);
		onNewIntent(getIntent());
	}
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch(v.getId()) {
            case R.id.recognize_input:
                startIdRecognizeActivity();
                break;
        }
    }

    private static final int REQUEST_CODE_RECONGNIZE = 1001;
    private void startIdRecognizeActivity() {
        Intent intent = new Intent();
        intent.setClass(this,IDRecoginzeActivity.class);
        startActivityForResult(intent, REQUEST_CODE_RECONGNIZE);
    }
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
                        CYXM_EDIT.setText(idcard.getName());
                        CYSFZ_EDIT.setText(idcard.getIdNumber());


                        attachment = new Attachment();
                        attachment.setCheck_task_id(local_user_info.getCheck_task_id());
                        attachment.setCard_id(idcard.getIdNumber());
                        byte[]  bmp = idcard.getWlt();
                        pic = BitmapFactory.decodeByteArray(bmp, 0, bmp.length);
                        attachment.setContent(pic);
                        attachment.setName(idcard.getIdNumber() + "-" + "102.jpg");
                        attachment.setType(TYPE_IMAGE_SHENGFENZHEN);
                        attachment.setPath(local_user_info.getTime() + "/" + TEST_XZBM + "/"+ idcard.getIdNumber() + "/102/"+ idcard.getIdNumber() + "-102.jpg");

                    }
                }
            }
        }
    }
	SimpleUserInfo local_user_info;
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);

        local_user_info = (SimpleUserInfo) intent.getSerializableExtra(Const.KEY_USER_INFO);
		if (local_user_info.getIdNumber() == null) {
            // new added
            newAdd = true;
		}
        loadFamilySituation();
	}
	
	private void loadFamilySituation() {
        if (local_user_info.getIdNumber() != null) {
            mFamilyMemberInfo = DBHelper.getInstance().getOneUncheckedMember(local_user_info.getIdNumber(), local_user_info.getCheck_task_id());
        }
		if(mFamilyMemberInfo == null) {
            FamilyBase base = new FamilyBase();
            mFamilyMemberInfo = base.new member();
            mFamilyMemberInfo.setCheck_task_id(local_user_info.getCheck_task_id());
		}
		updateViews();
	}
	
	private void updateViews() {

        CYXM_EDIT.setText(mFamilyMemberInfo.getCyxm());
        CYSFZ_EDIT.setText(mFamilyMemberInfo.getCysfzh());

        if (mFamilyMemberInfo.getXb() != null) {
            if (mFamilyMemberInfo.getXb().equals("1")) {
                CYXB_MAN.setChecked(true);
            } else if (mFamilyMemberInfo.getXb().equals("2")) {
                CYXB_WOMEN.setChecked(true);
            }
        }

        CYNL_EDIT.setText(mFamilyMemberInfo.getNl());
        if (mFamilyMemberInfo.getLdnl() != null) {
            if (mFamilyMemberInfo.getLdnl().equals("01")) {
                CYLDNL_1.setChecked(true);
            } else if (mFamilyMemberInfo.getLdnl().equals("02")) {
                CYLDNL_2.setChecked(true);
            } else if (mFamilyMemberInfo.getLdnl().equals("03")) {
                CYLDNL_3.setChecked(true);
            } else if (mFamilyMemberInfo.getLdnl().equals("04")) {
                CYLDNL_4.setChecked(true);
            }
        }

        if (mFamilyMemberInfo.getJkzk() != null) {
            if (mFamilyMemberInfo.getJkzk().equals("10")) {
                CYJKQK_1.setChecked(true);
            } else if (mFamilyMemberInfo.getJkzk().equals("20")) {
                CYJKQK_2.setChecked(true);
            } else if (mFamilyMemberInfo.getJkzk().equals("40")) {
                CYJKQK_3.setChecked(true);
            }
        }

        if (mFamilyMemberInfo.getCjlb() != null) {
            if (mFamilyMemberInfo.getCjlb().equals("61")) {
                CYCJLB_1.setChecked(true);
            } else if (mFamilyMemberInfo.getCjlb().equals("62")) {
                CYCJLB_2.setChecked(true);
            } else if (mFamilyMemberInfo.getCjlb().equals("63")) {
                CYCJLB_3.setChecked(true);
            } else if (mFamilyMemberInfo.getCjlb().equals("64")) {
                CYCJLB_4.setChecked(true);
            } else if (mFamilyMemberInfo.getCjlb().equals("65")) {
                CYCJLB_5.setChecked(true);
            } else if (mFamilyMemberInfo.getCjlb().equals("66")) {
                CYCJLB_6.setChecked(true);
            } else if (mFamilyMemberInfo.getCjlb().equals("67")) {
                CYCJLB_7.setChecked(true);
            } else if (mFamilyMemberInfo.getCjlb().equals("69")) {
                CYCJLB_8.setChecked(true);
            }
        }

        if (mFamilyMemberInfo.getCjdj() != null) {
            if (mFamilyMemberInfo.getCjdj().equals("01")) {
                CYCJDJ_1.setChecked(true);
            } else if (mFamilyMemberInfo.getCjdj().equals("02")) {
                CYCJDJ_2.setChecked(true);
            } else if (mFamilyMemberInfo.getCjdj().equals("03")) {
                CYCJDJ_3.setChecked(true);
            } else if (mFamilyMemberInfo.getCjdj().equals("04")) {
                CYCJDJ_4.setChecked(true);
            }
        }

        if (mFamilyMemberInfo.getWhcd() != null) {
            if (mFamilyMemberInfo.getWhcd().equals("10")) {
                CYWHCD_1.setChecked(true);
            } else if (mFamilyMemberInfo.getWhcd().equals("20")) {
                CYWHCD_2.setChecked(true);
            } else if (mFamilyMemberInfo.getWhcd().equals("30")) {
                CYWHCD_3.setChecked(true);
            } else if (mFamilyMemberInfo.getWhcd().equals("40")) {
                CYWHCD_4.setChecked(true);
            } else if (mFamilyMemberInfo.getWhcd().equals("50")) {
                CYWHCD_5.setChecked(true);
            } else if (mFamilyMemberInfo.getWhcd().equals("60")) {
                CYWHCD_6.setChecked(true);
            } else if (mFamilyMemberInfo.getWhcd().equals("70")) {
                CYWHCD_7.setChecked(true);
            } else if (mFamilyMemberInfo.getWhcd().equals("90")) {
                CYWHCD_8.setChecked(true);
            }
        }

        if (mFamilyMemberInfo.getSfzx() != null) {
            if (mFamilyMemberInfo.getSfzx().equals("01")) {
                CYSFZX_YES.setChecked(true);
            } else if (mFamilyMemberInfo.getSfzx().equals("02")) {
                CYSFZX_NO.setChecked(true);
            }
        }

        if (mFamilyMemberInfo.getYsqrgx() != null) {
            if (mFamilyMemberInfo.getYsqrgx().equals("01")) {
                CYYSQRGX_1.setChecked(true);
            } else if (mFamilyMemberInfo.getYsqrgx().equals("10")) {
                CYYSQRGX_2.setChecked(true);
            } else if (mFamilyMemberInfo.getYsqrgx().equals("20")) {
                CYYSQRGX_3.setChecked(true);
            } else if (mFamilyMemberInfo.getYsqrgx().equals("30")) {
                CYYSQRGX_4.setChecked(true);
            } else if (mFamilyMemberInfo.getYsqrgx().equals("40")) {
                CYYSQRGX_5.setChecked(true);
            } else if (mFamilyMemberInfo.getYsqrgx().equals("50")) {
                CYYSQRGX_6.setChecked(true);
            } else if (mFamilyMemberInfo.getYsqrgx().equals("60")) {
                CYYSQRGX_7.setChecked(true);
            } else if (mFamilyMemberInfo.getYsqrgx().equals("70")) {
                CYYSQRGX_8.setChecked(true);
            } else if (mFamilyMemberInfo.getYsqrgx().equals("99")) {
                CYYSQRGX_9.setChecked(true);
            }
        }

        if (mFamilyMemberInfo.getSfzzp() != null) {
            if (mFamilyMemberInfo.getSfzzp().equals("1")) {
                CYSFYTX_YES.setChecked(true);
            } else if (mFamilyMemberInfo.getSfzzp().equals("0")) {
                CYSFYTX_NO.setChecked(true);
            }
        }

        if (mFamilyMemberInfo.getHcdxzp() != null) {
            if (mFamilyMemberInfo.getHcdxzp().equals("01")) {
                CYSFHCZP_YES.setChecked(true);
            } else if (mFamilyMemberInfo.getHcdxzp().equals("02")) {
                CYSFHCZP_NO.setChecked(true);
            }
        }

        if (mFamilyMemberInfo.getHcsfz() != null) {
            if (mFamilyMemberInfo.getHcsfz().equals("01")) {
                CYSFHCSFZ_YES.setChecked(true);
            } else if (mFamilyMemberInfo.getHcsfz().equals("02")) {
                CYSFHCSFZ_NO.setChecked(true);
            }
        }

        if (mFamilyMemberInfo.getHchkb() != null) {
            if (mFamilyMemberInfo.getHchkb().equals("01")) {
                CYSFHCHKB_YES.setChecked(true);
            } else if (mFamilyMemberInfo.getHchkb().equals("02")) {
                CYSFHCHKB_NO.setChecked(true);
            }
        }
        if (mFamilyMemberInfo.getSfz_status() != null) {
            if (mFamilyMemberInfo.getSfz_status().equals("01")) {
                CYSFZZT_YES.setChecked(true);
            } else if (mFamilyMemberInfo.getSfz_status().equals("02")) {
                CYSFZZT_NO.setChecked(true);
            }
        }
        if (mFamilyMemberInfo.getRyzt() != null) {
            if (mFamilyMemberInfo.getRyzt().equals("01")) {
                CYRYZT_1.setChecked(true);
            } else if (mFamilyMemberInfo.getRyzt().equals("02")) {
                CYRYZT_2.setChecked(true);
            } else if (mFamilyMemberInfo.getRyzt().equals("03")) {
                CYRYZT_3.setChecked(true);
            } else if (mFamilyMemberInfo.getRyzt().equals("04")) {
                CYRYZT_4.setChecked(true);
            } else if (mFamilyMemberInfo.getRyzt().equals("05")) {
                CYRYZT_5.setChecked(true);
            } else if (mFamilyMemberInfo.getRyzt().equals("06")) {
                CYRYZT_6.setChecked(true);
            } else if (mFamilyMemberInfo.getRyzt().equals("07")) {
                CYRYZT_7.setChecked(true);
            }
        }
	}

	@Override
	protected void setupViews(View view) {

        mBtnRecognizeInput = (Button)view.findViewById(R.id.recognize_input);

        mBtnRecognizeInput.setOnClickListener(this);
		// TODO Auto-generated method stub
		View item = view.findViewById(R.id.CYXM);
        CYXM = (TextView)item.findViewById(R.id.name);
        CYXM_EDIT = (EditText)item.findViewById(R.id.input);

        item = view.findViewById(R.id.CYSFZ);
        CYSFZ = (TextView)item.findViewById(R.id.name);
        CYSFZ_EDIT = (EditText)item.findViewById(R.id.input);

		item = view.findViewById(R.id.CYXB);
        CYXB = (TextView)item.findViewById(R.id.name);
        CYXB_MAN = (RadioButton)item.findViewById(R.id.radio1);
        CYXB_WOMEN = (RadioButton)item.findViewById(R.id.radio2);

        item = view.findViewById(R.id.CYNL);
        CYNL = (TextView)item.findViewById(R.id.name);
        CYNL_EDIT = (EditText)item.findViewById(R.id.input);

        item = view.findViewById(R.id.CYLDNL);
        CYLDNL = (TextView)item.findViewById(R.id.name);
        CYLDNL_1 = (RadioButton)item.findViewById(R.id.radio1);
        CYLDNL_2 = (RadioButton)item.findViewById(R.id.radio2);
        CYLDNL_3 = (RadioButton)item.findViewById(R.id.radio3);
        CYLDNL_4 = (RadioButton)item.findViewById(R.id.radio4);

        item = view.findViewById(R.id.CYJKQK);
        CYJKQK = (TextView)item.findViewById(R.id.name);
        CYJKQK_1 = (RadioButton)item.findViewById(R.id.radio1);
        CYJKQK_2 = (RadioButton)item.findViewById(R.id.radio2);
        CYJKQK_3 = (RadioButton)item.findViewById(R.id.radio3);

        item = view.findViewById(R.id.CYCJLB);
        CYCJLB = (TextView)item.findViewById(R.id.name);
        CYCJLB_1 = (RadioButton)item.findViewById(R.id.radio1);
        CYCJLB_2 = (RadioButton)item.findViewById(R.id.radio2);
        CYCJLB_3 = (RadioButton)item.findViewById(R.id.radio3);
        CYCJLB_4 = (RadioButton)item.findViewById(R.id.radio4);
        CYCJLB_5 = (RadioButton)item.findViewById(R.id.radio5);
        CYCJLB_6 = (RadioButton)item.findViewById(R.id.radio6);
        CYCJLB_7 = (RadioButton)item.findViewById(R.id.radio7);
        CYCJLB_8 = (RadioButton)item.findViewById(R.id.radio8);

        item = view.findViewById(R.id.CYCJDJ);
        CYCJDJ = (TextView)item.findViewById(R.id.name);
        CYCJDJ_1 = (RadioButton)item.findViewById(R.id.radio1);
        CYCJDJ_2 = (RadioButton)item.findViewById(R.id.radio2);
        CYCJDJ_3 = (RadioButton)item.findViewById(R.id.radio3);
        CYCJDJ_4 = (RadioButton)item.findViewById(R.id.radio4);

        item = view.findViewById(R.id.CYWHCD);
        CYWHCD = (TextView)item.findViewById(R.id.name);
        CYWHCD_1 = (RadioButton)item.findViewById(R.id.radio1);
        CYWHCD_2 = (RadioButton)item.findViewById(R.id.radio2);
        CYWHCD_3 = (RadioButton)item.findViewById(R.id.radio3);
        CYWHCD_4 = (RadioButton)item.findViewById(R.id.radio4);
        CYWHCD_5 = (RadioButton)item.findViewById(R.id.radio5);
        CYWHCD_6 = (RadioButton)item.findViewById(R.id.radio6);
        CYWHCD_7 = (RadioButton)item.findViewById(R.id.radio7);
        CYWHCD_8 = (RadioButton)item.findViewById(R.id.radio8);

        item = view.findViewById(R.id.CYSFZX);
        CYSFZX = (TextView)item.findViewById(R.id.name);
        CYSFZX_YES = (RadioButton)item.findViewById(R.id.radio1);
        CYSFZX_NO = (RadioButton)item.findViewById(R.id.radio2);

        item = view.findViewById(R.id.CYYSQRGX);
        CYYSQRGX = (TextView)item.findViewById(R.id.name);
        CYYSQRGX_1 = (RadioButton)item.findViewById(R.id.radio1);
        CYYSQRGX_2 = (RadioButton)item.findViewById(R.id.radio2);
        CYYSQRGX_3 = (RadioButton)item.findViewById(R.id.radio3);
        CYYSQRGX_4 = (RadioButton)item.findViewById(R.id.radio4);
        CYYSQRGX_5 = (RadioButton)item.findViewById(R.id.radio5);
        CYYSQRGX_6 = (RadioButton)item.findViewById(R.id.radio6);
        CYYSQRGX_7 = (RadioButton)item.findViewById(R.id.radio7);
        CYYSQRGX_8 = (RadioButton)item.findViewById(R.id.radio8);
        CYYSQRGX_9 = (RadioButton)item.findViewById(R.id.radio9);

        item = view.findViewById(R.id.CYSFYTX);
        CYSFYTX = (TextView)item.findViewById(R.id.name);
        CYSFYTX_YES = (RadioButton)item.findViewById(R.id.radio1);
        CYSFYTX_NO = (RadioButton)item.findViewById(R.id.radio2);

        item = view.findViewById(R.id.CYSFHCZP);
        CYSFHCZP = (TextView)item.findViewById(R.id.name);
        CYSFHCZP_YES = (RadioButton)item.findViewById(R.id.radio1);
        CYSFHCZP_NO = (RadioButton)item.findViewById(R.id.radio2);

        item = view.findViewById(R.id.CYSFHCSFZ);
        CYSFHCSFZ = (TextView)item.findViewById(R.id.name);
        CYSFHCSFZ_YES = (RadioButton)item.findViewById(R.id.radio1);
        CYSFHCSFZ_NO = (RadioButton)item.findViewById(R.id.radio2);

        item = view.findViewById(R.id.CYSFHCHKB);
        CYSFHCHKB = (TextView)item.findViewById(R.id.name);
        CYSFHCHKB_YES = (RadioButton)item.findViewById(R.id.radio1);
        CYSFHCHKB_NO = (RadioButton)item.findViewById(R.id.radio2);

        item = view.findViewById(R.id.CYSFZZT);
        CYSFZZT = (TextView)item.findViewById(R.id.name);
        CYSFZZT_YES = (RadioButton)item.findViewById(R.id.radio1);
        CYSFZZT_NO = (RadioButton)item.findViewById(R.id.radio2);


        item = view.findViewById(R.id.CYRYZT);
        CYRYZT = (TextView)item.findViewById(R.id.name);
        CYRYZT_1 = (RadioButton)item.findViewById(R.id.radio1);
        CYRYZT_2 = (RadioButton)item.findViewById(R.id.radio2);
        CYRYZT_3 = (RadioButton)item.findViewById(R.id.radio3);
        CYRYZT_4 = (RadioButton)item.findViewById(R.id.radio4);
        CYRYZT_5 = (RadioButton)item.findViewById(R.id.radio5);
        CYRYZT_6 = (RadioButton)item.findViewById(R.id.radio6);
        CYRYZT_7 = (RadioButton)item.findViewById(R.id.radio7);
        initViews();
	}
	
	private void initViews() {
        CYXM.setText("成员姓名");
        CYSFZ.setText("成员身份证号");
        CYXB.setText("性别");
        CYNL.setText("年龄");
        CYLDNL.setText("劳动能力");
        CYJKQK.setText("健康状况");
        CYCJLB.setText("残疾类别");
        CYCJDJ.setText("残疾等级");
        CYWHCD.setText("文化程度");
        CYSFZX.setText("是否在校");
        CYYSQRGX.setText("与申请人关系");
        CYSFYTX.setText("是否有该人员身份证头像照片");
        CYSFHCZP.setText("是否已核查对象照片");
        CYSFHCSFZ.setText("是否已核查身份证");
        CYSFHCHKB.setText("是否已核查户口本");
        CYSFZZT.setText("身份证核查方式");
        CYRYZT.setText("人员状态");
	}
	
	
	private void saveMemberSituation() {

        mFamilyMemberInfo.setCyxm(CYXM_EDIT.getText().toString().trim());
        mFamilyMemberInfo.setCysfzh(CYSFZ_EDIT.getText().toString().trim());

        if (CYXB_MAN.isChecked()) {
            mFamilyMemberInfo.setXb("1");
        } else if (CYXB_WOMEN.isChecked()) {
            mFamilyMemberInfo.setXb("2");
        }

        mFamilyMemberInfo.setNl(CYNL_EDIT.getText().toString().trim());

        if (CYLDNL_1.isChecked()) {
            mFamilyMemberInfo.setLdnl("01");
        } else if (CYLDNL_2.isChecked()) {
            mFamilyMemberInfo.setLdnl("02");
        } else if (CYLDNL_3.isChecked()) {
            mFamilyMemberInfo.setLdnl("03");
        } else if (CYLDNL_4.isChecked()) {
            mFamilyMemberInfo.setLdnl("04");
        }

        if (CYJKQK_1.isChecked()) {
            mFamilyMemberInfo.setJkzk("10");
        } else if (CYJKQK_2.isChecked()) {
            mFamilyMemberInfo.setJkzk("20");
        } else if (CYJKQK_3.isChecked()) {
            mFamilyMemberInfo.setJkzk("40");
        }

        if (CYCJLB_1.isChecked()) {
            mFamilyMemberInfo.setCjlb("61");
        } else if (CYCJLB_2.isChecked()) {
            mFamilyMemberInfo.setCjlb("62");
        } else if (CYCJLB_3.isChecked()) {
            mFamilyMemberInfo.setCjlb("63");
        } else if (CYCJLB_4.isChecked()) {
            mFamilyMemberInfo.setCjlb("64");
        } else if (CYCJLB_5.isChecked()) {
            mFamilyMemberInfo.setCjlb("65");
        } else if (CYCJLB_6.isChecked()) {
            mFamilyMemberInfo.setCjlb("66");
        } else if (CYCJLB_7.isChecked()) {
            mFamilyMemberInfo.setCjlb("67");
        } else if (CYCJLB_8.isChecked()) {
            mFamilyMemberInfo.setCjlb("69");
        }

        if (CYCJDJ_1.isChecked()) {
            mFamilyMemberInfo.setCjdj("01");
        } else if (CYCJDJ_2.isChecked()) {
            mFamilyMemberInfo.setCjdj("02");
        } else if (CYCJDJ_3.isChecked()) {
            mFamilyMemberInfo.setCjdj("03");
        } else if (CYCJDJ_4.isChecked()) {
            mFamilyMemberInfo.setCjdj("04");
        }

        if (CYWHCD_1.isChecked()) {
            mFamilyMemberInfo.setWhcd("10");
        } else if (CYWHCD_2.isChecked()) {
            mFamilyMemberInfo.setWhcd("20");
        } else if (CYWHCD_3.isChecked()) {
            mFamilyMemberInfo.setWhcd("30");
        } else if (CYWHCD_4.isChecked()) {
            mFamilyMemberInfo.setWhcd("40");
        } else if (CYWHCD_5.isChecked()) {
            mFamilyMemberInfo.setWhcd("50");
        } else if (CYWHCD_6.isChecked()) {
            mFamilyMemberInfo.setWhcd("60");
        } else if (CYWHCD_7.isChecked()) {
            mFamilyMemberInfo.setWhcd("70");
        } else if (CYWHCD_8.isChecked()) {
            mFamilyMemberInfo.setWhcd("90");
        }

        if (CYSFZX_YES.isChecked()) {
            mFamilyMemberInfo.setSfzx("01");
        } else if (CYSFZX_NO.isChecked()) {
            mFamilyMemberInfo.setSfzx("02");
        }

        if (CYYSQRGX_1.isChecked()) {
            mFamilyMemberInfo.setYsqrgx("01");
        } else if (CYYSQRGX_2.isChecked()) {
            mFamilyMemberInfo.setYsqrgx("10");
        } else if (CYYSQRGX_3.isChecked()) {
            mFamilyMemberInfo.setYsqrgx("20");
        } else if (CYYSQRGX_4.isChecked()) {
            mFamilyMemberInfo.setYsqrgx("30");
        } else if (CYYSQRGX_5.isChecked()) {
            mFamilyMemberInfo.setYsqrgx("40");
        } else if (CYYSQRGX_6.isChecked()) {
            mFamilyMemberInfo.setYsqrgx("50");
        } else if (CYYSQRGX_7.isChecked()) {
            mFamilyMemberInfo.setYsqrgx("60");
        } else if (CYYSQRGX_8.isChecked()) {
            mFamilyMemberInfo.setYsqrgx("70");
        } else if (CYYSQRGX_9.isChecked()) {
            mFamilyMemberInfo.setYsqrgx("99");
        }
        if (CYSFYTX_YES.isChecked()) {
            mFamilyMemberInfo.setSfzzp("1");
        } else if (CYSFYTX_NO.isChecked()) {
            mFamilyMemberInfo.setSfzzp("0");
        }

        if (CYSFHCZP_YES.isChecked()) {
            mFamilyMemberInfo.setHcdxzp("01");
        } else if (CYSFHCZP_NO.isChecked()) {
            mFamilyMemberInfo.setHcdxzp("02");
        }

        if (CYSFHCSFZ_YES.isChecked()) {
            mFamilyMemberInfo.setHcsfz("01");
        } else if (CYSFHCSFZ_NO.isChecked()) {
            mFamilyMemberInfo.setHcsfz("02");
        }

        if (CYSFHCHKB_YES.isChecked()) {
            mFamilyMemberInfo.setHchkb("01");
        } else if (CYSFHCHKB_NO.isChecked()) {
            mFamilyMemberInfo.setHchkb("02");
        }


        if (CYSFZZT_YES.isChecked()) {
            mFamilyMemberInfo.setSfz_status("01");
        } else if (CYSFZZT_NO.isChecked()) {
            mFamilyMemberInfo.setSfz_status("02");
        }


        if (CYRYZT_1.isChecked()) {
            mFamilyMemberInfo.setRyzt("01");
        } else if (CYRYZT_2.isChecked()) {
            mFamilyMemberInfo.setRyzt("02");
        } else if (CYRYZT_3.isChecked()) {
            mFamilyMemberInfo.setRyzt("03");
        } else if (CYRYZT_4.isChecked()) {
            mFamilyMemberInfo.setRyzt("04");
        } else if (CYRYZT_5.isChecked()) {
            mFamilyMemberInfo.setRyzt("05");
        } else if (CYRYZT_6.isChecked()) {
            mFamilyMemberInfo.setRyzt("06");
        } else if (CYRYZT_7.isChecked()) {
            mFamilyMemberInfo.setRyzt("07");
        }

        mFamilyMemberInfo.setFather_id(local_user_info.getFather_card_id());
        boolean ret = false;
        if (attachment != null) {
            DBHelper.getInstance().insertOrUpdateAttachment(attachment);
            ret = DBHelper.getInstance().insertOrUpdateMember(mFamilyMemberInfo,
                    pic);
        } else {
            ret = DBHelper.getInstance().insertOrUpdateMember(mFamilyMemberInfo);
        }
		if(ret == true) {
			finish();
			ToastUtils.showLongToast("保存成功");
		}
		else {
			ToastUtils.showLongToast("保存失败");
		}
	}
	
	private boolean checkInputCompleted() {
        //TODO
        return true;
    }
}
