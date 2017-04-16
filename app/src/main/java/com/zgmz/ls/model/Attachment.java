package com.zgmz.ls.model;

import android.graphics.Bitmap;

public class Attachment extends BaseData {
	
	public static final int TYPE_IMAGE = 0;
	public static final int TYPE_VIDEO = 1;
    public static final int TYPE_IMAGE_HUKOUBEN = 101;
    public static final int TYPE_IMAGE_SHENGFENZHEN = 102;
    public static final int TYPE_IMAGE_PEOPLE = 103;
    public static final int TYPE_VIDEO_PEOPLE = 104;
    public static final int TYPE_VIDEO_QUANJIA = 105;
    public static final int TYPE_FINGER = 800;			// 指纹
	public static final int TYPE_SIGNTURE_MANAGER = 801; // 签名照片-工作人员
	public static final int TYPE_SIGNTURE_USER = 802;    // 签名照片-用户
	public static final int TYPE_BEFORE_CHECKED_IMAGE = 803; // 最近三次检查过的照片
    public static final int TYPE_IMAGE_OTHER = 900;
	
	public static final int TYPE_ADD = 1001;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getCheck_task_id() {
		return check_task_id;
	}

	public void setCheck_task_id(String check_task_id) {
		this.check_task_id = check_task_id;
	}

	public String getCard_id() {
		return card_id;
	}

	public void setCard_id(String card_id) {
		this.card_id = card_id;
	}

	public Bitmap getContent() {
		return content;
	}

	public void setContent(Bitmap content) {
		this.content = content;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	private int type = TYPE_IMAGE;

	private String check_task_id;

	private String card_id;
	
	private Bitmap content;

	private String path;

	public String ToJSONString() {
		return String.format("{\"clmc\":\"%s\", \"cllx\":\"%d\"}",
				name, type);
	}

	public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    private int resId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private String name;
}
