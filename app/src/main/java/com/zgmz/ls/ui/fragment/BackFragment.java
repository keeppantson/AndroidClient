package com.zgmz.ls.ui.fragment;

import java.lang.reflect.Field;

import com.zgmz.ls.R;

import android.support.v4.app.Fragment;
import android.view.View;

public class BackFragment extends TitleBarFragment {

	@Override
	protected void onConfigrationTitleBar() {
		// TODO Auto-generated method stub
		setTitleBarLeftButtonBack();
	}
	
	protected final void setTitleBarLeftButtonBack() {
		setTitleBarLeftImageButtonImageResource(R.drawable.title_back);
	}

	@Override
	public void onTitleBarLeftButtonOnClick(View v) {
		onBackPressed();
	}
	
	/**
	 * TODO 
	 * @see com.niuniuparking.ui.fragment.BaseFragment#onBackPressed()
	 */
	@Override
	protected boolean onBackPressed() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		
		try {  
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");  
            childFragmentManager.setAccessible(true);  
            childFragmentManager.set(this, null);  
  
        } catch (NoSuchFieldException e) {  
            throw new RuntimeException(e);  
        } catch (IllegalAccessException e) {  
            throw new RuntimeException(e);  
        }  
	}
	
}
