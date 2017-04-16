package com.zgmz.ls.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.zgmz.ls.R;
import com.zgmz.ls.base.Const;
import com.zgmz.ls.base.SubActivity;
import com.zgmz.ls.db.DBHelper;
import com.zgmz.ls.dialog.ListDialog;
import com.zgmz.ls.dialog.ListItem;
import com.zgmz.ls.model.CheckTask;
import com.zgmz.ls.model.FamilyBase;
import com.zgmz.ls.model.SimpleUserInfo;
import com.zgmz.ls.model.UserInfo;
import com.zgmz.ls.utils.Family;
import com.zgmz.ls.utils.PreferencesUtils;
import com.zgmz.ls.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FamilyBaseSituationActivity extends SubActivity  implements View.OnClickListener {

    private TextView mName_title;
    private TextView mName;

    private TextView suo_shu_qu_yu;
    private TextView mIDNumber;
    private TextView id_caozuoren;
    private TextView id_sqrxm;
    private TextView id_sqrsfz;
    private ImageView mAvatar;
    private ImageView mImageView;
    private FamilyBase mFamilyDBInfo;
    private CheckTask mTaskInfo;

    Button he_cha_nian_du_xuanze;
    Button ri_qi_xuanze;
    Button jian_cha_lei_xing_xuanze;
    Button shen_qing_ri_qi_xuanze;
    Button jiu_zhu_ye_wu_lei_xing_xuanze;
    Button zhi_pin_yuan_yin_xuanze;
	ImageButton bao_cun;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jia_ting_xin_xi_detail);
        setupViews();
		onNewIntent(getIntent());

        calendar=Calendar.getInstance();
        updateUserInfo();

        FamilyBase family = DBHelper.getInstance().getFamilyWithTaskID(LocalUserInfo.getCheck_task_id());
        if (family != null) {
            if (family.getStatus().equals("1")) {
                mImageView.setImageDrawable(getResources().getDrawable(R.drawable.yi_he_cha_tou_ming));
            } else {
                mImageView.setImageDrawable(getResources().getDrawable(R.drawable.wei_he_cha_flag));
            }

            if (family.getJzywlx() != null) {
                if (family.getJzywlx().equals("110")) {
                    jiu_zhu_ye_wu_lei_xing_xuanze.setText("最低生活保障");
                } else if (family.getJzywlx().equals("130")) {
                    jiu_zhu_ye_wu_lei_xing_xuanze.setText("特困人员供养");
                }
            }
            if (family.getZyzpyy() != null) {
                if (family.getZyzpyy().equals("01")) {
                    zhi_pin_yuan_yin_xuanze.setText("疾病");
                } else if (family.getZyzpyy().equals("02")) {
                    zhi_pin_yuan_yin_xuanze.setText("灾害");
                } else if (family.getZyzpyy().equals("03")) {
                    zhi_pin_yuan_yin_xuanze.setText("残疾");
                } else if (family.getZyzpyy().equals("04")) {
                    zhi_pin_yuan_yin_xuanze.setText("缺乏劳动力(老弱)");
                } else if (family.getZyzpyy().equals("05")) {
                    zhi_pin_yuan_yin_xuanze.setText("失业");
                } else if (family.getZyzpyy().equals("06")) {
                    zhi_pin_yuan_yin_xuanze.setText("失地");
                } else if (family.getZyzpyy().equals("99")) {
                    zhi_pin_yuan_yin_xuanze.setText("其他");
                }
            }

            if (family.getSqrq() != null) {
                shen_qing_ri_qi_xuanze.setText(family.getSqrq());
            }
        }

        CheckTask task = DBHelper.getInstance().get_check_task(LocalUserInfo.getCheck_task_id());
        if (task != null) {
            if (task.getLx() != null) {
                if (task.getLx().equals("01")) {
                    jian_cha_lei_xing_xuanze.setText("核查");
                } else if (task.getLx().equals("02")){
                    jian_cha_lei_xing_xuanze.setText("抽查");
                } else if (task.getLx().equals("03")){
                    jian_cha_lei_xing_xuanze.setText("新增");
                }
            }

            if (task.getDate() != null) {
                ri_qi_xuanze.setText(task.getDate());
            }

            if (task.getNd() != null) {
                he_cha_nian_du_xuanze.setText(task.getNd());
            }

            if (task.getFzr() != null) {
                id_caozuoren.setText(task.getFzr());
            }
        }
	}

    protected void onConfigrationTitleBar() {
        // TODO Auto-generated method stub
        super.onConfigrationTitleBar();
        setTitleBarTitleText("家庭信息");
        //hideTitleBar();
    }
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);

        LocalUserInfo = (SimpleUserInfo) intent.getSerializableExtra(Const.KEY_USER_INFO);
		if(LocalUserInfo != null) {
			loadFamilySituation();
		}
		else {
			ToastUtils.showLongToast("用户不存在！");
			finish();
		}
	}

    int mYear = 0, mMonth = 0, mDay = 0;
    int choice;
    int type = 0;
    @Override
    public void onClick(View v) {

        // TODO Auto-generated method stub
        switch(v.getId()) {
            case R.id.id_he_cha_nian_du:
                startHeChaNianDu();
                break;
            case R.id.id_ri_qi:
                startHeChaRiQi();
                break;
            case R.id.id_jian_cha_lei_xing:
                startJianChaLeiXing();
                break;
            case R.id.id_shen_qin_ri_qi:
                startShenQinRiQi();
                break;
            case R.id.id_jiu_zhu_ye_wu_lei_xing:
                startJiuZhuYeWuLeiXing();
                break;
            case R.id.id_zhi_pin_yuan_yin:
                startZhiPinYuanYin();
                break;
            case R.id.bao_xun_he_cha:
                bao_cun();
                break;
        }

    }

    void bao_cun() {
        saveFamilySituation();
        finish();
    }

    void startZhiPinYuanYin() {
        choice = 0;
        List<String> strList = new ArrayList<String>();
        strList.add("疾病");
        strList.add("灾害");
        strList.add("残疾");
        strList.add("缺乏劳动力(老弱)");
        strList.add("失业");
        strList.add("失地");
        strList.add("其他");
        showSingleChoiceDialog(strList);
        if(choice == 0) {
            mFamilyDBInfo.setZyzpyy("01");
        } else if(choice == 1) {
            mFamilyDBInfo.setZyzpyy("02");
        } else if(choice == 2) {
            mFamilyDBInfo.setZyzpyy("03");
        } else if(choice == 3) {
            mFamilyDBInfo.setZyzpyy("04");
        } else if(choice == 4) {
            mFamilyDBInfo.setZyzpyy("05");
        } else if(choice == 5) {
            mFamilyDBInfo.setZyzpyy("06");
        } else if(choice == 6) {
            mFamilyDBInfo.setZyzpyy("99");
        }
        type = 0;
    }

    void startJiuZhuYeWuLeiXing() {
        choice = 0;
        List<String> strList = new ArrayList<String>();
        strList.add("最低生活保障");
        strList.add("特困人员供养");
        showSingleChoiceDialog(strList);
        if(choice == 0) {
            mFamilyDBInfo.setJzywlx("110");
        } else if(choice == 1) {
            mFamilyDBInfo.setJzywlx("130");
        }
        type = 1;
    }

    void startShenQinRiQi() {
        StartTimePicker(3);

        type = 2;

    }
    void startJianChaLeiXing() {
        choice = 0;
        List<String> strList = new ArrayList<String>();
        strList.add("检查");
        strList.add("核查");
        strList.add("新增");
        showSingleChoiceDialog(strList);
        if(choice == 0) {
            mTaskInfo.setLx("01");
        } else if(choice == 1) {
            mTaskInfo.setLx("02");
        } else if(choice == 3) {
            mTaskInfo.setLx("03");
        }

        type = 3;
    }
    private Calendar calendar;

    private void StartTimePicker(final int type) {
        DatePickerDialog.OnDateSetListener mdateListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                view.clearFocus();
                mYear = view.getYear();
                mMonth = view.getMonth() + 1;
                mDay = dayOfMonth;
                if (type == 1) {
                    if(mMonth < 10) {
                        ri_qi_xuanze.setText(String.valueOf(mYear) + "-0" + String.valueOf(mMonth) + "-" + String.valueOf(mDay));
                    } else {
                        ri_qi_xuanze.setText(String.valueOf(mYear) + "-" + String.valueOf(mMonth) + "-" + String.valueOf(mDay));
                    }
                    change_ri_qi_xuanze = true;
                } else if (type == 2) {
                    he_cha_nian_du_xuanze.setText(String.valueOf(mYear));
                    change_he_cha_nian_du_xuanze = true;
                } else if (type == 3) {
                    if(mMonth < 10) {
                        shen_qing_ri_qi_xuanze.setText(String.valueOf(mYear) + "-0" + String.valueOf(mMonth) + "-" + String.valueOf(mDay));
                    } else {
                        shen_qing_ri_qi_xuanze.setText(String.valueOf(mYear) + "-" + String.valueOf(mMonth) + "-" + String.valueOf(mDay));
                    }
                    change_shen_qing_ri_qi_xuanze = true;
                }
            }
        };
        DatePickerDialog date_dialog = new DatePickerDialog(this, mdateListener, mYear, mMonth, mDay);
        DatePicker datePicker = date_dialog.getDatePicker();
        calendar.set(2015, 1,1);
        datePicker.setMinDate(calendar.getTime().getTime());
        calendar.set(2018, 1,1);
        datePicker.setMaxDate(calendar.getTime().getTime());
        date_dialog.show();
    }
    void startHeChaRiQi() {
        StartTimePicker(1);
        type = 4;
    }
    void startHeChaNianDu() {
        StartTimePicker(2);
        type = 5;
    }

    void startXinBie() {
        List<String> strList = new ArrayList<String>();
        strList.add("男");
        strList.add("女");
        showSingleChoiceDialog(strList);
        type = 6;
    }

    private void loadFamilySituation() {
        mFamilyDBInfo = DBHelper.getInstance().getFamilyWithTaskID(LocalUserInfo.getCheck_task_id());
		if(mFamilyDBInfo == null) {
            mFamilyDBInfo = new FamilyBase();
            mFamilyDBInfo.setCheck_task_id(LocalUserInfo.getCheck_task_id());
		}
        mTaskInfo = DBHelper.getInstance().get_check_task(LocalUserInfo.getCheck_task_id());
        if(mTaskInfo == null) {
            mTaskInfo = new CheckTask();
            mTaskInfo.setCheck_task_id(LocalUserInfo.getCheck_task_id());
        }
	}

	protected void setupViews() {
		// TODO Auto-generated method stub

        mAvatar = (ImageView) this.findViewById(R.id.avatar);
        mName = (TextView) this.findViewById(R.id.id_username);
        mName_title = (TextView) this.findViewById(R.id.id_name);
        mIDNumber = (TextView) this.findViewById(R.id.id_shengfenzheng);
        suo_shu_qu_yu = (TextView) this.findViewById(R.id.id_suoshuquyu);
        id_caozuoren = (TextView) this.findViewById(R.id.id_caozuoren);
        id_sqrxm = (TextView) this.findViewById(R.id.id_shenqinren_xing_ming);
        id_sqrsfz = (TextView) this.findViewById(R.id.id_shenqinren_shengfenzheng);

        he_cha_nian_du_xuanze = (Button)this.findViewById(R.id.id_he_cha_nian_du);
        ri_qi_xuanze = (Button)this.findViewById(R.id.id_ri_qi);
        jian_cha_lei_xing_xuanze = (Button)this.findViewById(R.id.id_jian_cha_lei_xing);
        shen_qing_ri_qi_xuanze = (Button)this.findViewById(R.id.id_shen_qin_ri_qi);
        jiu_zhu_ye_wu_lei_xing_xuanze = (Button)this.findViewById(R.id.id_jiu_zhu_ye_wu_lei_xing);
        zhi_pin_yuan_yin_xuanze = (Button)this.findViewById(R.id.id_zhi_pin_yuan_yin);
        bao_cun = (ImageButton)this.findViewById(R.id.bao_xun_he_cha);

        he_cha_nian_du_xuanze.setOnClickListener(this);
        ri_qi_xuanze.setOnClickListener(this);
        jian_cha_lei_xing_xuanze.setOnClickListener(this);
        shen_qing_ri_qi_xuanze.setOnClickListener(this);
        jiu_zhu_ye_wu_lei_xing_xuanze.setOnClickListener(this);
        zhi_pin_yuan_yin_xuanze.setOnClickListener(this);
        bao_cun.setOnClickListener(this);

        mImageView = (ImageView) this.findViewById(R.id.he_cha_zhuang_tai);
	}

    private UserInfo mUserInfo;
    private SimpleUserInfo LocalUserInfo;
    private void updateUserInfo() {
        mUserInfo = DBHelper.getInstance().getOneUncheckedMember(LocalUserInfo.getIdNumber(),
                LocalUserInfo.getCheck_task_id(), true);
        updateUserBaseInfo(mUserInfo);
        id_caozuoren.setText(PreferencesUtils.getInstance().getUsername());
    }
    private void updateUserBaseInfo(UserInfo info) {
        if(info == null) return;
        mName.setText(info.getName());
        mName_title.setText(info.getName());
        mIDNumber.setText(info.getIdNumber());
        id_sqrxm.setText(info.getName());
        id_sqrsfz.setText(info.getIdNumber());
        suo_shu_qu_yu.setText(mFamilyDBInfo.getXzqhdm());
        if(info.getAvatar() != null) {
            mAvatar.setImageBitmap(info.getAvatar());
        }
    }

    boolean change_he_cha_nian_du_xuanze = false;
    boolean change_ri_qi_xuanze = false;
    boolean change_shen_qing_ri_qi_xuanze = false;
	private void saveFamilySituation() {

        if (change_he_cha_nian_du_xuanze == true) {
            mTaskInfo.setNd(he_cha_nian_du_xuanze.getText().toString());
        }
        if (change_ri_qi_xuanze == true) {
            mTaskInfo.setDate(ri_qi_xuanze.getText().toString().trim());
        }
        mTaskInfo.setFzr(PreferencesUtils.getInstance().getUsername());

        mFamilyDBInfo.setXzqhdm(suo_shu_qu_yu.getText().toString());
        mFamilyDBInfo.setSqrxm(mName.getText().toString());
        mFamilyDBInfo.setSqrsfzh(mIDNumber.getText().toString());
        if (change_shen_qing_ri_qi_xuanze == true) {
            mFamilyDBInfo.setSqrq(shen_qing_ri_qi_xuanze.getText().toString());
        }

        if (mFamilyDBInfo.getJzywlx() == null){
            ToastUtils.showLongToast("未设置救助业务类型");
            return;
        }
        if (mFamilyDBInfo.getSqrq() == null){
            ToastUtils.showLongToast("未设置申请日期");
            return;
        }
        if (mFamilyDBInfo.getSqrxm() == null){
            ToastUtils.showLongToast("未设置申请人姓名");
            return;
        }
        if (mFamilyDBInfo.getSqrsfzh() == null){
            ToastUtils.showLongToast("未设置申请人身份证号码");
            return;
        }
        if (mTaskInfo.getLx() == null) {
            ToastUtils.showLongToast("未设置核查类型");
            return;
        }
        if (mTaskInfo.getNd() == null) {
            ToastUtils.showLongToast("未设置核查年度");
            return;
        }
        if (mTaskInfo.getFzr() == null) {
            ToastUtils.showLongToast("未设置核查负责人");
            return;
        }
        if (mTaskInfo.getDate() == null) {
            ToastUtils.showLongToast("未设置核查日期");
            return;
        }
		if(DBHelper.getInstance().insertOrUpdateFamilyBase(mFamilyDBInfo)) {
			finish();
			ToastUtils.showLongToast("家庭保存成功");
		}
		else {
			ToastUtils.showLongToast("家庭保存失败");
		}
        if(DBHelper.getInstance().insertOrUpdateCheckTask(mTaskInfo)) {
            finish();
            ToastUtils.showLongToast("任务保存成功");
        }
        else {
            ToastUtils.showLongToast("任务保存失败");
        }
	}
    List<ListItem> item_list = new ArrayList<ListItem>();
    private void showSingleChoiceDialog(List<String> list){
        item_list.clear();
        for (int i = 0; i < list.size(); i++) {
            ListItem list_item = new ListItem();
            list_item.name = list.get(i).toString();
            item_list.add(i, list_item);
        }

        ListDialog listDialog = new ListDialog(this) ;
        listDialog.setListItem(item_list);
        WindowManager.LayoutParams params = listDialog.getWindow()
                .getAttributes();
        params.width = 1000;
        params.height = 650;
        listDialog.getWindow().setAttributes(params);
        listDialog.setOnDialogItemClickListener(new ListDialog.OnDialogItemClickListener() {

            @Override
            public void onItemClick(int id, Object data) {
                if (id == -1) {
                    return;
                }
                choice = id;
                if (type == 0) {
                    zhi_pin_yuan_yin_xuanze.setText(item_list.get(choice).name);
                } else if(type == 1) {
                    jiu_zhu_ye_wu_lei_xing_xuanze.setText(item_list.get(choice).name);
                } else if(type == 3) {
                    jian_cha_lei_xing_xuanze.setText(item_list.get(choice).name);
                }
            }
        });
        listDialog.show();
    }
}
