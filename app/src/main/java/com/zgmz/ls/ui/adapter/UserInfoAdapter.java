package com.zgmz.ls.ui.adapter;

import java.util.List;

import com.zgmz.ls.R;
import com.zgmz.ls.model.UserInfo;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class UserInfoAdapter extends BaseAdapter {
	
	private Activity mContext;
	
	private LayoutInflater mInflater;
	
	private List<UserInfo> mItems;
	
	public UserInfoAdapter(Activity context, List<UserInfo> list) {
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

    public int getSelectedIndex(){
        return selectedIndex;
    }

    public void setSelectedIndex(int index){
        selectedIndex = index;
    }
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	int selectedIndex;
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
		UserInfo info = mItems.get(position);
		if(info != null) {
			name.setText(info.getName());
			idNumber.setText(info.getIdNumber());
			if(info.getAvatar() != null) {
				avatar.setImageBitmap(info.getAvatar());
			}
		}
		if(selectedIndex == position){
			name.setTextColor(0xFFFF0000);
		}

        if(selectedIndex == -2){
            name.setTextColor(0xFFFF0000);
        }
        if(selectedIndex == -3){
            name.setTextColor(0xFF000000);
        }
		return view;
	}

}
