package com.zgmz.ls.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class FamilyPagerFragmentAdapter extends FragmentPagerAdapter {

	private ArrayList<Fragment> list;

	public FamilyPagerFragmentAdapter(FragmentManager fm) {
		super(fm);
	}



	public FamilyPagerFragmentAdapter(FragmentManager fm,
                                      ArrayList<Fragment> list) {
		super(fm);
		this.list = list;
	}



	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}
	
}
