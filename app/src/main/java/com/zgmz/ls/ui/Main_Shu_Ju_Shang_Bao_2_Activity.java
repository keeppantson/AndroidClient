package com.zgmz.ls.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.zgmz.ls.R;
import com.zgmz.ls.base.SubActivity;
import com.zgmz.ls.db.DBHelper;
import com.zgmz.ls.model.FamilyBase;
import com.zgmz.ls.model.UserInfo;
import com.zgmz.ls.ui.adapter.UserInfoAdapter;
import com.zgmz.ls.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import static com.zgmz.ls.R.id.empty_check;

public class Main_Shu_Ju_Shang_Bao_2_Activity extends SubActivity implements View.OnClickListener {

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

    protected void onConfigrationTitleBar() {
        // TODO Auto-generated method stub
        super.onConfigrationTitleBar();
        setTitleBarTitleText("数据上报");
        setTitleBarRightImageButtonImageResource(R.drawable.quan_xuan);
    }

    @Override
    public void onTitleBarRightButtonOnClick(View v) {
        select_all();
        return;
    }
	UserInfoAdapter mUncheckedAdapter;

    List<UserInfo> mUncheckedUserInfos = new ArrayList<UserInfo>();
    List<UserInfo> mSelectedUserInfos = new ArrayList<UserInfo>();

    View emptyFrame;
    View listFrame;
    ListView mUncheckList;
    Button mBtnTiaoZhan;
	Button mBtnCancel;
    Button mButtonTiaoZhuan;
    private void updateUncheckedData() {
        List<UserInfo> list = DBHelper.getInstance().getUploadingFamily(100, true);
        if(list != null) {
            mUncheckedUserInfos.clear();
            mUncheckedUserInfos.addAll(list);
            mUncheckedAdapter.notifyDataSetChanged();
        }
        if (list.size() == 0) {
            emptyFrame.setVisibility(View.VISIBLE);
            listFrame.setVisibility(View.GONE);
        } else {
            emptyFrame.setVisibility(View.GONE);
            listFrame.setVisibility(View.VISIBLE);
        }
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.shu_ju_shang_bao_yi_shang_chuan);
        emptyFrame = this.findViewById(R.id.empty_check);
        listFrame = this.findViewById(R.id.other);
        mUncheckList = (ListView) this.findViewById(R.id.list);

        mUncheckedAdapter = new UserInfoAdapter(this, mUncheckedUserInfos);
        mUncheckList.setOnItemClickListener(mUncheckedItemListener);

        mUncheckList.setAdapter(mUncheckedAdapter);


        //mBtnAllSelected = (Button)this.findViewById(R.id.shu_ju_shang_bao_quan_xuan);
        mBtnCancel = (Button)this.findViewById(R.id.qu_xiao_1);
        mBtnTiaoZhan = (Button)this.findViewById(R.id.mei_you_ji_lu);
        mButtonTiaoZhuan = (Button)this.findViewById(R.id.dai_shang_chuan_1);
        //mBtnAllSelected.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);
        mButtonTiaoZhuan.setOnClickListener(this);
        mBtnTiaoZhan.setOnClickListener(this);


        updateUncheckedData();
	}

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        updateUncheckedData();
    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch(v.getId()) {

            case R.id.mei_you_ji_lu:
                tiao_zhuan();
                break;

            case R.id.qu_xiao_1:
                qu_xiao();
                break;
            case R.id.dai_shang_chuan_1:
                tiao_zhuan();
                break;

        }
    }
    void tiao_zhuan() {
       finish();
    }
    private void upload(UserInfo info) {
        FamilyBase family = new FamilyBase();
        family.setCheck_task_id(info.getCheck_task_id());
        family.setIsChecked("2");
        family.setSqrsfzh(info.getIdNumber());
        DBHelper.getInstance().insertOrUpdateFamilyBase(family);
        return;
    }

    private void start_upload() {
        for (int i =0; i < mSelectedUserInfos.size(); i++) {
            UserInfo info = mSelectedUserInfos.get(i);
            upload(info);
        }
        mSelectedUserInfos.clear();
        updateUncheckedData();
    }

    void qu_xiao() {

        mUncheckedAdapter.setSelectedIndex(-3);
        mSelectedUserInfos.clear();
        updateUncheckedData();
    }
    private void select_all() {
        for (int i =0; i < mUncheckedUserInfos.size(); i++) {
            mSelectedUserInfos.add(mUncheckedUserInfos.get(i));
        }

        mUncheckedAdapter.setSelectedIndex(-2);
        mUncheckedAdapter.notifyDataSetChanged();
    }
    AdapterView.OnItemClickListener mUncheckedItemListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // TODO Auto-generated method stub
            if(mUncheckedUserInfos.size()>position) {
                //startUserInfoActivity(mUncheckedUserInfos.get(position));
                mSelectedUserInfos.add(mUncheckedUserInfos.get(position));
            }

            mUncheckedAdapter.setSelectedIndex(position);
            mUncheckedAdapter.notifyDataSetChanged();
        }
    };
	@Override
	public void onBackPressed() {
		finish();
	}

}
