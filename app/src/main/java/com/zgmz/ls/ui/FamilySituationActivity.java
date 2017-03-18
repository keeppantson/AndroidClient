package com.zgmz.ls.ui;

import com.zgmz.ls.R;
import com.zgmz.ls.base.Const;
import com.zgmz.ls.base.SubActivity;
import com.zgmz.ls.db.DBHelper;
import com.zgmz.ls.model.FamilySituation;
import com.zgmz.ls.model.SimpleUserInfo;
import com.zgmz.ls.utils.ToastUtils;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class FamilySituationActivity extends SubActivity {
	
	private TextView mItemNameCode1001;
//	private RadioGroup mRadioGroupCode1001;
	private RadioButton mRadioBtn1Code1001;
	private RadioButton mRadioBtn2Code1001;
	
	private TextView mItemNameCode1002;
//	private RadioGroup mRadioGroupCode1002;
	private RadioButton mRadioBtn1Code1002;
	private RadioButton mRadioBtn2Code1002;
	
	private TextView mItemNameCode1003;
//	private RadioGroup mRadioGroupCode1003;
	private RadioButton mRadioBtn1Code1003;
	private RadioButton mRadioBtn2Code1003;
	
	private TextView mItemNameCode1004;
	private TextView mItemUnitCode1004;
	private EditText mItemInputCode1004;
	
	private TextView mItemNameCode1005;
	private TextView mItemUnitCode1005;
	private EditText mItemInputCode1005;
	
	private TextView mItemNameCode1006;
	private TextView mItemUnitCode1006;
	private EditText mItemInputCode1006;
	
	private TextView mItemNameCode1007;
	private TextView mItemUnitCode1007;
	private EditText mItemInputCode1007;
	
	private TextView mItemNameCode1008;
	private TextView mItemUnitCode1008;
	private EditText mItemInputCode1008;
	
	private TextView mItemNameCode1009;
	private TextView mItemUnitCode1009;
	private EditText mItemInputCode1009;
	
	private TextView mItemNameCode1010;
	private TextView mItemUnitCode1010;
	private EditText mItemInputCode1010;
	
	private TextView mItemNameCode1011;
	private TextView mItemUnitCode1011;
	private EditText mItemInputCode1011;
	
	private TextView mItemNameCode1012;
	private TextView mItemUnitCode1012;
	private EditText mItemInputCode1012;
	
	private TextView mItemNameCode1013;
	private TextView mItemUnitCode1013;
	private EditText mItemInputCode1013;
	
	private TextView mItemNameCode1014;
	private TextView mItemUnitCode1014;
	private EditText mItemInputCode1014;
	
	private TextView mItemNameCode1015;
	private TextView mItemUnitCode1015;
	private EditText mItemInputCode1015;
	
	private TextView mItemNameCode1016;
	private TextView mItemUnitCode1016;
	private EditText mItemInputCode1016;
	
	private TextView mItemNameCode1017;
	private TextView mItemUnitCode1017;
	private EditText mItemInputCode1017;
	
	
	private int mUserId = 0;

	private FamilySituation mFamilySituation;
	
	@Override
	protected void onConfigrationTitleBar() {
		// TODO Auto-generated method stub
		super.onConfigrationTitleBar();
		setTitleBarTitleText(R.string.title_family_situation);
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
		setContentView(R.layout.family_situation);
		onNewIntent(getIntent());
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		
		SimpleUserInfo info = (SimpleUserInfo) intent.getSerializableExtra(Const.KEY_USER_INFO);
		if(info != null) {
			mUserId = info.getUserId();
			loadFamilySituation();
		}
		else {
			ToastUtils.showLongToast("用户不存在！");
			finish();
		}
	}
	
	private void loadFamilySituation() {
		mFamilySituation = DBHelper.getInstance().getFamilySituation(mUserId);
		if(mFamilySituation == null) {
			mFamilySituation = new FamilySituation();
			mFamilySituation.setUserId(mUserId);
		}
		updateViews();
	}
	
	private void updateViews() {
		FamilySituation situation = mFamilySituation;
		if(situation == null) return;
		
		if(situation.getCode1001() != Const.INT_INVALID) {
			if(situation.getCode1001() == Const.INT_YES) {
				mRadioBtn1Code1001.setChecked(true);
			}
			else if(situation.getCode1001() == Const.INT_NO) {
				mRadioBtn2Code1001.setChecked(true);
			}
		}
		
		if(situation.getCode1002() != Const.INT_INVALID) {
			if(situation.getCode1002() == Const.INT_YES) {
				mRadioBtn1Code1002.setChecked(true);
			}
			else if(situation.getCode1002() == Const.INT_NO) {
				mRadioBtn2Code1002.setChecked(true);
			}
		}
		
		if(situation.getCode1003() != Const.INT_INVALID) {
			if(situation.getCode1003() == Const.INT_YES) {
				mRadioBtn1Code1003.setChecked(true);
			}
			else if(situation.getCode1003() == Const.INT_NO) {
				mRadioBtn2Code1003.setChecked(true);
			}
		}
		
		if(situation.getCode1004() != Const.INT_INVALID) {
			mItemInputCode1004.setText(String.valueOf(situation.getCode1004()));
		}
		
		if(situation.getCode1005() != Const.INT_INVALID) {
			mItemInputCode1005.setText(String.valueOf(situation.getCode1005()));
		}
		
		if(situation.getCode1006() != Const.INT_INVALID) {
			mItemInputCode1006.setText(String.valueOf(situation.getCode1006()));
		}
		
		if(situation.getCode1007() != Const.INT_INVALID) {
			mItemInputCode1007.setText(String.valueOf(situation.getCode1007()));
		}
		
		if(situation.getCode1008() != Const.INT_INVALID) {
			mItemInputCode1008.setText(String.valueOf(situation.getCode1008()));
		}
		
		if(situation.getCode1009() != Const.INT_INVALID) {
			mItemInputCode1009.setText(String.valueOf(situation.getCode1009()));
		}
		
		if(situation.getCode1010() != Const.INT_INVALID) {
			mItemInputCode1010.setText(String.valueOf(situation.getCode1010()));
		}
		
		if(situation.getCode1011() != Const.INT_INVALID) {
			mItemInputCode1011.setText(String.valueOf(situation.getCode1011()));
		}
		
		if(situation.getCode1012() != Const.INT_INVALID) {
			mItemInputCode1012.setText(String.valueOf(situation.getCode1012()));
		}
		
		if(situation.getCode1013() != Const.INT_INVALID) {
			mItemInputCode1013.setText(String.valueOf(situation.getCode1013()));
		}
		
		if(situation.getCode1014() != Const.INT_INVALID) {
			mItemInputCode1014.setText(String.valueOf(situation.getCode1014()));
		}
		
		if(situation.getCode1015() != Const.INT_INVALID) {
			mItemInputCode1015.setText(String.valueOf(situation.getCode1015()));
		}
		
		if(situation.getCode1016() != Const.INT_INVALID) {
			mItemInputCode1016.setText(String.valueOf(situation.getCode1016()));
		}
		
		if(situation.getCode1017() != Const.INT_INVALID) {
			mItemInputCode1017.setText(String.valueOf(situation.getCode1017()));
		}
	}
	
	@Override
	protected void setupViews(View view) {
		// TODO Auto-generated method stub
		View item = view.findViewById(R.id.code1001);
		mItemNameCode1001 = (TextView)item.findViewById(R.id.name);
//		mRadioGroupCode1001 = (RadioGroup)item.findViewById(R.id.radio_group);
		mRadioBtn1Code1001 = (RadioButton)item.findViewById(R.id.radio1);
		mRadioBtn2Code1001 = (RadioButton)item.findViewById(R.id.radio2);
		
		item = view.findViewById(R.id.code1002);
		mItemNameCode1002 = (TextView)item.findViewById(R.id.name);
//		mRadioGroupCode1002 = (RadioGroup)item.findViewById(R.id.radio_group);
		mRadioBtn1Code1002 = (RadioButton)item.findViewById(R.id.radio1);
		mRadioBtn2Code1002 = (RadioButton)item.findViewById(R.id.radio2);
		
		item = view.findViewById(R.id.code1003);
		mItemNameCode1003 = (TextView)item.findViewById(R.id.name);
//		mRadioGroupCode1003 = (RadioGroup)item.findViewById(R.id.radio_group);
		mRadioBtn1Code1003 = (RadioButton)item.findViewById(R.id.radio1);
		mRadioBtn2Code1003 = (RadioButton)item.findViewById(R.id.radio2);
		
		item = view.findViewById(R.id.code1004);
		mItemNameCode1004 = (TextView)item.findViewById(R.id.name);
		mItemUnitCode1004 = (TextView)item.findViewById(R.id.unit);
		mItemInputCode1004 = (EditText)item.findViewById(R.id.input);
		
		item = view.findViewById(R.id.code1005);
		mItemNameCode1005 = (TextView)item.findViewById(R.id.name);
		mItemUnitCode1005 = (TextView)item.findViewById(R.id.unit);
		mItemInputCode1005 = (EditText)item.findViewById(R.id.input);
		
		item = view.findViewById(R.id.code1006);
		mItemNameCode1006 = (TextView)item.findViewById(R.id.name);
		mItemUnitCode1006 = (TextView)item.findViewById(R.id.unit);
		mItemInputCode1006 = (EditText)item.findViewById(R.id.input);
		
		item = view.findViewById(R.id.code1007);
		mItemNameCode1007 = (TextView)item.findViewById(R.id.name);
		mItemUnitCode1007 = (TextView)item.findViewById(R.id.unit);
		mItemInputCode1007 = (EditText)item.findViewById(R.id.input);
		
		item = view.findViewById(R.id.code1008);
		mItemNameCode1008 = (TextView)item.findViewById(R.id.name);
		mItemUnitCode1008 = (TextView)item.findViewById(R.id.unit);
		mItemInputCode1008 = (EditText)item.findViewById(R.id.input);
		
		item = view.findViewById(R.id.code1009);
		mItemNameCode1009 = (TextView)item.findViewById(R.id.name);
		mItemUnitCode1009 = (TextView)item.findViewById(R.id.unit);
		mItemInputCode1009 = (EditText)item.findViewById(R.id.input);
		
		item = view.findViewById(R.id.code1010);
		mItemNameCode1010 = (TextView)item.findViewById(R.id.name);
		mItemUnitCode1010 = (TextView)item.findViewById(R.id.unit);
		mItemInputCode1010 = (EditText)item.findViewById(R.id.input);
		
		item = view.findViewById(R.id.code1011);
		mItemNameCode1011 = (TextView)item.findViewById(R.id.name);
		mItemUnitCode1011 = (TextView)item.findViewById(R.id.unit);
		mItemInputCode1011 = (EditText)item.findViewById(R.id.input);
		
		item = view.findViewById(R.id.code1012);
		mItemNameCode1012 = (TextView)item.findViewById(R.id.name);
		mItemUnitCode1012 = (TextView)item.findViewById(R.id.unit);
		mItemInputCode1012 = (EditText)item.findViewById(R.id.input);
		
		item = view.findViewById(R.id.code1013);
		mItemNameCode1013 = (TextView)item.findViewById(R.id.name);
		mItemUnitCode1013 = (TextView)item.findViewById(R.id.unit);
		mItemInputCode1013 = (EditText)item.findViewById(R.id.input);
		
		item = view.findViewById(R.id.code1014);
		mItemNameCode1014 = (TextView)item.findViewById(R.id.name);
		mItemUnitCode1014 = (TextView)item.findViewById(R.id.unit);
		mItemInputCode1014 = (EditText)item.findViewById(R.id.input);
		
		item = view.findViewById(R.id.code1015);
		mItemNameCode1015 = (TextView)item.findViewById(R.id.name);
		mItemUnitCode1015 = (TextView)item.findViewById(R.id.unit);
		mItemInputCode1015 = (EditText)item.findViewById(R.id.input);
		
		item = view.findViewById(R.id.code1016);
		mItemNameCode1016 = (TextView)item.findViewById(R.id.name);
		mItemUnitCode1016 = (TextView)item.findViewById(R.id.unit);
		mItemInputCode1016 = (EditText)item.findViewById(R.id.input);
		
		item = view.findViewById(R.id.code1017);
		mItemNameCode1017 = (TextView)item.findViewById(R.id.name);
		mItemUnitCode1017 = (TextView)item.findViewById(R.id.unit);
		mItemInputCode1017 = (EditText)item.findViewById(R.id.input);
		
		initViews();
	}
	
	private void initViews() {
		mItemNameCode1001.setText(R.string.name_code1001);
		mItemNameCode1002.setText(R.string.name_code1002);
		mItemNameCode1003.setText(R.string.name_code1003);
		mItemNameCode1004.setText(R.string.name_code1004);
		mItemNameCode1005.setText(R.string.name_code1005);
		mItemNameCode1006.setText(R.string.name_code1006);
		mItemNameCode1007.setText(R.string.name_code1007);
		mItemNameCode1008.setText(R.string.name_code1008);
		mItemNameCode1009.setText(R.string.name_code1009);
		mItemNameCode1010.setText(R.string.name_code1010);
		mItemNameCode1011.setText(R.string.name_code1011);
		mItemNameCode1012.setText(R.string.name_code1012);
		mItemNameCode1013.setText(R.string.name_code1013);
		mItemNameCode1014.setText(R.string.name_code1014);
		mItemNameCode1015.setText(R.string.name_code1015);
		mItemNameCode1016.setText(R.string.name_code1016);
		mItemNameCode1017.setText(R.string.name_code1017);
		
		mItemUnitCode1004.setText(R.string.unit_hu);
		mItemUnitCode1005.setText(R.string.unit_hu);
		mItemUnitCode1006.setText(R.string.unit_ren);
		mItemUnitCode1007.setText(R.string.unit_ren);
		mItemUnitCode1008.setText(R.string.unit_ren);
		mItemUnitCode1009.setText(R.string.unit_ren);
		mItemUnitCode1010.setText(R.string.unit_ren);
		mItemUnitCode1011.setText(R.string.unit_ren);
		mItemUnitCode1012.setText(R.string.unit_ren);
		mItemUnitCode1013.setText(R.string.unit_ren);
		mItemUnitCode1014.setText(R.string.unit_ren);
		mItemUnitCode1015.setText(R.string.unit_ren);
		mItemUnitCode1016.setText(R.string.unit_ren);
		mItemUnitCode1017.setText(R.string.unit_ren);
		
	}
	
	
	private void saveFamilySituation() {
		
		mFamilySituation.setCompleted(checkInputCompleted());
		
		if(DBHelper.getInstance().insertOrUpdateFamilySituation(mFamilySituation)) {
			finish();
			ToastUtils.showLongToast("保存成功");
		}
		else {
			ToastUtils.showLongToast("保存失败");
		}
	}
	
	private boolean checkInputCompleted() {
		if(mRadioBtn1Code1001.isChecked()) {
			mFamilySituation.setCode1001(Const.INT_YES);
		}
		else if(mRadioBtn2Code1001.isChecked()){
			mFamilySituation.setCode1001(Const.INT_NO);
		}
		else {
			return false;
		}
		
		if(mRadioBtn1Code1002.isChecked()) {
			mFamilySituation.setCode1002(Const.INT_YES);
		}
		else if(mRadioBtn2Code1002.isChecked()){
			mFamilySituation.setCode1002(Const.INT_NO);
		}
		else {
			return false;
		}
		
		if(mRadioBtn1Code1003.isChecked()) {
			mFamilySituation.setCode1003(Const.INT_YES);
		}
		else if(mRadioBtn2Code1003.isChecked()){
			mFamilySituation.setCode1003(Const.INT_NO);
		}
		else {
			return false;
		}
		
		String count = null;
		
		count = mItemInputCode1004.getText().toString().trim();
		if(TextUtils.isEmpty(count)) return false;
		mFamilySituation.setCode1004(Integer.valueOf(count));
		
		count = mItemInputCode1005.getText().toString().trim();
		if(TextUtils.isEmpty(count)) return false;
		mFamilySituation.setCode1005(Integer.valueOf(count));
		
		count = mItemInputCode1006.getText().toString().trim();
		if(TextUtils.isEmpty(count)) return false;
		mFamilySituation.setCode1006(Integer.valueOf(count));
		
		count = mItemInputCode1007.getText().toString().trim();
		if(TextUtils.isEmpty(count)) return false;
		mFamilySituation.setCode1007(Integer.valueOf(count));
		
		count = mItemInputCode1008.getText().toString().trim();
		if(TextUtils.isEmpty(count)) return false;
		mFamilySituation.setCode1008(Integer.valueOf(count));
		
		count = mItemInputCode1009.getText().toString().trim();
		if(TextUtils.isEmpty(count)) return false;
		mFamilySituation.setCode1009(Integer.valueOf(count));
		
		count = mItemInputCode1010.getText().toString().trim();
		if(TextUtils.isEmpty(count)) return false;
		mFamilySituation.setCode1010(Integer.valueOf(count));
		
		count = mItemInputCode1011.getText().toString().trim();
		if(TextUtils.isEmpty(count)) return false;
		mFamilySituation.setCode1011(Integer.valueOf(count));
		
		count = mItemInputCode1012.getText().toString().trim();
		if(TextUtils.isEmpty(count)) return false;
		mFamilySituation.setCode1012(Integer.valueOf(count));
		
		count = mItemInputCode1013.getText().toString().trim();
		if(TextUtils.isEmpty(count)) return false;
		mFamilySituation.setCode1013(Integer.valueOf(count));
		
		count = mItemInputCode1014.getText().toString().trim();
		if(TextUtils.isEmpty(count)) return false;
		mFamilySituation.setCode1014(Integer.valueOf(count));
		
		count = mItemInputCode1015.getText().toString().trim();
		if(TextUtils.isEmpty(count)) return false;
		mFamilySituation.setCode1015(Integer.valueOf(count));
		
		count = mItemInputCode1016.getText().toString().trim();
		if(TextUtils.isEmpty(count)) return false;
		mFamilySituation.setCode1016(Integer.valueOf(count));
		
		count = mItemInputCode1017.getText().toString().trim();
		if(TextUtils.isEmpty(count)) return false;
		mFamilySituation.setCode1017(Integer.valueOf(count));
		
		return true;
	}
}
