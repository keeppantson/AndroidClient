package com.zz.idcard;

public class IDCardDevice {
	
    public native static int ReadIdCard(int timeout, String portName,
    		byte[] textData, byte[] photoData, byte[] message);
  
    static{
		try
		{
			System.loadLibrary("IDCardReader_zz");
		} 
		catch(UnsatisfiedLinkError e)
		{
			System.out.println(e.getMessage());
		} 
    }
 
    public int ReadIDCardInfo(int timeout, byte[] textData, String portName,
			byte[] photoData, byte[] message) throws Exception {
    	return ReadIdCard(timeout, portName, textData, photoData, message);
    }
}
