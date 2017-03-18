package com.zgmz.ls.module.fp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.zgmz.ls.module.fp.Command.DataLengthType;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android_serialport_api.SerialPort;

public abstract class SerialPortController {
	
	private SerialPort mSerialPort;
	
	private InputStream mInputStream;
	
	private OutputStream mOutputStream;
	
	public interface OnDataReceiver {
		
		public static final int SUCCESS = 0;
		
		public static final int TIMEOUT = -1;
		
		public static final int  ERROR = -2;
		
		public void onReceiverData(Command cmd, byte[] data);
		
		public void onTimeout(Command cmd);
		
		public void onError(Command cmd, int errorNo);
		
	}
	
	
	private OnDataReceiver mOnDataReceiver;
	
	public void setOnDataReceiver(OnDataReceiver receiver) {
		mOnDataReceiver = receiver;
	}
	
	public SerialPortController(File device, int baudrate) {
		try {
			mSerialPort = new SerialPort(device, baudrate, 0);
			mInputStream = mSerialPort.getInputStream();
			mOutputStream = mSerialPort.getOutputStream();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public InputStream getInputStream() {
		return mInputStream;
	}
	
	
	public OutputStream getOutputStream() {
		return mOutputStream;
	}
	
	
	protected void start() {
		mCmdThread = new CmdThread();
		mCmdThread.start();
	}
	
	protected void stop() {
		if(isOpen()) {
			mCmdThread.quit();
			mCmdThread = null;
		}
	}
	
	public boolean isOpen() {
		return (mCmdThread != null && mCmdThread.isRunning());
	}
	
	public void sendCommand(Command cmd) {
		if(isOpen()) {
			mCmdThread.sendCommand(cmd);
		}
	}
	
	public void clear() {
		byte buf[]=new byte[4096];
		try {
			while(mInputStream.available()>0)
				mInputStream.read(buf);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// 固定数据长度
	abstract public int getFixedDataLength(Command cmd);
	
	// 通过协议头解析数据长度
	abstract public int getUnfixedDataLength(Command cmd, byte[] buffer);
	
	
	private CmdThread mCmdThread;
	
	
	class CmdThread extends Thread {
		private CmdHandler mCmdHandler;
		
		private boolean bRunning = false;

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Looper.prepare();
			bRunning = true;
			mCmdHandler = new CmdHandler();
			Looper.loop();
		}
		
		public boolean isRunning() {
			return bRunning;
		}
		
		public void quit() {
			mCmdHandler.sendEmptyMessage(CmdHandler.QUIT);
			bRunning = false;
		}
		
		public void sendCommand(Command cmd) {
			if(mCmdHandler != null) {
				Message msg = mCmdHandler.obtainMessage();
				msg.what = CmdHandler.CMD;
				msg.obj = cmd;
				msg.sendToTarget();
			}
		}
	}

	
	private void receiverData(Command cmd, int errorNo, byte[] data) {
		if(mOnDataReceiver != null) {
			if(errorNo == OnDataReceiver.SUCCESS) {
				mOnDataReceiver.onReceiverData(cmd, data);
			}
			else if(errorNo == OnDataReceiver.TIMEOUT) {
				mOnDataReceiver.onTimeout(cmd);
			}
			else {
				mOnDataReceiver.onError(cmd, errorNo);
			}
		}
	}
	
	
	class CmdHandler extends Handler {
		
		private static final int QUIT = 0x1001;
		
		private static final int CMD = 0x1002;
		
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch(msg.what) {
				case QUIT:
					quit();
					break;
				case CMD:
					this.sendCommand((Command) msg.obj);
					break;
			}
			super.handleMessage(msg);
		}
		
		
		private void quit() {
			Looper.myLooper().quit();
		}
		
		public void sendCommand(Command cmd) {
//			Logger.i("CmdHandler sendCommand cmd = " + cmd.toString());
			clear();
			doSendCommand(cmd);
			doReceiverData(cmd);
		}
		
	}
	
	
	private void doSendCommand(Command cmd) {
		try {
			mOutputStream.write(cmd.getCmd());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private void doReceiverData(Command cmd) {
		
		try {
			long start_time = System.currentTimeMillis();
			byte[] buffer = new byte[4096];
//			byte[] head = new byte[8];
			byte[] data = null;
			int dataPos = 0;
			if(cmd.getDataLenghtType() == DataLengthType.FIXED) {
				data = new byte[getFixedDataLength(cmd)];
				//Logger.i("dateLenghType is fixed 8");
			}
			int recvLen = 0;
			int dataLen = 0;
			long timeout = cmd.getTimeout();
			
			do{
				sleep(10);
				recvLen = 0;
				if(mInputStream.available()>0) {
					recvLen=mInputStream.read(buffer);
				}
				//Logger.eLine("recvLen = " + recvLen);
				if(recvLen<=0){ continue;}
				
				if(data == null) {
					
					data = new byte[getUnfixedDataLength(cmd, buffer) + 11];
					//System.arraycopy(buffer, 0, head, 0, 8);
					//Logger.i(" tu onReceiverData  data = " + HexTools.bytesToHexString(head));
					//break;
				}
				
				dataLen += recvLen;
				System.arraycopy(buffer, 0, data, dataPos, recvLen);
				dataPos += recvLen;
				
				//Logger.iLine("recvLen = " + recvLen + " " + HexTools.bytesToHexString(buffer));
				
				if(data.length == dataLen){break;}
			}
			while((System.currentTimeMillis()-start_time) < timeout);
			
			
			if((System.currentTimeMillis()-start_time) < timeout) {
				if(dataLen>0 && data.length==dataLen)
					receiverData(cmd, OnDataReceiver.SUCCESS, data);
				else 
					receiverData(cmd, OnDataReceiver.ERROR, null);
			}
			else {
			
				receiverData(cmd, OnDataReceiver.TIMEOUT, null);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			receiverData(cmd, OnDataReceiver.ERROR, null);
			e.printStackTrace();
		}
		
	}
	
	private void sleep(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
		
}
