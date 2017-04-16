package com.zgmz.ls.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.zgmz.ls.R;
import com.zgmz.ls.base.Const;
import com.zgmz.ls.base.Const.InfoType;
import com.zgmz.ls.base.SharedDatas;
import com.zgmz.ls.db.DBHelper;
import com.zgmz.ls.model.FamilyBase;
import com.zgmz.ls.model.UserInfo;
import com.zgmz.ls.ui.CheckFamilyInfoActivity;
import com.zgmz.ls.ui.SearchActivity;
import com.zgmz.ls.ui.adapter.UserInfoAdapter;
import com.zgmz.ls.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class TabShuJuShangBaoFragment extends TitleBarFragment implements OnClickListener{

	
	View mFrameUncheck;
	
	View mFrameChecked;
	ListView mUncheckList;
	
	ListView mCheckedList;
	
	UserInfoAdapter mUncheckedAdapter;
	
	List<UserInfo> mUncheckedUserInfos = new ArrayList<UserInfo>();
	
	UserInfoAdapter mCheckedAdapter;
	
	List<UserInfo> mCheckedUserInfos = new ArrayList<UserInfo>();
	
	
	private static final int STATE_UNCHECK = 1;
	
	private static final int STATE_CHECKED = 2;
	
	int mState = STATE_UNCHECK;

	Button mBtnAllUpload;
	Button mBtnStartUpload;

	List<UserInfo> mToUpload = new ArrayList<UserInfo>();

	@Override
	protected void onConfigrationTitleBar() {
		// TODO Auto-generated method stub
		setTitleBarTitleText(R.string.title_tab_check);
	}

	@Override
	public void onCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.tab_check);
		setTitleBarTitleText(R.string.shujushangbao);
		setTitleBarLeftImageButtonImageResource(R.drawable.title_back);
	}

	@Override
	public void onTitleBarLeftButtonOnClick(View v) {
		getActivity().finish();
	}

	@Override
	protected void setupViews(View root) {
		// TODO Auto-generated method stub
		mFrameUncheck = root.findViewById(R.id.frame_unuploaded);
		mFrameChecked = root.findViewById(R.id.frame_uploading);

		
		mUncheckList = (ListView) root.findViewById(R.id.unloading_list);
		mCheckedList = (ListView) root.findViewById(R.id.uploaded_list);
		
		mUncheckedAdapter = new UserInfoAdapter(getActivity(), mUncheckedUserInfos);
		mUncheckList.setAdapter(mUncheckedAdapter);
		
		mUncheckList.setOnItemClickListener(mUncheckedItemListener);
		
		mCheckedAdapter = new UserInfoAdapter(getActivity(), mCheckedUserInfos);
		mCheckedList.setAdapter(mCheckedAdapter);
		
		mCheckedList.setOnItemClickListener(mCheckedItemListener);

		mBtnAllUpload = (Button)root.findViewById(R.id.all_upload);
		mBtnAllUpload.setOnClickListener(this);
		mBtnStartUpload = (Button)root.findViewById(R.id.start_upload);
		mBtnStartUpload.setOnClickListener(this);

        ifneedUpdate();
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

            case R.id.start_upload:
                start_upload();
                break;

            case R.id.all_upload:
                all_upload();
                break;
		}
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
        for (int i =0; i < mToUpload.size(); i++) {
            UserInfo info = mToUpload.get(i);
            upload(info);
        }
        mToUpload.clear();
        ifneedUpdate();
    }
    private void all_upload() {
        for (int i =0; i < mUncheckedUserInfos.size(); i++) {
            UserInfo info = mUncheckedUserInfos.get(i);
            upload(info);
        }
        mToUpload.clear();
        ifneedUpdate();
    }
	
	OnItemClickListener mUncheckedItemListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// TODO Auto-generated method stub
			if(mUncheckedUserInfos.size() > position) {
                mToUpload.add(mUncheckedUserInfos.get(position));
			}
		}
	};
	
	OnItemClickListener mCheckedItemListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// TODO Auto-generated method stub
			if(mCheckedUserInfos.size()>position) {
				startUserInfoActivity(mCheckedUserInfos.get(position));
			}
		}
	};
	
	
	private void startUserInfoActivity(UserInfo userInfo) {
        ToastUtils.showLongToast("正在尝试上传，结束后会自动删除相关记录");
        return;
	}
	
	private void updateUncheckedData() {
		List<UserInfo> list = DBHelper.getInstance().getCheckedFamily(100, true);
		if(list != null) {
			mUncheckedUserInfos.clear();
			mUncheckedUserInfos.addAll(list);
			mUncheckedAdapter.notifyDataSetChanged();
		}
	}
	
	private void updateCheckedData() {
		List<UserInfo> list = DBHelper.getInstance().getUploadingFamily(100, true);
		if(list != null) {
			mCheckedUserInfos.clear();
			mCheckedUserInfos.addAll(list);
			mCheckedAdapter.notifyDataSetChanged();
		}
	}
	
	private void ifneedUpdate() {
        updateUncheckedData();
        updateCheckedData();
	}

}
