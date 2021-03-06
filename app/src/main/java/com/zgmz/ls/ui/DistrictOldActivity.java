package com.zgmz.ls.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.zgmz.ls.R;
import com.zgmz.ls.base.Const;
import com.zgmz.ls.base.SubActivity;
import com.zgmz.ls.db.DBHelper;
import com.zgmz.ls.model.District;
import com.zgmz.ls.model.SimpleUserInfo;
import com.zgmz.ls.utils.ToastUtils;

public class DistrictOldActivity extends Activity implements OnClickListener, AMapLocationListener{
	
	private static final String DEFAULT_PROVICE = "贵州省";
	private static final String DEFAULT_CITY = "贵州市";
	private static final String DEFAULT_DISTRICT = "南山区";
	
	EditText mInputProvice;
	EditText mInputCity;
	EditText mInputAddress;
	EditText mInputLocation;
	
	Button mBtnLocation;
	Button mBtnComplete;
	
	private AMapLocationClient locationClient = null;
	private AMapLocationClientOption locationOption = null;
	
	AMapLocation mCurAMapLocation;
	
	SimpleUserInfo mUserInfo;
	
	District mDistrict;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.district);
		initAmap();
		setupViews();
		onNewIntent(getIntent());
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		mUserInfo = (SimpleUserInfo) intent.getSerializableExtra(Const.KEY_USER_INFO); 
		if(mUserInfo != null) {
			loadDistrictFromDatabase();
		}
		else {
			ToastUtils.showLongToast("未知用户");
			finish();
		}
	}
	
	private void loadDistrictFromDatabase() {
		mDistrict = DBHelper.getInstance().getDistrict(mUserInfo.getUserId());
		if(mDistrict == null) {
			mDistrict = new District();
			mDistrict.setUserId(mUserInfo.getUserId());
			mDistrict.setProvice(DEFAULT_PROVICE);
			mDistrict.setCity(DEFAULT_CITY);
			mDistrict.setDistric(DEFAULT_DISTRICT);
		}
		updateViews();
	}
	
	private void initAmap() {
		locationClient = new AMapLocationClient(this.getApplicationContext());
		locationOption = new AMapLocationClientOption();
		// 设置定位模式为高精度模式
		locationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
		// 设置定位监听
		locationClient.setLocationListener(this);
	}
	
	private void updateViews() {
		if(mDistrict == null) return;
		District district = mDistrict;
		mInputProvice.setText(district.getProvice());
		mInputCity.setText(district.getCity()+" - " + district.getDistric());
		mInputAddress.setText(district.getAddress());
		mInputLocation.setText(district.getLocation());
	}

	protected void setupViews() {
		// TODO Auto-generated method stub
		mInputProvice = (EditText)this.findViewById(R.id.input_provice);
		mInputCity = (EditText)this.findViewById(R.id.input_city);
		mInputAddress = (EditText)this.findViewById(R.id.input_address);
		
		mBtnLocation = (Button)this.findViewById(R.id.btn_location);
		mBtnComplete = (Button)this.findViewById(R.id.btn_complete);
		
		mBtnLocation.setOnClickListener(this);
		mBtnComplete.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
			case R.id.btn_location:
				obatinLocation();
				break;
			case R.id.btn_complete:
				save();
				break;
		}
	}
	
	public void obatinLocation() {
		startLocation();
	}
	
	public void save() {
		String address = mInputAddress.getText().toString().trim();
		String location = mInputLocation.getText().toString().trim();
		
		if(TextUtils.isEmpty(address)) {
			ToastUtils.showLongToast("详细地址不能空");
		}
		else if(TextUtils.isEmpty(location)){
			ToastUtils.showLongToast("位置信息不能为空");
		}
		else {
			mDistrict.setAddress(address);
			mDistrict.setCompleted(true);
			if(DBHelper.getInstance().insertOrUpdatDistrict(mDistrict)) {
				ToastUtils.showLongToast("保存成功");
				finish();
			}
			else{
				ToastUtils.showLongToast("区域信息保存失败");
			}
		}
		
	}
	
	
	protected void startLocation() {
		// 设置定位参数
		locationClient.setLocationOption(locationOption);
		// 启动定位
		locationClient.startLocation();
	}
	
	protected void stopLocation() {
		if(locationClient.isStarted())
			locationClient.stopLocation();
		
	}
	
	public void updateDistrictLocation(AMapLocation location) {
		mCurAMapLocation = location;
		mDistrict.setLocation(location.getAddress());
		mDistrict.setLatitude(location.getLatitude());
		mDistrict.setLongitude(location.getLongitude());
		mInputLocation.setText(mDistrict.getLocation());
	}

	@Override
	public void onLocationChanged(AMapLocation location) {
		// TODO Auto-generated method stub
		if(location != null && location.getErrorCode()==0) {
			updateDistrictLocation(location);
		}
		else {
			ToastUtils.showLongToast("获取位置失败");
		}
		stopLocation();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		stopLocation();
	}
}
