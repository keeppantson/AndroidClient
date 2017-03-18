package com.zgmz.ls.ui;

import com.zgmz.ls.R;
import com.zgmz.ls.base.Const;
import com.zgmz.ls.base.Const.HousingStructure;
import com.zgmz.ls.base.SubActivity;
import com.zgmz.ls.db.DBHelper;
import com.zgmz.ls.model.FamilyProperty;
import com.zgmz.ls.model.SimpleUserInfo;
import com.zgmz.ls.utils.ToastUtils;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class FamilyPropertyActivity extends SubActivity {
	
	private TextView mItemNameCode2001;
	private RadioGroup mRadioGroupCode2001;
	private RadioButton mRadioBtn1Code2001;
	private RadioButton mRadioBtn2Code2001;
	
	private TextView mItemNameCode2002;
	private TextView mItemUnitCode2002;
	private EditText mItemInputCode2002;
	
	private TextView mItemNameCode2003;
//	private RadioGroup mRadioGroupCode2003;
	private RadioButton mRadioBtn1Code2003;
	private RadioButton mRadioBtn2Code2003;
	private RadioButton mRadioBtn3Code2003;
	private RadioButton mRadioBtn4Code2003;
	
	private TextView mItemNameCode2004;
	private TextView mItemUnitCode2004;
	private EditText mItemInputCode2004;
	
	private TextView mItemNameCode2005;
	private TextView mItemUnitCode2005;
	private EditText mItemInputCode2005;
	
	private TextView mItemNameCode2006;
	private TextView mItemUnitCode2006;
	private EditText mItemInputCode2006;
	
	private TextView mItemNameCode2007;
	private TextView mItemUnitCode2007;
	private EditText mItemInputCode2007;
	
	private TextView mItemNameCode2008;
	private TextView mItemUnitCode2008;
	private EditText mItemInputCode2008;
	
	private TextView mItemNameCode2009;
	private TextView mItemUnitCode2009;
	private EditText mItemInputCode2009;
	
	private TextView mItemNameCode2010;
	private TextView mItemUnitCode2010;
	private EditText mItemInputCode2010;
	
	private TextView mItemNameCode2011;
	private TextView mItemUnitCode2011;
	private EditText mItemInputCode2011;
	
	private TextView mItemNameCode2012;
	private TextView mItemUnitCode2012;
	private EditText mItemInputCode2012;
	
	private TextView mItemNameCode2013;
	private TextView mItemUnitCode2013;
	private EditText mItemInputCode2013;
	

	private int mUserId = 0;

	private FamilyProperty mFamilyProperty;
	
	@Override
	protected void onConfigrationTitleBar() {
		// TODO Auto-generated method stub
		super.onConfigrationTitleBar();
		setTitleBarTitleText(R.string.title_family_property);
		setTitleBarRightButtonText(R.string.save);
	}
	
	@Override
	public void onTitleBarRightButtonOnClick(View v) {
		// TODO Auto-generated method stub
		saveFamilySituation();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.family_property);
		onNewIntent(getIntent());
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		
		SimpleUserInfo info = (SimpleUserInfo) intent.getSerializableExtra(Const.KEY_USER_INFO);
		if(info != null) {
			mUserId = info.getUserId();
			loadFamilyProperty();
		}
		else {
			ToastUtils.showLongToast("用户不存在！");
			finish();
		}
	}
	
	private void loadFamilyProperty() {
		mFamilyProperty = DBHelper.getInstance().getFamilyProperty(mUserId);
		if(mFamilyProperty == null) {
			mFamilyProperty = new FamilyProperty();
			mFamilyProperty.setUserId(mUserId);
		}
		updateViews();
	}
	
	private void updateViews() {
		FamilyProperty property = mFamilyProperty;
		if(property == null) return;
		
		if(property.getCode2001() != Const.INT_INVALID) {
			if(property.getCode2001() == Const.INT_YES) {
				mRadioBtn1Code2001.setChecked(true);
			}
			else if(property.getCode2001() == Const.INT_NO) {
				mRadioBtn2Code2001.setChecked(true);
			}
		}
		
		if(property.getCode2002() != Const.INT_INVALID) {
			mItemInputCode2002.setText(String.valueOf(property.getCode2002()));
		}
		
		if(property.getCode2003() != Const.INT_INVALID) {
			if(property.getCode2003() == HousingStructure.WOOD) {
				mRadioBtn1Code2003.setChecked(true);
			}
			else if(property.getCode2003() == HousingStructure.BRICK_AND_WOOD) {
				mRadioBtn2Code2003.setChecked(true);
			}
			else if(property.getCode2003() == HousingStructure.BRICK_AND_CONCRETE) {
				mRadioBtn3Code2003.setChecked(true);
			}
			else if(property.getCode2003() == HousingStructure.THATCH) {
				mRadioBtn4Code2003.setChecked(true);
			}
		}
		
		if(property.getCode2004() != Const.INT_INVALID) {
			mItemInputCode2004.setText(String.valueOf(property.getCode2004()));
		}
		
		if(property.getCode2005() != Const.INT_INVALID) {
			mItemInputCode2005.setText(String.valueOf(property.getCode2005()));
		}
		
		if(property.getCode2006() != Const.INT_INVALID) {
			mItemInputCode2006.setText(String.valueOf(property.getCode2006()));
		}
		
		if(property.getCode2007() != Const.INT_INVALID) {
			mItemInputCode2007.setText(String.valueOf(property.getCode2007()));
		}
		
		if(property.getCode2008() != Const.INT_INVALID) {
			mItemInputCode2008.setText(String.valueOf(property.getCode2008()));
		}
		
		if(property.getCode2009() != Const.INT_INVALID) {
			mItemInputCode2009.setText(String.valueOf(property.getCode2009()));
		}
		
		if(property.getCode2010() != Const.INT_INVALID) {
			mItemInputCode2010.setText(String.valueOf(property.getCode2010()));
		}
		
		if(property.getCode2011() != Const.INT_INVALID) {
			mItemInputCode2011.setText(String.valueOf(property.getCode2011()));
		}
		
		if(property.getCode2012() != Const.INT_INVALID) {
			mItemInputCode2012.setText(String.valueOf(property.getCode2012()));
		}
		
		if(property.getCode2013() != Const.INT_INVALID) {
			mItemInputCode2013.setText(String.valueOf(property.getCode2013()));
		}
	}
	
	
	@Override
	protected void setupViews(View view) {
		// TODO Auto-generated method stub
		View item = view.findViewById(R.id.code2001);
		mItemNameCode2001 = (TextView)item.findViewById(R.id.name);
		mRadioGroupCode2001 = (RadioGroup)item.findViewById(R.id.radio_group);
		mRadioBtn1Code2001 = (RadioButton)item.findViewById(R.id.radio1);
		mRadioBtn2Code2001 = (RadioButton)item.findViewById(R.id.radio2);
		
		item = view.findViewById(R.id.code2002);
		mItemNameCode2002 = (TextView)item.findViewById(R.id.name);
		mItemUnitCode2002 = (TextView)item.findViewById(R.id.unit);
		mItemInputCode2002 = (EditText)item.findViewById(R.id.input);
		
		item = view.findViewById(R.id.code2003);
		mItemNameCode2003 = (TextView)item.findViewById(R.id.name);
//		mRadioGroupCode2003 = (RadioGroup)item.findViewById(R.id.radio_group);
		mRadioBtn1Code2003 = (RadioButton)item.findViewById(R.id.radio1);
		mRadioBtn2Code2003 = (RadioButton)item.findViewById(R.id.radio2);
		mRadioBtn3Code2003 = (RadioButton)item.findViewById(R.id.radio3);
		mRadioBtn4Code2003 = (RadioButton)item.findViewById(R.id.radio4);
		
		item = view.findViewById(R.id.code2004);
		mItemNameCode2004 = (TextView)item.findViewById(R.id.name);
		mItemUnitCode2004 = (TextView)item.findViewById(R.id.unit);
		mItemInputCode2004 = (EditText)item.findViewById(R.id.input);
		
		item = view.findViewById(R.id.code2005);
		mItemNameCode2005 = (TextView)item.findViewById(R.id.name);
		mItemUnitCode2005 = (TextView)item.findViewById(R.id.unit);
		mItemInputCode2005 = (EditText)item.findViewById(R.id.input);
		
		item = view.findViewById(R.id.code2006);
		mItemNameCode2006 = (TextView)item.findViewById(R.id.name);
		mItemUnitCode2006 = (TextView)item.findViewById(R.id.unit);
		mItemInputCode2006 = (EditText)item.findViewById(R.id.input);
		
		item = view.findViewById(R.id.code2007);
		mItemNameCode2007 = (TextView)item.findViewById(R.id.name);
		mItemUnitCode2007 = (TextView)item.findViewById(R.id.unit);
		mItemInputCode2007 = (EditText)item.findViewById(R.id.input);
		
		item = view.findViewById(R.id.code2008);
		mItemNameCode2008 = (TextView)item.findViewById(R.id.name);
		mItemUnitCode2008 = (TextView)item.findViewById(R.id.unit);
		mItemInputCode2008 = (EditText)item.findViewById(R.id.input);
		
		item = view.findViewById(R.id.code2009);
		mItemNameCode2009 = (TextView)item.findViewById(R.id.name);
		mItemUnitCode2009 = (TextView)item.findViewById(R.id.unit);
		mItemInputCode2009 = (EditText)item.findViewById(R.id.input);
		
		item = view.findViewById(R.id.code2010);
		mItemNameCode2010 = (TextView)item.findViewById(R.id.name);
		mItemUnitCode2010 = (TextView)item.findViewById(R.id.unit);
		mItemInputCode2010 = (EditText)item.findViewById(R.id.input);
		
		item = view.findViewById(R.id.code2011);
		mItemNameCode2011 = (TextView)item.findViewById(R.id.name);
		mItemUnitCode2011 = (TextView)item.findViewById(R.id.unit);
		mItemInputCode2011 = (EditText)item.findViewById(R.id.input);
		
		item = view.findViewById(R.id.code2012);
		mItemNameCode2012 = (TextView)item.findViewById(R.id.name);
		mItemUnitCode2012 = (TextView)item.findViewById(R.id.unit);
		mItemInputCode2012 = (EditText)item.findViewById(R.id.input);
		
		item = view.findViewById(R.id.code2013);
		mItemNameCode2013 = (TextView)item.findViewById(R.id.name);
		mItemUnitCode2013 = (TextView)item.findViewById(R.id.unit);
		mItemInputCode2013 = (EditText)item.findViewById(R.id.input);
		
		initViews();
	}
	
	private void initViews() {
		
		mRadioGroupCode2001.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if(checkedId == R.id.radio1) {
					mItemInputCode2002.setText(String.valueOf(0));
				}

			}
		});
		
		mRadioBtn1Code2003.setText(R.string.wood);
		mRadioBtn2Code2003.setText(R.string.brick_and_wood);
		mRadioBtn3Code2003.setText(R.string.brick_and_concrete);
		mRadioBtn4Code2003.setText(R.string.thatch);
		
		mItemNameCode2001.setText(R.string.name_code2001);
		mItemNameCode2002.setText(R.string.name_code2002);
		mItemNameCode2003.setText(R.string.name_code2003);
		mItemNameCode2004.setText(R.string.name_code2004);
		mItemNameCode2005.setText(R.string.name_code2005);
		mItemNameCode2006.setText(R.string.name_code2006);
		mItemNameCode2007.setText(R.string.name_code2007);
		mItemNameCode2008.setText(R.string.name_code2008);
		mItemNameCode2009.setText(R.string.name_code2009);
		mItemNameCode2010.setText(R.string.name_code2010);
		mItemNameCode2011.setText(R.string.name_code2011);
		mItemNameCode2012.setText(R.string.name_code2012);
		mItemNameCode2013.setText(R.string.name_code2013);

		
		mItemUnitCode2002.setText(R.string.unit_sq_m);
		mItemUnitCode2004.setText(R.string.unit_mu);
		mItemUnitCode2005.setText(R.string.unit_mu);
		mItemUnitCode2006.setText(R.string.unit_mu);
		mItemUnitCode2007.setText(R.string.unit_tai);
		mItemUnitCode2008.setText(R.string.unit_tai);
		mItemUnitCode2009.setText(R.string.unit_tai);
		mItemUnitCode2010.setText(R.string.unit_tai);
		mItemUnitCode2011.setText(R.string.unit_bu);
		mItemUnitCode2012.setText(R.string.unit_bu);
		mItemUnitCode2013.setText(R.string.unit_liang);
	}

	
	private void saveFamilySituation() {
		mFamilyProperty.setCompleted(checkInputCompleted());
		if(DBHelper.getInstance().insertOrUpdateFamilyProperty(mFamilyProperty)) {
			finish();
			ToastUtils.showLongToast("保存成功");
		}
		else {
			ToastUtils.showLongToast("保存失败");
		}
	}
	
	private boolean checkInputCompleted() {
		if(mRadioBtn1Code2001.isChecked()) {
			mFamilyProperty.setCode2001(Const.INT_YES);
		}
		else if(mRadioBtn2Code2001.isChecked()){
			mFamilyProperty.setCode2001(Const.INT_NO);
		}
		else {
			return false;
		}
		
		if(mRadioBtn1Code2003.isChecked()) {
			mFamilyProperty.setCode2003(HousingStructure.WOOD);
		}
		else if(mRadioBtn2Code2003.isChecked()){
			mFamilyProperty.setCode2003(HousingStructure.BRICK_AND_WOOD);
		}
		else if(mRadioBtn3Code2003.isChecked()){
			mFamilyProperty.setCode2003(HousingStructure.BRICK_AND_CONCRETE);
		}
		else if(mRadioBtn4Code2003.isChecked()){
			mFamilyProperty.setCode2003(HousingStructure.THATCH);
		}
		else {
			return false;
		}
		
		String count = null;
		
		count = mItemInputCode2002.getText().toString().trim();
		if(TextUtils.isEmpty(count)) return false;
		mFamilyProperty.setCode2002(Integer.valueOf(count));
		
		count = mItemInputCode2004.getText().toString().trim();
		if(TextUtils.isEmpty(count)) return false;
		mFamilyProperty.setCode2004(Integer.valueOf(count));
		
		count = mItemInputCode2005.getText().toString().trim();
		if(TextUtils.isEmpty(count)) return false;
		mFamilyProperty.setCode2005(Integer.valueOf(count));
		
		count = mItemInputCode2006.getText().toString().trim();
		if(TextUtils.isEmpty(count)) return false;
		mFamilyProperty.setCode2006(Integer.valueOf(count));
		
		count = mItemInputCode2007.getText().toString().trim();
		if(TextUtils.isEmpty(count)) return false;
		mFamilyProperty.setCode2007(Integer.valueOf(count));
		
		count = mItemInputCode2008.getText().toString().trim();
		if(TextUtils.isEmpty(count)) return false;
		mFamilyProperty.setCode2008(Integer.valueOf(count));
		
		count = mItemInputCode2009.getText().toString().trim();
		if(TextUtils.isEmpty(count)) return false;
		mFamilyProperty.setCode2009(Integer.valueOf(count));
		
		count = mItemInputCode2010.getText().toString().trim();
		if(TextUtils.isEmpty(count)) return false;
		mFamilyProperty.setCode2010(Integer.valueOf(count));
		
		count = mItemInputCode2011.getText().toString().trim();
		if(TextUtils.isEmpty(count)) return false;
		mFamilyProperty.setCode2011(Integer.valueOf(count));
		
		count = mItemInputCode2012.getText().toString().trim();
		if(TextUtils.isEmpty(count)) return false;
		mFamilyProperty.setCode2012(Integer.valueOf(count));
		
		count = mItemInputCode2013.getText().toString().trim();
		if(TextUtils.isEmpty(count)) return false;
		mFamilyProperty.setCode2013(Integer.valueOf(count));
		
		return true;
	}
	
}
