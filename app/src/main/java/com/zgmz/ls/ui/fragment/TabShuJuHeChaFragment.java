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
import com.zgmz.ls.model.UserInfo;
import com.zgmz.ls.ui.CheckFamilyInfoActivity;
import com.zgmz.ls.ui.SearchActivity;
import com.zgmz.ls.ui.adapter.UserInfoAdapter;

import java.util.ArrayList;
import java.util.List;

public class TabShuJuHeChaFragment extends TitleBarFragment implements OnClickListener{
	
	View mFrameUncheck;
	
	View mFrameChecked;
	
	TextView mUncheckEmpty;
	
	TextView mCheckEmpty;
	
	ListView mUncheckList;
	
	ListView mCheckedList;
	
	UserInfoAdapter mUncheckedAdapter;
	
	List<UserInfo> mUncheckedUserInfos = new ArrayList<UserInfo>();
	
	UserInfoAdapter mCheckedAdapter;
	
	List<UserInfo> mCheckedUserInfos = new ArrayList<UserInfo>();
	
	
	private static final int STATE_UNCHECK = 1;

	
	int mState = STATE_UNCHECK;

	@Override
	protected void onConfigrationTitleBar() {
		// TODO Auto-generated method stub
		setTitleBarTitleText(R.string.shujuhecha);
		setTitleBarLeftImageButtonImageResource(R.drawable.title_back);
	}

	@Override
	public void onTitleBarLeftButtonOnClick(View v) {
		getActivity().finish();
	}
	@Override
	public void onCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.tab_uncheck);
	}
	
	
	@Override
	protected void setupViews(View root) {
		// TODO Auto-generated method stub
		mFrameUncheck = root.findViewById(R.id.frame_uncheck);
		mFrameChecked = root.findViewById(R.id.frame_checked);
		
		mUncheckEmpty = (TextView) root.findViewById(R.id.empty_uncheck);
		mCheckEmpty = (TextView) root.findViewById(R.id.empty_check);
		
		mUncheckList = (ListView) root.findViewById(R.id.uncheck_list);
		mCheckedList = (ListView) root.findViewById(R.id.checked_list);
		
		mUncheckList.setEmptyView(mUncheckEmpty);
		mCheckedList.setEmptyView(mCheckEmpty);
		
		
		mUncheckedAdapter = new UserInfoAdapter(getActivity(), mUncheckedUserInfos);
		mUncheckList.setAdapter(mUncheckedAdapter);
		
		mUncheckList.setOnItemClickListener(mUncheckedItemListener);
		
		mCheckedAdapter = new UserInfoAdapter(getActivity(), mCheckedUserInfos);
		mCheckedList.setAdapter(mCheckedAdapter);
		
		mCheckedList.setOnItemClickListener(mCheckedItemListener);
       
		showTabUncheck();
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
		}
	}
	
	
	OnItemClickListener mUncheckedItemListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// TODO Auto-generated method stub
			if(mUncheckedUserInfos.size()>position) {
				startUserInfoActivity(mUncheckedUserInfos.get(position));
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
		Intent intent = new Intent();
		intent.putExtra(Const.KEY_USER_INFO, userInfo.toSimpleUserInfo());
		intent.putExtra(Const.KEY_TYPE, InfoType.CHECK);
		intent.setClass(getActivity(),CheckFamilyInfoActivity.class);
		startActivity(intent);
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		updateUncheckedData();
	}
	private void showTabUncheck() {
		mFrameUncheck.setVisibility(View.VISIBLE);
		mFrameChecked.setVisibility(View.GONE);
		mState = STATE_UNCHECK;
		updateUncheckedData();
	}
	
	private void updateUncheckedData() {
		List<UserInfo> list = DBHelper.getInstance().getUncheckedFamily(100, true);
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
