package com.zgmz.ls.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.zgmz.ls.R;
import com.zgmz.ls.base.Const;
import com.zgmz.ls.base.SubActivity;
import com.zgmz.ls.db.DBHelper;
import com.zgmz.ls.model.CheckTask;
import com.zgmz.ls.model.FamilyBase;
import com.zgmz.ls.model.SimpleUserInfo;
import com.zgmz.ls.utils.ToastUtils;

public class FamilyBaseSituationOldActivity extends SubActivity {

	private TextView HCND;
	private EditText HCND_EDIT;

	private TextView HCRQ;
	private EditText HCRQ_EDIT;

	private TextView HCFZR;
	private EditText HCFZR_EDIT;

	private TextView HCLX;
	private RadioButton HCLX_HC;
	private RadioButton HCLX_CC;
    private RadioButton HCLX_XZ;

    private TextView XZQHDM;
    private EditText XZQHDM_EDIT;

    private TextView SQRXM;
    private EditText SQRXM_EDIT;

    private TextView SQRSFZHM;
    private EditText SQRSFZHM_EDIT;

    private TextView SQRQ;
    private EditText SQRQ_EDIT;

    private TextView SQLX;
    private RadioButton SQLX_ZD;
    private RadioButton SQLX_TK;

    private TextView ZYZPYY;
    private RadioButton ZYZPYY_JB;
    private RadioButton ZYZPYY_ZH;
    private RadioButton ZYZPYY_CJ;
    private RadioButton ZYZPYY_LR;
    private RadioButton ZYZPYY_SY;
    private RadioButton ZYZPYY_SD;
    private RadioButton ZYZPYY_QT;

    private FamilyBase mFamilyDBInfo;
    private CheckTask mTaskInfo;
	
	@Override
	protected void onConfigrationTitleBar() {
		// TODO Auto-generated method stub
		super.onConfigrationTitleBar();
		setTitleBarTitleText(R.string.title_family_situation);
		setTitleBarRightButtonText(R.string.save);
	}
	
