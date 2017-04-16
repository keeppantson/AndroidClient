package com.zgmz.ls.model;

import android.graphics.Bitmap;

public class QuHuaMa extends BaseData {

	private String name;
	private String id;
	private String father_id;
	private String depth;
    public String toString(){
        return name;
    }
    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFather_id() {
		return father_id;
	}

	public void setFather_id(String father_id) {
		this.father_id = father_id;
	}

	public String getDepth() {
		return depth;
	}

	public void setDepth(String depth) {
		this.depth = depth;
	}
}
