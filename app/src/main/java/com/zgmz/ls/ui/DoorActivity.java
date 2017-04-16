package com.zgmz.ls.ui;

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

import com.zgmz.ls.R;
import com.zgmz.ls.base.Const;
import com.zgmz.ls.base.SharedDatas;
import com.zgmz.ls.base.SubActivity;
import com.zgmz.ls.db.DBHelper;
import com.zgmz.ls.model.IdCard;
import com.zgmz.ls.model.UserInfo;
import com.zgmz.ls.ui.adapter.UserInfoAdapter;

import java.util.ArrayList;
import java.util.List;

public class DoorActivity extends SubActivity implements OnClickListener, OnItemClickListener{

	
	
	ImageButton mBtnRuHuHeCha;
	
	ImageButton mBtnXinZengBaoSong;

	ImageButton mBtnSheZhi;

	ImageButton mBtnShuJuXiaZai;

	ImageButton mBtnShuJuHeCha;

	ImageButton mBtnShuJuShangBao;
	

	//ListView mListView;
	//TextView mEmptyView;
	
	UserInfoAdapter mAdapter;
	
	List<UserInfo> mUserInfos = new ArrayList<UserInfo>();
	
	
	private static final int STATE_SEARCH = 1;
	
	private static final int STATE_RESULT = 2;
	
	private int state = STATE_SEARCH;

	protected void onConfigrationTitleBar() {
		// TODO Auto-generated method stub
		super.onConfigrationTitleBar();
		setTitleBarTitleText("入户核查");
        //hideTitleBar();
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
		setContentView(R.layout.door_main);
        setupViews();
	}

	//@Override
	protected void setupViews() {
		// TODO Auto-generated method stub
		mBtnRuHuHeCha = (ImageButton)this.findViewById(R.id.ru_hu_he_cha_bottom);
		mBtnXinZengBaoSong = (ImageButton)this.findViewById(R.id.xin_zeng_bao_song_bottom);
		mBtnSheZhi = (ImageButton)this.findViewById(R.id.she_zhi);



        mBtnShuJuHeCha = (ImageButton)this.findViewById(R.id.shu_ju_he_cha);
        mBtnShuJuXiaZai = (ImageButton)this.findViewById(R.id.shu_ju_xia_zai);
        mBtnShuJuShangBao = (ImageButton)this.findViewById(R.id.shu_ju_shang_bao);
		
		mAdapter = new UserInfoAdapter(this, mUserInfos);

		mBtnRuHuHeCha.setOnClickListener(this);
		mBtnXinZengBaoSong.setOnClickListener(this);
		mBtnSheZhi.setOnClickListener(this);

		mBtnShuJuXiaZai.setOnClickListener(this);
		mBtnShuJuHeCha.setOnClickListener(this);
		mBtnShuJuShangBao.setOnClickListener(this);
		showSearchView();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {

			case R.id.shu_ju_he_cha:
                startRuHuHeChaMainActivity();
				break;
			case R.id.shu_ju_shang_bao:
				startShuJuShangBaoActivity();
				break;
			case R.id.shu_ju_xia_zai:
				startShuJuXiaZaiActivity();
				break;
			case R.id.ru_hu_he_cha_bottom:
				startRuHuHeChaActivity();
				break;
			case R.id.xin_zeng_bao_song_bottom:
				startXinZengBaoSongActivity();
				break;
			case R.id.she_zhi:
				startSheZhiActivity();
				break;
		}
	}

    private void startShuJuHeChaActivity() {
        Intent intent = new Intent();
        intent.setClass(this, DoorActivity.class);
        startActivityForResult(intent, REQUEST_CODE_ID_RECOGNIZE);
    }

    private void startShuJuShangBaoActivity() {
        Intent intent = new Intent();
        intent.setClass(this, Main_Shu_Ju_Shang_Bao_Activity.class);
        startActivityForResult(intent, REQUEST_CODE_FINGER_PRINT);
    }

    private void startShuJuXiaZaiActivity() {
        Intent intent = new Intent();
        intent.setClass(this, Main_Shu_Ju_Xia_Zai_Activity.class);
        startActivityForResult(intent, REQUEST_CODE_ID_INPUT);
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


    private void startRuHuHeChaMainActivity() {

        Intent intent = new Intent();
        intent.setClass(this, DoorHeChaActivity.class);
        startActivityForResult(intent, REQUEST_CODE_FINGER_PRINT);
    }
	private void startRuHuHeChaActivity() {
		// DO Nothing.
		return;
	}
	
	private void startXinZengBaoSongActivity() {
		Intent intent = new Intent();
		intent.setClass(this, Main_Lu_Ru_Activity.class);
		startActivityForResult(intent, REQUEST_CODE_FINGER_PRINT);
	}
	
	private void startSheZhiActivity() {
		Intent intent = new Intent();
		intent.setClass(this, Main_She_Zhi_Activity.class);
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
		state = STATE_SEARCH;
	}
	
	private void showResult() {
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
