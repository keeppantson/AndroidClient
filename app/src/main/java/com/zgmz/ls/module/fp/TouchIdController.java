package com.zgmz.ls.module.fp;


import java.io.File;

import com.zgmz.ls.module.fp.SerialPortController.OnDataReceiver;
import com.zgmz.ls.utils.ToastUtils;

import android.graphics.Bitmap;
import android.os.Handler;

public class TouchIdController extends SerialPortController implements TouchIdFunction, OnDataReceiver {
	
	private static final int DEFAULT_BAUDRATE = 19200;
	
	private static final String PATH = "/dev/ttyMT3";
	private PowerOperate powerOperate;
	Handler mHandler;
	
	
	private TouchIdCallback mCallback;
	
	
	public void setCallBack(TouchIdCallback callback) {
		mCallback = callback;
	}
	
	public TouchIdController() {
		this(DEFAULT_BAUDRATE);
	}
	
	public TouchIdController( int baudrate) {
		this(new File(PATH), baudrate);
		// TODO Auto-generated constructor stub
	}

	public TouchIdController(File device, int baudrate) {
		super(device, baudrate);
		// TODO Auto-generated constructor stub
		setOnDataReceiver(this);
		mHandler = new Handler();
		powerOperate = new PowerOperate();
	}
	

	@Override
	public int getFixedDataLength(Command cmd) {
		if(cmd.getCmdId() == TouchIdCommand.CMD_CAPTURE) {
			return 10240;
		}
		return 8;
	}

	@Override
	public int getUnfixedDataLength(Command cmd, byte[] buffer) {
		// TODO Auto-generated method stub
		if(cmd.getCmdId() == TouchIdCommand.CMD_CAPTURE || cmd.getCmdId() == TouchIdCommand.CMD_COLLECT_AND_EXTRACT
				|| cmd.getCmdId() == TouchIdCommand.CMD_GET_USER_EIGENVALUE){
			//int dataLength = (0x0000FFFF & (0x0000FF00 & (int)(buffer[2]<<8)+(int)( buffer[3])));
			int dataLength = ((0xFF00 & (buffer[2]<<8)) + (0x00FF & buffer[3]));
			return dataLength;
		}
		return 0;
	}
	
	
	private void reponseCallback(Runnable runnable) {
		if(mCallback != null) {
//			mHandler.postDelayed(runnable, 0);
			mHandler.post(runnable);
		}
	}
	
