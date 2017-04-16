package finger;

import android.os.Environment;

import java.io.File;

public class ToolUnit {
	/**
	 * @author chen.gs
	 * @category ��ȡsd��·��
	 * */
	public static String getSDCardPath(){
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED); //�ж�sd���Ƿ����
		if (sdCardExist)
		{
			sdDir = Environment.getExternalStorageDirectory();  //��ȡ��Ŀ¼
			return sdDir.toString();
		}
		else
		{
			return null;
		}	
	}
}
