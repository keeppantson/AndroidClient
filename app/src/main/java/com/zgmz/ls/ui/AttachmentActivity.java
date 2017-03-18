package com.zgmz.ls.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import com.zgmz.ls.R;
import com.zgmz.ls.base.Const;
import com.zgmz.ls.base.SubActivity;
import com.zgmz.ls.db.DBHelper;
import com.zgmz.ls.dialog.AddPhotoDialog;
import com.zgmz.ls.model.Attachment;
import com.zgmz.ls.model.SimpleUserInfo;
import com.zgmz.ls.utils.BitmapHelper;
import com.zgmz.ls.utils.ToastUtils;
import com.zgmz.ls.view.CellItemInfo;
import com.zgmz.ls.view.SingleCellLayout;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;

public class AttachmentActivity extends SubActivity implements OnClickListener, OnLongClickListener {

	View mRoot;
	
	SingleCellLayout mSingleCellLayout;
	
	ImageView mImageView;

	List<CellItemInfo> mCellItemInfos = new ArrayList<CellItemInfo>();
	
	int mUserId;

	@Override
	protected void onConfigrationTitleBar() {
		// TODO Auto-generated method stub
		super.onConfigrationTitleBar();
		setTitleBarTitleText(R.string.title_attachment);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.attachment);
		onNewIntent(getIntent());
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		SimpleUserInfo userInfo = (SimpleUserInfo) intent.getSerializableExtra(Const.KEY_USER_INFO); 
		if(userInfo != null) {
			mUserId = userInfo.getUserId();
			loadData();
		}
		else {
			ToastUtils.showShortToast("未知用户");
			finish();
		}
	}

	@Override
	protected void setupViews(View view) {
		// TODO Auto-generated method stub
		mRoot = view;
		mSingleCellLayout = (SingleCellLayout) view.findViewById(R.id.cell_layout);
		mImageView = (ImageView) view.findViewById(R.id.image);
		mImageView.setVisibility(View.GONE);
		mImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				hideImage();
			}
		});
		initCellContent();
	}
	
	
	public void hideImage() {
		mImageView.setVisibility(View.GONE);
		mImageView.setImageDrawable(null);
	}
	
	public void showImage(String path) {
		if(TextUtils.isEmpty(path)) return;
		mImageView.setVisibility(View.VISIBLE);
		Bitmap bmp = BitmapHelper.getThumbBitmap(path, mRoot.getWidth(), mRoot.getHeight());
		if(bmp != null) {
			mImageView.setImageBitmap(bmp);
		}
		else 
			mImageView.setImageResource(R.drawable.photo_image);
			
	}

	public void initCellContent() {
		if (mSingleCellLayout.getSingleCellContainer().getChildCount() > 0)
			return;
		SingleCellLayout cellLayout = mSingleCellLayout;
		for (int x = 0; x < cellLayout.getCountX(); x++) {
			for (int y = 0; y < cellLayout.getCountY(); y++) {
				View Image = View.inflate(this, R.layout.attach_item, null);
				SingleCellLayout.LayoutParams lp = new SingleCellLayout.LayoutParams(x, y);
				Image.setOnClickListener(this);
				Image.setOnLongClickListener(this);
				cellLayout.addViewToCellLayout(Image, x, y, lp, true);
			}
		}
	}

	public void loadData() {
		// TODO Auto-generated method stub
		List<Attachment> attachs = DBHelper.getInstance().getAttachments(mUserId);
		List<CellItemInfo> data = new ArrayList<CellItemInfo>();
		CellItemInfo info;
		Attachment attach;
		int size = 0;
		if(attachs != null) {
			size = attachs.size();
			for (int i = 0; i < size; i++) {
				info = new CellItemInfo();
				attach = attachs.get(i);
				attach.setType(Attachment.TYPE_IMAGE);
				attach.setResId(R.drawable.photo_image);
				info.setX(i % mSingleCellLayout.getCountX());
				info.setY(i / mSingleCellLayout.getCountX());
				info.setObj(attach);
				data.add(info);
			}
		}
		
		info = new CellItemInfo();
		attach = new Attachment();
		attach.setType(Attachment.TYPE_ADD);
		attach.setResId(R.drawable.photo_add);
		info.setX(size % mSingleCellLayout.getCountX());
		info.setY(size / mSingleCellLayout.getCountX());
		info.setObj(attach);
		data.add(info);
		updateAttachments(data);
	}

	private void updateAttachments(List<CellItemInfo> list) {
		mCellItemInfos.clear();
		mCellItemInfos.addAll(list);
		updateCellViews();
	}

	private void updateCellViews() {
		mSingleCellLayout.clear();
		mSingleCellLayout.addCellList(mCellItemInfos);
	}

	
	CellItemInfo mCurCellItemInfo;
	
	@Override
	public boolean onLongClick(View v) {
		// TODO Auto-generated method stub

		CellItemInfo info = (CellItemInfo) v.getTag();
		if (info != null) {
			removeCell(info);
			return true;
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		CellItemInfo info = (CellItemInfo) v.getTag();
		mCurCellItemInfo = info;
		if (info != null) {
			Attachment attach = (Attachment) info.getObj();
			if (attach.getType() == Attachment.TYPE_ADD) {
				showAddAttachmentDialog();
			}
			else if(attach.getType() == Attachment.TYPE_IMAGE){
				showImage(attach.getPath());
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == AddPhotoDialog.REQUEST_CODE_ALBUM) {
			if (resultCode == Activity.RESULT_OK) {
				Uri selectedImage = data.getData();
				String path = null;
				try {
					String[] filePathColumn = { MediaStore.Images.Media.DATA };
					Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
					cursor.moveToFirst();
					int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
					path = cursor.getString(columnIndex);
					cursor.close();
				} catch (Exception e) {

				}
				String name = DateFormat.format("yyyyMMdd_hhmmss",Calendar.getInstance(Locale.CHINA)) + ".jpg";
				saveAttachment(name, path);
			}
		} else if (requestCode == AddPhotoDialog.REQUEST_CODE_CAMERA) {
			if (resultCode == Activity.RESULT_OK) {
//				Bundle bundle = data.getExtras();
//				Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
				Bitmap bitmap = BitmapHelper.getThumbBitmap(Const.IMAGE_TEMP_FILE_LOCATION, 720, 720);
				FileOutputStream b = null;
				String name = DateFormat.format("yyyyMMdd_hhmmss",Calendar.getInstance(Locale.CHINA)) + ".jpg";
				String bashPath = Environment.getExternalStorageDirectory()+"/.ls/image/";
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
				
				saveAttachment(name, fileName);
			}
		}
	}

	private void removeCell(CellItemInfo info) {
		if (mCellItemInfos.contains(info)) {
			if(DBHelper.getInstance().deleteAttachment((Attachment) info.getObj())) {
				ToastUtils.showShortToast("删除成功");
				mSingleCellLayout.clear();
				loadData();
			}
		}
	}

	AddPhotoDialog mAddPhotoDialog;

	private void showAddAttachmentDialog() {
		if (mAddPhotoDialog == null) {
			mAddPhotoDialog = new AddPhotoDialog(this);
		}
		mAddPhotoDialog.show();
	}

	
	private void saveAttachment(String name, String path) {
		Attachment attachment = new Attachment();
		attachment.setUserId(mUserId);
		attachment.setName(name);
		attachment.setPath(path);
		if(DBHelper.getInstance().insertAttachment(attachment)) {
			DBHelper.getInstance().updateAttachmentFlag(mUserId);
			loadData();
		}
		
	}
	
}
