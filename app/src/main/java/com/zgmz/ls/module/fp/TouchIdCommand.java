package com.zgmz.ls.module.fp;

import java.nio.ByteBuffer;

public class TouchIdCommand extends Command {
	
	public interface Response {
		public final static byte ACK_SUCCESS = (byte)0x00; //操作成功
		public final static byte ACK_FAIL = (byte)0x01; //操作失败
		public final static byte ACK_FULL = (byte)0x04; //指纹数据库已满
		public final static byte ACK_NOUSER = (byte)0x05; //无此用户
		public final static byte ACK_USER_OCCUPIED = (byte)0x06; //此ID用户已存在
		public final static byte ACK_USER_EXIST = (byte)0x07; //用户已存在
		public final static byte ACK_TIMEOUT = (byte)0x08; //采集超时
	}

	
	private static final int PROTOCAL_TYPE = 1;
	
	private static final long DEFAULT_TIMEOUT = 10000;
	
	
	public final static byte HEAD = (byte)0xF5;
	
	public final static byte CMD_ENROLL1 = (byte)0x01;//添加指纹步骤一
	public final static byte CMD_ENROLL2 = (byte)0x02;//添加指纹步骤二
	public final static byte CMD_ENROLL3 = (byte)0x03;//添加指纹步骤三
	public final static byte CMD_REMOVE_USER = (byte)0x04;//删除指定编号指纹
	public final static byte CMD_REMOVE_ALL = (byte)0x05;//清空所有指纹
	public final static byte CMD_USER_COUNT = (byte)0x09;//取用户总数
	public final static byte CMD_INDENTIFY = (byte)0x0b;//1:1比对
	public final static byte CMD_SEARCH = (byte)0x0c;//1：N比对
	public final static byte CMD_CAPTURE = (byte)0x24; 
	public final static byte CMD_COLLECT_AND_EXTRACT = (byte)0x23; // 采集指纹并提取特征值
	public final static byte CMD_GET_USER_EIGENVALUE = (byte)0x31; // 下载DSP模块数据库内指定用户特征值
	public final static byte CMD_UPLOAD_AND_COMPARE = (byte)0x44; // 上传特征值与采集指纹比对
	
	
	public TouchIdCommand() {
		setDataLenghtType(DataLengthType.FIXED);
		setProtocolType(PROTOCAL_TYPE);
		setTimeout(DEFAULT_TIMEOUT);
	}
	
	
	
	public static TouchIdCommand enroll1(short userId) {
		TouchIdCommand cmd =  create8BitsUserId(CMD_ENROLL1, userId, (byte)1);
		cmd.setUserId(userId);
		return cmd;
	}
	
	public static TouchIdCommand enroll2(short userId) {
		TouchIdCommand cmd =  create8BitsUserId(CMD_ENROLL2, userId, (byte)1);
		cmd.setUserId(userId);
		return cmd;
	}
	
	public static TouchIdCommand enroll3(short userId) {
		TouchIdCommand cmd =  create8BitsUserId(CMD_ENROLL3, userId, (byte)1);
		cmd.setUserId(userId);
		return cmd;
	}
	
	public static TouchIdCommand removeUser(short userId) {
		TouchIdCommand cmd =  create8BitsUserId(CMD_REMOVE_USER, userId, (byte)0);
		cmd.setUserId(userId);
		return cmd;
	}
	
	public static TouchIdCommand removeAll() {
		return create8BitsUserId(CMD_REMOVE_ALL, (short)0, (byte)0);
	}
	
	public static TouchIdCommand userCount() {
		return create8BitsCmd(CMD_USER_COUNT);
	}
	
	public static TouchIdCommand identify(short userId) {
		TouchIdCommand cmd =  create8BitsUserId(CMD_INDENTIFY, userId, (byte)0);
		cmd.setUserId(userId);
		return cmd;
	}
	
	public static TouchIdCommand search() {
		return create8BitsUserId(CMD_SEARCH, (byte)0, (byte)0);
	}
	
	public static TouchIdCommand captureImage(){
		
		TouchIdCommand cmd = create8BitsCmd(CMD_CAPTURE);
		cmd.setDataLenghtType(DataLengthType.UNFIXED);
		return cmd;
		
	}
	
	public static TouchIdCommand collectAndExtract(){
		
		TouchIdCommand cmd = create8BitsCmd(CMD_COLLECT_AND_EXTRACT);
		cmd.setDataLenghtType(DataLengthType.UNFIXED);
		return cmd;
		
	}
	
	public static TouchIdCommand getUserEigenValue(short userId){
		TouchIdCommand cmd =  create8BitsUserId(CMD_GET_USER_EIGENVALUE, userId, (byte)0);
		cmd.setUserId(userId);
		cmd.setDataLenghtType(DataLengthType.UNFIXED);
		return cmd;
	}
	
	public static TouchIdCommand uploadAndCompare(byte[] data){
		if(data == null) return null;
		int length = data.length+3;
		byte high = (byte) ((length & 0xFF00) >> 8);
		byte low = (byte) (length & 0x00FF);
		
		TouchIdCommand cmd = create8Bits(CMD_UPLOAD_AND_COMPARE, high, low, (byte)0, (byte)0);
		ByteBuffer buffer = ByteBuffer.allocate(length+11);
		buffer.put(cmd.getCmd());
		buffer.put(HEAD);
		buffer.put((byte)0);
		buffer.put((byte)0);
		buffer.put((byte)0);
		buffer.put(data);
		buffer.put(cmdGenCHK(9, length+8, buffer.array()));
		buffer.put(HEAD);
	
		cmd.setCmd(buffer.array());
		cmd.setDataLenghtType(DataLengthType.FIXED);
		return cmd;
		
	}
	
	private static TouchIdCommand create8BitsUserId(byte cmd, short userId, byte byte4) {
		byte high = (byte) ((userId & 0xFF00) >> 8);
		byte low = (byte) (userId & 0x00FF);
		return create8Bits(cmd, high, low, byte4, (byte)0);
	}
	
	private static TouchIdCommand create8BitsCmd(byte cmd) {
		return create8Bits(cmd, (byte)0, (byte)0, (byte)0, (byte)0);
	}
	
	private static TouchIdCommand create8Bits(byte cmd, byte byte2, byte byte3, byte byte4, byte byte5) {
		TouchIdCommand command = new TouchIdCommand();
//		byte[] temp = new byte[8];
//		temp[0] = HEAD;
//		temp[1] = cmd;
//		temp[2] = byte2;
//		temp[3] = byte3;
//		temp[4] = byte4;
//		temp[5] = byte5;
//		temp[6] = cmdGenCHK(2, 6, temp);
//		temp[7] = HEAD;
		ByteBuffer buffer = ByteBuffer.allocate(8);
		buffer.put(HEAD);
		buffer.put(cmd);
		buffer.put(byte2);
		buffer.put(byte3);
		buffer.put(byte4);
		buffer.put(byte5);
		buffer.put(cmdGenCHK(1, 5, buffer.array()));
		buffer.put(HEAD);
		command.setCmd(buffer.array());
		command.setCmdId(cmd);
		return command;
	}
	
	private static byte cmdGenCHK(int start, int end, byte[] data){
		byte temp = data[start];
		
		for(int i=start+1; i <= end; i++){
			temp ^= data[i];
		}
		return temp;
	}
	
}
