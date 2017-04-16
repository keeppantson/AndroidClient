package com.zgmz.ls.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.zgmz.ls.R;
import com.zgmz.ls.base.Const;
import com.zgmz.ls.base.SharedDatas;
import com.zgmz.ls.base.SubActivity;
import com.zgmz.ls.db.DBHelper;
import com.zgmz.ls.db.TableTools;
import com.zgmz.ls.model.CheckTask;
import com.zgmz.ls.model.DownloadTask;
import com.zgmz.ls.model.IdCard;
import com.zgmz.ls.model.QuHuaMa;
import com.zgmz.ls.model.SimpleUserInfo;
import com.zgmz.ls.utils.CharUtils;
import com.zgmz.ls.utils.IDCardTools;
import com.zgmz.ls.utils.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.zgmz.ls.model.CheckTask.STATUS_DOWNLOADING;
import static com.zgmz.ls.model.CheckTask.STATUS_NEW_ADDED;
import static com.zgmz.ls.ui.LoginActivity.TEST_USERNAME;

public class AddDownLoadTaskActivity extends SubActivity implements OnClickListener{

	EditText mShengfenzheng;
	String shengfenzheng;
	EditText mShengqingDate;
	String appdate;
    EditText mHCDate;
    String hcdate;
    EditText mHCYear;
    String hcyear;
	private TextView HCLX;
	private RadioButton HCLX_HC;
	private RadioButton HCLX_CC;

	Button mBtnRecognizeInput;
	Button mBtnCommit;
	String download_task_type;

	public Spinner spinner_city;
	public Spinner spinner_town;
	public Spinner spinner_street;
	public Spinner spinner_building;

    private ArrayAdapter<QuHuaMa> adapterCity;
    private ArrayAdapter<QuHuaMa> adapterTown;
    private ArrayAdapter<QuHuaMa> adapterStreet;
    private ArrayAdapter<QuHuaMa> adapterBuilding;
    private List<QuHuaMa> listCity = new ArrayList<QuHuaMa>();
    private List<QuHuaMa> listTown = new ArrayList<QuHuaMa>();
    private List<QuHuaMa> listStreet = new ArrayList<QuHuaMa>();
    private List<QuHuaMa> listBuilding = new ArrayList<QuHuaMa>();

