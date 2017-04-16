package com.zgmz.ls.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zgmz.ls.R;
import com.zgmz.ls.model.DownloadTask;
import com.zgmz.ls.model.UserInfo;

import java.util.List;

public class DownLoadTaskAdapter extends BaseAdapter {

	private Activity mContext;

	private LayoutInflater mInflater;

	private List<DownloadTask> mItems;

	public DownLoadTaskAdapter(Activity context, List<DownloadTask> list) {
		mContext = context;
		mInflater = mContext.getLayoutInflater();
		mItems = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(mItems != null) return mItems.size();
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if(mItems != null) return mItems.get(position);
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = null;
		if(convertView != null) {
			view = convertView;
		}
		else {
			view = mInflater.inflate(R.layout.list_item_user_info, parent, false);
		}
		ImageView avatar = (ImageView)view.findViewById(R.id.avatar);
		TextView name = (TextView)view.findViewById(R.id.name);
		TextView idNumber = (TextView)view.findViewById(R.id.id_number);
		DownloadTask info = mItems.get(position);
		if(info != null) {
			if(info.getQu_hua_ma() != null) {
                name.setText("地区批量下载");
                idNumber.setText(info.getQu_hua_ma());
            } else {
                name.setText("单家庭下载");
                idNumber.setText(info.getNow_work_target());
            }

		}
		return view;
	}

}