	@Override
	public void onTitleBarRightButtonOnClick(View v) {
		// TODO Auto-generated method stub
		saveFamilySituation();
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.family_base_situation);
		onNewIntent(getIntent());
	}
	SimpleUserInfo local_user_info;
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);

		local_user_info = (SimpleUserInfo) intent.getSerializableExtra(Const.KEY_USER_INFO);
		if(local_user_info != null) {
			loadFamilySituation();
		}
		else {
			ToastUtils.showLongToast("用户不存在！");
			finish();
		}
	}
	
	private void loadFamilySituation() {
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
		updateViews();
	}
	
	private void updateViews() {

        HCND_EDIT.setText(mTaskInfo.getNd());
        HCRQ_EDIT.setText(mTaskInfo.getDate());
        HCFZR_EDIT.setText(mTaskInfo.getFzr());
        if (mTaskInfo.getLx().equals("01")) {
            HCLX_HC.setChecked(true);
        } else if (mTaskInfo.getLx().equals("02")) {
            HCLX_CC.setChecked(true);
        } else if (mTaskInfo.getLx().equals("03")) {
            HCLX_XZ.setChecked(true);
        }
        XZQHDM_EDIT.setText(mFamilyDBInfo.getXzqhdm());
        SQRXM_EDIT.setText(mFamilyDBInfo.getSqrxm());
        SQRSFZHM_EDIT.setText(mFamilyDBInfo.getSqrsfzh());
        SQRQ_EDIT.setText(mFamilyDBInfo.getSqrq());
        if (mFamilyDBInfo.getJzywlx() != null) {
            if (mFamilyDBInfo.getJzywlx().equals("110")) {
                SQLX_ZD.setChecked(true);
            } else if (mFamilyDBInfo.getJzywlx().equals("130")) {
                SQLX_TK.setChecked(true);
            }
        }

        if (mFamilyDBInfo.getZyzpyy() != null) {
            if (mFamilyDBInfo.getZyzpyy().equals("01")) {
                ZYZPYY_JB.setChecked(true);
            } else if (mFamilyDBInfo.getZyzpyy().equals("02")) {
                ZYZPYY_ZH.setChecked(true);
            } else if (mFamilyDBInfo.getZyzpyy().equals("03")) {
                ZYZPYY_CJ.setChecked(true);
            } else if (mFamilyDBInfo.getZyzpyy().equals("04")) {
                ZYZPYY_LR.setChecked(true);
            } else if (mFamilyDBInfo.getZyzpyy().equals("05")) {
                ZYZPYY_SY.setChecked(true);
            } else if (mFamilyDBInfo.getZyzpyy().equals("06")) {
                ZYZPYY_SD.setChecked(true);
            } else if (mFamilyDBInfo.getZyzpyy().equals("99")) {
                ZYZPYY_QT.setChecked(true);
            }
        }

	}

	@Override
	protected void setupViews(View view) {
		// TODO Auto-generated method stub
		View item = view.findViewById(R.id.HCND);
        HCND = (TextView)item.findViewById(R.id.name);
        HCND_EDIT = (EditText)item.findViewById(R.id.input);

        item = view.findViewById(R.id.HCRQ);
        HCRQ = (TextView)item.findViewById(R.id.name);
        HCRQ_EDIT = (EditText)item.findViewById(R.id.input);

        item = view.findViewById(R.id.HCFZR);
        HCFZR = (TextView)item.findViewById(R.id.name);
        HCFZR_EDIT = (EditText)item.findViewById(R.id.input);

		item = view.findViewById(R.id.HCLX);
        HCLX = (TextView)item.findViewById(R.id.name);
        HCLX_HC = (RadioButton)item.findViewById(R.id.radio1);
        HCLX_CC = (RadioButton)item.findViewById(R.id.radio2);
        HCLX_XZ = (RadioButton)item.findViewById(R.id.radio3);

        item = view.findViewById(R.id.XZQBM);
        XZQHDM = (TextView)item.findViewById(R.id.name);
        XZQHDM_EDIT = (EditText)item.findViewById(R.id.input);

        item = view.findViewById(R.id.SQRXM);
        SQRXM = (TextView)item.findViewById(R.id.name);
        SQRXM_EDIT = (EditText)item.findViewById(R.id.input);

        item = view.findViewById(R.id.SFZHM);
        SQRSFZHM = (TextView)item.findViewById(R.id.name);
        SQRSFZHM_EDIT = (EditText)item.findViewById(R.id.input);

        item = view.findViewById(R.id.SQRQ);
        SQRQ = (TextView)item.findViewById(R.id.name);
        SQRQ_EDIT = (EditText)item.findViewById(R.id.input);

        item = view.findViewById(R.id.JZLX);
        SQLX = (TextView)item.findViewById(R.id.name);
        SQLX_ZD = (RadioButton)item.findViewById(R.id.radio1);
        SQLX_TK = (RadioButton)item.findViewById(R.id.radio2);

        item = view.findViewById(R.id.ZYZPYY);
        ZYZPYY = (TextView)item.findViewById(R.id.name);
        ZYZPYY_JB = (RadioButton)item.findViewById(R.id.radio1);
        ZYZPYY_ZH = (RadioButton)item.findViewById(R.id.radio2);
        ZYZPYY_CJ = (RadioButton)item.findViewById(R.id.radio3);
        ZYZPYY_LR = (RadioButton)item.findViewById(R.id.radio4);
        ZYZPYY_SY = (RadioButton)item.findViewById(R.id.radio5);
        ZYZPYY_SD = (RadioButton)item.findViewById(R.id.radio6);
        ZYZPYY_QT = (RadioButton)item.findViewById(R.id.radio7);
        initViews();
	}
	
	private void initViews() {
        HCND.setText("年度");
        HCRQ.setText("日期");
        HCFZR.setText("操作人");
        HCLX.setText("检查类型");
        XZQHDM.setText("行政区编码");
        SQRXM.setText("申请人姓名");
        SQRSFZHM.setText("申请人身份证号码");
        SQRQ.setText("申请日期");
        SQLX.setText("救助业务类型");
        ZYZPYY.setText("主要致贫原因");
	}
	
	
	private void saveFamilySituation() {

        mTaskInfo.setNd(HCND_EDIT.getText().toString().trim());
        mTaskInfo.setDate(HCRQ_EDIT.getText().toString().trim());
        mTaskInfo.setFzr(HCFZR_EDIT.getText().toString().trim());
        if (HCLX_HC.isChecked()) {
            mTaskInfo.setLx("01");
        } else if (HCLX_CC.isChecked()) {
            mTaskInfo.setLx("02");
        } else if (HCLX_XZ.isChecked()) {
            mTaskInfo.setLx("03");
        }

        mFamilyDBInfo.setXzqhdm(XZQHDM_EDIT.getText().toString().trim());
        mFamilyDBInfo.setSqrxm(SQRXM_EDIT.getText().toString().trim());
        mFamilyDBInfo.setSqrsfzh(SQRSFZHM_EDIT.getText().toString().trim());
        mFamilyDBInfo.setSqrq(SQRQ_EDIT.getText().toString().trim());
        mFamilyDBInfo.setXzqhdm(XZQHDM_EDIT.getText().toString().trim());

        if (SQLX_ZD.isChecked()) {
            mFamilyDBInfo.setJzywlx("110");
        } else if (SQLX_TK.isChecked()) {
            mFamilyDBInfo.setJzywlx("130");
        }

        if (ZYZPYY_JB.isChecked()) {
            mFamilyDBInfo.setZyzpyy("01");
        } else if (ZYZPYY_ZH.isChecked()) {
            mFamilyDBInfo.setZyzpyy("02");
        } else if (ZYZPYY_CJ.isChecked()) {
            mFamilyDBInfo.setZyzpyy("03");
        } else if (ZYZPYY_LR.isChecked()) {
            mFamilyDBInfo.setZyzpyy("04");
        } else if (ZYZPYY_SY.isChecked()) {
            mFamilyDBInfo.setZyzpyy("05");
        } else if (ZYZPYY_SD.isChecked()) {
            mFamilyDBInfo.setZyzpyy("06");
        } else if (ZYZPYY_QT.isChecked()) {
            mFamilyDBInfo.setZyzpyy("99");
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
	
	private boolean checkInputCompleted() {
        //TODO
        return true;
    }
}
