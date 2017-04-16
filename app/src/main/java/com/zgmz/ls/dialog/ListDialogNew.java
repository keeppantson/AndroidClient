
/**
 * Project Name:NiuNiuCommon
 * File Name:ListDialog.java
 * Package Name:com.niuniuparking.dialog
 * Date:2015年6月28日下午12:52:48
 * Copyright (c) 2015, buddyyan@126.com All Rights Reserved.
 *
 */
package com.zgmz.ls.dialog;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zgmz.ls.R;

import java.util.ArrayList;
import java.util.List;

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
public class ListDialogNew extends AlertDialog implements OnItemClickListener{


	public interface OnDialogItemClickListener {
		public void onItemClick(int id, Object data);
	}


	ListView mListView;

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
	public ListDialogNew(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		setContentView(R.layout.dialog_list_new);
	}

	/**
	 * TODO
	 * @see com.niuniuparking.dialog.AlertDialog#setupViews()
	 */
	@Override
	public void setupViews() {
		// TODO Auto-generated method stub
		mListView = (ListView)findViewById(R.id.list);
		mAdapter = new ItemAdapter();
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
	}


	public void setListItem(List<ListItem> items) {
		mItems.clear();
		if(items != null) {
			mItems.addAll(items);
		}
		mAdapter.notifyDataSetChanged();
	}

	/**
	 * TODO
	 * @see OnItemClickListener#onItemClick(AdapterView, View, int, long)
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		if(mOnDialogItemClickListener != null) {
			ListItem item = mItems.get(position);
			mOnDialogItemClickListener.onItemClick(item.id, item.data);
		}
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

		/**
		 * TODO
		 * @see android.widget.Adapter#getView(int, View, ViewGroup)
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view  = null;
			if(convertView == null) {
				view = getActivity().getLayoutInflater().inflate(R.layout.dialog_list_item_new, parent, false);
			}
			else {
				view = convertView;
			}
			
			ImageView icon = (ImageView)view.findViewById(R.id.icon);
			TextView name = (TextView)view.findViewById(R.id.name);
			
			ListItem item = mItems.get(position);
			if(item != null) {
				if(item.icon > 0) {
					icon.setImageResource(item.icon);
				}
				else {
					icon.setImageDrawable(null);
				}
				name.setText(item.name);
			}
			
			return view;
		}
		
	}
	

}
