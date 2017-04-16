package com.zgmz.ls.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.zgmz.ls.R;
import com.zgmz.ls.base.BottomTabActivity;
import com.zgmz.ls.base.Const;
import com.zgmz.ls.base.SharedDatas;
import com.zgmz.ls.base.SubActivity;
import com.zgmz.ls.db.DBHelper;
import com.zgmz.ls.model.Attachment;
import com.zgmz.ls.model.CheckTask;
import com.zgmz.ls.model.FamilyBase;
import com.zgmz.ls.model.IdCard;
import com.zgmz.ls.model.SimpleUserInfo;
import com.zgmz.ls.model.UserInfo;
import com.zgmz.ls.ui.adapter.UserInfoAdapter;
import com.zgmz.ls.ui.fragment.TabCheckFragment;
import com.zgmz.ls.ui.fragment.TabRecordFragment;
import com.zgmz.ls.ui.fragment.TabSettingsFragment;
import com.zgmz.ls.utils.PreferencesUtils;
import com.zgmz.ls.utils.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.zgmz.ls.model.Attachment.TYPE_IMAGE_SHENGFENZHEN;
import static com.zgmz.ls.model.CheckTask.STATUS_NEW_ADDED;

public class Main_Lu_Ru_Activity extends SubActivity implements View.OnClickListener {

	Button mBtnManualInput;

