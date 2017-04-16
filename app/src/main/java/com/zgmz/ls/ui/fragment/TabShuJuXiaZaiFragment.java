package com.zgmz.ls.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.zgmz.ls.R;
import com.zgmz.ls.base.Const;
import com.zgmz.ls.base.Const.InfoType;
import com.zgmz.ls.base.SharedDatas;
import com.zgmz.ls.db.DBHelper;
import com.zgmz.ls.db.TableTools;
import com.zgmz.ls.model.DownloadTask;
import com.zgmz.ls.model.IdCard;
import com.zgmz.ls.model.SimpleUserInfo;
import com.zgmz.ls.model.UserInfo;
import com.zgmz.ls.ui.AddDownLoadTaskActivity;
import com.zgmz.ls.ui.CheckFamilyInfoActivity;
import com.zgmz.ls.ui.IDRecoginzeActivity;
import com.zgmz.ls.ui.SearchActivity;
import com.zgmz.ls.ui.adapter.DownLoadTaskAdapter;
import com.zgmz.ls.ui.adapter.UserInfoAdapter;
import com.zgmz.ls.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class TabShuJuXiaZaiFragment extends TitleBarFragment implements OnClickListener{

	
	View mFrameUncheck;
	
	TextView mCheckEmpty;
	
	ListView mUncheckList;
	
	//ListView mCheckedList;
	
	DownLoadTaskAdapter mUncheckedAdapter;
	Button mBtnAddDownloadTask;
	
	List<DownloadTask> mUncheckedUserInfos = new ArrayList<DownloadTask>();

	
	private static final int STATE_UNCHECK = 1;
	
	int mState = STATE_UNCHECK;
	@Override
	protected void onConfigrationTitleBar() {
		// TODO Auto-generated method stub
		setTitleBarTitleText(R.string.shujuxiazai);
        setTitleBarLeftImageButtonImageResource(R.drawable.title_back);
	}
    @Override
    public void onTitleBarLeftButtonOnClick(View v) {
        getActivity().finish();
    }

	@Override
	public void onCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.tab_download);
	}
	
	
	@Override
	protected void setupViews(View root) {
		// TODO Auto-generated method stub
		mFrameUncheck = root.findViewById(R.id.frame_uncheck);
		
		mUncheckList = (ListView) root.findViewById(R.id.uncheck_list);

		mBtnAddDownloadTask = (Button)root.findViewById(R.id.add_download);
		mBtnAddDownloadTask.setOnClickListener(this);
		
		mUncheckedAdapter = new DownLoadTaskAdapter(getActivity(), mUncheckedUserInfos);
		mUncheckList.setAdapter(mUncheckedAdapter);
		
		mUncheckList.setOnItemClickListener(mUncheckedItemListener);
       
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
			case R.id.add_download:
                gotoAddDownLoadTask();
				break;
		}
	}

    public void gotoAddDownLoadTask() {
        Intent intent = new Intent();
        intent.setClass(getActivity(),AddDownLoadTaskActivity.class);
        startActivity(intent);
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
	
	private void startUserInfoActivity(DownloadTask userInfo) {
        ToastUtils.showLongToast("还未下载完，一旦下载结束，可以选择“数据核查”项目进入查看");
        return;
	}
	
	private void showTabUncheck() {
		mFrameUncheck.setVisibility(View.VISIBLE);
		mState = STATE_UNCHECK;
		updateUncheckedData();
	}
	
	private void updateUncheckedData() {
		List<DownloadTask> list = DBHelper.getInstance().getDownloadTask(20);
		if(list != null) {
			mUncheckedUserInfos.clear();
			mUncheckedUserInfos.addAll(list);
			mUncheckedAdapter.notifyDataSetChanged();
		}
	}
	
	private void ifneedUpdate() {
		if(SharedDatas.getInstance().isCheckUpdated()) {
			if(mState == STATE_UNCHECK) {
				updateUncheckedData();
			}
		}
	}
	

}
