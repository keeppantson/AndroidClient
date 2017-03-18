package com.zgmz.ls.module;

import com.synjones.handset.IDCardReaderModule;
import com.synjones.idcard.IDCard;
import com.synjones.idcard.IDCardReaderRetInfo;
import com.zgmz.ls.model.IdCard;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class IDNumberReader {

//	private Context mContext;
	private IDCardReaderModule mIdCardReaderModule;
	private IDCard mIdCard = null;
	private int mStatus = 0;

	private long mTimeout = 10000;

	private boolean bReading = false;

	private ReaderThread mReaderThread;

	// 读取成功
	private static final int MSG_READ_SUCCESS = 1;
	// 读取计时
	private static final int MSG_READ_TIME = 2;
	// 读取失败
	private static final int MSG_READ_FAILURE = 3;

	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_READ_SUCCESS:
				if (mIdCard != null) {
					if (mOnIDReaderListener != null) {
						mOnIDReaderListener.onReadSuccess(convert(mIdCard));
					}
				}
				break;
			case MSG_READ_TIME:
				if (mOnIDReaderListener != null && mReaderThread != null) {
					mOnIDReaderListener.onReadTime(msg.arg1, msg.arg2);
				}
				break;
			case MSG_READ_FAILURE:
				if (mIdCard == null) {
					if (mOnIDReaderListener != null) {
						mOnIDReaderListener.onReadFailure(mStatus);
					}
				}
				break;
			}
		};
	};

	public interface OnIDReaderListener {
		public void onReadSuccess(IdCard idcard);

		public void onReadTime(long time, long timeout);

		public void onReadFailure(int status);
	}

	private OnIDReaderListener mOnIDReaderListener;

	public void setOnIDReaderListener(OnIDReaderListener listener) {
		mOnIDReaderListener = listener;
	}

	public IDNumberReader(Context context) {
		mIdCardReaderModule = new IDCardReaderModule(context);
	}

	public void setTimeout(long timeout) {
		mTimeout = timeout;
	}

	public long getTimeout() {
		return mTimeout;
	}

	public void startReader() {

	}

	public void stopReader() {
		mReaderThread.stopReader();
		mReaderThread = null;
	}

	/**
	 * 打开读卡器
	 * 
	 * @Title OpenReader
	 */
	public void OpenReader() {
		if (bReading)
			return;
		mReaderThread = new ReaderThread();
		mReaderThread.setTimeout(getTimeout());
		mReaderThread.start();
	}

	/**
	 * 关闭读卡器
	 * 
	 * @Title CloseReader
	 */
	public void CloseReader() {
		mReaderThread.stopReader();
		mReaderThread = null;
	}

	private class ReaderThread extends Thread {

		boolean reading = false;

		long startTime = 0;

		long timeout;

		public void setTimeout(long timeout) {
			this.timeout = timeout;
		}

		public long getReadTime() {
			return System.currentTimeMillis() - startTime;
		}

		public void stopReader() {
			if (reading) {
				reading = false;
				try {
					join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			mIdCardReaderModule.open();
			bReading = true;
			reading = true;
			startTime = System.currentTimeMillis();

			while (reading) {
				// 判断超时
				if ((timeout + startTime) < System.currentTimeMillis()) {
					mHandler.sendEmptyMessage(MSG_READ_FAILURE);
					mStatus = -1;
					break;
				} else {
					Message msg = mHandler.obtainMessage();
					msg.what = MSG_READ_TIME;
					msg.arg1 = (int) getReadTime();
					msg.arg2 = (int) getTimeout();
					mHandler.sendMessage(msg);
				}

				try {
					IDCardReaderRetInfo retInfo = mIdCardReaderModule.getIDcardInfo(false, true, false);//获取身份证对象
//					IDCardReaderRetInfo retInfo = mIdCardReaderModule.getIDcardInfo();
					// samvID=idCardReaderModule.getSamvIDString();
					// String
					// appendAddress=idCardReaderModule.getAppendAddress();
					if (retInfo.errCode == IDCardReaderRetInfo.ERROR_OK) {
						mIdCard = retInfo.card;
						mStatus = 0;
						mHandler.sendEmptyMessage(MSG_READ_SUCCESS);
						break;
					} else {
						mIdCard = null;
						mStatus = -2;
						// bytesToHexString(new
						// byte[]{retInfo.sw1,retInfo.sw2,retInfo.sw3});
					}

					Thread.sleep(300);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} // while
				// 停止读卡线程
			mIdCardReaderModule.close();
			bReading = false;
		}
	}

	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			stringBuilder.append("0x");
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}

			stringBuilder.append(hv.toUpperCase());
			stringBuilder.append(",");
		}
		return stringBuilder.toString();
	}

//	public byte[] decodeWlt(byte[] wlt) throws Exception {
//		String bmpPath = mContext.getFileStreamPath("photo.bmp").getAbsolutePath();
//		String wltPath = mContext.getFileStreamPath("photo.wlt").getAbsolutePath();
//		File wltFile = new File(wltPath);
//
//		try {
//			FileOutputStream fos = new FileOutputStream(wltFile);
//			fos.write(wlt);
//			fos.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		//// byte[] wlt_ok=FileOperate.readAllSdcardFile("/wlt.wlt");
//		// log.error("wlt");
//		// fos.write(wlt.img);
//
//		DecodeWlt dw = new DecodeWlt();
//
//		int result = dw.Wlt2Bmp(wltPath, bmpPath);
//		byte[] buffer = null;
//		FileInputStream fin;
//		try {
//			File bmpFile = new File(bmpPath);
//			fin = new FileInputStream(bmpFile);
//			int length = fin.available();
//			buffer = new byte[length];
//			fin.read(buffer);
//			fin.close();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return buffer;
//	}
//
//	private void decodePhoto2Card(IDCard card) {
//		try {
//			ToastUtils.showLongToast("decodePhoto2Card");
//			byte bmpBytes[] = decodeWlt(card.getWlt());
//			Bitmap bmp = null;
//			if (bmpBytes != null) {
//				ToastUtils.showLongToast("create bitmap");
//				bmp = BitmapFactory.decodeByteArray(bmpBytes, 0, bmpBytes.length);
//			} else {
//				ToastUtils.showLongToast("create bitmap failure");
//			}
//			card.setPhoto(bmp);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}

	public IdCard convert(IDCard idcard) {
		IdCard card = new IdCard();
		card.setName(idcard.getName());
		card.setSex(idcard.getSex());
		card.setBirth(idcard.getBirthday());
		card.setAddress(idcard.getAddress());
		card.setIdNumber(idcard.getIDCardNo().toUpperCase());
		card.setAuthority(idcard.getGrantDept());
		card.setNation(idcard.getNation());
		card.setStartValidDate(idcard.getUserLifBebinWithPoint());
		card.setWlt(idcard.getWlt());
		card.setEndValidDate(idcard.getUserLifEndWithPoint());
		card.setWlt(idcard.getWlt());
		card.setAvatar(idcard.getPhoto());
		card.setCompleted(true);
		return card;
	}
}
