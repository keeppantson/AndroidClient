package com.zgmz.ls.ui;

import com.zgmz.ls.R;
import com.zgmz.ls.base.SubActivity;
import com.zgmz.ls.utils.Family;
import com.zgmz.ls.utils.FamilyInfo;
import com.zgmz.ls.utils.FamilyMember;
import com.zgmz.ls.utils.Meterial;
import com.zgmz.ls.utils.RestClient;
import com.zgmz.ls.utils.RestResult;

import android.os.Bundle;
import android.util.Base64;
import android.view.View;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class AboutActivity extends SubActivity {

	@Override
	protected void onConfigrationTitleBar() {
		// TODO Auto-generated method stub
		super.onConfigrationTitleBar();
		setTitleBarTitleText(R.string.title_about);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		Thread thread=new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try {
//					RestResult res = TestUploadFamily();
//					System.out.println(String.format("mx: server = %s, rpc = %s", res.server, res.rpc));
//
//					Thread.sleep(1000);
//					TestGetProgress(res.body.getString("token"), res.server, res.rpc);
//
//					Thread.sleep(10000);
//					TestGetProgress(res.body.getString("token"), res.server, res.rpc);

					TestUploadFile("115.28.185.19", "localhost:50001");

				} catch (Exception e) {
					e.printStackTrace();
				}
				//TestUploadFile();
				//TestGetProgress();
				//RestClient client = new RestClient("115.28.185.19", "mx", "pwd", "1234567890");
				//RestResult result = client.GetConfig();
				//System.out.println("mx: get result:" + result.body.toString());

			}
		});
		thread.start();

		setContentView(R.layout.about);
	}
	
	@Override
	protected void setupViews(View view) {
		// TODO Auto-generated method stub
		super.setupViews(view);
	}

	private void TestGetProgress(String token, String server, String rpc) {
		RestClient client = new RestClient("115.28.185.19", "mx", "pwd", "1234567890");
		RestResult result = client.GetProgress(token, server, rpc);
		System.out.println("mx: get result:" + result.body.toString());
	}

	private void TestUploadFile(String server, String rpc) throws Exception {
		RestClient client = new RestClient("115.28.185.19", "mx", "pwd", "1234567890");

		ArrayList<String> path = new ArrayList<String>();
		ArrayList<byte[]> content = new ArrayList<byte[]>();
		// file1
		path.add("2017-11-12/441502001001/231512198109118873/101/231512198109118873-01.jpg");
		content.add("d2Vxd2UxZXF3ZXdxZXE=".getBytes());

		//file2
		path.add("2017-11-12/441502001001/231512198109118873/101/231512198109118873-01.mp4");
		content.add("d2llaHdxaWVxd2VqcXdlbHF3ZXdxZXF3ZXF3ZXF3".getBytes());

		RestResult result = client.UploadFile(path.get(0), content.get(0), server, rpc);
		System.out.println("mx: get result:" + result.body.toString());
	}

	private RestResult TestUploadFamily() throws Exception {
		RestClient client = new RestClient("115.28.185.19", "mx", "pwd", "1234567890");
		Family f = new Family();
		f.fInfo = new FamilyInfo();
		f.fInfo.xzqhdm = "123456789012";
		f.fInfo.sqrxm = "翔";
		f.fInfo.sqrsfzh = "340000190000001000";
		f.fInfo.sqrq = "1900-01-01";
		f.fInfo.jzywlx = "110";
		f.fInfo.zyzpyy = "01";
		f.fInfo.bzrks = 2; //保障人口数
		f.fInfo.jtyzsr = 10000; //家庭月总收入
		f.fInfo.sfynrfp = "是"; //是否已纳入扶贫
		f.fInfo.lxdh = "18800000000"; //联系电话
		f.fInfo.jzdz = "xx市xx路"; //居住地址
		f.fInfo.jzksny = "19000101"; //救助开始年月, 格式为yyyyMM
		f.fInfo.Jzjzny = "20000101"; //救助截止年月, 格式为yyyyMM

		FamilyMember fm0 = new FamilyMember();
		fm0.cyxm = "艾米";
		fm0.cysfzh = "340000190000001000";
		fm0.xb = "女";
		fm0.nl = 20;
		fm0.ldnl = "01";
		fm0.jkzk = "10";
		fm0.cjlb = "61";
		fm0.whcd = "10";
		fm0.sfzx = "01";
		fm0.ysqrgx = "准女友";
		fm0.sfzzp = "是";
		fm0.ryzt = "01";
		fm0.hcdxzp = "是";
		fm0.hcsfz = "是";
		fm0.hchkb = "是";
		f.cyxx.add(fm0);

		FamilyMember fm1 = new FamilyMember();
		fm1.cyxm = "小米";
		fm1.cysfzh = "340000190000001000";
		fm1.xb = "男";
		fm1.nl = 20;
		fm1.ldnl = "01";
		fm1.jkzk = "10";
		fm1.cjlb = "61";
		fm1.whcd = "10";
		fm1.sfzx = "01";
		fm1.ysqrgx = "准男友";
		fm1.sfzzp = "是";
		fm1.ryzt = "01";
		fm1.hcdxzp = "是";
		fm1.hcsfz = "是";
		fm1.hchkb = "是";
		f.cyxx.add(fm1);

		Meterial met0 = new Meterial();
		met0.cllx = "身份证";
		met0.clxx = "/home/path0";
		f.zmclxx.add(met0);

		Meterial met1 = new Meterial();
		met1.cllx = "视频";
		met1.clxx = "/home/video";
		f.zmclxx.add(met1);
		RestResult res = client.UploadFamily(f.ToJSONString());
		System.out.println("mx: receive json body:" + res.body.toString());
		return res;
	}
	
}
