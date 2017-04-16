
package org.zz.jni;

public class mxComFingerDriver{
	
	static {
		System.loadLibrary("mxComFingerDriver");
	}
	/****************************************************************************
 		�� 	 �ܣ���ȡ��汾
 		�� 	 ����szVersion  - �汾��Ϣ��100�ֽ�
 		��  	�أ�	0 - �ɹ�,����  - ʧ��
	 *****************************************************************************/
	public native int mxGetComDriverVersion(byte[] szVersion);
	
	/****************************************************************************
 		�� 	 �ܣ���ȡ�豸�汾
 		�� 	 ����szDevNodeName  - �����豸�ڵ�����
				nBaudRate      - ������
				szVersion      - �汾��Ϣ��100�ֽ�
 		��  	�أ�	0 - �ɹ�,����  - ʧ��
	 *****************************************************************************/
	public native int mxGetComDevVersion(String szDevNodeName,int nBaudRate,byte[] szVersion);

	/****************************************************************************
		��	�ܣ�	������ָ֤��ģ��
		��	����	szDevNodeName   -�����豸�ڵ�����
				nBaudRate       -������
				dwWaitTime		-��ʱʱ�䣨��λ�����룩 
				lpMbBuf			-ָ��ģ��(�������������512*3���ֽ�)
		��	�أ�0-�ɹ�������-ʧ��
	 *****************************************************************************/
	public native int mxGetComIdCardMb(String szDevNodeName,int nBaudRate,int dwWaitTime,byte[] lpMbBuf);

	/****************************************************************************
		��	�ܣ�	������ָ֤������
		��	����	szDevNodeName   -�����豸�ڵ�����
				nBaudRate       -������
				dwWaitTime		-��ʱʱ�䣨��λ�����룩 
				lpTzBuf			-ָ������(�������������512���ֽ�)
		��	�أ�0-�ɹ�������-ʧ��
	*****************************************************************************/
	public native int mxGetComIdCardTz(String szDevNodeName,int nBaudRate, int dwWaitTime,byte[] lpTzBuf);
	
	/****************************************************************************
	��	�ܣ���ȡָ��ͼ��
	��  	����	szDevNodeName  - �����豸�ڵ�����
			nBaudRate      - ������
			dwWaitTime	-	��ʱʱ�� (��λ������)
			lpImgData	-	ͼ������ (�������������208*288���ֽ�)
	��  �أ�0 - �ɹ�	���� - ʧ��
	 *****************************************************************************/
	public native  int mxGetImageDY(String szDevNodeName, int iBaudRate,int dwWaitTime,byte[] lpImgData);

}
