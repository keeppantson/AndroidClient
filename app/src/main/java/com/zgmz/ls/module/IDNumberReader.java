package com.zgmz.ls.module;

import com.android.charger.EmGpio;
import com.guoguang.jni.JniCall;
import com.zgmz.ls.AppContext;
import com.zgmz.ls.utils.BitmapUtils;
import com.zz.idcard.IDCardDevice;
import com.zgmz.ls.model.IdCard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class IDNumberReader {

//	private Context mContext;

	String Tag = "IDNumberReader";

	IDCardDevice idcardDevice = null;
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
    public static IDNumberReader instance = null;
    public static synchronized IDNumberReader getInstance() {
        if (instance == null) {
            instance = new IDNumberReader(AppContext.getAppContext());
        }
        return instance;
    }
	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_READ_SUCCESS:
					if (mOnIDReaderListener != null) {
						mOnIDReaderListener.onReadSuccess(convert(textData, photoData, true));
				}
				break;
			case MSG_READ_TIME:
				if (mOnIDReaderListener != null && mReaderThread != null) {
					mOnIDReaderListener.onReadTime(msg.arg1, msg.arg2);
				}
				break;
			case MSG_READ_FAILURE:
                if (mOnIDReaderListener != null) {
                    mOnIDReaderListener.onReadFailure(mStatus);
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
	}

	public void setTimeout(long timeout) {
		mTimeout = timeout;
	}

	public long getTimeout() {
		return mTimeout;
	}
	/**
	 * 打开读卡器
	 * 
	 * @Title OpenReader
	 */
	public void OpenReader() {
		if (idcardDevice == null) {
			idcardDevice = new IDCardDevice();
		}
		Log.v(Tag,"OpenReader -1");
		if (bReading)
            return;
		mtSetGPIOValue(95, true);
		Log.v(Tag,"OpenReader -2");
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
        if (mReaderThread != null) {
            mReaderThread.stopReader();
            mReaderThread = null;
        }
		mtSetGPIOValue(95, false);
		// mtSetGPIOValue(95, false);
	}
	public void mtSetGPIOValue(int pin, boolean bHigh)
	{
		if (pin < 0) {
			return;
		}
		EmGpio.gpioInit();
		EmGpio.setGpioMode(pin);
		if (bHigh)
		{
			EmGpio.setGpioOutput(pin);
			EmGpio.setGpioDataHigh(pin);
		}
		else
		{
			EmGpio.setGpioOutput(pin);
			EmGpio.setGpioDataLow(pin);
		}
		EmGpio.gpioUnInit();
	}

    byte[] textData = new byte[256];
    byte[] photoData = new byte[1024];
    byte[] message = new byte[100];
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
			bReading = true;
			reading = true;
			startTime = System.currentTimeMillis();
			try {
				Thread.sleep(1200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			while (reading) {
				// 判断超时

				Log.v(Tag,"OpenReader -3");
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

				Log.v(Tag,"OpenReader -4");
				try {
                    int result = IDCardDevice.ReadIdCard(6,
                            "/dev/ttyMT1", textData, photoData, message);
//					IDCardReaderRetInfo retInfo = mIdCardReaderModule.getIDcardInfo();
					// samvID=idCardReaderModule.getSamvIDString();
					// String
					// appendAddress=idCardReaderModule.getAppendAddress();
					if (result==0) {
						mStatus = 0;
						Log.v(Tag,"OpenReader -5");
						mHandler.sendEmptyMessage(MSG_READ_SUCCESS);
						break;
					} else {
						mStatus = -2;
						// bytesToHexString(new
						// byte[]{retInfo.sw1,retInfo.sw2,retInfo.sw3});
					}

					Log.v(Tag,"OpenReader -6: " + result);
					Thread.sleep(300);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} // while
				// 停止读卡线程
			bReading = false;
		}
	}
    private static final int mPhotoWidth      = 102;
    private static final int mPhotoWidthBytes = (((mPhotoWidth * 3 + 3) / 4) * 4);
    private static final int mPhotoHeight     = 126;
    private static final int mPhotoSize       = (14 + 40 + mPhotoWidthBytes * mPhotoHeight);
    public int Wlt2Bmp(byte[] wlt, byte[] bmp) {
        if(bmp.length < mPhotoSize)
            return -12;
        JniCall.Huaxu_Wlt2Bmp(wlt, bmp, 0);
        return 0;
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

    public static String unicode2String(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length / 2; i++) {
            int a = bytes[2 * i + 1];
            if (a < 0) {
                a = a + 256;
            }
            int b = bytes[2 * i];
            if (b < 0) {
                b = b + 256;
            }
            int c = (a << 8) | b;
			if (c == 32) {
				break;
			}
            sb.append((char) c);
        }
        return sb.toString();
    }
    String[] FOLK = { "汉", "蒙古", "回", "藏", "维吾尔", "苗", "彝", "壮", "布依", "朝鲜",
            "满", "侗", "瑶", "白", "土家", "哈尼", "哈萨克", "傣", "黎", "傈僳", "佤", "畲",
            "高山", "拉祜", "水", "东乡", "纳西", "景颇", "柯尔克孜", "土", "达斡尔", "仫佬", "羌",
            "布朗", "撒拉", "毛南", "仡佬", "锡伯", "阿昌", "普米", "塔吉克", "怒", "乌孜别克",
            "俄罗斯", "鄂温克", "德昂", "保安", "裕固", "京", "塔塔尔", "独龙", "鄂伦春", "赫哲",
            "门巴", "珞巴", "基诺", "", "", "穿青人", "家人", "", "", "", "", "", "", "",
            "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
            "", "", "", "", "", "", "", "", "", "", "", "", "其他", "外国血统", "",
            ""
    };
	public IdCard convert(byte[] cardInfo, byte[] photoData, Boolean bShowImage) {
		IdCard card = new IdCard();

        byte[] id_Name = new byte[30]; // 姓名
        byte[] id_Sex = new byte[2]; // 性别 1为男 其他为女
        byte[] id_Rev = new byte[4]; // 民族
        byte[] id_Born = new byte[16]; // 出生日期
        byte[] id_Home = new byte[70]; // 住址
        byte[] id_Code = new byte[36]; // 身份证号
        byte[] _RegOrg = new byte[30]; // 签发机关
        byte[] id_ValidPeriodStart = new byte[16]; // 有效日期 起始日期16byte 截止日期16byte
        byte[] id_ValidPeriodEnd = new byte[16];
        byte[] id_NewAddr = new byte[36]; // 预留区域

        int iLen = 0;
        for (int i = 0; i < id_Name.length; i++) {
            id_Name[i] = cardInfo[i + iLen];
        }
        iLen = iLen + id_Name.length;
        card.setName(unicode2String(id_Name));

        for (int i = 0; i < id_Sex.length; i++) {
            id_Sex[i] = cardInfo[iLen + i];
        }
        iLen = iLen + id_Sex.length;

        if (id_Sex[0] == '1') {
            card.setSex("男");
        } else {
            card.setSex("女");
        }

        for (int i = 0; i < id_Rev.length; i++) {
            id_Rev[i] = cardInfo[iLen + i];
        }
        iLen = iLen + id_Rev.length;
        if (id_Rev[0] != 0) {
            int iRev = Integer.parseInt(unicode2String(id_Rev));
            card.setNation(FOLK[iRev - 1]);
        }

        for (int i = 0; i < id_Born.length; i++) {
            id_Born[i] = cardInfo[iLen + i];
        }
        iLen = iLen + id_Born.length;
        card.setBirth(unicode2String(id_Born));

        for (int i = 0; i < id_Home.length; i++) {
            id_Home[i] = cardInfo[iLen + i];
        }
        iLen = iLen + id_Home.length;
        card.setAddress(unicode2String(id_Home));

        for (int i = 0; i < id_Code.length; i++) {
            id_Code[i] = cardInfo[iLen + i];
        }
        iLen = iLen + id_Code.length;
        card.setIdNumber(unicode2String(id_Code));

        for (int i = 0; i < _RegOrg.length; i++) {
            _RegOrg[i] = cardInfo[iLen + i];
        }
        iLen = iLen + _RegOrg.length;
        card.setAuthority(unicode2String(_RegOrg));

        for (int i = 0; i < id_ValidPeriodStart.length; i++) {
            id_ValidPeriodStart[i] = cardInfo[iLen + i];
        }
        iLen = iLen + id_ValidPeriodStart.length;
        for (int i = 0; i < id_ValidPeriodEnd.length; i++) {
            id_ValidPeriodEnd[i] = cardInfo[iLen + i];
        }
        iLen = iLen + id_ValidPeriodEnd.length;
        card.setStartValidDate(unicode2String(id_ValidPeriodStart));
        card.setEndValidDate(unicode2String(id_ValidPeriodEnd));

        for (int i = 0; i < id_NewAddr.length; i++) {
            id_NewAddr[i] = cardInfo[iLen + i];
        }
        iLen = iLen + id_NewAddr.length;

        if (bShowImage == true) {

            byte[] bmp = new byte[mPhotoSize];
            Wlt2Bmp(photoData, bmp);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bmp, 0, bmp.length);
            card.setAvatar(bitmap);
            card.setWlt(bmp);
        }

        card.setCompleted(true);
		return card;
	}
}
