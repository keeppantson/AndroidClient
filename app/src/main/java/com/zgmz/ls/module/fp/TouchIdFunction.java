package com.zgmz.ls.module.fp;

public interface TouchIdFunction {
	
	public void open();
	
	public void close();

	public void enroll1(int userId);
	
	public void enroll2(int userId);
	
	public void enroll3(int userId);
	
	public void verfiy();
	
	public void identify(int userId);
	
	public void search();
	
	public void removeUser(int userId);
	
	public void removeAll();
	
	public void captureImage();
	
	public void collectAndExtract();
	
	public void getUserEigenValue(int userId);
	
	public void uploadAndCompare(byte[] data);
}
