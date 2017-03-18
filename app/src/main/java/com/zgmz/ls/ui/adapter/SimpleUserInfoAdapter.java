package com.zgmz.ls.ui.adapter;

import java.util.List;

import com.zgmz.ls.R;
import com.zgmz.ls.model.SimpleUserInfo;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SimpleUserInfoAdapter extends BaseAdapter {
	
	private Activity mContext;
	
	private LayoutInflater mInflater;
	
	private List<SimpleUserInfo> mItems;
	
	public SimpleUserInfoAdapter(Activity context, List<SimpleUserInfo> list) {
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
//		ImageView avatar = (ImageView)view.findViewById(R.id.avatar);
		TextView name = (TextView)view.findViewById(R.id.name);
		TextView idNumber = (TextView)view.findViewById(R.id.id_number);
		SimpleUserInfo info = mItems.get(position);
		if(info != null) {
			name.setText(info.getName());
			idNumber.setText(info.getIdNumber());
		}
		return view;
	}

}
