package org.zz.jni; 
public class zzFingerAlg{
	static {
		System.loadLibrary("mxFingerAlgIdCard");
	}
	/**
	 * @author   chen.gs
	 * @category ��ȡ�㷨�汾��
	 * @param    version �C �㷨�汾��100�ֽ�
	 * @return    0 - �ɹ�
	 *           ����  - ʧ��  	
	 * */
	public native int mxGetVersion(byte[] version);

	/**
	 * @author   chen.gs
	 * @category �����������ָ������ֵ���бȶ�
	 * @param   mbBuf  - ָ��ָ��ģ���ָ�룬����=512�ֽ�
	 *          tzBuf  - ָ��ָ��������ָ�룬����=512�ֽ�
	 *          level  -  ƥ��ȼ�
	 * @return   0 - �ɹ�
	 *          ���� - ʧ��  	
	 * */
	public native int mxFingerMatch512(byte[] mbBuf,byte[] tzBuf,int level);
	
}
