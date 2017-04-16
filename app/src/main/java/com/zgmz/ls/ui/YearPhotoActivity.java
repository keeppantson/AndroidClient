package com.zgmz.ls.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import com.zgmz.ls.R;
import com.zgmz.ls.base.Const;
import com.zgmz.ls.base.SubActivity;
import com.zgmz.ls.db.DBHelper;
import com.zgmz.ls.model.Attachment;
import com.zgmz.ls.model.FamilyBase;
import com.zgmz.ls.model.SimpleUserInfo;
import com.zgmz.ls.utils.BitmapHelper;
import com.zgmz.ls.utils.BitmapUtils;
import com.zgmz.ls.utils.FileUtils;
import com.zgmz.ls.utils.ToastUtils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

public class YearPhotoActivity extends SubActivity implements OnClickListener{

	ImageButton mPhoto;

	Button mBtnComplete;

	Attachment mAttachment;

	String Check_Task_ID;
	SimpleUserInfo userInfo;
	String Card_ID;
	String Type = String.valueOf(Attachment.TYPE_IMAGE);

	int mUserId = 0;
	Bitmap mBitmap = null;

	protected void onConfigrationTitleBar() {
		// TODO Auto-generated method stub
		super.onConfigrationTitleBar();
		setTitleBarTitleText("年度摄像");
		//hideTitleBar();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.year_photo);
		setupViews();
		onNewIntent(getIntent());
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
        userInfo = (SimpleUserInfo) intent.getSerializableExtra(Const.KEY_USER_INFO);
		Type = "103";
		if(userInfo != null) {
			mUserId = userInfo.getUserId();
			Card_ID = userInfo.getIdNumber();
			Check_Task_ID = userInfo.getCheck_task_id();
            loadYearPhoto();
		}
		else {
			ToastUtils.showLongToast("用户不存在！");
			finish();
		}
	}

	private void loadYearPhoto() {
		mAttachment = DBHelper.getInstance().getAttachment(Card_ID, Check_Task_ID, Type);
		if(mAttachment == null) {
			mAttachment = new Attachment();
			mAttachment.setCheck_task_id(Check_Task_ID);
			mAttachment.setCard_id(Card_ID);
			mAttachment.setPath(userInfo.getAttachment_path());
			mAttachment.setType(Integer.parseInt(Type));
			mAttachment.setContent(null);
		}
        FamilyBase family = DBHelper.getInstance().getFamilyWithTaskID(Check_Task_ID);
        userInfo.setTime(family.getSqrq());
        userInfo.setXzqhbm(family.getXzqhdm());
		updatePhoto();
	}

	private void updatePhoto() {
		Attachment attach = mAttachment;
		if(attach != null && attach.getContent() != null) {
			//FileUtils.writeToFile(attach.getContent().toString(), Const.IMAGE_TEMP_FILE_LOCATION);
			mPhoto.setImageBitmap(BitmapHelper.getThumbBitmap(Const.IMAGE_TEMP_FILE_LOCATION, 720, 720));
			FileUtils.deleteFile(Const.IMAGE_TEMP_FILE_LOCATION);
		}
	}

	protected void setupViews() {
		// TODO Auto-generated method stub
		mPhoto = (ImageButton)this.findViewById(R.id.photo);

		mBtnComplete = (Button)this.findViewById(R.id.btn_complete);


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
            if (resultCode == Activity.RESULT_OK) {
                if (resultCode == Activity.RESULT_OK) {
                    Bitmap bitmap = BitmapHelper.getThumbBitmap(Const.IMAGE_TEMP_FILE_LOCATION, 720, 720);
                    FileOutputStream b = null;
                    String name = DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
                    String bashPath = Environment.getExternalStorageDirectory() + "/.ls/image/";
                    File file = new File(bashPath);
                    file.mkdirs();// 创建文件夹
                    String fileName = bashPath + name;

                    try {
                        b = new FileOutputStream(fileName);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            b.flush();
                            b.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    requestCode = 103;
                    String path = userInfo.getTime() + "/" + userInfo.getXzqhbm() + "/" + userInfo.getFather_card_id()
                            + "/" + requestCode + "/" + userInfo.getIdNumber() + "-" + requestCode + ".jpg";
                    Bitmap bmp = BitmapUtils.getFileImage(fileName);
                    name = userInfo.getIdNumber() + "-" + requestCode + ".jpg";
                    if (bmp != null) {
                        saveAttachment(name, path, bmp, requestCode, userInfo.getIdNumber(), userInfo.getCheck_task_id());
                        mBitmap = bmp;
                        mPhoto.setImageBitmap(bmp);
                    } else {
                        saveAttachment(name, path, bmp, requestCode, userInfo.getIdNumber(), userInfo.getCheck_task_id());
                    }
                }
            }
        }
	}


	private void save() {

		if(mBitmap == null) {
			ToastUtils.showLongToast("照片未更新");
			return;
		}

		finish();
	}

    private void saveAttachment(String name, String path, Bitmap content, int type, String card_id, String task_id) {
        Attachment attachment = new Attachment();
        attachment.setUserId(mUserId);
        attachment.setName(name);
        attachment.setPath(path);
        attachment.setContent(content);
        attachment.setType(type);
        attachment.setCheck_task_id(task_id);
        attachment.setCard_id(card_id);
        DBHelper.getInstance().insertOrUpdateAttachment(attachment);

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
