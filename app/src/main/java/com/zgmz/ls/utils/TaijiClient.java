package com.zgmz.ls.utils;

import java.io.OutputStream;

/**
 * Created by mixiang on 3/18/17.
 */

public class TaijiClient {
    public TaijiClient(String userName, String passWord, String imei) {
    }

    /* 行政区划获取接口
    xzqhbm : 行政区划编码
    xzqhsd : xzqhsd
     */
    public RestResult GetZoneInfo(String xzqhbm, int xzqhsd) {
        return null;
    }

    /* 救助家庭列表批量查询接口
    xzqhbm : 12位行政区划编码
    jzywlx : 救助业务
    hqsl : 每次获取数量
    hqcs : 获取次数
     */
    public RestResult BatchGetFamily(String xzqhbm, String jzywlx, int hqsl, int hqcs) {
        return null;
    }


    /* 救助家庭查询
    sqrsfzh : 申请人身份证号
    jzywlx : 救助业务
    jzny : jzny
     */
    public Family GetFamily(String sqrsfzh, String jzywlx, String jzny) {
        return null;
    }

    /* 获取材料
    uri : 需要获得的东西.(不是完整uri, 不包括太极endpoint)
     */
    public byte[] GetMaterial(String uri) {
        return null;
    }
}