	Button mBtnRecognizeInput;
    @Override
    protected void onConfigrationTitleBar() {
        // TODO Auto-generated method stub
        super.onConfigrationTitleBar();
        setTitleBarTitleText("录入");
    }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.lu_ru);


        // TODO Auto-generated method stub
        mBtnRecognizeInput = (Button)this.findViewById(R.id.recognize_input);
        mBtnManualInput = (Button)this.findViewById(R.id.manual_input);



        mBtnRecognizeInput.setOnClickListener(this);
        mBtnManualInput.setOnClickListener(this);
	}

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch(v.getId()) {
            case R.id.recognize_input:
                startIdRecognizeActivity();
                break;
            case R.id.manual_input:
                startIdManualInputActivity();
                break;
        }
    }
	@Override
	public void onBackPressed() {
		finish();
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
                    if(idcard != null)
                        createUserAndShowUserInfo(idcard);
                }
            }
        }
        else if(requestCode == REQUEST_CODE_MANUAL) {
            if(resultCode == Activity.RESULT_OK) {
                SimpleUserInfo info  = (SimpleUserInfo) data.getSerializableExtra(Const.KEY_USER_INFO);
                if(info != null) {
                    createUserAndShowUserInfo(info.toUserInfo());
                }
            }
        }


    }

    private static final int REQUEST_CODE_RECONGNIZE = 1001;

    private static final int REQUEST_CODE_MANUAL = 1002;

    private void startIdRecognizeActivity() {
        Intent intent = new Intent();
        intent.setClass(this,IDRecoginzeActivity.class);
        startActivityForResult(intent, REQUEST_CODE_RECONGNIZE);
    }


    private void startIdManualInputActivity() {
        Intent intent = new Intent();
        intent.setClass(this,ShouDongXiaZaiActivity.class);
        startActivityForResult(intent, REQUEST_CODE_MANUAL);
    }

    private void createUserAndShowUserInfo(UserInfo userInfo) {
        if(!DBHelper.getInstance().hasUser(userInfo.getIdNumber())) {
            DBHelper.getInstance().insertUser(userInfo);
        }
        UserInfo info = DBHelper.getInstance().getUserInfo(userInfo.getIdNumber());
        if(info != null) {
            CheckTask task = new CheckTask();
            Long time = System.currentTimeMillis();

            String checkTaskId = Long.toString(time);
            while (DBHelper.getInstance().hasCheckTask(checkTaskId)) {
                time += 1;
                checkTaskId = Long.toString(time);
            }
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            SimpleDateFormat formatter    =   new SimpleDateFormat("yyyy-MM-dd");
            Date curDate = new Date(time);//获取当前时间
            String str =  formatter.format(curDate);
            task.setLx("03");
            task.setFzr(PreferencesUtils.getInstance().getUsername());
            task.setNd(Integer.toString(year));
            task.setDate(str);
            task.setCheck_task_id(Long.toString(time));
            task.setStatus(STATUS_NEW_ADDED);

            DBHelper.getInstance().insertOrUpdateCheckTask(task);

            FamilyBase family = new FamilyBase();
            family.setCheck_task_id(Long.toString(time));
            family.setXzqhdm(PreferencesUtils.getInstance().getQHM());
            family.setSqrxm(userInfo.getName());
            family.setSqrsfzh(userInfo.getIdNumber());
            family.setSqrq(str);
            family.setIsChecked("0");
            DBHelper.getInstance().insertOrUpdateFamilyBase(family);

            FamilyBase.member member = family.new member();
            member.setCheck_task_id(Long.toString(time));
            member.setCyxm(userInfo.getName());
            member.setCysfzh(userInfo.getIdNumber());
            member.setFather_id(userInfo.getIdNumber());
            member.setRyzt("01");
            member.setSfz_status("02");
            DBHelper.getInstance().insertOrUpdateMember(member);

            info.setCheck_task_id(Long.toString(time));
            startUserInfoActivity(info);
        }
        else {
            ToastUtils.showLongToast("用户信息失效");
        }
    }

    private void createUserAndShowUserInfo(IdCard idCard) {
        if(!DBHelper.getInstance().hasUser(idCard.getIdNumber())) {
            DBHelper.getInstance().insertUser(idCard);
        }

        UserInfo info = DBHelper.getInstance().getUserInfo(idCard.getIdNumber());
        if(info != null) {
            idCard.setUserId(info.getUserId());
            if(DBHelper.getInstance().insertOrUpdateIdCard(idCard)) {
                info.setFlagId(true);
                SharedDatas.getInstance().recordUpdated();
            }
            CheckTask task = new CheckTask();
            Long time = System.currentTimeMillis();
            String checkTaskId = Long.toString(time);
            while (DBHelper.getInstance().hasCheckTask(checkTaskId)) {
                time += 1;
                checkTaskId = Long.toString(time);
            }
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            SimpleDateFormat    formatter    =   new SimpleDateFormat("yyyy-MM-dd");
            Date curDate = new Date(time);//获取当前时间
            String str =  formatter.format(curDate);
            task.setLx("03");
            task.setFzr(PreferencesUtils.getInstance().getUsername());
            task.setNd(Integer.toString(year));
            task.setDate(str);
            task.setCheck_task_id(Long.toString(time));
            task.setStatus(STATUS_NEW_ADDED);

            DBHelper.getInstance().insertOrUpdateCheckTask(task);

            FamilyBase family = new FamilyBase();
            family.setCheck_task_id(Long.toString(time));
            family.setXzqhdm(PreferencesUtils.getInstance().getQHM());
            family.setSqrxm(idCard.getName());
            family.setSqrsfzh(idCard.getIdNumber());
            family.setSqrq(str);
            family.setIsChecked("0");
            byte[] bmp = idCard.getWlt();
            Bitmap pic = BitmapFactory.decodeByteArray(bmp, 0, bmp.length);
            DBHelper.getInstance().insertOrUpdateFamilyBase(family, pic);

            FamilyBase.member member = family.new member();
            member.setCheck_task_id(Long.toString(time));
            member.setCyxm(idCard.getName());
            member.setCysfzh(idCard.getIdNumber());
            member.setFather_id(idCard.getIdNumber());
            member.setRyzt("01");
            member.setSfz_status("01");
            if (idCard.getSex().equals("男")) {
                member.setXb("1");
            } else {
                member.setXb("2");
            }
            bmp = idCard.getWlt();
            pic = BitmapFactory.decodeByteArray(bmp, 0, bmp.length);
            DBHelper.getInstance().insertOrUpdateMember(member, pic);

            Attachment attachment = new Attachment();
            attachment.setCheck_task_id(Long.toString(time));
            attachment.setCard_id(idCard.getIdNumber());
            bmp = idCard.getWlt();
            pic = BitmapFactory.decodeByteArray(bmp, 0, bmp.length);
            attachment.setContent(pic);
            attachment.setName(idCard.getIdNumber() + "-" + "102.jpg");
            attachment.setType(TYPE_IMAGE_SHENGFENZHEN);
            attachment.setPath(str + "/" + PreferencesUtils.getInstance().getQHM() + "/"+ idCard.getIdNumber() + "/102/"+ idCard.getIdNumber() + "-102.jpg");
            DBHelper.getInstance().insertAttachment(attachment);

            info.setCheck_task_id(Long.toString(time));
            startUserInfoActivity(info);
        }else {
            ToastUtils.showLongToast("用户已失效");
        }
    }

    private void startUserInfoActivity(UserInfo info) {
        Intent intent = new Intent();
        intent.putExtra(Const.KEY_USER_INFO, info.toSimpleUserInfo());
        intent.putExtra(Const.KEY_TYPE, Const.InfoType.INPUT);
        intent.setClass(this,CheckFamilyInfoActivity.class);
        startActivity(intent);
    }

}
