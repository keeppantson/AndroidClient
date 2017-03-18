package com.zgmz.ls.model;

import com.zgmz.ls.base.Const;

public class BaseData  implements Const{
	
	private int userId;
	
	private boolean completed = false;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
	
}
