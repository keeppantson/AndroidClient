package com.zgmz.ls.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

import com.zgmz.ls.R;
import com.zgmz.ls.base.Const;
import com.zgmz.ls.base.SubActivity;
import com.zgmz.ls.db.DBHelper;
import com.zgmz.ls.model.Attachment;
import com.zgmz.ls.model.FamilyBase;
import com.zgmz.ls.model.SimpleUserInfo;
import com.zgmz.ls.utils.BitmapHelper;
import com.zgmz.ls.utils.FileUtils;
import com.zgmz.ls.utils.ToastUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.zgmz.ls.ui.LoginActivity.TEST_XZBM;

public class YearVideoActivity extends SubActivity implements OnClickListener{

	ImageButton mVideo;
	
	Button mBtnComplete;
	
	Attachment mAttachment;

	String Check_Task_ID;
	SimpleUserInfo info;
	String Card_ID;
	String Type = String.valueOf(Attachment.TYPE_VIDEO);
	
	int mUserId = 0;
	Bitmap mBitmap = null;

	@Override
	protected void onConfigrationTitleBar() {
		// TODO Auto-generated method stub
		super.onConfigrationTitleBar();
		setTitleBarTitleText(R.string.title_year_video);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.year_video);
		onNewIntent(getIntent());
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		info = (SimpleUserInfo) intent.getSerializableExtra(Const.KEY_USER_INFO);
		Type = intent.getStringExtra(Const.KEY_ATTACHMENT_TYPE);
		if(info != null) {
			mUserId = info.getUserId();
			Card_ID = info.getIdNumber();
			Check_Task_ID = info.getCheck_task_id();
			loadYearVideo();
		}
		else {
			ToastUtils.showLongToast("用户不存在！");
			finish();
		}
	}
	
	private void loadYearVideo() {
		mAttachment = DBHelper.getInstance().getAttachment(Card_ID, Check_Task_ID, Type);
		if(mAttachment == null) {
			mAttachment = new Attachment();
			mAttachment.setCheck_task_id(Check_Task_ID);
			mAttachment.setCard_id(Card_ID);
			mAttachment.setPath(info.getAttachment_path());
			mAttachment.setType(Integer.parseInt(Type));
			mAttachment.setContent(null);
		}
        FamilyBase family = DBHelper.getInstance().getFamilyWithTaskID(Check_Task_ID);
        info.setTime(family.getSqrq());
		updateVideo();
	}
	
	private void updateVideo() {
		Attachment attach = mAttachment;
		if(attach != null && attach.getContent() != null) {
			//FileUtils.writeToFile(attach.getContent().toString(), Const.VIDEO_TEMP_FILE_LOCATION);
			getImageFromVideoFile(Const.VIDEO_TEMP_FILE_LOCATION);//实例化File对象，文件路径为"/sdcard/.ls/video/temp.3gp"
			FileUtils.deleteFile(Const.VIDEO_TEMP_FILE_LOCATION);
		}
	}
	
	@Override
	protected void setupViews(View view) {
		// TODO Auto-generated method stub
		mVideo = (ImageButton)view.findViewById(R.id.video);
		mBtnComplete = (Button)view.findViewById(R.id.btn_complete);
		mVideo.setOnClickListener(this);
		mBtnComplete.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
		case R.id.video:
			startCameraActivity();
			break;
		case R.id.btn_complete:
			save();
			break;
		}
		
	}


	private static final int REQUEST_CODE_VIDEO = 0x4002;

	void getImageFromVideoFile(String path) {
		MediaMetadataRetriever mmr = new MediaMetadataRetriever();//实例化MediaMetadataRetriever对象
		File file = new File(path);//实例化File对象，文件路径为"/sdcard/.ls/video/temp.3gp"
		if(file.exists()) {
			mmr.setDataSource(file.getAbsolutePath());//设置数据源为该文件对象指定的绝对路径
			Bitmap bitmap = mmr.getFrameAtTime();//获得视频第一帧的Bitmap对象

			if (bitmap != null) {
				mBitmap = bitmap;
				mVideo.setImageBitmap(bitmap);
			}
		}
	}
	String vod_content;
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode == REQUEST_CODE_VIDEO) {
			if(resultCode == Activity.RESULT_OK) {
				getImageFromVideoFile(Const.VIDEO_TEMP_FILE_LOCATION);//实例化File对象，文件路径为"/sdcard/.ls/video/temp.3gp"
                // 保存Content
				vod_content = FileUtils.getStringFromFile(Const.VIDEO_TEMP_FILE_LOCATION);
				if(vod_content != null) {
					// TODO
					mAttachment.setContent(mBitmap);
				}
			}
		}
	}
	
	
	private void save() {
		
		if(mBitmap == null) {
			ToastUtils.showLongToast("视频未更新");
			return;
		}
		mAttachment.setName(info.getIdNumber()+"-1.3gp");
        mAttachment.setType(1);
		mAttachment.setPath(info.getTime() + "/" + TEST_XZBM +
				"/"+ info.getIdNumber() + "/1/"+ info.getIdNumber() + "-1.3gp");
		if(DBHelper.getInstance().insertOrUpdateAttachment(mAttachment)){
			ToastUtils.showLongToast("保存成功");
		}
		else {
			ToastUtils.showLongToast("保存失败");
		}
		FileUtils.CopySdcardFile("/sdcard/.ls/video/temp.3gp", "/sdcard/.ls/video/" + info.getIdNumber() + "-1.3gp");
        finish();
	}
	
	
	private boolean sevaYearVideo(Bitmap bitmap) {
		if(bitmap == null) return false;
		FileOutputStream b = null;
		String name = "year"+ mUserId;
		File file = new File(Const.VIDEO_BASE_LOCATION);
		file.mkdirs();// 创建文件夹
		String fileName = Const.VIDEO_BASE_LOCATION + name + ".3gp";

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
		Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Const.VIDEO_URI);
		startActivityForResult(intent, REQUEST_CODE_VIDEO);
	}
}
