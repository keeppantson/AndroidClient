package com.zgmz.ls.ui;

import java.util.ArrayList;
import java.util.List;

import com.zgmz.ls.R;
import com.zgmz.ls.base.Const;
import com.zgmz.ls.base.SharedDatas;
import com.zgmz.ls.base.SubActivity;
import com.zgmz.ls.db.DBHelper;
import com.zgmz.ls.model.IdCard;
import com.zgmz.ls.model.UserInfo;
import com.zgmz.ls.ui.adapter.UserInfoAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class SearchActivity extends SubActivity implements OnClickListener, OnItemClickListener{
	
	View mFrameSearch;
	View mFrameResult;
	
	
	ImageButton mBtnIdRecognize;
	
	ImageButton mBtnFingerprint;
	
	ImageButton mBtnIdNumber;
	
	
	ListView mListView;
	TextView mEmptyView;
	
	UserInfoAdapter mAdapter;
	
	List<UserInfo> mUserInfos = new ArrayList<UserInfo>();
	
	
	private static final int STATE_SEARCH = 1;
	
	private static final int STATE_RESULT = 2;
	
	private int state = STATE_SEARCH;

	@Override
	protected void onConfigrationTitleBar() {
		// TODO Auto-generated method stub
		super.onConfigrationTitleBar();
		setTitleBarTitleText(R.string.title_search);
	}
	
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(state == STATE_RESULT) {
			clearResult();
			showSearchView();
		}
		else {
			super.onBackPressed();
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
	}
	
	@Override
	protected void setupViews(View view) {
		// TODO Auto-generated method stub
		mFrameSearch = view.findViewById(R.id.search);
		mBtnIdRecognize = (ImageButton)view.findViewById(R.id.id_recoginze);
		mBtnFingerprint = (ImageButton)view.findViewById(R.id.fingerprint);
		mBtnIdNumber = (ImageButton)view.findViewById(R.id.id_number);
		

		
		mListView = (ListView) mFrameResult.findViewById(R.id.list);
		mEmptyView = (TextView) mFrameResult.findViewById(R.id.empty_text);
		mListView.setEmptyView(mEmptyView);
		
		mAdapter = new UserInfoAdapter(this, mUserInfos);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		
		mBtnIdRecognize.setOnClickListener(this);
		mBtnFingerprint.setOnClickListener(this);
		mBtnIdNumber.setOnClickListener(this);
		
		showSearchView();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
			case R.id.id_recoginze:
				startIdRecoginzeSimpleActivity();
				break;
			case R.id.fingerprint:
				startFingerpringSimpleActivity();
				break;
			case R.id.id_number:
				startIdInputSimpleActivity();
				break;
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		if(mUserInfos.size()>position) {
			startUserInfoActivity(mUserInfos.get(position));
		}
	}
	
	
	private void startUserInfoActivity(UserInfo info) {
		Intent intent = new Intent();
		intent.putExtra(Const.KEY_USER_INFO, info.toSimpleUserInfo());
		intent.setClass(this,UserInfoActivity.class);
		startActivity(intent);
	}
	
	private static final int REQUEST_CODE_ID_RECOGNIZE = 0x4001;
	
	private static final int REQUEST_CODE_FINGER_PRINT = 0x4002;
	
	private static final int REQUEST_CODE_ID_INPUT = 0x4003;
	
	
	private void startIdRecoginzeSimpleActivity() {
		Intent intent = new Intent();
		intent.setClass(this, IDRecoginzeSimpleActivity.class);
		startActivityForResult(intent, REQUEST_CODE_ID_RECOGNIZE);
	}
	
	private void startFingerpringSimpleActivity() {
		Intent intent = new Intent();
		intent.setClass(this, FingerprintSimpleActivitiy.class);
		startActivityForResult(intent, REQUEST_CODE_FINGER_PRINT);
	}
	
	private void startIdInputSimpleActivity() {
		Intent intent = new Intent();
		intent.setClass(this, IDInputActivity.class);
		startActivityForResult(intent, REQUEST_CODE_ID_INPUT);
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(requestCode == REQUEST_CODE_ID_RECOGNIZE) {
			if(resultCode == Activity.RESULT_OK) {
				String key = data.getStringExtra(Const.KEY_ID_CARD);
				if(key != null) {
					IdCard idcard = (IdCard) SharedDatas.getInstance().getValue(key);
					String idNumber = null;
					if(idcard != null) {
						idNumber = idcard.getIdNumber();
					}
					searchUser(idNumber);
				}
				else {
					showResult(null);
				}
			}
		}
		else if(requestCode == REQUEST_CODE_FINGER_PRINT) {
			if(resultCode == Activity.RESULT_OK) {
				int userId = data.getIntExtra(Const.KEY_USER_ID, 0);
				if(userId > 0) {
					searchUser(userId);
				}
				else {
					showResult(null);
				}
			}
		}
		else if(requestCode == REQUEST_CODE_ID_INPUT) {
			if(resultCode == Activity.RESULT_OK) {
				String idNumber = data.getStringExtra(Const.KEY_ID_NUMBER);
				searchUser(idNumber);
			}
		}
	}
	
	
	private void clearResult() {
		updateUserInfo(null);
	}
	
	private void updateUserInfo(UserInfo info) {
		mUserInfos.clear();
		if(info != null) {
			mUserInfos.add(info);
		}
		mAdapter.notifyDataSetChanged();
	}
	
	private void showSearchView() {
		mFrameResult.setVisibility(View.GONE);
		mFrameSearch.setVisibility(View.VISIBLE);
		state = STATE_SEARCH;
	}
	
	private void showResult() {
		mFrameResult.setVisibility(View.VISIBLE);
		mFrameSearch.setVisibility(View.GONE);
		state = STATE_RESULT;
	}
	
	private void showResult(UserInfo info) {
		showResult();
		updateUserInfo(info);
	}
	
	
	
	private void searchUser(int userId) {
		UserInfo user = DBHelper.getInstance().getUserInfo(userId);
		showResult(user);
	}
	
	
	private void searchUser(String idNumber) {
		UserInfo user = DBHelper.getInstance().getUserInfo(idNumber);
		showResult(user);
	}
	
	
	
}
