package org.zz.jni; 
public class zzFingerAlg{
	static {
		System.loadLibrary("mxFingerAlgIdCard");
	}
	/**
	 * @author   chen.gs
	 * @category 获取算法版本号
	 * @param    version – 算法版本，100字节
	 * @return    0 - 成功
	 *           其他  - 失败  	
	 * */
	public native int mxGetVersion(byte[] version);

	/**
	 * @author   chen.gs
	 * @category 对输入的两个指纹特征值进行比对
	 * @param   mbBuf  - 指向指纹模板的指针，长度=512字节
	 *          tzBuf  - 指向指纹特征的指针，长度=512字节
	 *          level  -  匹配等级
	 * @return   0 - 成功
	 *          其他 - 失败  	
	 * */
	public native int mxFingerMatch512(byte[] mbBuf,byte[] tzBuf,int level);
	
}
