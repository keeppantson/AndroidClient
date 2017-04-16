package com.zgmz.ls.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.zgmz.ls.R;
import com.zgmz.ls.db.DBHelper;
import com.zgmz.ls.model.FamilyBase;
import com.zgmz.ls.model.UserInfo;
import com.zgmz.ls.ui.CheckFamilyInfoActivity;
import com.zgmz.ls.utils.ToastUtils;

import java.util.List;

public class UserInfoMemberAdapter extends BaseAdapter {

	private Activity mContext;

	private LayoutInflater mInflater;

	private List<UserInfo> mItems;

	public UserInfoMemberAdapter(Activity context, List<UserInfo> list) {
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
			view = mInflater.inflate(R.layout.list_item_user_info_member, parent, false);
		}
		ImageView avatar = (ImageView)view.findViewById(R.id.avatar);
		TextView name = (TextView)view.findViewById(R.id.name);
		TextView idNumber = (TextView)view.findViewById(R.id.id_number);
		ImageButton deleteUser = (ImageButton)view.findViewById(R.id.delete);
        UserInfo info = mItems.get(position);
        final int index = position;
        deleteUser.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View arg2) {

                UserInfo info = mItems.get(index);
				FamilyBase family = DBHelper.getInstance().getFamilyWithTaskID(info.getCheck_task_id());
				if (family.getSqrsfzh().equals(info.getIdNumber())) {
					ToastUtils.showLongToast("不能删除户主信息");
					return;
				}
                DBHelper.getInstance().deleteHCMember(info.getIdNumber(), info.getCheck_task_id());
                CheckFamilyInfoActivity activity = (CheckFamilyInfoActivity)mContext;
                activity.updateUncheckedData();
            }
        });
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
