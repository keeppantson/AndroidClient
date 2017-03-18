package com.zgmz.ls.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.zgmz.ls.R;
import com.zgmz.ls.base.Const;
import com.zgmz.ls.base.SubActivity;
import com.zgmz.ls.db.DBHelper;
import com.zgmz.ls.model.Attachment;
import com.zgmz.ls.model.SimpleUserInfo;
import com.zgmz.ls.utils.BitmapHelper;
import com.zgmz.ls.utils.ToastUtils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

public class YearPhotoActivity extends SubActivity implements OnClickListener{
	
	ImageButton mPhoto;
	
	Button mBtnComplete;
	
	Attachment mAttachment;
	
	int mUserId = 0;
	Bitmap mBitmap;

	@Override
	protected void onConfigrationTitleBar() {
		// TODO Auto-generated method stub
		super.onConfigrationTitleBar();
		setTitleBarTitleText(R.string.title_year_photo);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.year_photo);
		onNewIntent(getIntent());
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		SimpleUserInfo info = (SimpleUserInfo) intent.getSerializableExtra(Const.KEY_USER_INFO);
		if(info != null) {
			mUserId = info.getUserId();
			loadYearPhoto();
		}
		else {
			ToastUtils.showLongToast("用户不存在！");
			finish();
		}
	}
	
	private void loadYearPhoto() {
		mAttachment = DBHelper.getInstance().getYearPhoto(mUserId);
		if(mAttachment == null) {
			mAttachment = new Attachment();
			mAttachment.setUserId(mUserId);
		}
		updatePhoto();
	}
	
	private void updatePhoto() {
		Attachment attach = mAttachment;
		if(attach != null && !TextUtils.isEmpty(attach.getPath())) {
			mPhoto.setImageBitmap(BitmapHelper.getThumbBitmap(attach.getPath(), 720, 720));
		}
	}
	
	@Override
	protected void setupViews(View view) {
		// TODO Auto-generated method stub
		mPhoto = (ImageButton)view.findViewById(R.id.photo);
		
		mBtnComplete = (Button)view.findViewById(R.id.btn_complete);
		
		
		mPhoto.setOnClickListener(this);
		mBtnComplete.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
		case R.id.photo:
			startCameraActivity();
			break;
		case R.id.btn_complete:
			save();
			break;
		}
		
	}
	
	
	private static final int REQUEST_CODE_CAMERA = 0x4001;
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode == REQUEST_CODE_CAMERA) {
			if(resultCode == Activity.RESULT_OK) {
//
//				Bundle bundle = data.getExtras();
//				Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
				Bitmap bitmap = BitmapHelper.getThumbBitmap(Const.IMAGE_TEMP_FILE_LOCATION, 720, 720);
				if(bitmap != null) {
					mBitmap = bitmap;
					mPhoto.setImageBitmap(bitmap);
				}
				
			}
		}
	}
	
	
	private void save() {
		
		if(mBitmap == null) {
			ToastUtils.showLongToast("照片未更新");
			return;
		}
		
		if(sevaYearPhoto(mBitmap)) {
			if(DBHelper.getInstance().insertAttachment(mAttachment)){
				DBHelper.getInstance().updateYearPhotoFlag(mUserId);
				ToastUtils.showLongToast("保存成功");
				finish();
			}
			else {
				ToastUtils.showLongToast("保存失败");
			}
		}
		else {
			ToastUtils.showLongToast("保存失败");
		}
		
	}
	
	
	private boolean sevaYearPhoto(Bitmap bitmap) {
		if(bitmap == null) return false;
		FileOutputStream b = null;
		String name = "year"+ mUserId;
		File file = new File(Const.IMAGE_BASE_LOCATION);
		file.mkdirs();// 创建文件夹
		String fileName = Const.IMAGE_BASE_LOCATION + name + ".jpg";

		try {
			b = new FileOutputStream(fileName);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
			
			mAttachment.setName(name);
			mAttachment.setPath(fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				b.flush();
				b.close();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
	
	
	private void startCameraActivity() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Const.IMAGE_URI);
		startActivityForResult(intent, REQUEST_CODE_CAMERA);
	}
}
