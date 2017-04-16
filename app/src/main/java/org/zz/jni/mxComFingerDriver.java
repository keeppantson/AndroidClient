
package org.zz.jni;

public class mxComFingerDriver{
	
	static {
		System.loadLibrary("mxComFingerDriver");
	}
	/****************************************************************************
 		功 	 能：获取库版本
 		参 	 数：szVersion  - 版本信息，100字节
 		返  	回：	0 - 成功,其他  - 失败
	 *****************************************************************************/
	public native int mxGetComDriverVersion(byte[] szVersion);
	
	/****************************************************************************
 		功 	 能：获取设备版本
 		参 	 数：szDevNodeName  - 串口设备节点名称
				nBaudRate      - 波特率
				szVersion      - 版本信息，100字节
 		返  	回：	0 - 成功,其他  - 失败
	 *****************************************************************************/
	public native int mxGetComDevVersion(String szDevNodeName,int nBaudRate,byte[] szVersion);

	/****************************************************************************
		功	能：	读二代证指纹模板
		参	数：	szDevNodeName   -串口设备节点名称
				nBaudRate       -波特率
				dwWaitTime		-超时时间（单位：毫秒） 
				lpMbBuf			-指纹模板(输出参数，至少512*3个字节)
		返	回：0-成功，其它-失败
	 *****************************************************************************/
	public native int mxGetComIdCardMb(String szDevNodeName,int nBaudRate,int dwWaitTime,byte[] lpMbBuf);

	/****************************************************************************
		功	能：	读二代证指纹特征
		参	数：	szDevNodeName   -串口设备节点名称
				nBaudRate       -波特率
				dwWaitTime		-超时时间（单位：毫秒） 
				lpTzBuf			-指纹特征(输出参数，至少512个字节)
		返	回：0-成功，其它-失败
	*****************************************************************************/
	public native int mxGetComIdCardTz(String szDevNodeName,int nBaudRate, int dwWaitTime,byte[] lpTzBuf);
	
	/****************************************************************************
	功	能：获取指纹图像
	参  	数：	szDevNodeName  - 串口设备节点名称
			nBaudRate      - 波特率
			dwWaitTime	-	超时时间 (单位：毫秒)
			lpImgData	-	图像数据 (输出参数，至少208*288个字节)
	返  回：0 - 成功	其他 - 失败
	 *****************************************************************************/
	public native  int mxGetImageDY(String szDevNodeName, int iBaudRate,int dwWaitTime,byte[] lpImgData);

}
