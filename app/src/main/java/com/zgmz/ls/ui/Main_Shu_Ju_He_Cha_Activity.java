package com.zgmz.ls.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.zgmz.ls.R;
import com.zgmz.ls.base.BottomTabActivity;
import com.zgmz.ls.base.Const;
import com.zgmz.ls.base.SubActivity;
import com.zgmz.ls.db.DBHelper;
import com.zgmz.ls.model.UserInfo;
import com.zgmz.ls.ui.adapter.FamilyPagerFragmentAdapter;
import com.zgmz.ls.ui.adapter.UserInfoAdapter;
import com.zgmz.ls.ui.adapter.UserInfoFamilyAdapter;
import com.zgmz.ls.ui.fragment.FamilyPagerFragment;
import com.zgmz.ls.ui.fragment.TabCheckFragment;
import com.zgmz.ls.ui.fragment.TabShuJuHeChaFragment;
import com.zgmz.ls.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import static com.zgmz.ls.ui.LoginActivity.PAGE_ITEM_NUM;

public class Main_Shu_Ju_He_Cha_Activity extends SubActivity implements View.OnClickListener {

	//ListView mUncheckList;

	int total_count;


	private ViewPager mViewPager;
	protected void onConfigrationTitleBar() {
		// TODO Auto-generated method stub
		super.onConfigrationTitleBar();
		setTitleBarTitleText("数据核查");
		setTitleBarRightImageButtonImageResource(R.drawable.search_btn_normal);
	}

	@Override
	public void onTitleBarRightButtonOnClick(View v) {
		start_sousuo();
		return;
	}

	public void start_sousuo() {
		Intent intent = new Intent();
		intent.setClass(this, SearchActivity.class);
		startActivity(intent);
	}

    private ArrayList<Fragment> FragmentList = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ru_hu_he_cha_dai_he_cha);
		setupViews();
        total_count = DBHelper.getInstance().getUncheckedFamilyCount();
        int page_number = 0;
		if (total_count == 0) {
			page_number = 1;
		} else {
			page_number = total_count % PAGE_ITEM_NUM == 0 ? (total_count / PAGE_ITEM_NUM) :
					(total_count / PAGE_ITEM_NUM) + 1;
		}
        FragmentList = new ArrayList<Fragment>();
        for (int i = 0; i < page_number; i++) {
            Fragment zh = FamilyPagerFragment.newInstance(i, page_number);
            FragmentList.add(zh);
        }

        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
        mViewPager.setAdapter(new FamilyPagerFragmentAdapter(getSupportFragmentManager(), FragmentList));
        mViewPager.setCurrentItem(0);
        mViewPager.setOnPageChangeListener(new MyViewPagerChangedListener());
	}
    private int currPosition = 0;
    class MyViewPagerChangedListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageSelected(int arg0) {
            Log.d("onchanged", "onchanged " + arg0);
            TranslateAnimation ta =  new TranslateAnimation(0, 0, 0, 0);


            currPosition = arg0;

            ta.setDuration(300);
            ta.setFillAfter(true);
        }

    }
	protected void setupViews() {
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
	}



	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	}


	private void ifneedUpdate() {

	}


}
