package com.zgmz.ls.ui;

import com.zgmz.ls.R;
import com.zgmz.ls.base.Const;
import com.zgmz.ls.base.SubActivity;
import com.zgmz.ls.db.DBHelper;
import com.zgmz.ls.model.FamilySpending;
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

public class FamilySpendingActivity extends SubActivity {

	private View mTotalView;
	private TextView mTotalName;
	private TextView mTotalUnit;
	private TextView mTotalMoney;
	
	private TextView mItemNameCode4001;
	private TextView mItemUnitCode4001;
	private TextView mItemMoneyCode4001;
	
	private TextView mItemNameCode4002;
	private TextView mItemUnitCode4002;
	private EditText mItemMoneyCode4002;
	
	private TextView mItemNameCode4003;
	private TextView mItemUnitCode4003;
	private EditText mItemMoneyCode4003;
	
	private TextView mItemNameCode4004;
	private TextView mItemUnitCode4004;
	private EditText mItemMoneyCode4004;
	
	private TextView mItemNameCode4005;
	private TextView mItemUnitCode4005;
	private EditText mItemMoneyCode4005;
	
	private TextView mItemNameCode4006;
	private TextView mItemUnitCode4006;
	private EditText mItemMoneyCode4006;
	
	private TextView mItemNameCode4007;
	private TextView mItemUnitCode4007;
	private EditText mItemMoneyCode4007;
	
	private TextView mItemNameCode4008;
	private TextView mItemUnitCode4008;
	private EditText mItemMoneyCode4008;
	
	private TextView mItemNameCode4009;
	private TextView mItemUnitCode4009;
	private EditText mItemMoneyCode4009;

	private int mUserId = 0;

	private FamilySpending mFamilySpending = new FamilySpending();
	
	@Override
	protected void onConfigrationTitleBar() {
		// TODO Auto-generated method stub
		super.onConfigrationTitleBar();
		setTitleBarTitleText(R.string.title_family_spending);
		setTitleBarRightButtonText(R.string.save);
	}
	

