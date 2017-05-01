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
			view = mInflater.inflate(R.layout.list_item_user_info_download, parent, false);
		}
		TextView name = (TextView)view.findViewById(R.id.name);
		TextView idNumber = (TextView)view.findViewById(R.id.number);
		TextView ratio = (TextView)view.findViewById(R.id.ratio);
		DownloadTask info = mItems.get(position);
		if(info != null) {
			if(info.getQu_hua_ma() != null) {
                name.setText("区划码：" + info.getQu_hua_ma());
				int total = Integer.valueOf(info.getTotal_number());
				int now = Integer.valueOf(info.getPage_id());
				if (total == 0) {
					idNumber.setText("下载进度：" + "0%" );
				} else {
					idNumber.setText("下载进度：" + (now * 100 / total) +"%" );
				}
            } else {
                name.setText("身份证号码：" + info.getNow_work_target());
            }

		}
		return view;
	}

}
