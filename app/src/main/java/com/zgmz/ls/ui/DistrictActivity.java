package com.zgmz.ls.ui;

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

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

public class DistrictActivity extends SubActivity implements OnClickListener, AMapLocationListener{
	
	private static final String DEFAULT_PROVICE = "贵州省";
	private static final String DEFAULT_CITY = "贵州市";
	private static final String DEFAULT_DISTRICT = "南山区";
	
	EditText mInputProvice;
	EditText mInputCity;
	EditText mInputAddress;

	Button mBtnLocation;
	Button mBtnComplete;

    private AMapLocationClient locationClient = null;
	private AMapLocationClientOption locationOption = null;

	AMapLocation mCurAMapLocation;
	
	SimpleUserInfo mUserInfo;
	
	District mDistrict;

	@Override
	protected void onConfigrationTitleBar() {
		// TODO Auto-generated method stub
		super.onConfigrationTitleBar();
		setTitleBarTitleText("GPS信息");
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.district);
		initAmap();
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
		mInputAddress.setText(district.getLocation());
	}
	
	@Override
	protected void setupViews(View view) {
		// TODO Auto-generated method stub
		mInputProvice = (EditText)view.findViewById(R.id.input_provice);
		mInputCity = (EditText)view.findViewById(R.id.input_city);
		mInputAddress = (EditText)view.findViewById(R.id.input_address);
		
		mBtnLocation = (Button)view.findViewById(R.id.btn_location);
		mBtnComplete = (Button)view.findViewById(R.id.btn_complete);
		
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
		String location = mInputAddress.getText().toString().trim();
		
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
        finish();
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
	public static String sHA1(Context context) {
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(
					context.getPackageName(), PackageManager.GET_SIGNATURES);
			byte[] cert = info.signatures[0].toByteArray();
			MessageDigest md = MessageDigest.getInstance("SHA1");
			byte[] publicKey = md.digest(cert);
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < publicKey.length; i++) {
				String appendString = Integer.toHexString(0xFF & publicKey[i])
						.toUpperCase(Locale.US);
				if (appendString.length() == 1)
					hexString.append("0");
				hexString.append(appendString);
				hexString.append(":");
			}
			String result = hexString.toString();
			return result.substring(0, result.length()-1);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
	public void updateDistrictLocation(AMapLocation location) {
		mCurAMapLocation = location;

        mDistrict.setCity(location.getCity());
        mDistrict.setProvice(location.getProvince());
        mDistrict.setLocation(location.getLocationDetail());
        mDistrict.setDistric(location.getDistrict());
		mDistrict.setLocation(location.getAddress());
		mDistrict.setLatitude(location.getLatitude());
		mDistrict.setLongitude(location.getLongitude());
        mInputAddress.setText(mDistrict.getLocation());

        updateViews();
	}

	@Override
	public void onLocationChanged(AMapLocation location) {
		// TODO Auto-generated method stub
		if(location != null && location.getErrorCode()==0) {
			updateDistrictLocation(location);
		}
		else {
			int ret = location.getErrorCode();
			String stt = sHA1(getAppContext());
			String str = "获取位置失败: ret is " + ret + ". error msg is " + location.getErrorInfo();
			ToastUtils.showLongToast(str);
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
