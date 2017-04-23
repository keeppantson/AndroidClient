package com.zgmz.ls.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;

import com.zgmz.ls.R;
import com.zgmz.ls.base.Const;
import com.zgmz.ls.base.SubActivity;
import com.zgmz.ls.db.DBHelper;
import com.zgmz.ls.model.UserInfo;
import com.zgmz.ls.ui.adapter.UserInfoAdapter;
import com.zgmz.ls.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class Main_Shu_Ju_He_Cha_Yi_He_Cha_Activity extends SubActivity implements View.OnClickListener {

	ListView mUncheckList;

	UserInfoAdapter mUncheckedAdapter;

	List<UserInfo> mUncheckedUserInfos = new ArrayList<UserInfo>();
    List<UserInfo> mSelectedUserInfos = new ArrayList<UserInfo>();
	ImageButton shan_chu;
	//ImageButton tiao_zhan_yi_he_cha;

	CheckBox quanbu;

	protected void onConfigrationTitleBar() {
		// TODO Auto-generated method stub
		super.onConfigrationTitleBar();
		setTitleBarTitleText("数据核查");
		//setTitleBarRightImageButtonImageResource(R.drawable.dai_he_cha);
	}

	@Override
	public void onTitleBarRightButtonOnClick(View v) {
		//startHeChaYiHeChaActivity();
		return;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ru_hu_he_cha_yi_he_cha);
		setupViews();
	}

	protected void setupViews() {
		// TODO Auto-generated method stub


		mUncheckList = (ListView) this.findViewById(R.id.uncheck_list);

		mUncheckedAdapter = new UserInfoAdapter(this, mUncheckedUserInfos);
		mUncheckList.setOnItemClickListener(mUncheckedItemListener);

        mUncheckList.setAdapter(mUncheckedAdapter);
		//tiao_zhan_yi_he_cha = (ImageButton)this.findViewById(R.id.he_cha_yi_he_cha);
		quanbu = (CheckBox)this.findViewById(R.id.quan_bu);
		shan_chu  = (ImageButton)this.findViewById(R.id.shan_chu);

		//tiao_zhan_yi_he_cha.setOnClickListener(this);
		quanbu.setOnClickListener(this);
		shan_chu.setOnClickListener(this);
        mUncheckedAdapter.setSelectedIndex(-1);
        updateUncheckedData();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		ifneedUpdate();
	}


	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
            case R.id.quan_bu:
                quan_bu_xuan_ze();
                break;
            case R.id.shan_chu:
                shan_chu();
                break;
			case R.id.he_cha:
				he_cha();
				break;
		}
	}
    private void startHeChaYiHeChaActivity() {
		finish();
    }

    private void quan_bu_xuan_ze() {
        if (quanbu.isChecked()) {
            for(int i = 0; i < mUncheckedUserInfos.size(); i++) {
                mSelectedUserInfos.add(mUncheckedUserInfos.get(i));
            }
            mUncheckedAdapter.setSelectedIndex(-2);
            mUncheckedAdapter.notifyDataSetChanged();
        } else {

            mSelectedUserInfos.clear();
            mUncheckedAdapter.setSelectedIndex(-3);
            mUncheckedAdapter.notifyDataSetChanged();
        }
    }

    private void he_cha() {
        startHeChaActivity();
    }

    private void shan_chu() {
        for (int i = 0; i < mSelectedUserInfos.size(); i++) {
            DBHelper.getInstance().deleteAllTasks(mSelectedUserInfos.get(0).getCheck_task_id());
        }
        updateUncheckedData();
        mSelectedUserInfos.clear();
        return;
    }

    private void startHeChaActivity() {
        if (mSelectedUserInfos.size() > 1 || mSelectedUserInfos.size() == 0) {
            ToastUtils.showShortToast("请确认仅选择一项做核查");
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(Const.KEY_USER_INFO, mSelectedUserInfos.get(0).toSimpleUserInfo());
        intent.putExtra(Const.KEY_TYPE, Const.InfoType.CHECK);
        intent.setClass(this,CheckFamilyInfoActivity.class);
        startActivity(intent);
    }


    AdapterView.OnItemClickListener mUncheckedItemListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// TODO Auto-generated method stub
			if(mUncheckedUserInfos.size()>position) {
				mSelectedUserInfos.clear();
				//startUserInfoActivity(mUncheckedUserInfos.get(position));
                mSelectedUserInfos.add(mUncheckedUserInfos.get(position));
			}

			mUncheckedAdapter.setSelectedIndex(position);
            mUncheckedAdapter.notifyDataSetChanged();
			he_cha();
		}
	};


	private void startUserInfoActivity(UserInfo userInfo) {
		Intent intent = new Intent();
		intent.putExtra(Const.KEY_USER_INFO, userInfo.toSimpleUserInfo());
		intent.putExtra(Const.KEY_TYPE, Const.InfoType.CHECK);
		intent.setClass(this,CheckFamilyInfoActivity.class);
		startActivity(intent);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		updateUncheckedData();
	}

	private void updateUncheckedData() {
		List<UserInfo> list = DBHelper.getInstance().getCheckedFamily(100, true);
		if(list != null) {
			mUncheckedUserInfos.clear();
			mUncheckedUserInfos.addAll(list);
			mUncheckedAdapter.notifyDataSetChanged();
		}
	}

	private void ifneedUpdate() {
		updateUncheckedData();
	}


}
