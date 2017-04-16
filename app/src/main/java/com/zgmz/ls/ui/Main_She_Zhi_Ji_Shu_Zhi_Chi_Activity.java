package com.zgmz.ls.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.zgmz.ls.R;

public class Main_She_Zhi_Ji_Shu_Zhi_Chi_Activity extends Activity implements View.OnClickListener {

	ImageButton jian_kuang;
	ImageButton guanyu;

	ImageButton mBtnRuHuHeCha;

	ImageButton mBtnXinZengBaoSong;

	ImageButton mBtnSheZhi;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.she_zhi_ji_shu_zhi_chi);

		mBtnRuHuHeCha = (ImageButton)this.findViewById(R.id.ru_hu_he_cha_bottom);
		mBtnXinZengBaoSong = (ImageButton)this.findViewById(R.id.xin_zeng_bao_song_bottom);
		mBtnSheZhi = (ImageButton)this.findViewById(R.id.she_zhi);
		jian_kuang  = (ImageButton)this.findViewById(R.id.jian_kuang_1);
		guanyu  = (ImageButton)this.findViewById(R.id.she_zhi_guan_yu_2);
		mBtnRuHuHeCha.setOnClickListener(this);
		mBtnXinZengBaoSong.setOnClickListener(this);
		mBtnSheZhi.setOnClickListener(this);
        jian_kuang.setOnClickListener(this);
		guanyu.setOnClickListener(this);
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
            case R.id.she_zhi_guan_yu_2:
                startSheZhiGuanYuActivity();
                break;


		}
	}


	private void startRuHuHeChaActivity() {
		Intent intent = new Intent();
		intent.setClass(this, DoorActivity.class);
		startActivityForResult(intent, REQUEST_CODE_ID_RECOGNIZE);
	}

    private void startSheZhiGuanYuActivity() {
        Intent intent = new Intent();
        intent.setClass(this, Main_She_Zhi_Guan_Yu_Activity.class);
        startActivity(intent);
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
