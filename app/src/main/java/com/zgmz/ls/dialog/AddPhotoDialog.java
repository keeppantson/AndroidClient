package com.zgmz.ls.dialog;

import com.zgmz.ls.R;
import com.zgmz.ls.base.Const;

import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;

public class AddPhotoDialog extends BaseDialog implements android.view.View.OnClickListener {

	public static final int REQUEST_CODE_ALBUM = 0x12001;

	public static final int REQUEST_CODE_CAMERA = 0x12002;

	Button mAlbum;

	Button mCamera;

	public AddPhotoDialog(Context context) {
		super(context, R.style.Dialog, Gravity.BOTTOM);
		// TODO Auto-generated constructor stub
		getWindow().setWindowAnimations(R.style.Animation_Bottom);
		setCanceledOnTouchOutside(true);
		setContentView();
	}

	@Override
	public void setupViews() {
		// TODO Auto-generated method stub
		mAlbum = (Button) findViewById(R.id.album);
		mCamera = (Button) findViewById(R.id.camera);

		mAlbum.setOnClickListener(this);
		mCamera.setOnClickListener(this);
	}

	@SuppressWarnings("deprecation")
	public void setContentView() {

		View layout = mInflater.inflate(R.layout.dialog_add_photo, null, false);

		Display display = getWindow().getWindowManager().getDefaultDisplay();
		super.setContentView(layout, new LayoutParams(display.getWidth(), LayoutParams.WRAP_CONTENT));
		setupViews();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.album:
			startAlbumActivity();
			break;
		case R.id.camera:
			startCameraActivity();
			break;
		default:
			break;
		}

		dismiss();
	}

	private void startAlbumActivity() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		getActivity().startActivityForResult(intent, REQUEST_CODE_ALBUM);

	}

	private void startCameraActivity() {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Const.IMAGE_URI);
        getActivity().startActivityForResult(openCameraIntent, REQUEST_CODE_CAMERA);
	}

}
