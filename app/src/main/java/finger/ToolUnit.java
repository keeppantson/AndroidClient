package finger;

import android.os.Environment;

import java.io.File;

public class ToolUnit {
	/**
	 * @author chen.gs
	 * @category 获取sd卡路径
	 * */
	public static String getSDCardPath(){
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
		if (sdCardExist)
		{
			sdDir = Environment.getExternalStorageDirectory();  //获取跟目录
			return sdDir.toString();
		}
		else
		{
			return null;
		}	
	}
}