    QuHuaMa allQuHuaMa = new QuHuaMa();
    SpinnerSelectedListener listener = new SpinnerSelectedListener();
    QuHuaMa selectquhuama = null;
	@Override
	protected void onConfigrationTitleBar() {
		// TODO Auto-generated method stub
		super.onConfigrationTitleBar();
		setTitleBarTitleText(R.string.title_new_task);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_download_task);
	}
	Activity thisActivity;
	@Override
	protected void setupViews(View view) {
		// TODO Auto-generated method stub
		mShengfenzheng = (EditText)view.findViewById(R.id.shengfenzheng);
        mShengqingDate = (EditText)view.findViewById(R.id.appDate);
        mHCDate = (EditText)view.findViewById(R.id.hcDate);
        mHCYear = (EditText)view.findViewById(R.id.hcYear);

		mBtnRecognizeInput = (Button)view.findViewById(R.id.recognize_input);
		mBtnRecognizeInput.setOnClickListener(this);
		mBtnCommit = (Button)view.findViewById(R.id.add_download);
		mBtnCommit.setOnClickListener(this);

        thisActivity = this;
		View item = view.findViewById(R.id.HCLX);
		HCLX = (TextView)item.findViewById(R.id.name);
		HCLX_HC = (RadioButton)item.findViewById(R.id.radio1);
		HCLX_CC = (RadioButton)item.findViewById(R.id.radio2);

		HCLX.setText("检查类型");
		spinner_city = (Spinner) view.findViewById(R.id.spinner_city);
		spinner_town = (Spinner) view.findViewById(R.id.spinner_town);
		spinner_street = (Spinner) view.findViewById(R.id.spinner_street);
		spinner_building = (Spinner) view.findViewById(R.id.spinner_building);

        allQuHuaMa.setId("all");
        allQuHuaMa.setName("全部");
        allQuHuaMa.setDepth("0");
        listCity = DBHelper.getInstance().getQuHuaMaWithDepth("3");
        listCity.add(allQuHuaMa);
        adapterCity = new ArrayAdapter<QuHuaMa>(this,android.R.layout.simple_spinner_item, listCity);
        adapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_city.setAdapter(adapterCity);
        spinner_city.setSelection(listCity.size() - 1);

        spinner_city.setOnItemSelectedListener(listener);
        spinner_town.setOnItemSelectedListener(listener);
        spinner_street.setOnItemSelectedListener(listener);
        spinner_building.setOnItemSelectedListener(listener);
	}

    class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {


        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            // TODO Auto-generated method stub

            QuHuaMa quhuama = null;

            if (arg0.equals(spinner_city)) {
                quhuama = adapterCity.getItem(arg2);
            } else if (arg0.equals(spinner_town)) {
                quhuama = adapterTown.getItem(arg2);
            } else if (arg0.equals(spinner_street)) {
                quhuama = adapterStreet.getItem(arg2);
            } else if (arg0.equals(spinner_building)) {
                quhuama = adapterBuilding.getItem(arg2);
            } else {
                return;
            }
            ToastUtils.showShortToast("You chose: " + quhuama.getName());
            if (!quhuama.getDepth().equals("0")) {
                selectquhuama = quhuama;
            }
            if (quhuama.getDepth().equals("3")) {
                listTown = DBHelper.getInstance().getQuHuaMaWithFatherID(quhuama.getId());
                listTown.add(allQuHuaMa);
                adapterTown = new ArrayAdapter<QuHuaMa>(thisActivity,android.R.layout.simple_spinner_item, listTown);
                adapterTown.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_town.setAdapter(adapterTown);
                spinner_town.setSelection(listTown.size() - 1);
            } else if (quhuama.getDepth().equals("4")) {
                listStreet = DBHelper.getInstance().getQuHuaMaWithFatherID(quhuama.getId());
                listStreet.add(allQuHuaMa);
                adapterStreet = new ArrayAdapter<QuHuaMa>(thisActivity,android.R.layout.simple_spinner_item, listStreet);
                adapterStreet.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_street.setAdapter(adapterStreet);
                spinner_street.setSelection(listStreet.size() - 1);

            } else if (quhuama.getDepth().equals("5")) {
                listBuilding = DBHelper.getInstance().getQuHuaMaWithFatherID(quhuama.getId());
                listBuilding.add(allQuHuaMa);
                adapterBuilding = new ArrayAdapter<QuHuaMa>(thisActivity,android.R.layout.simple_spinner_item, listBuilding);
                adapterBuilding.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_building.setAdapter(adapterBuilding);
                spinner_building.setSelection(listBuilding.size() - 1);
            }
                /* 将mySpinner 显示*/
            arg0.setVisibility(View.VISIBLE);
        }
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
            ToastUtils.showShortToast("You chose: all");
            arg0.setVisibility(View.VISIBLE);
        }
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
			case R.id.add_download:
				commit();
				break;
			case R.id.recognize_input:
				startIdRecognizeActivity();
				break;
		}
	}

	private void commit() {
        download_task_type = null;
        if (HCLX_HC.isChecked()) {
            download_task_type = "01";
        } else if (HCLX_CC.isChecked()) {
            download_task_type = "02";
        }
        shengfenzheng = mShengfenzheng.getText().toString();
        appdate = mShengqingDate.getText().toString();
        if (appdate.equals("") || download_task_type == null) {
            shengfenzheng = null;
        }
        if (selectquhuama != null) {

            ToastUtils.showShortToast("Commit QuHuaMa You chose: " + selectquhuama.getName());
            DownloadTask quhuama_task = new DownloadTask();
            quhuama_task.setApply_type(download_task_type);
            quhuama_task.setQu_hua_ma(selectquhuama.getId());
            quhuama_task.setPage_id("0");
            quhuama_task.setPage_number("1");
            quhuama_task.setTotal_number("0");
            DBHelper.getInstance().insertOrUpdateDownloadTask(quhuama_task);
        } else if (shengfenzheng != null) {

            ToastUtils.showShortToast("Commit QuHuaMa You chose: " + shengfenzheng);
            DownloadTask noraml_task = new DownloadTask();
            noraml_task.setApply_type(download_task_type);
            noraml_task.setNow_work_target(shengfenzheng);
            noraml_task.setNow_work_target_apply_time(appdate);
            DBHelper.getInstance().insertOrUpdateDownloadTask(noraml_task);
        }
/*
        appdate = mShengqingDate.getText().toString().trim();
        shengfenzheng = mShengfenzheng.getText().toString().trim();
        hcdate = mHCDate.getText().toString().trim();
        hcyear = mHCYear.getText().toString().trim();
        CheckTask task = new CheckTask();
        Long time = System.currentTimeMillis();
        task.setLx(download_task_type);
        task.setFzr(TEST_USERNAME);
        task.setNd(hcyear);
        task.setDate(hcdate);
        task.setCheck_task_id(Long.toString(time));
        task.setStatus(STATUS_DOWNLOADING);
        task.setTarget(shengfenzheng);

        DBHelper.getInstance().insertOrUpdateCheckTask(task);
*/
        finish();
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
						mShengfenzheng.setText(idcard.getIdNumber());
					}
				}
			}
		}
	}

}