	@Override
	public void onTitleBarRightButtonOnClick(View v) {
		// TODO Auto-generated method stub
		saveFamilySpending();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.family_spending);
		onNewIntent(getIntent());
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		
		SimpleUserInfo info = (SimpleUserInfo) intent.getSerializableExtra(Const.KEY_USER_INFO);
		if(info != null) {
			mUserId = info.getUserId();
			loadFamilySpending();
		}
		else {
			ToastUtils.showLongToast("用户不存在！");
			finish();
		}
	}
	
	private void loadFamilySpending() {
		mFamilySpending = DBHelper.getInstance().getFamilySpending(mUserId);
		if(mFamilySpending == null) {
			mFamilySpending = new FamilySpending();
			mFamilySpending.setUserId(mUserId);
		}
		updateViews();
	}
	
	private void updateViews() {
		FamilySpending spending = mFamilySpending;
				
		if(spending.getCode4002() != Const.INT_INVALID) {
			mItemMoneyCode4002.setText(String.valueOf(spending.getCode4001()));
		}
		
		if(spending.getCode4003() != Const.INT_INVALID) {
			mItemMoneyCode4003.setText(String.valueOf(spending.getCode4003()));
		}
		
		if(spending.getCode4004() != Const.INT_INVALID) {
			mItemMoneyCode4004.setText(String.valueOf(spending.getCode4004()));
		}
		
		if(spending.getCode4005() != Const.INT_INVALID) {
			mItemMoneyCode4005.setText(String.valueOf(spending.getCode4005()));
		}
		
		if(spending.getCode4006() != Const.INT_INVALID) {
			mItemMoneyCode4006.setText(String.valueOf(spending.getCode4006()));
		}
		
		if(spending.getCode4007() != Const.INT_INVALID) {
			mItemMoneyCode4007.setText(String.valueOf(spending.getCode4007()));
		}
		
		if(spending.getCode4008() != Const.INT_INVALID) {
			mItemMoneyCode4008.setText(String.valueOf(spending.getCode4008()));
		}
		
		if(spending.getCode4009() != Const.INT_INVALID) {
			mItemMoneyCode4009.setText(String.valueOf(spending.getCode4009()));
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
		
		item = view.findViewById(R.id.code4001);
		mItemNameCode4001 = (TextView)item.findViewById(R.id.name);
		mItemUnitCode4001 = (TextView)item.findViewById(R.id.unit);
		mItemMoneyCode4001 = (TextView)item.findViewById(R.id.money);
		
		item = view.findViewById(R.id.code4002);
		mItemNameCode4002 = (TextView)item.findViewById(R.id.name);
		mItemUnitCode4002 = (TextView)item.findViewById(R.id.unit);
		mItemMoneyCode4002 = (EditText)item.findViewById(R.id.input);
		
		item = view.findViewById(R.id.code4003);
		mItemNameCode4003 = (TextView)item.findViewById(R.id.name);
		mItemUnitCode4003 = (TextView)item.findViewById(R.id.unit);
		mItemMoneyCode4003 = (EditText)item.findViewById(R.id.input);
		
		item = view.findViewById(R.id.code4004);
		mItemNameCode4004 = (TextView)item.findViewById(R.id.name);
		mItemUnitCode4004 = (TextView)item.findViewById(R.id.unit);
		mItemMoneyCode4004 = (EditText)item.findViewById(R.id.input);
		
		item = view.findViewById(R.id.code4005);
		mItemNameCode4005 = (TextView)item.findViewById(R.id.name);
		mItemUnitCode4005 = (TextView)item.findViewById(R.id.unit);
		mItemMoneyCode4005 = (EditText)item.findViewById(R.id.input);
		
		item = view.findViewById(R.id.code4006);
		mItemNameCode4006 = (TextView)item.findViewById(R.id.name);
		mItemUnitCode4006 = (TextView)item.findViewById(R.id.unit);
		mItemMoneyCode4006 = (EditText)item.findViewById(R.id.input);
		
		item = view.findViewById(R.id.code4007);
		mItemNameCode4007 = (TextView)item.findViewById(R.id.name);
		mItemUnitCode4007 = (TextView)item.findViewById(R.id.unit);
		mItemMoneyCode4007 = (EditText)item.findViewById(R.id.input);
		
		item = view.findViewById(R.id.code4008);
		mItemNameCode4008 = (TextView)item.findViewById(R.id.name);
		mItemUnitCode4008 = (TextView)item.findViewById(R.id.unit);
		mItemMoneyCode4008 = (EditText)item.findViewById(R.id.input);
		
		item = view.findViewById(R.id.code4009);
		mItemNameCode4009 = (TextView)item.findViewById(R.id.name);
		mItemUnitCode4009 = (TextView)item.findViewById(R.id.unit);
		mItemMoneyCode4009 = (EditText)item.findViewById(R.id.input);
		
		initViews();
	}
	
	private void initViews() {
		
		mTotalView.setBackgroundColor(getResources().getColor(R.color.spending_total_bg));
		mTotalName.setTextColor(getResources().getColor(R.color.spending_text));
		mTotalMoney.setTextColor(getResources().getColor(R.color.spending_text));
		
		mItemMoneyCode4001.setTextColor(getResources().getColor(R.color.spending_text));
		mItemMoneyCode4002.setTextColor(getResources().getColor(R.color.spending_text));
		mItemMoneyCode4003.setTextColor(getResources().getColor(R.color.spending_text));
		mItemMoneyCode4004.setTextColor(getResources().getColor(R.color.spending_text));
		mItemMoneyCode4005.setTextColor(getResources().getColor(R.color.spending_text));
		mItemMoneyCode4006.setTextColor(getResources().getColor(R.color.spending_text));
		mItemMoneyCode4007.setTextColor(getResources().getColor(R.color.spending_text));
		mItemMoneyCode4008.setTextColor(getResources().getColor(R.color.spending_text));
		mItemMoneyCode4009.setTextColor(getResources().getColor(R.color.spending_text));
		
		mItemMoneyCode4002.setBackgroundResource(R.drawable.item_input_c);
		mItemMoneyCode4003.setBackgroundResource(R.drawable.item_input_c);
		mItemMoneyCode4004.setBackgroundResource(R.drawable.item_input_c);
		mItemMoneyCode4005.setBackgroundResource(R.drawable.item_input_c);
		mItemMoneyCode4006.setBackgroundResource(R.drawable.item_input_c);
		mItemMoneyCode4007.setBackgroundResource(R.drawable.item_input_c);
		mItemMoneyCode4008.setBackgroundResource(R.drawable.item_input_c);
		mItemMoneyCode4009.setBackgroundResource(R.drawable.item_input_c);
		
		mTotalName.setText(R.string.spending_total);
		mTotalUnit.setText(R.string.unit_yuan);
		
		mItemNameCode4001.setText(R.string.name_code4001);
		mItemNameCode4002.setText(R.string.name_code4002);
		mItemNameCode4003.setText(R.string.name_code4003);
		mItemNameCode4004.setText(R.string.name_code4004);
		mItemNameCode4005.setText(R.string.name_code4005);
		mItemNameCode4006.setText(R.string.name_code4006);
		mItemNameCode4007.setText(R.string.name_code4007);
		mItemNameCode4008.setText(R.string.name_code4008);
		mItemNameCode4009.setText(R.string.name_code4009);

		
		mItemUnitCode4001.setText(R.string.unit_yuan);
		mItemUnitCode4002.setText(R.string.unit_yuan);
		mItemUnitCode4003.setText(R.string.unit_yuan);
		mItemUnitCode4004.setText(R.string.unit_yuan);
		mItemUnitCode4005.setText(R.string.unit_yuan);
		mItemUnitCode4006.setText(R.string.unit_yuan);
		mItemUnitCode4007.setText(R.string.unit_yuan);
		mItemUnitCode4008.setText(R.string.unit_yuan);
		mItemUnitCode4009.setText(R.string.unit_yuan);
		
		
		mItemMoneyCode4002.addTextChangedListener(mTextWatcher);
		mItemMoneyCode4003.addTextChangedListener(mTextWatcher);
		mItemMoneyCode4004.addTextChangedListener(mTextWatcher);
		mItemMoneyCode4005.addTextChangedListener(mTextWatcher);
		mItemMoneyCode4006.addTextChangedListener(mTextWatcher);
		mItemMoneyCode4007.addTextChangedListener(mTextWatcher);
		mItemMoneyCode4008.addTextChangedListener(mTextWatcher);
		mItemMoneyCode4009.addTextChangedListener(mTextWatcher);
	}
	
	private void updateMoney() {
		updateMoneyCode4001();
		updateMoneyTotal();
	}
	
	private void updateMoneyCode4001() {
		int sum = 0;
		String money = mItemMoneyCode4002.getText().toString().trim();
		if(!TextUtils.isEmpty(money)) {
			sum += Integer.valueOf(money);
		}
		
		money = mItemMoneyCode4003.getText().toString().trim();
		if(!TextUtils.isEmpty(money)) {
			sum += Integer.valueOf(money);
		}
		
		money = mItemMoneyCode4004.getText().toString().trim();
		if(!TextUtils.isEmpty(money)) {
			sum += Integer.valueOf(money);
		}
		
		money = mItemMoneyCode4005.getText().toString().trim();
		if(!TextUtils.isEmpty(money)) {
			sum += Integer.valueOf(money);
		}
		
		money = mItemMoneyCode4006.getText().toString().trim();
		if(!TextUtils.isEmpty(money)) {
			sum += Integer.valueOf(money);
		}
		
		money = mItemMoneyCode4007.getText().toString().trim();
		if(!TextUtils.isEmpty(money)) {
			sum += Integer.valueOf(money);
		}
		
		mItemMoneyCode4001.setText(String.valueOf(sum));
	}
	
	private void updateMoneyTotal() {
		int sum = 0;
		String money = mItemMoneyCode4001.getText().toString().trim();
		if(!TextUtils.isEmpty(money)) {
			sum += Integer.valueOf(money);
		}
		
		money = mItemMoneyCode4008.getText().toString().trim();
		if(!TextUtils.isEmpty(money)) {
			sum += Integer.valueOf(money);
		}
		
		money = mItemMoneyCode4009.getText().toString().trim();
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
	
	private void saveFamilySpending() {
		mFamilySpending.setCompleted(checkInputCompleted());
		
		if(DBHelper.getInstance().insertOrUpdateFamilySpending(mFamilySpending)) {
			finish();
			ToastUtils.showLongToast("保存成功");
		}
		else {
			ToastUtils.showLongToast("保存失败");
		}
	}
	
	private boolean checkInputCompleted() {
		
		String money = null;
		money = mItemMoneyCode4001.getText().toString().trim();
		if(TextUtils.isEmpty(money)) return false;
		mFamilySpending.setCode4001(Integer.valueOf(money));
		
		money = mItemMoneyCode4002.getText().toString().trim();
		if(TextUtils.isEmpty(money)) return false;
		mFamilySpending.setCode4002(Integer.valueOf(money));
		
		money = mItemMoneyCode4003.getText().toString().trim();
		if(TextUtils.isEmpty(money)) return false;
		mFamilySpending.setCode4003(Integer.valueOf(money));
		
		money = mItemMoneyCode4004.getText().toString().trim();
		if(TextUtils.isEmpty(money)) return false;
		mFamilySpending.setCode4004(Integer.valueOf(money));
		
		money = mItemMoneyCode4005.getText().toString().trim();
		if(TextUtils.isEmpty(money)) return false;
		mFamilySpending.setCode4005(Integer.valueOf(money));
		
		money = mItemMoneyCode4006.getText().toString().trim();
		if(TextUtils.isEmpty(money)) return false;
		mFamilySpending.setCode4006(Integer.valueOf(money));
		
		money = mItemMoneyCode4007.getText().toString().trim();
		if(TextUtils.isEmpty(money)) return false;
		mFamilySpending.setCode4007(Integer.valueOf(money));
		
		money = mItemMoneyCode4008.getText().toString().trim();
		if(TextUtils.isEmpty(money)) return false;
		mFamilySpending.setCode4008(Integer.valueOf(money));
		
		money = mItemMoneyCode4009.getText().toString().trim();
		if(TextUtils.isEmpty(money)) return false;
		mFamilySpending.setCode4009(Integer.valueOf(money));

		return true;
	}
	
}
