package com.zgmz.ls.ui;

import com.zgmz.ls.R;
import com.zgmz.ls.base.Const;
import com.zgmz.ls.base.SubActivity;
import com.zgmz.ls.utils.IDCardTools;
import com.zgmz.ls.utils.ToastUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class IDInputActivity extends SubActivity implements OnClickListener{

	EditText mIdNumber;
	
	Button mBtnComplete;
	
	@Override
	protected void onConfigrationTitleBar() {
		// TODO Auto-generated method stub
		super.onConfigrationTitleBar();
		setTitleBarTitleText(R.string.title_id_manual);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.id_input);
	}
	
	@Override
	protected void setupViews(View view) {
		// TODO Auto-generated method stub
		mIdNumber = (EditText)view.findViewById(R.id.id_number);
		
		mBtnComplete = (Button)view.findViewById(R.id.btn_complete);
		mBtnComplete.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
			case R.id.btn_complete:
				backResult();
				break;
		}
	}
	
	
	
	private void backResult() {
		String idNumber = mIdNumber.getText().toString().trim();
		
		if(TextUtils.isEmpty(idNumber)) {
			ToastUtils.showLongToast("身份证号不能为空");
			return;
		}
		
		String error = IDCardTools.IDCardValidate(idNumber);
		
		if(!TextUtils.isEmpty(error)) {
			ToastUtils.showLongToast(error);
			return;
		}
		
		
		Intent data = getIntent();
		data.putExtra(Const.KEY_ID_NUMBER, idNumber.toUpperCase());
		setResult(Activity.RESULT_OK, data);
		
		finish();
	}

}
