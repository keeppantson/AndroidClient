package com.zgmz.ls.view;

/**
 * Created by buddyyan on 3/21/15.
 */
public class CellItemInfo {

    public static final String EMPTY_STRING = "";

    private int x = -1;

    private int y = -1;

    private int id = 0;

    private int screen = 0;

    private int type = 0;
    
    private Object obj;

    public int getType() {
        return type;
    }

    public CellItemInfo setType(int type) {
        this.type = type;
        return this;
    }

    public int getX() {
        return x;
    }


    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public CellItemInfo setXY(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public int getScreen() {
        return screen;
    }

    public void setScreen(int screen) {
        this.screen = screen;
    }


    public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	@Override
    public String toString() {
        return "ItemInfo{" +
                "x=" + x +
                ", y=" + y +
                ", id=" + id +
                ", screen=" + screen +
                ", type=" + type +
                '}';
    }
}
