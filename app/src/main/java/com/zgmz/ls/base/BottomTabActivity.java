package com.zgmz.ls.base;

import java.util.ArrayList;
import java.util.List;

import com.zgmz.ls.R;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

abstract public class BottomTabActivity extends Activity implements OnClickListener{
	
	
	List<Tab> mTabs = new ArrayList<Tab>();
	
	LinearLayout mTabWidget;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bottom_tabs_activity);
		setupViews();
		initTabs();
	}

	abstract protected void initTabs();

	private void setupViews() {
		mTabWidget = (LinearLayout)findViewById(R.id.tab_widget);
	}

	
	public View getTabWidget() {
		return mTabWidget;
	}
	
	public void addTab(int stringId, int iconId, Fragment content) {
		addTab(getString(stringId), getResources().getDrawable(iconId), content);
	}
	
	public void addTab(String text, Drawable icon, Fragment content) {
		if(content == null) return;
		Tab tab = createTab(text, icon);
		tab.setTabContent(content);
		mTabs.add(tab);
		addTabToTabWidget(tab);
	}
	
	
	private void addTabToTabWidget(Tab tab) {
		mTabWidget.addView(tab.getTabView());
	}
	
	public Tab getTab(int index) {
		return mTabs.get(index);
	}
	
	
	public int getTabCount() {
		return mTabs.size();
	}
	
	
	public void setSelected(int index) {
		if(index < 0 || index>=getTabCount()) return;
		
		Tab selectTab = getTab(index);
		for(Tab tab : mTabs) {
			if(selectTab == tab) {
				tab.setSelected(true);
			}
			else {
				tab.setSelected(false);
			}
		}
	}

	private Tab createTab(String text, Drawable icon) {
		Tab tab = new Tab(getLayoutInflater().inflate(R.layout.tab, mTabWidget, false)); 
		tab.setIndex(getTabCount());
		tab.setText(text);
		tab.setIcon(icon);
		tab.setOnClickListener(this);
		return tab;
	}
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		for(Tab tab : mTabs) {
			if(tab.getTabView() == v) {
				onClick(tab);
			}
		}
	}
	
	private void onClick(Tab tab) {
		showTab(tab);
	}
	
	public void showTab(int index) {
		if(index>=0 && index <getTabCount()) {
			showTab(mTabs.get(index));
		}
	}
	
	
	private void showTab(Tab tab) {
		setSelected(tab.getIndex());
		for(Tab t : mTabs) {
			if(t == tab) {
				t.showTabContent();
			}
			else {
				t.hideTabContent();
			}
		}
	}
	
	
	class Tab{
		private View tabView;
		
		private int index = -1;
		
		private ImageView imageView;
		
		private TextView textView;
		
		private Fragment tabContent;
		
		private boolean bAdded = false;
		
		public void setTabContent(Fragment content) {
			this.tabContent = content;
			
		}
		
		public void hideTabContent() {
			if(bAdded) {
				FragmentTransaction transaction = getFragmentManager().beginTransaction();
				transaction.hide(tabContent);
				transaction.commit();
			}
		}
		
		
		public void showTabContent() {
			FragmentTransaction transaction = getFragmentManager().beginTransaction();
			if(!bAdded) {
				transaction.add(R.id.tab_content, tabContent);
				bAdded = true;
			}
			else {
				transaction.show(tabContent);
			}
			
			transaction.commit();
		}
		
		public Fragment getTabContent() {
			return this.getTabContent();
		}
		
		public View getTabView() {
			return tabView;
		}
		
		public Tab(View tab) {
			tabView = tab;
			imageView = (ImageView) tab.findViewById(R.id.icon);
			textView = (TextView)tab.findViewById(R.id.text);
		}
		
		public void setIndex(int index) {
			this.index = index;
		}
		
		public int getIndex() {
			return index;
		}
		
		public void setSelected(boolean selected) {
			tabView.setSelected(selected);
		}
		
		@SuppressWarnings("deprecation")
		public void setTabBackground(Drawable drawable) {
			if(Build.VERSION.SDK_INT < VERSION_CODES.JELLY_BEAN) {
				tabView.setBackgroundDrawable(drawable);
			}
			else {
				tabView.setBackground(drawable);
			}
		}
		
		public void setText(String text) {
			textView.setText(text);
		}
		
		public void setIcon(Drawable drawable) {
			imageView.setImageDrawable(drawable);
		}
		
		public void setOnClickListener(OnClickListener listener) {
			tabView.setOnClickListener(listener);
		}
		
	}
	
}
