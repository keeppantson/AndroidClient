package com.zgmz.ls.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.zgmz.ls.R;
import com.zgmz.ls.base.Const;
import com.zgmz.ls.base.SharedDatas;
import com.zgmz.ls.base.SubActivity;
import com.zgmz.ls.db.DBHelper;
import com.zgmz.ls.dialog.ListDialog;
import com.zgmz.ls.dialog.ListItem;
import com.zgmz.ls.model.DownloadTask;
import com.zgmz.ls.model.IdCard;
import com.zgmz.ls.model.QuHuaMa;
import com.zgmz.ls.ui.adapter.DownLoadTaskAdapter;
import com.zgmz.ls.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.view.Window.FEATURE_NO_TITLE;

public class Main_Shu_Ju_Xia_Zai_Pi_Liang_Activity extends SubActivity implements OnClickListener, CompoundButton.OnCheckedChangeListener {

	private static final int CANCAL_QUIT_TIME = 2000;

	private static final int MSG_CANCAL_QUIT = 0x0001;

	boolean bCanQiut = false;

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

    Button ri_qi_xuanze;
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
    private Calendar calendar;
    String download_task_type = null;
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == zui_di) {
            if (isChecked) {
                download_task_type = "110";
                te_kun.setChecked(false);
            }
        } else if (buttonView == te_kun) {
            if (isChecked) {
                download_task_type = "130";
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
            case R.id.dan_jia_ting_xia_zai:
                startAnDiQuXiaZaiActivity();
                break;
            case R.id.shiji_xuanze:
                shi_ji_dialog_show();
                break;
            case R.id.xian_ji_xuanze:
                xian_ji_dialog_show();
                break;
            case R.id.jie_dao_xuanze:
                jie_dao_dialog_show();
                break;
            case R.id.cun_xuanze:
                cun_dialog_show();
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
    Button shiji_xuanze;
    Button xianji_xuanze;
    Button jiedao_xuanze;
    Button cun_xuanze;
    EditText jiuzhu_nian_yue;

    CheckBox zui_di;
    CheckBox te_kun;

    ImageButton mBtnAddDownloadTask;

	DownLoadTaskAdapter mUncheckedAdapter;

    private List<QuHuaMa> listCity = new ArrayList<QuHuaMa>();
    private List<QuHuaMa> listTown = new ArrayList<QuHuaMa>();
    private List<QuHuaMa> listStreet = new ArrayList<QuHuaMa>();
    private List<QuHuaMa> listBuilding = new ArrayList<QuHuaMa>();
    ListView mDownLoadTaskListView;
    List<DownloadTask> mDownLoadTasks = new ArrayList<DownloadTask>();

    OnItemClickListener mUncheckedItemListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // TODO Auto-generated method stub
            if(mDownLoadTasks.size() > position) {
                ToastUtils.showLongToast("还未下载完，一旦下载结束，可以选择“数据核查”项目进入查看");
            }
        }
    };

    protected void onConfigrationTitleBar() {
        // TODO Auto-generated method stub
        super.onConfigrationTitleBar();
        setTitleBarTitleText("数据下载");
        //hideTitleBar();
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.xia_zai_2);


        mFrameUncheck = this.findViewById(R.id.frame_uncheck);

        mDownLoadTaskListView = (ListView) this.findViewById(R.id.uncheck_list);

        mBtnAddDownloadTask = (ImageButton)this.findViewById(R.id.xiazai_bottom);
        mBtnAnDiQuXiaZai = (ImageButton)this.findViewById(R.id.dan_jia_ting_xia_zai);
        shiji_xuanze = (Button) this.findViewById(R.id.shiji_xuanze);
        xianji_xuanze = (Button)this.findViewById(R.id.xian_ji_xuanze);
        jiedao_xuanze = (Button)this.findViewById(R.id.jie_dao_xuanze);
        cun_xuanze = (Button)this.findViewById(R.id.cun_xuanze);
        ri_qi_xuanze = (Button) this.findViewById(R.id.jiu_zhu_nian_yue);

        zui_di = (CheckBox)this.findViewById(R.id.zui_di);
        te_kun = (CheckBox)this.findViewById(R.id.te_kun);

        mUncheckedAdapter = new DownLoadTaskAdapter(this, mDownLoadTasks);
        mDownLoadTaskListView.setAdapter(mUncheckedAdapter);

        mDownLoadTaskListView.setOnItemClickListener(mUncheckedItemListener);
        ri_qi_xuanze.setOnClickListener(this);
        mBtnAddDownloadTask.setOnClickListener(this);
        mBtnAnDiQuXiaZai.setOnClickListener(this);
        shiji_xuanze.setOnClickListener(this);
        xianji_xuanze.setOnClickListener(this);
        jiedao_xuanze.setOnClickListener(this);
        cun_xuanze.setOnClickListener(this);

        zui_di.setOnCheckedChangeListener(this);
        te_kun.setOnCheckedChangeListener(this);

        allQuHuaMa.setId("all");
        allQuHuaMa.setName("全部");
        allQuHuaMa.setDepth("3");
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
            if (list.get(i).getQu_hua_ma() != null) {
                validlist.add(list.get(i));
            }
        }
        if(!validlist.isEmpty()) {
            mDownLoadTasks.clear();
            mDownLoadTasks.addAll(validlist);
            mUncheckedAdapter.notifyDataSetChanged();
        }
    }

    private void startXiaZai() {
        if (yourChoiceQuHuaMa == null) {
            ToastUtils.showShortToast("请确认选择正确的区划码");
            return;
        }
        if (yourChoiceQuHuaMa.getName() == null) {
            ToastUtils.showShortToast("请确认选择正确的区划码");
            return;
        }
        if (download_task_type == null) {
            ToastUtils.showShortToast("请确认选择正确的救助类型");
            return;
        }
        ToastUtils.showShortToast("您选择的批量下载区域为: " + yourChoiceQuHuaMa.getName());
        DownloadTask quhuama_task = new DownloadTask();
        quhuama_task.setApply_type(download_task_type);
        quhuama_task.setQu_hua_ma(yourChoiceQuHuaMa.getId());
        quhuama_task.setPage_id("0");
        quhuama_task.setPage_number("1");
        quhuama_task.setTotal_number("0");
        DBHelper.getInstance().insertOrUpdateDownloadTask(quhuama_task);


        updateUncheckedData();
    }
    private void startAnDiQuXiaZaiActivity() {
        finish();
    }
    int now_depth = 0;
    void shi_ji_dialog_show() {
        listCity = DBHelper.getInstance().getQuHuaMaWithDepth("3");
        allQuHuaMa.setDepth("3");
        listCity.add(allQuHuaMa);
        now_depth = 0;
        showSingleChoiceDialog(listCity);
    }
    void xian_ji_dialog_show() {
        if (yourChoiceQuHuaMa == null) {
            return;
        }
        listTown = DBHelper.getInstance().getQuHuaMaWithFatherID(yourChoiceQuHuaMa.getId());
        allQuHuaMa.setDepth("4");
        listTown.add(allQuHuaMa);
        now_depth = 1;
        showSingleChoiceDialog(listTown);
        if (yourChoiceQuHuaMa != null) {
            xianji_xuanze.setText(yourChoiceQuHuaMa.toString());
        }
    }
    void jie_dao_dialog_show() {

        if (yourChoiceQuHuaMa == null) {
            return;
        }
        now_depth = 2;
        listStreet = DBHelper.getInstance().getQuHuaMaWithFatherID(yourChoiceQuHuaMa.getId());
        allQuHuaMa.setDepth("5");
        listStreet.add(allQuHuaMa);
        showSingleChoiceDialog(listStreet);
        if (yourChoiceQuHuaMa != null) {
            jiedao_xuanze.setText(yourChoiceQuHuaMa.toString());
        }
    }
    void cun_dialog_show() {

        if (yourChoiceQuHuaMa == null) {
            return;
        }
        now_depth = 3;
        listBuilding = DBHelper.getInstance().getQuHuaMaWithFatherID(yourChoiceQuHuaMa.getId());
        allQuHuaMa.setDepth("6");
        listBuilding.add(allQuHuaMa);
        showSingleChoiceDialog(listBuilding);

        if (yourChoiceQuHuaMa != null) {
            cun_xuanze.setText(yourChoiceQuHuaMa.toString());
        }
    }

    QuHuaMa allQuHuaMa = new QuHuaMa();
    QuHuaMa yourChoiceQuHuaMa = null;
    public List<QuHuaMa> now_list;
    private void showSingleChoiceDialog(List<QuHuaMa> list){
        List<ListItem> item_list = new ArrayList<ListItem>();
        for (int i = 0; i < list.size(); i++) {
            ListItem list_item = new ListItem();
            list_item.name = list.get(i).toString();
            item_list.add(i, list_item);
        }
        now_list = list;
        yourChoiceQuHuaMa = now_list.get(0);

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
                yourChoiceQuHuaMa = now_list.get(id);
                if (now_depth == 0) {
                    shiji_xuanze.setText(yourChoiceQuHuaMa.toString());
                } else if (now_depth == 1) {
                    xianji_xuanze.setText(yourChoiceQuHuaMa.toString());
                } else if (now_depth == 2) {
                    jiedao_xuanze.setText(yourChoiceQuHuaMa.toString());
                } else if (now_depth == 3) {
                    cun_xuanze.setText(yourChoiceQuHuaMa.toString());
                }
            }
        });
        // 第二个参数是默认选项，此处设置为0
        /*listDialog.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        yourChoiceQuHuaMa = now_list.get(which);
                    }
                });
        singleChoiceDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (yourChoiceQuHuaMa != null) {
                            ToastUtils.showShortToast("你选择了" + yourChoiceQuHuaMa.toString());
                        }

                        if (yourChoiceQuHuaMa != null) {
                            if (now_depth == 0) {
                                shiji_xuanze.setText(yourChoiceQuHuaMa.toString());
                            } else if (now_depth == 1) {
                                xianji_xuanze.setText(yourChoiceQuHuaMa.toString());
                            } else if (now_depth == 2) {
                                jiedao_xuanze.setText(yourChoiceQuHuaMa.toString());
                            } else if (now_depth == 3) {
                                cun_xuanze.setText(yourChoiceQuHuaMa.toString());
                            }
                        }
                    }
                });
                */
        listDialog.show();
    }
}
