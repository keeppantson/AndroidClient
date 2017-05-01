package com.zgmz.ls.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.zgmz.ls.R;
import com.zgmz.ls.base.Const;
import com.zgmz.ls.db.DBHelper;
import com.zgmz.ls.model.UserInfo;
import com.zgmz.ls.ui.CheckFamilyInfoActivity;
import com.zgmz.ls.ui.adapter.UserInfoFamilyAdapter;
import com.zgmz.ls.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import static com.zgmz.ls.ui.LoginActivity.PAGE_ITEM_NUM;

public class FamilyPagerFragment extends Fragment implements View.OnClickListener {

    private int page_num = 0;
    private int total_num = 0;
    ImageView imageView;

    public static FamilyPagerFragment newInstance(int s, int total){
        FamilyPagerFragment myFragment = new FamilyPagerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("key", s);
        bundle.putInt("total", total);
        myFragment.setArguments(bundle);
        return myFragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        page_num = bundle != null? bundle.getInt("key") : 0;
        total_num = bundle != null? bundle.getInt("total") : 0;
        super.onCreate(savedInstanceState);
    }
    ImageButton he_cha;
    ImageButton shan_chu;
    CheckBox quanbu;

    List<UserInfo> mSelectedUserInfos = new ArrayList<UserInfo>();
    private ListView listView;

    private UserInfoFamilyAdapter mUncheckedAdapter;

    private List<UserInfo> mUncheckedUserInfos ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.page_list_view,container,false);
        listView = (ListView) view.findViewById(R.id.mylistview);

        imageView = (ImageView)view.findViewById(R.id.imageView);
        mUncheckedAdapter = new UserInfoFamilyAdapter(getActivity(), mUncheckedUserInfos);
        quanbu = (CheckBox)view.findViewById(R.id.quan_bu);
        he_cha  = (ImageButton)view.findViewById(R.id.he_cha);
        shan_chu  = (ImageButton)view.findViewById(R.id.shan_chu);

        quanbu.setOnClickListener(this);
        he_cha.setOnClickListener(this);
        shan_chu.setOnClickListener(this);

        mUncheckedUserInfos = getdata();
        mUncheckedAdapter = new UserInfoFamilyAdapter(getActivity(), mUncheckedUserInfos);
        listView.setOnItemClickListener(mUncheckedItemListener);
        listView.setAdapter(mUncheckedAdapter);

        mSelectedUserInfos.clear();
        mUncheckedAdapter.setSelectedIndex(-1);
        mUncheckedAdapter.notifyDataSetChanged();

        if (page_num == 0) {
            imageView.setImageResource(R.drawable.right);
        } else if (page_num == total_num - 1){
            imageView.setImageResource(R.drawable.left);
        } else {
            imageView.setImageResource(R.drawable.middle);
        }
        return view;
    }

    private List<UserInfo> getdata(){
        int size = 0;
        if (mUncheckedUserInfos != null) {
            size = mUncheckedUserInfos.size();

        }
        if (mUncheckedUserInfos == null) {
            mUncheckedUserInfos = new ArrayList<UserInfo>();
        }
        mUncheckedUserInfos.clear();
        mUncheckedUserInfos = DBHelper.getInstance().getRangeUncheckedFamily(page_num * PAGE_ITEM_NUM, PAGE_ITEM_NUM, true);
        return mUncheckedUserInfos;
    }
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch(v.getId()) {
            case R.id.quan_bu:
                quan_bu_xuan_ze();
                break;
            case R.id.he_cha:
                he_cha();
                break;
            case R.id.shan_chu:
                shan_chu();
                break;
        }
    }
    private void quan_bu_xuan_ze() {

        List<UserInfo> list = DBHelper.getInstance().getRangeUncheckedFamily(page_num*PAGE_ITEM_NUM, PAGE_ITEM_NUM, true);
        if(list != null) {
            mUncheckedUserInfos.clear();
            mUncheckedUserInfos.addAll(list);
        }
        if (quanbu.isChecked()) {
            mSelectedUserInfos.clear();
            for(int i = 0; i < mUncheckedUserInfos.size(); i++) {
                mSelectedUserInfos.add(mUncheckedUserInfos.get(i));
            }
            mUncheckedAdapter.setSelectedIndex(-2);
            mUncheckedAdapter.notifyDataSetChanged();
        } else {
            mSelectedUserInfos.clear();
            mUncheckedAdapter.setSelectedIndex(-3);
            mUncheckedAdapter.notifyDataSetChanged();
        }
    }

    private void he_cha() {
        startHeChaActivity();
    }

    private void shan_chu() {
        for (int i = 0; i < mSelectedUserInfos.size(); i++) {
            DBHelper.getInstance().deleteAllTasks(mSelectedUserInfos.get(i).getCheck_task_id());
        }
        List<UserInfo> list = DBHelper.getInstance().getRangeUncheckedFamily(page_num*PAGE_ITEM_NUM, PAGE_ITEM_NUM, true);
        if(list != null) {
            mUncheckedUserInfos.clear();
            mUncheckedUserInfos.addAll(list);
        }
        mSelectedUserInfos.clear();
        mUncheckedAdapter.setSelectedIndex(-1);
        mUncheckedAdapter.notifyDataSetChanged();
    }

    private void startHeChaActivity() {
        if (mSelectedUserInfos.size() > 1  || mSelectedUserInfos.size() == 0) {
            ToastUtils.showShortToast("请确认仅选择一项做核查");
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(Const.KEY_USER_INFO, mSelectedUserInfos.get(0).toSimpleUserInfo());
        intent.putExtra(Const.KEY_TYPE, Const.InfoType.CHECK);
        intent.setClass(getActivity(),CheckFamilyInfoActivity.class);
        startActivity(intent);
    }


    AdapterView.OnItemClickListener mUncheckedItemListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // TODO Auto-generated method stub
            if(mUncheckedUserInfos.size()>position) {
                //startUserInfoActivity(mUncheckedUserInfos.get(position));
                boolean need_insert = true;
                for (int i = 0; i < mSelectedUserInfos.size(); i++) {
                    if (mUncheckedUserInfos.get(position).getCheck_task_id().equals(mSelectedUserInfos.get(i).getCheck_task_id())) {
                        need_insert = false;
                        mSelectedUserInfos.remove(i);
                        break;
                    }
                }

                if (need_insert) {
                    mSelectedUserInfos.add(mUncheckedUserInfos.get(position));
                }

            }

            mUncheckedAdapter.setSelectedIndex(position);
            mUncheckedAdapter.notifyDataSetChanged();
        }
    };

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (mUncheckedUserInfos == null) {
                return;
            }
            List<UserInfo> list = DBHelper.getInstance().getRangeUncheckedFamily(page_num*PAGE_ITEM_NUM, PAGE_ITEM_NUM, true);
            if(list != null) {
                mUncheckedUserInfos.clear();
                mUncheckedUserInfos.addAll(list);
            }
            mSelectedUserInfos.clear();
            mUncheckedAdapter.setSelectedIndex(-1);
            mUncheckedAdapter.notifyDataSetChanged();
        }
    }
}