	private void callbackTimeout(final Command cmd) {
		reponseCallback(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				mCallback.onTimeout(cmd);
			}
		});
	}
	
	@Override
	public void onReceiverData(final Command cmd, byte[] data) {
		// TODO Auto-generated method stub
	//	Logger.i("onReceiverData data = " + HexTools.bytesToHexString(data) + "dataLenth = " + data.length);
//		ToastUtils.showShortToast("receiver: " + HexTools.bytesToHexString(data));
		switch(cmd.getCmdId()) {
			case TouchIdCommand.CMD_ENROLL1:
			{
				final byte ret = data[4];
				reponseCallback(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(ret == 0x00) {
//							ToastUtils.showShortToast("第一次录入成功");
						}
						else if(ret ==0x01) {
							ToastUtils.showShortToast("指纹录入失败");
						}
						else if(ret==0x06) {
							ToastUtils.showShortToast("此ID用户已存在");
						}
						else if(ret==0x07) {
							ToastUtils.showShortToast("用户已存在");
						}
						else if(ret == 0x08) {
							callbackTimeout(cmd);
							ToastUtils.showShortToast("指纹录入超时");
						}
						mCallback.onResponseEnrollFirst(cmd, ret);
					}
				});
				
				
				
			}
				break;
			case TouchIdCommand.CMD_ENROLL2:
			{
				final byte ret = data[4];
				reponseCallback(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(ret == 0x00) {
//							ToastUtils.showShortToast("请第三次录入指纹");
						}
						else if(ret ==0x01) {
							ToastUtils.showShortToast("指纹录入失败");
						}
						else if(ret == 0x08) {
							callbackTimeout(cmd);
							ToastUtils.showShortToast("指纹录入超时");
						}
						mCallback.onResponseEnrollSecond(cmd, ret);
					}
				});
				
				
			}
				break;
			case TouchIdCommand.CMD_ENROLL3:
			{
				final byte ret = data[4];
				reponseCallback(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(ret == 0x00) {
//							ToastUtils.showShortToast("指纹录入成功");
						}
						else if(ret ==0x01) {
							ToastUtils.showShortToast("指纹录入失败");
						}
						else if(ret == 0x08) {
							callbackTimeout(cmd);
							ToastUtils.showShortToast("指纹录入超时");
						}
						mCallback.onResponseEnrollThird(cmd, ret);
					}
				});
				
				
			}
				break;
			case TouchIdCommand.CMD_REMOVE_USER:
			{
				final byte ret = data[4];
				reponseCallback(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub

						if(ret == 0x00) {
//							ToastUtils.showShortToast("用户删除成功");
						}
						else if(ret == 0x01) {
							ToastUtils.showShortToast("用户删除失败");
						}
						else if(ret == 0x05) {
							ToastUtils.showShortToast("无此用户");
						}
						else if(ret == 0x08) {
							callbackTimeout(cmd);
						}
						mCallback.onResponseRemoveUser(cmd, ret);
					}
				});
				
				
			}
				break;
			case TouchIdCommand.CMD_REMOVE_ALL:
			{
				final byte ret = data[4];
				reponseCallback(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub

						if(ret == 0x00) {
//							ToastUtils.showShortToast("删除所有用户成功");
						}
						else if(ret == 0x01) {
							ToastUtils.showShortToast("删除失败");
						}
						mCallback.onResponseRemoveAll(cmd, ret);
					}
				});
				
				
			}
				break;
			case TouchIdCommand.CMD_INDENTIFY:
			{
				final byte ret = data[4];
				reponseCallback(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(ret == 0x00) {
						}
						else if(ret == 0x01) {
							ToastUtils.showShortToast("指纹校验：错误");
						}
						else if(ret == 0x05) {
							ToastUtils.showShortToast("无此用户");
						}
						else if(ret == 0x08) {
							callbackTimeout(cmd);
							ToastUtils.showShortToast("指纹识别超时");
						}
						else {
							
						}
//						mCallback.onResponseEnrollSecond(cmd, ret);
					}
				});

			}
				break;
			case TouchIdCommand.CMD_SEARCH:
			{
				final byte ret = data[4];
				final int userId = ((data[2]&0xFF)<<8) + data[3];
				cmd.setUserId(userId);
				
				reponseCallback(new Runnable() {
					
					@Override
					public void run() {
						if(ret == 0x00) {
//							ToastUtils.showLongToast("search userId = " + userId);
						}
						else if(ret == 0x01) {
//							ToastUtils.showLongToast("search userId = " + userId);
						}
						else if(ret == 0x05) {
							ToastUtils.showShortToast("无此用户");
						}
						else if (ret == 0x08) {
							ToastUtils.showShortToast("录入超时");
						}
						// TODO Auto-generated method stub
						mCallback.onResponseSearch(cmd, ret);
					}
				});
				
			}
				break;
			case TouchIdCommand.CMD_CAPTURE:
			{
				final byte ret = data[4];
				if(ret == 0x00) {
					int len = data[2]<<8+data[3];
					
					if(len > 0) {
						byte[] rawData = new byte[len];
						System.arraycopy(data, 9, rawData, 0, len);
						final Bitmap bmp = BitmapConverter.gray2ARGB(BitmapConverter.extendGray(rawData), 96, 96);
						reponseCallback(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								mCallback.onCaptureImage(cmd, ret, bmp );
							}
						});
					}
				}
				else {
					reponseCallback(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							mCallback.onCaptureImage(cmd, ret, null);
						}
					});
				}
				
				
			}
				break;
			case TouchIdCommand.CMD_COLLECT_AND_EXTRACT:
			{
				final byte ret = data[4];
				if(ret == 0x00) {
					int len = getUnfixedDataLength(cmd,data);
					if(len == 196) {
						len = len-3;
						final byte[] rawData = new byte[len];
						System.arraycopy(data, 12, rawData, 0, len);
						reponseCallback(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								mCallback.onCollectAndExtract(cmd, ret, rawData);
							}
						});
					}
				}
				else {
					reponseCallback(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							mCallback.onCollectAndExtract(cmd, ret, null);
						}
					});
				}
				
				
			}
			break;
			case TouchIdCommand.CMD_GET_USER_EIGENVALUE:
			{
				final byte ret = data[4];
				if(ret == 0x00) {
					int len = getUnfixedDataLength(cmd,data);
					if(len == 196) {
						len = len-3;
						final byte[] rawData = new byte[len];
						System.arraycopy(data, 12, rawData, 0, len);
						reponseCallback(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								mCallback.onReponseUserEigenValue(cmd, ret, rawData);
							}
						});
					}
				}
				else {
					reponseCallback(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							mCallback.onReponseUserEigenValue(cmd, ret, null);
						}
					});
				}
				
				
			}
			break;
			case TouchIdCommand.CMD_UPLOAD_AND_COMPARE:
			{
				final byte ret = data[4];
				reponseCallback(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(ret == 0x00) {
//							ToastUtils.showShortToast("指纹比对正确");
						}
						else if(ret == 0x08) {
							callbackTimeout(cmd);
							ToastUtils.showShortToast("指纹采集超时");
						}
						else {
							ToastUtils.showShortToast("指纹比对错误");
						}
						mCallback.onResponseUploadAndCompare(cmd, ret);
					}
				});
				
			}
			break;
		}
		
	}
	
	@Override
	public void onTimeout(Command cmd) {
		// TODO Auto-generated method stub
		callbackTimeout(cmd);
	}
	
	@Override
	public void onError(Command cmd, int errorNo) {
		// TODO Auto-generated method stub
		ToastUtils.showShortToast("onError: " + cmd.getCmdId());
	}
	
	

	@Override
	public void open() {
		powerOperate.enableFingerprintModule_5Volt();
		// TODO Auto-generated method stub
		start();
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		stop();
		powerOperate.disableFingerprintModule_5Volt();
		
	}

	@Override
	public void enroll1(int userId) {
		// TODO Auto-generated method stub
		TouchIdCommand cmd = TouchIdCommand.enroll1((short) userId);
		cmd.setTimeout(10000);
		sendCommand(cmd);
	}
	
	@Override
	public void enroll2(int userId) {
		// TODO Auto-generated method stub
		TouchIdCommand cmd = TouchIdCommand.enroll2((short) userId);
		cmd.setTimeout(10000);
		sendCommand(cmd);
	}
	
	@Override
	public void enroll3(int userId) {
		// TODO Auto-generated method stub
		TouchIdCommand cmd = TouchIdCommand.enroll3((short) userId);
		cmd.setTimeout(10000);
		sendCommand(cmd);
	}

	@Override
	public void verfiy() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void identify(int userId) {
		// TODO Auto-generated method stub
		TouchIdCommand cmd = TouchIdCommand.identify((short) userId);
		cmd.setTimeout(10000);
		sendCommand(cmd);
	}
	
	@Override
	public void search() {
		// TODO Auto-generated method stub
		TouchIdCommand cmd = TouchIdCommand.search();
		cmd.setTimeout(10000);
		sendCommand(cmd);
	}
	
	@Override
	public void removeUser(int userId) {
		// TODO Auto-generated method stub
		TouchIdCommand cmd = TouchIdCommand.removeUser((short) userId);
		cmd.setTimeout(10000);
		sendCommand(cmd);
	}
	
	@Override
	public void removeAll() {
		// TODO Auto-generated method stub
		TouchIdCommand cmd = TouchIdCommand.removeAll();
		cmd.setTimeout(10000);
		sendCommand(cmd);
	}

	@Override
	public void captureImage() {
		// TODO Auto-generated method stub
		TouchIdCommand cmd = TouchIdCommand.captureImage();
		cmd.setTimeout(20000);
		sendCommand(cmd);
	}

	@Override
	public void collectAndExtract() {
		// TODO Auto-generated method stub
		TouchIdCommand cmd = TouchIdCommand.collectAndExtract();
		cmd.setTimeout(10000);
		sendCommand(cmd);
	}
	
	@Override
	public void getUserEigenValue(int userId) {
		// TODO Auto-generated method stub
		TouchIdCommand cmd = TouchIdCommand.getUserEigenValue((short) userId);
		cmd.setTimeout(10000);
		sendCommand(cmd);
	}

	@Override
	public void uploadAndCompare(byte[] data) {
		// TODO Auto-generated method stub
		TouchIdCommand cmd = TouchIdCommand.uploadAndCompare(data);
		if(cmd != null) {
			cmd.setTimeout(10000);
			sendCommand(cmd);
		}
	}
	
	
}
