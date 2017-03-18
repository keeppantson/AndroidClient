package com.zgmz.ls.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import com.zgmz.ls.R;
import com.zgmz.ls.base.Const;
import com.zgmz.ls.base.SharedDatas;
import com.zgmz.ls.db.DBHelper;
import com.zgmz.ls.model.IdCard;
import com.zgmz.ls.model.SimpleUserInfo;
import com.zgmz.ls.model.UserInfo;
import com.zgmz.ls.ui.IDManualActivity;
import com.zgmz.ls.ui.IDRecoginzeActivity;
import com.zgmz.ls.ui.UserInfoActivity;
import com.zgmz.ls.ui.adapter.UserInfoAdapter;
import com.zgmz.ls.utils.ToastUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class TabRecordFragment extends TitleBarFragment implements OnClickListener, OnItemClickListener{
	
	Button mBtnManualInput;
	
	Button mBtnRecognizeInput;
	
	ListView mListView;
	
	TextView mEmptyText;
	
	UserInfoAdapter mAdapter;
	
	
	List<UserInfo> mUserInfos = new ArrayList<UserInfo>();

	@Override
	protected void onConfigrationTitleBar() {
		// TODO Auto-generated method stub
		setTitleBarTitleText(R.string.title_tab_record);
	}
	
	@Override
	public void onCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreated(savedInstanceState);
		setContentView(R.layout.tab_record);
		updateUserInfos();
	}
	
	
	@Override
	protected void setupViews(View root) {
		// TODO Auto-generated method stub
		mBtnRecognizeInput = (Button)root.findViewById(R.id.recognize_input);
		mBtnManualInput = (Button)root.findViewById(R.id.manual_input);
		
		mEmptyText = (TextView)root.findViewById(R.id.empty_text);
		mListView = (ListView)root.findViewById(R.id.list);
		
		mListView.setEmptyView(mEmptyText);
		
		mAdapter = new UserInfoAdapter(getActivity(), mUserInfos);
		mListView.setAdapter(mAdapter);
		
		mListView.setOnItemClickListener(this);
		
		mBtnRecognizeInput.setOnClickListener(this);
		mBtnManualInput.setOnClickListener(this);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(SharedDatas.getInstance().isRecordUpdated()) {
			SharedDatas.getInstance().clearRecordUpdated();
			updateUserInfos();
		}
	}
	
	public void updateUserInfos() {
		List<UserInfo> userInfos = DBHelper.getInstance().getLastUserInfos(10, true);
		if(userInfos != null) {
			mUserInfos.clear();
			mUserInfos.addAll(userInfos);
			mAdapter.notifyDataSetChanged();
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
			case R.id.recognize_input:
				startIdRecognizeActivity();
				break;
			case R.id.manual_input:
				startIdManualInputActivity();
				break;
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
		// TODO Auto-generated method stub
		if(mUserInfos.size()>position) {
			startUserInfoActivity(mUserInfos.get(position));
		}
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
					if(idcard != null)
						createUserAndShowUserInfo(idcard);
				}
			}
		}
		else if(requestCode == REQUEST_CODE_MANUAL) {
			if(resultCode == Activity.RESULT_OK) {
				SimpleUserInfo info  = (SimpleUserInfo) data.getSerializableExtra(Const.KEY_USER_INFO);
				if(info != null) {
					createUserAndShowUserInfo(info.toUserInfo());
				}
			}
		}
		
		
	}
	
	private static final int REQUEST_CODE_RECONGNIZE = 1001;
	
	private static final int REQUEST_CODE_MANUAL = 1002;
	
	private void startIdRecognizeActivity() {
		Intent intent = new Intent();
		intent.setClass(getActivity(),IDRecoginzeActivity.class);
		startActivityForResult(intent, REQUEST_CODE_RECONGNIZE);
	}
	
	
	private void startIdManualInputActivity() {
		Intent intent = new Intent();
		intent.setClass(getActivity(),IDManualActivity.class);
		startActivityForResult(intent, REQUEST_CODE_MANUAL);
	}
	
	private void createUserAndShowUserInfo(UserInfo userInfo) {
		if(!DBHelper.getInstance().hasUser(userInfo.getIdNumber())) {
			if(DBHelper.getInstance().insertUser(userInfo)) {
				UserInfo info = DBHelper.getInstance().getUserInfo(userInfo.getIdNumber());
				if(info != null) {
					SharedDatas.getInstance().recordUpdated();
					startUserInfoActivity(info);
				}
			}
			else {
				ToastUtils.showLongToast("新建用户失败");
			}
		}
		else {
			ToastUtils.showLongToast("用户已存在");
		}
	}
	
	private void createUserAndShowUserInfo(IdCard idCard) {
		if(!DBHelper.getInstance().hasUser(idCard.getIdNumber())) {
			if(DBHelper.getInstance().insertUser(idCard)) {
				
				UserInfo info = DBHelper.getInstance().getUserInfo(idCard.getIdNumber());
				if(info != null) {
					idCard.setUserId(info.getUserId());
					if(DBHelper.getInstance().insertOrUpdateIdCard(idCard)) {
						info.setFlagId(true);
						SharedDatas.getInstance().recordUpdated();
					}
					startUserInfoActivity(info);
				}
			}
			else {
				ToastUtils.showLongToast("新建用户失败");
			}
		}
		else {
			ToastUtils.showLongToast("用户已存在");
		}
	}
	
	private void startUserInfoActivity(UserInfo info) {
		Intent intent = new Intent();
		intent.putExtra(Const.KEY_USER_INFO, info.toSimpleUserInfo());
		intent.setClass(getActivity(),UserInfoActivity.class);
		startActivity(intent);
	}
	
	
}
