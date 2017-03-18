package com.zgmz.ls.utils;

import java.io.File;

import com.zgmz.ls.base.Const;

public class FileUtils {

	public static void ensureImageLocation() {
		File file = new File(Const.IMAGE_BASE_LOCATION);
		if(!file.exists()) {
			file.mkdirs();
		}
	}
}
