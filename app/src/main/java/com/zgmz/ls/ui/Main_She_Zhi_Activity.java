package com.zgmz.ls.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;

import com.zgmz.ls.R;
import com.zgmz.ls.base.BottomTabActivity;
import com.zgmz.ls.ui.fragment.TabCheckFragment;
import com.zgmz.ls.ui.fragment.TabRecordFragment;
import com.zgmz.ls.ui.fragment.TabSettingsFragment;
import com.zgmz.ls.utils.ToastUtils;

public class Main_She_Zhi_Activity extends Activity implements View.OnClickListener {


	ImageButton mBtnRuHuHeCha;

	ImageButton mBtnXinZengBaoSong;

	ImageButton mBtnSheZhi;
	ImageButton mBtnSheZhiGuanYu;
	ImageButton mBtnSheZhiJiShuZhiChi;
	ImageButton mBtnSheZhiMiMa;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.she_zhi_jian_kuang);

		mBtnRuHuHeCha = (ImageButton)this.findViewById(R.id.ru_hu_he_cha_bottom);
		mBtnXinZengBaoSong = (ImageButton)this.findViewById(R.id.xin_zeng_bao_song_bottom);
		mBtnSheZhi = (ImageButton)this.findViewById(R.id.she_zhi);
		mBtnSheZhiGuanYu = (ImageButton)this.findViewById(R.id.she_zhi_guan_yu_2);
		mBtnSheZhiJiShuZhiChi = (ImageButton)this.findViewById(R.id.ji_shu_zhi_chi);
		mBtnSheZhiMiMa = (ImageButton)this.findViewById(R.id.li_xian_mi_ma);

		mBtnRuHuHeCha.setOnClickListener(this);
		mBtnXinZengBaoSong.setOnClickListener(this);
		mBtnSheZhi.setOnClickListener(this);
		mBtnSheZhiGuanYu.setOnClickListener(this);
        mBtnSheZhiJiShuZhiChi.setOnClickListener(this);
        mBtnSheZhiMiMa.setOnClickListener(this);
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
				// DO Nothing
				break;

			case R.id.she_zhi_guan_yu_2:
				startSheZhiActivity();
				break;
            case R.id.ji_shu_zhi_chi:
                ji_shu_zhi_chi();
                break;
            case R.id.li_xian_mi_ma:
                li_xian_mi_ma();
                break;

		}
	}

    private void li_xian_mi_ma() {
        Intent intent = new Intent();
        intent.setClass(this, PasswordActivity.class);
        startActivity(intent);
    }
    private void ji_shu_zhi_chi() {
        Intent intent = new Intent();
        intent.setClass(this, Main_She_Zhi_Ji_Shu_Zhi_Chi_Activity.class);
        startActivity(intent);
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
		Intent intent = new Intent();
		intent.setClass(this, Main_She_Zhi_Guan_Yu_Activity.class);
		startActivityForResult(intent, REQUEST_CODE_ID_INPUT);
	}
	@Override
	public void onBackPressed() {
		finish();
	}

}
