package com.zgmz.ls.utils;

/**
 * Created by mixiang on 3/12/17.
 */

public class Meterial {
    public String cllx; //材料类型
    public String clxx; //材料文件在服务器存储路径

    public String ToJSONString() {
        return String.format("\"cllx\":\"%s\", \"clxx\":\"%s\"", cllx, clxx);
    }
}
