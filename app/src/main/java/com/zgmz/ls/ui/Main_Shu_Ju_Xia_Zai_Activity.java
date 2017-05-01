package com.zgmz.ls.ui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.zgmz.ls.R;
import com.zgmz.ls.base.BottomTabActivity;
import com.zgmz.ls.base.Const;
import com.zgmz.ls.base.SharedDatas;
import com.zgmz.ls.base.SubActivity;
import com.zgmz.ls.db.DBHelper;
import com.zgmz.ls.model.DownloadTask;
import com.zgmz.ls.model.IdCard;
import com.zgmz.ls.model.SimpleUserInfo;
import com.zgmz.ls.ui.adapter.DownLoadTaskAdapter;
import com.zgmz.ls.ui.fragment.TabCheckFragment;
import com.zgmz.ls.ui.fragment.TabShuJuXiaZaiFragment;
import com.zgmz.ls.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Main_Shu_Ju_Xia_Zai_Activity extends SubActivity implements OnClickListener, CompoundButton.OnCheckedChangeListener {
	
	private static final int CANCAL_QUIT_TIME = 2000;
	
	private static final int MSG_CANCAL_QUIT = 0x0001;
	
	boolean bCanQiut = false;

    Button ri_qi_xuanze;
	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch(msg.what) {
				case MSG_CANCAL_QUIT:
					bCanQiut = false;
					break;
			}
			super.handleMessage(msg);
		}
		
	};
    protected void onConfigrationTitleBar() {
        // TODO Auto-generated method stub
        super.onConfigrationTitleBar();
        setTitleBarTitleText("数据下载");
        //hideTitleBar();
    }
    String download_task_type;
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == zui_di) {
            if (isChecked) {
                download_task_type = "01";
                te_kun.setChecked(false);
            }
        } else if (buttonView == te_kun) {
            if (isChecked) {
                download_task_type = "02";
                zui_di.setChecked(false);
            }
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch(v.getId()) {

            case R.id.xiazai_bottom:
                startXiaZai();
                break;
            case R.id.an_di_qu_pi_liang_xia_zai:
                startAnDiQuXiaZaiActivity();
                break;
            case R.id.sao_miao_shen_fen_zhen:
                startSaoMiaoShenFenZhengActivity();
                break;
            case R.id.shou_dong_shu_ru_shen_fen_zheng:
                startShouDongXiaZaiActivity();
                break;
            case R.id.jiu_zhu_nian_yue:
                startHeChaRiQi();
                break;
        }
    }

    void startHeChaRiQi() {
        StartTimePicker();
    }
    View mFrameUncheck;
	ImageButton mBtnAnDiQuXiaZai;
	ImageButton mBtnShenFenZhengShuRu;
    ImageButton mBtnShouDongShuRu;
    EditText jiuzhu_nian_yue;

    CheckBox zui_di;
    CheckBox te_kun;

    ImageButton mBtnAddDownloadTask;

	DownLoadTaskAdapter mUncheckedAdapter;

    String shengfenzheng;
    ListView mDownLoadTaskListView;
    List<DownloadTask> mDownLoadTasks = new ArrayList<DownloadTask>();

    AdapterView.OnItemClickListener mUncheckedItemListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // TODO Auto-generated method stub
            if(mDownLoadTasks.size() > position) {
                ToastUtils.showLongToast("还未下载完，一旦下载结束，可以选择“数据核查”项目进入查看");
            }
        }
    };
    private Calendar calendar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.xia_zai_1);


        mFrameUncheck = this.findViewById(R.id.frame_uncheck);

        mDownLoadTaskListView = (ListView) this.findViewById(R.id.uncheck_list);

        mBtnAddDownloadTask = (ImageButton)this.findViewById(R.id.xiazai_bottom);
        mBtnAnDiQuXiaZai = (ImageButton)this.findViewById(R.id.an_di_qu_pi_liang_xia_zai);
        mBtnShenFenZhengShuRu = (ImageButton)this.findViewById(R.id.sao_miao_shen_fen_zhen);
        mBtnShouDongShuRu = (ImageButton)this.findViewById(R.id.shou_dong_shu_ru_shen_fen_zheng);
        ri_qi_xuanze = (Button) this.findViewById(R.id.jiu_zhu_nian_yue);
        zui_di = (CheckBox)this.findViewById(R.id.zui_di);
        te_kun = (CheckBox)this.findViewById(R.id.te_kun);

        mUncheckedAdapter = new DownLoadTaskAdapter(this, mDownLoadTasks);
        mDownLoadTaskListView.setAdapter(mUncheckedAdapter);

        mDownLoadTaskListView.setOnItemClickListener(mUncheckedItemListener);
        ri_qi_xuanze.setOnClickListener(this);
        mBtnAddDownloadTask.setOnClickListener(this);
        mBtnAnDiQuXiaZai.setOnClickListener(this);
        mBtnShenFenZhengShuRu.setOnClickListener(this);
        mBtnShouDongShuRu.setOnClickListener(this);

        zui_di.setOnCheckedChangeListener(this);
        te_kun.setOnCheckedChangeListener(this);

        updateUncheckedData();

        calendar= Calendar.getInstance();
	}
	@Override
	public void onBackPressed() {
        finish();
	}

    private void updateUncheckedData() {
        List<DownloadTask> list = DBHelper.getInstance().getDownloadTask(20);
        List<DownloadTask> validlist = new ArrayList<DownloadTask>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getQu_hua_ma() == null) {
                validlist.add(list.get(i));
            }
        }
        if(!validlist.isEmpty()) {
            mDownLoadTasks.clear();
            mDownLoadTasks.addAll(validlist);
            mUncheckedAdapter.notifyDataSetChanged();
        }
    }

    private static final int REQUEST_CODE_RECONGNIZE = 1001;
    private static final int REQUEST_CODE_SHOU_DONG= 1002;
    private static final int PI_LIANG_XIA_ZAI = 1003;
    private void startXiaZai() {
        DownloadTask noraml_task = new DownloadTask();
        noraml_task.setApply_type(download_task_type);
        noraml_task.setNow_work_target(shengfenzheng);

        noraml_task.setNow_work_target_apply_time(ri_qi_xuanze.getText().toString());
        DBHelper.getInstance().insertOrUpdateDownloadTask(noraml_task);


        updateUncheckedData();
    }
    int mYear = 0, mMonth = 0, mDay = 0;
    private void StartTimePicker() {
        DatePickerDialog.OnDateSetListener mdateListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                view.clearFocus();
                mYear = view.getYear();
                mMonth = view.getMonth() + 1;
                mDay = dayOfMonth;
                String str;
                if (mMonth < 10) {
                    str = String.valueOf(mYear) + "0" + String.valueOf(mMonth);
                } else {
                    str = String.valueOf(mYear)+ String.valueOf(mMonth);
                }
                ri_qi_xuanze.setText(str);
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
    private void startAnDiQuXiaZaiActivity() {

        Intent intent = new Intent();
        intent.setClass(this,Main_Shu_Ju_Xia_Zai_Pi_Liang_Activity.class);
        startActivityForResult(intent, PI_LIANG_XIA_ZAI);
    }
    private void startSaoMiaoShenFenZhengActivity() {

        Intent intent = new Intent();
        intent.setClass(this,IDRecoginzeActivity.class);
        startActivityForResult(intent, REQUEST_CODE_RECONGNIZE);
    }
    private void startShouDongXiaZaiActivity(){

        Intent intent = new Intent();
        intent.setClass(this,ShouDongXiaZaiActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SHOU_DONG);
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
                        shengfenzheng = idcard.getIdNumber();
                     }
                }
            }
        } else if(requestCode == REQUEST_CODE_SHOU_DONG) {
            if(resultCode == Activity.RESULT_OK) {
                SimpleUserInfo info  = (SimpleUserInfo) data.getSerializableExtra(Const.KEY_USER_INFO);
                shengfenzheng = info.getIdNumber();
            }
        } else if(requestCode == PI_LIANG_XIA_ZAI) {
            if(resultCode == Activity.RESULT_OK) {
                finish();
            }
        }
    }
}
