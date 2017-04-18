package com.zgmz.ls.ui;

import com.zgmz.ls.AppContext;
import com.zgmz.ls.R;
import com.zgmz.ls.base.BaseSplashActivity;
import com.zgmz.ls.helper.AccountHelper;
import com.zgmz.ls.utils.ConfigClient;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SplashActivity extends BaseSplashActivity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void initSplash() {
		// TODO Auto-generated method stub
		setDuration(3000);
		setLayoutId(R.layout.splash);
	}

	@Override
	public void setupViews() {
		// TODO Auto-generated method stub
//		TextView view = (TextView) findViewById(R.id.name);

	}
	
	
	@Override
	protected void gotoTargetActivity() {
		// TODO Auto-generated method stub
		
//		super.gotoTargetActivity();
//		startTargetActivity();

		try {
			if (upgradeIfNeeded())
			{
				return;
			}
		} catch (Exception e) {
			throw new RuntimeException("upgrade apk failed with exception:" + e.toString());
		}

		if(AccountHelper.getInstance().isLogined()) {
			super.gotoTargetActivity();
		}
		else {
			AccountHelper.startLoginUI();
		}
	}


	// return true if upgrade is needed
	private Boolean upgradeIfNeeded() throws Exception {
		System.out.println("mx: enter upgradeIfNeeded");
		PackageInfo pInfo = null;
		String version = null;
		pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
		version = pInfo.versionName;

		final ConfigClient client = new ConfigClient();
		Thread thread=new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try {
					client.Init();
				} catch (IOException e) {
					throw new RuntimeException(e.toString());
				} catch (JSONException e) {
					throw new RuntimeException(e.toString());
				} catch (Exception e) {
					throw new RuntimeException(e.toString());
				}

			}
		});
		thread.start();
		thread.join();

		version = version + ".apk";
		if (version.equals(client.remoteApkName))
		{
			System.out.println("mx: upgradeIfNeeded: version equal. No need to upgrade! cur:" + version);
			return false;
		}
		else
		{
			System.out.println("mx: upgradeIfNeeded: version not equal. Upgrading! cur:" + version + " remote:" + client.remoteApkName);
			ExecutorService executor = Executors.newSingleThreadExecutor();
			Callable<File> callable = new Callable<File>() {
				@Override
				public File call() throws Exception {
					return client.DowloadApk(client.remoteApkName);
				}
			};
			Future<File> future = executor.submit(callable);
			// future.get() returns 2 or raises an exception if the thread dies, so safer
			executor.shutdown();
			File newApk = future.get();

			//upgrade apk
			System.out.println("mx: upgradeIfNeeded: Upgrade with apk:" + Uri.fromFile(newApk).toString());
			Intent promptInstall = new Intent(Intent.ACTION_VIEW)
					.setDataAndType(Uri.fromFile(newApk),
							"application/vnd.android.package-archive");
			promptInstall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(promptInstall);
			//AppContext.getAppContext().startActivity(promptInstall);
			return true;
		}
	}
	
	protected void startTargetActivity() {
		Intent intent = new Intent();
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setClass(this, SearchActivity.class);
		startActivity(intent);
	}
	

}
