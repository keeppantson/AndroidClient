package com.zgmz.ls.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.zgmz.ls.base.Const;

public class FileUtils {

	public static void ensureDownloadLocation() {
		File file = new File(Const.DOWNLOAD_LOCATION);
		if(!file.exists()) {
			file.mkdirs();
		}
	}
	public static void ensureImageLocation() {
		File file = new File(Const.IMAGE_BASE_LOCATION);
		if(!file.exists()) {
			file.mkdirs();
		}
	}
	public static void ensureVideoLocation() {
		File file = new File(Const.VIDEO_BASE_LOCATION);
		if(!file.exists()) {
			file.mkdirs();
		}
	}

	public static void writeToFile(String content, String path, String pathandname) {
		File file = new File(path);
		if(!file.exists()) {
			file.mkdirs();
		}
		file = new File(pathandname);
		if(!file.exists()) {
			return;
		}
		try {
			FileOutputStream out = new FileOutputStream(file, false);
			out.write(content.getBytes(), 0, content.getBytes().length);
		}catch (IOException exp) {
			return;
		}
	}

	public static void deleteFile(String path) {
		File file = new File(path);
		if(file.exists()) {
			deleteFile(path);
		}
	}

    public static String getStringFromFile(String path) {
        File file = new File(Const.VIDEO_TEMP_FILE_LOCATION);
        if(!file.exists()) {
            return null;
        }
        byte[] buffer = null;
        ByteArrayOutputStream bos = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            bos = new ByteArrayOutputStream();
            buffer = new byte[1024];
            int length = 0;
            while ((length = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, length);
            }
            fis.close();
            bos.close();
            return bos.toString();
        } catch (IOException exp) {
            return null;
        }
    }

	public static int CopySdcardFile(String fromFile, String toFile)
	{

		try
		{
			InputStream fosfrom = new FileInputStream(fromFile);
			OutputStream fosto = new FileOutputStream(toFile);
			byte bt[] = new byte[1024];
			int c;
			while ((c = fosfrom.read(bt)) > 0)
			{
				fosto.write(bt, 0, c);
			}
			fosfrom.close();
			fosto.close();
			return 0;

		} catch (Exception ex)
		{
			return -1;
		}
	}
}
