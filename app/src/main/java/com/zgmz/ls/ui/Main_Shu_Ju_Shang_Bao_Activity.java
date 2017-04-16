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
import com.zgmz.ls.base.BottomTabActivity;
import com.zgmz.ls.base.Const;
import com.zgmz.ls.base.SubActivity;
import com.zgmz.ls.db.DBHelper;
import com.zgmz.ls.model.FamilyBase;
import com.zgmz.ls.model.UserInfo;
import com.zgmz.ls.ui.adapter.UserInfoAdapter;
import com.zgmz.ls.ui.fragment.TabShuJuHeChaFragment;
import com.zgmz.ls.ui.fragment.TabShuJuShangBaoFragment;
import com.zgmz.ls.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class Main_Shu_Ju_Shang_Bao_Activity extends SubActivity implements View.OnClickListener {
	
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

	UserInfoAdapter mUncheckedAdapter;

    List<UserInfo> mUncheckedUserInfos = new ArrayList<UserInfo>();
    List<UserInfo> mSelectedUserInfos = new ArrayList<UserInfo>();

    ListView mUncheckList;
	Button mBtnStartUpload;
	Button mBtnCancel;
    Button mButtonTiaoZhuan;
    private void updateUncheckedData() {
        List<UserInfo> list = DBHelper.getInstance().getCheckedFamily(100, true);
        if(list != null) {
            mUncheckedUserInfos.clear();
            mUncheckedUserInfos.addAll(list);
            mUncheckedAdapter.notifyDataSetChanged();
        }
    }

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.shu_ju_shang_bao_dai_shang_chuan);


        mUncheckList = (ListView) this.findViewById(R.id.list);

        mUncheckedAdapter = new UserInfoAdapter(this, mUncheckedUserInfos);
        mUncheckList.setOnItemClickListener(mUncheckedItemListener);

        mUncheckList.setAdapter(mUncheckedAdapter);


        //mBtnAllSelected = (Button)this.findViewById(R.id.shu_ju_shang_bao_quan_xuan);
        mBtnStartUpload = (Button)this.findViewById(R.id.shu_ju_shang_bao_1);
        mBtnCancel = (Button)this.findViewById(R.id.qu_xiao_1);
        mButtonTiaoZhuan = (Button)this.findViewById(R.id.zheng_zai_shang_chuan_1);
        //mBtnAllSelected.setOnClickListener(this);
        mBtnStartUpload.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);
        mButtonTiaoZhuan.setOnClickListener(this);


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

            case R.id.shu_ju_shang_bao_1:
                start_upload();
                break;

            case R.id.qu_xiao_1:
                qu_xiao();
                break;
            case R.id.zheng_zai_shang_chuan_1:
                tiao_zhuan();
                break;

        }
    }
    void tiao_zhuan() {

        Intent intent = new Intent();
        intent.setClass(this,Main_Shu_Ju_Shang_Bao_2_Activity.class);
        startActivity(intent);
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
