
/**
 * Project Name:NiuNiuCommon
 * File Name:ListDialog.java
 * Package Name:com.niuniuparking.dialog
 * Date:2015年6月28日下午12:52:48
 * Copyright (c) 2015, buddyyan@126.com All Rights Reserved.
 *
 */
package com.zgmz.ls.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.zgmz.ls.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import static android.view.Window.FEATURE_NO_TITLE;

/**
 * ClassName: ListDialog 
 * Function: TODO 
 * Reason: TODO 
 * date: 2015年6月28日 下午12:52:48 
 *
 * @author buddyyan
 * @version 
 * @since JDK 1.6
 */
public class ListDialog extends AlertDialog implements OnItemClickListener, View.OnClickListener {
	
	
	public interface OnDialogItemClickListener {
		public void onItemClick(int id, Object data);
	}
	
	
	ListView mListView;
    Button confirm;
    Button cancel;
	
	OnDialogItemClickListener mOnDialogItemClickListener;
	
	ArrayList<ListItem> mItems = new ArrayList<ListItem>();
	
	ItemAdapter mAdapter;
	
	public void setOnDialogItemClickListener(OnDialogItemClickListener listener) {
		mOnDialogItemClickListener = listener;
	}

	/**
	 * Creates a new instance of ListDialog.
	 *
	 * @param context
	 */
	public ListDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		setContentView(R.layout.dialog_list);
	}

	@Override
	public void setupViews() {
		// TODO Auto-generated method stub
		mListView = (ListView)findViewById(R.id.list);
		mAdapter = new ItemAdapter();
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
        confirm = (Button)findViewById(R.id.dialog_confirm);
        cancel = (Button)findViewById(R.id.dialog_cancel);
        confirm.setOnClickListener(this);
        cancel.setOnClickListener(this);
	}

	int selected_pos = -1;
	public void setListItem(List<ListItem> items) {
		mItems.clear();
		if(items != null) {
			mItems.addAll(items);
		}
		mAdapter.notifyDataSetChanged();
	}


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch(v.getId()) {

            case R.id.dialog_confirm:
                if(mOnDialogItemClickListener != null) {
                    if (selected_pos >= 0 && selected_pos < mItems.size()) {
                        ListItem item = mItems.get(selected_pos);
                        mOnDialogItemClickListener.onItemClick(selected_pos, item.data);
                    }
                }
                break;
            case R.id.dialog_cancel:
                break;
        }

        dismiss();
    }
	
	/**
	 * TODO 
	 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
        selected_pos = position;
        mAdapter.setSelectedIndex(position);
        mAdapter.notifyDataSetChanged();
	}
	
	
	class ItemAdapter extends BaseAdapter {


		/**
		 * TODO 
		 * @see android.widget.Adapter#getCount()
		 */
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mItems.size();
		}

		/**
		 * TODO 
		 * @see android.widget.Adapter#getItem(int)
		 */
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mItems.get(position);
		}

		/**
		 * TODO 
		 * @see android.widget.Adapter#getItemId(int)
		 */
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
        int selectedIndex = -1;

        public void setSelectedIndex(int index){
            selectedIndex = index;
        }
		/**
		 * TODO 
		 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view  = null;
			if(convertView == null) {
				view = getActivity().getLayoutInflater().inflate(R.layout.dialog_list_item, parent, false);
			}
			else {
				view = convertView;
			}

			TextView name = (TextView)view.findViewById(R.id.name);
			
			ListItem item = mItems.get(position);
			if(item != null) {
				name.setText(item.name);
			}
            if(selectedIndex == position){
                name.setTextColor(0xFFFF0000);
                selected_pos = position;
            }
            else{
                name.setTextColor(0xFF000000);
            }
			
			return view;
		}
		
	}
	

}
