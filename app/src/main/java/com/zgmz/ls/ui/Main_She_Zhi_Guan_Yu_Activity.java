package com.zgmz.ls.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.zgmz.ls.R;

public class Main_She_Zhi_Guan_Yu_Activity extends Activity implements View.OnClickListener {

	ImageButton jian_kuang;

	ImageButton mBtnRuHuHeCha;

	ImageButton mBtnXinZengBaoSong;

	ImageButton mBtnSheZhi;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.she_zhi_guan_yu);

		mBtnRuHuHeCha = (ImageButton)this.findViewById(R.id.ru_hu_he_cha_bottom);
		mBtnXinZengBaoSong = (ImageButton)this.findViewById(R.id.xin_zeng_bao_song_bottom);
		mBtnSheZhi = (ImageButton)this.findViewById(R.id.she_zhi);
		jian_kuang  = (ImageButton)this.findViewById(R.id.jian_kuang_1);
		mBtnRuHuHeCha.setOnClickListener(this);
		mBtnXinZengBaoSong.setOnClickListener(this);
		mBtnSheZhi.setOnClickListener(this);
        jian_kuang.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {

			case R.id.ru_hu_he_cha_bottom:
				startRuHuHeChaActivity();
				break;
			case R.id.xin_zeng_bao_song_bottom:
				startXinZengBaoSongActivity();
				break;
			case R.id.she_zhi:
				startSheZhiActivity();
				break;

            case R.id.jian_kuang_1:
                startSheZhiActivity();
                break;

		}
	}


	private void startRuHuHeChaActivity() {
		Intent intent = new Intent();
		intent.setClass(this, DoorActivity.class);
		startActivityForResult(intent, REQUEST_CODE_ID_RECOGNIZE);
	}


	private static final int REQUEST_CODE_ID_RECOGNIZE = 0x4001;

	private static final int REQUEST_CODE_FINGER_PRINT = 0x4002;

	private static final int REQUEST_CODE_ID_INPUT = 0x4003;
	private void startXinZengBaoSongActivity() {
		Intent intent = new Intent();
		intent.setClass(this, Main_Lu_Ru_Activity.class);
		startActivityForResult(intent, REQUEST_CODE_FINGER_PRINT);
	}

	private void startSheZhiActivity() {
        finish();
	}
	@Override
	public void onBackPressed() {
		finish();
	}

}
