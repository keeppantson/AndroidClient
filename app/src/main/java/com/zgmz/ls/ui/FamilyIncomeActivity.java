package com.zgmz.ls.ui;

import com.zgmz.ls.R;
import com.zgmz.ls.base.Const;
import com.zgmz.ls.base.SubActivity;
import com.zgmz.ls.db.DBHelper;
import com.zgmz.ls.model.FamilyIncome;
import com.zgmz.ls.model.SimpleUserInfo;
import com.zgmz.ls.utils.ToastUtils;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class FamilyIncomeActivity extends SubActivity {
	
	private View mTotalView;
	private TextView mTotalName;
	private TextView mTotalUnit;
	private TextView mTotalMoney;
	
	private TextView mItemNameCode3001;
	private TextView mItemUnitCode3001;
	private EditText mItemMoneyCode3001;
	
	private TextView mItemNameCode3002;
	private TextView mItemUnitCode3002;
	private TextView mItemMoneyCode3002;
	
	private TextView mItemNameCode3003;
	private TextView mItemUnitCode3003;
	private EditText mItemMoneyCode3003;
	
	private TextView mItemNameCode3004;
	private TextView mItemUnitCode3004;
	private EditText mItemMoneyCode3004;
	
	private TextView mItemNameCode3005;
	private TextView mItemUnitCode3005;
	private EditText mItemMoneyCode3005;
	
	private TextView mItemNameCode3006;
	private TextView mItemUnitCode3006;
	private EditText mItemMoneyCode3006;
	
	private TextView mItemNameCode3007;
	private TextView mItemUnitCode3007;
	private EditText mItemMoneyCode3007;
	
	private TextView mItemNameCode3008;
	private TextView mItemUnitCode3008;
	private EditText mItemMoneyCode3008;
	
	private TextView mItemNameCode3009;
	private TextView mItemUnitCode3009;
	private EditText mItemMoneyCode3009;
	
	private TextView mItemNameCode3010;
	private TextView mItemUnitCode3010;
	private EditText mItemMoneyCode3010;


	private int mUserId = 0;

	private FamilyIncome mFamilyIncome;
	
	@Override
	protected void onConfigrationTitleBar() {
		// TODO Auto-generated method stub
		super.onConfigrationTitleBar();
		setTitleBarTitleText(R.string.title_family_income);
		setTitleBarRightButtonText(R.string.save);
	}
	

	@Override
	public void onTitleBarRightButtonOnClick(View v) {
		// TODO Auto-generated method stub
		saveFamilyIncome();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.family_income);
		onNewIntent(getIntent());
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		
		SimpleUserInfo info = (SimpleUserInfo) intent.getSerializableExtra(Const.KEY_USER_INFO);
		if(info != null) {
			mUserId = info.getUserId();
			loadFamilyIncome();
		}
		else {
			ToastUtils.showLongToast("用户不存在！");
			finish();
		}
	}
	
	private void loadFamilyIncome() {
		mFamilyIncome = DBHelper.getInstance().getFamilyIncome(mUserId);
		if(mFamilyIncome == null) {
			mFamilyIncome = new FamilyIncome();
			mFamilyIncome.setUserId(mUserId);
		}
		updateViews();
	}
	
	private void updateViews() {
		FamilyIncome income = mFamilyIncome;
				
		if(income.getCode3001() != Const.INT_INVALID) {
			mItemMoneyCode3001.setText(String.valueOf(income.getCode3001()));
		}
		
		if(income.getCode3003() != Const.INT_INVALID) {
			mItemMoneyCode3003.setText(String.valueOf(income.getCode3003()));
		}
		
		if(income.getCode3004() != Const.INT_INVALID) {
			mItemMoneyCode3004.setText(String.valueOf(income.getCode3004()));
		}
		
		if(income.getCode3005() != Const.INT_INVALID) {
			mItemMoneyCode3005.setText(String.valueOf(income.getCode3005()));
		}
		
		if(income.getCode3006() != Const.INT_INVALID) {
			mItemMoneyCode3006.setText(String.valueOf(income.getCode3006()));
		}
		
		if(income.getCode3007() != Const.INT_INVALID) {
			mItemMoneyCode3007.setText(String.valueOf(income.getCode3007()));
		}
		
		if(income.getCode3008() != Const.INT_INVALID) {
			mItemMoneyCode3008.setText(String.valueOf(income.getCode3008()));
		}
		
		if(income.getCode3009() != Const.INT_INVALID) {
			mItemMoneyCode3009.setText(String.valueOf(income.getCode3009()));
		}
		
		if(income.getCode3010() != Const.INT_INVALID) {
			mItemMoneyCode3010.setText(String.valueOf(income.getCode3010()));
		}
		
	}
	
	
	@Override
	protected void setupViews(View view) {
		// TODO Auto-generated method stub
		View item = view.findViewById(R.id.total);
		mTotalView = item;
		mTotalName = (TextView)item.findViewById(R.id.name);
		mTotalUnit = (TextView)item.findViewById(R.id.unit);
		mTotalMoney = (TextView)item.findViewById(R.id.money);
		
		item = view.findViewById(R.id.code3001);
		mItemNameCode3001 = (TextView)item.findViewById(R.id.name);
		mItemUnitCode3001 = (TextView)item.findViewById(R.id.unit);
		mItemMoneyCode3001 = (EditText)item.findViewById(R.id.input);
		
		item = view.findViewById(R.id.code3002);
		mItemNameCode3002 = (TextView)item.findViewById(R.id.name);
		mItemUnitCode3002 = (TextView)item.findViewById(R.id.unit);
		mItemMoneyCode3002 = (TextView)item.findViewById(R.id.money);
		
		item = view.findViewById(R.id.code3003);
		mItemNameCode3003 = (TextView)item.findViewById(R.id.name);
		mItemUnitCode3003 = (TextView)item.findViewById(R.id.unit);
		mItemMoneyCode3003 = (EditText)item.findViewById(R.id.input);
		
		item = view.findViewById(R.id.code3004);
		mItemNameCode3004 = (TextView)item.findViewById(R.id.name);
		mItemUnitCode3004 = (TextView)item.findViewById(R.id.unit);
		mItemMoneyCode3004 = (EditText)item.findViewById(R.id.input);
		
		item = view.findViewById(R.id.code3005);
		mItemNameCode3005 = (TextView)item.findViewById(R.id.name);
		mItemUnitCode3005 = (TextView)item.findViewById(R.id.unit);
		mItemMoneyCode3005 = (EditText)item.findViewById(R.id.input);
		
		item = view.findViewById(R.id.code3006);
		mItemNameCode3006 = (TextView)item.findViewById(R.id.name);
		mItemUnitCode3006 = (TextView)item.findViewById(R.id.unit);
		mItemMoneyCode3006 = (EditText)item.findViewById(R.id.input);
		
		item = view.findViewById(R.id.code3007);
		mItemNameCode3007 = (TextView)item.findViewById(R.id.name);
		mItemUnitCode3007 = (TextView)item.findViewById(R.id.unit);
		mItemMoneyCode3007 = (EditText)item.findViewById(R.id.input);
		
		item = view.findViewById(R.id.code3008);
		mItemNameCode3008 = (TextView)item.findViewById(R.id.name);
		mItemUnitCode3008 = (TextView)item.findViewById(R.id.unit);
		mItemMoneyCode3008 = (EditText)item.findViewById(R.id.input);
		
		item = view.findViewById(R.id.code3009);
		mItemNameCode3009 = (TextView)item.findViewById(R.id.name);
		mItemUnitCode3009 = (TextView)item.findViewById(R.id.unit);
		mItemMoneyCode3009 = (EditText)item.findViewById(R.id.input);
		
		item = view.findViewById(R.id.code3010);
		mItemNameCode3010 = (TextView)item.findViewById(R.id.name);
		mItemUnitCode3010 = (TextView)item.findViewById(R.id.unit);
		mItemMoneyCode3010 = (EditText)item.findViewById(R.id.input);
		
		initViews();
	}
	
	private void initViews() {
		
		mTotalView.setBackgroundColor(getResources().getColor(R.color.income_total_bg));
		mTotalName.setTextColor(getResources().getColor(R.color.income_text));
		mTotalMoney.setTextColor(getResources().getColor(R.color.income_text));
		
		mItemMoneyCode3001.setTextColor(getResources().getColor(R.color.income_text));
		mItemMoneyCode3002.setTextColor(getResources().getColor(R.color.income_text));
		mItemMoneyCode3003.setTextColor(getResources().getColor(R.color.income_text));
		mItemMoneyCode3004.setTextColor(getResources().getColor(R.color.income_text));
		mItemMoneyCode3005.setTextColor(getResources().getColor(R.color.income_text));
		mItemMoneyCode3006.setTextColor(getResources().getColor(R.color.income_text));
		mItemMoneyCode3007.setTextColor(getResources().getColor(R.color.income_text));
		mItemMoneyCode3008.setTextColor(getResources().getColor(R.color.income_text));
		mItemMoneyCode3009.setTextColor(getResources().getColor(R.color.income_text));
		mItemMoneyCode3010.setTextColor(getResources().getColor(R.color.income_text));
		
		mTotalName.setText(R.string.income_total);
		mTotalUnit.setText(R.string.unit_yuan);
		
		mItemNameCode3001.setText(R.string.name_code3001);
		mItemNameCode3002.setText(R.string.name_code3002);
		mItemNameCode3003.setText(R.string.name_code3003);
		mItemNameCode3004.setText(R.string.name_code3004);
		mItemNameCode3005.setText(R.string.name_code3005);
		mItemNameCode3006.setText(R.string.name_code3006);
		mItemNameCode3007.setText(R.string.name_code3007);
		mItemNameCode3008.setText(R.string.name_code3008);
		mItemNameCode3009.setText(R.string.name_code3009);
		mItemNameCode3010.setText(R.string.name_code3010);

		
		mItemUnitCode3001.setText(R.string.unit_yuan);
		mItemUnitCode3002.setText(R.string.unit_yuan);
		mItemUnitCode3003.setText(R.string.unit_yuan);
		mItemUnitCode3004.setText(R.string.unit_yuan);
		mItemUnitCode3005.setText(R.string.unit_yuan);
		mItemUnitCode3006.setText(R.string.unit_yuan);
		mItemUnitCode3007.setText(R.string.unit_yuan);
		mItemUnitCode3008.setText(R.string.unit_yuan);
		mItemUnitCode3009.setText(R.string.unit_yuan);
		mItemUnitCode3010.setText(R.string.unit_yuan);
		
		
		mItemMoneyCode3001.addTextChangedListener(mTextWatcher);
		mItemMoneyCode3003.addTextChangedListener(mTextWatcher);
		mItemMoneyCode3004.addTextChangedListener(mTextWatcher);
		mItemMoneyCode3005.addTextChangedListener(mTextWatcher);
		mItemMoneyCode3006.addTextChangedListener(mTextWatcher);
		mItemMoneyCode3007.addTextChangedListener(mTextWatcher);
		mItemMoneyCode3008.addTextChangedListener(mTextWatcher);
		mItemMoneyCode3009.addTextChangedListener(mTextWatcher);
		mItemMoneyCode3010.addTextChangedListener(mTextWatcher);
	}
	
	private void updateMoney() {
		updateMoneyCode3002();
		updateMoneyTotal();
	}
	
	private void updateMoneyCode3002() {
		int sum = 0;
		String money = mItemMoneyCode3003.getText().toString().trim();
		if(!TextUtils.isEmpty(money)) {
			sum += Integer.valueOf(money);
		}
		
		money = mItemMoneyCode3004.getText().toString().trim();
		if(!TextUtils.isEmpty(money)) {
			sum += Integer.valueOf(money);
		}
		
		money = mItemMoneyCode3005.getText().toString().trim();
		if(!TextUtils.isEmpty(money)) {
			sum += Integer.valueOf(money);
		}
		
		money = mItemMoneyCode3006.getText().toString().trim();
		if(!TextUtils.isEmpty(money)) {
			sum += Integer.valueOf(money);
		}
		
		money = mItemMoneyCode3007.getText().toString().trim();
		if(!TextUtils.isEmpty(money)) {
			sum += Integer.valueOf(money);
		}
		
		money = mItemMoneyCode3008.getText().toString().trim();
		if(!TextUtils.isEmpty(money)) {
			sum += Integer.valueOf(money);
		}
		
		mItemMoneyCode3002.setText(String.valueOf(sum));
	}
	
	private void updateMoneyTotal() {
		int sum = 0;
		String money = mItemMoneyCode3001.getText().toString().trim();
		if(!TextUtils.isEmpty(money)) {
			sum += Integer.valueOf(money);
		}
		
		money = mItemMoneyCode3002.getText().toString().trim();
		if(!TextUtils.isEmpty(money)) {
			sum += Integer.valueOf(money);
		}
		
		money = mItemMoneyCode3009.getText().toString().trim();
		if(!TextUtils.isEmpty(money)) {
			sum += Integer.valueOf(money);
		}
		
		money = mItemMoneyCode3010.getText().toString().trim();
		if(!TextUtils.isEmpty(money)) {
			sum += Integer.valueOf(money);
		}
		
		mTotalMoney.setText(String.valueOf(sum));
	}
	
	private TextWatcher mTextWatcher = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int money) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int money, int after) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			updateMoney();
		}
	};
	
	private void saveFamilyIncome() {
		
		mFamilyIncome.setCompleted(checkInputCompleted());
		
		if(DBHelper.getInstance().insertOrUpdateFamilyIncome(mFamilyIncome)) {
			finish();
			ToastUtils.showLongToast("保存成功");
		}
		else {
			ToastUtils.showLongToast("保存失败");
		}
	}
	
	private boolean checkInputCompleted() {
		
		String money = null;
		money = mItemMoneyCode3001.getText().toString().trim();
		if(TextUtils.isEmpty(money)) return false;
		mFamilyIncome.setCode3001(Integer.valueOf(money));
		
		money = mItemMoneyCode3002.getText().toString().trim();
		if(TextUtils.isEmpty(money)) return false;
		mFamilyIncome.setCode3002(Integer.valueOf(money));
		
		money = mItemMoneyCode3003.getText().toString().trim();
		if(TextUtils.isEmpty(money)) return false;
		mFamilyIncome.setCode3003(Integer.valueOf(money));
		
		money = mItemMoneyCode3004.getText().toString().trim();
		if(TextUtils.isEmpty(money)) return false;
		mFamilyIncome.setCode3004(Integer.valueOf(money));
		
		money = mItemMoneyCode3005.getText().toString().trim();
		if(TextUtils.isEmpty(money)) return false;
		mFamilyIncome.setCode3005(Integer.valueOf(money));
		
		money = mItemMoneyCode3006.getText().toString().trim();
		if(TextUtils.isEmpty(money)) return false;
		mFamilyIncome.setCode3006(Integer.valueOf(money));
		
		money = mItemMoneyCode3007.getText().toString().trim();
		if(TextUtils.isEmpty(money)) return false;
		mFamilyIncome.setCode3007(Integer.valueOf(money));
		
		money = mItemMoneyCode3008.getText().toString().trim();
		if(TextUtils.isEmpty(money)) return false;
		mFamilyIncome.setCode3008(Integer.valueOf(money));
		
		money = mItemMoneyCode3009.getText().toString().trim();
		if(TextUtils.isEmpty(money)) return false;
		mFamilyIncome.setCode3009(Integer.valueOf(money));
		
		money = mItemMoneyCode3010.getText().toString().trim();
		if(TextUtils.isEmpty(money)) return false;
		mFamilyIncome.setCode3010(Integer.valueOf(money));

		return true;
	}
}
