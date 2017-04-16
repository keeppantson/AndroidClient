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

	public static final int REQUEST_CODE_HUKOUBEN = 101;

	public static final int REQUEST_CODE_CAMERA = 103;

	public static final int REQUEST_CODE_SHENFENZHENG = 102;

	Button mShenfenzheng;

	// Button mCamera;

    Button mHukouben;

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
        mShenfenzheng = (Button) findViewById(R.id.shenfenzheng);
		//mCamera = (Button) findViewById(R.id.camera);
        mHukouben = (Button) findViewById(R.id.hukouben);

        mShenfenzheng.setOnClickListener(this);
		//mCamera.setOnClickListener(this);
        mHukouben.setOnClickListener(this);
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
		case R.id.shenfenzheng:
            startShengfenzhenActivity();
			break;
		//case R.id.camera:
		//	startCameraActivity();
		//	break;
        case R.id.hukouben:
            startCameraActivity();
            break;
		default:
			break;
		}

		dismiss();
	}

    private void startShengfenzhenActivity() {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Const.IMAGE_URI);
        getActivity().startActivityForResult(openCameraIntent, REQUEST_CODE_SHENFENZHENG);
    }

    private void startHukoubenActivity() {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Const.IMAGE_URI);
        getActivity().startActivityForResult(openCameraIntent, REQUEST_CODE_HUKOUBEN);
    }

	private void startCameraActivity() {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Const.IMAGE_URI);
        getActivity().startActivityForResult(openCameraIntent, REQUEST_CODE_CAMERA);
	}

}
