package com.zgmz.ls.utils;

import android.graphics.Bitmap;

import org.json.JSONObject;

import java.io.OutputStream;

/**
 * Created by mixiang on 3/18/17.
 */

public class TaijiClientLocal {
    public TaijiClientLocal(String userName, String passWord, String imei) {
    }

    public boolean login() {
        return true;
    }

    /* 行政区划获取接口
    xzqhbm : 行政区划编码
    xzqhsd : xzqhsd
     */
    public RestResult GetZoneInfo(String xzqhbm, int xzqhsd) {
        RestResult res = new RestResult();
        res.statusCode = 202;
        try {
           String str = "{\"total\":4, \"data\":[{\"xzqhmc\":\"湖南\",\"xzqhdm\":\"430000000000\",\"xzqhjb\":2}," +
                    "{\"xzqhmc\":\"长沙\",\"xzqhdm\":\"430100000000\",\"xzqhjb\":3}," +
                    "{\"xzqhmc\":\"芙蓉区\",\"xzqhdm\":\"430101000000\",\"xzqhjb\":4}," +
                    "{\"xzqhmc\":\"天心街道\",\"xzqhdm\":\"430101001000\",\"xzqhjb\":5}," +
                    "{\"xzqhmc\":\"第二居委会\",\"xzqhdm\":\"430101001001\",\"xzqhjb\":6}" +
                    "]}";
            res.body = new JSONObject(str);
        } catch (org.json.JSONException exp) {
        }

        return res;
    }

    /* 救助家庭列表批量查询接口
    xzqhbm : 12位行政区划编码
    jzywlx : 救助业务
    hqsl : 每次获取数量
    hqcs : 获取次数
     */
    public RestResult BatchGetFamily(String xzqhbm, String jzywlx, int hqsl, int hqcs) {
        RestResult res = new RestResult();
        res.statusCode = 202;
        try {
            String bodyStr = "{\"query\":\"success\",\"info\":\"\",\"end\":false,\"total\":1, \"num\":1,"+
                    " \"data\":[{\"jtid\":\"1\",\"xzqhdm\":\"430000000000\",\"sqrxm\":\"张三\"," +
                    "\"sqrsfzh\":\"320582198101128831\",\"sqrq\":\"2017-01-22\",\"zyzpyy\":\"01\"," +
                    "\"bzrks\":\"2\",\"jtyzsr\":\"20\",\"sfynrfp\":\"01\"," +
                    "\"lxdh\":\"13877638827\",\"jzdz\":\"gaoqinlu\",\"jzywlx\":\"01\"," +
                    "\"jzksny\":\"201701\",\"jzjzny\":\"201703\",\"hybzje\":\"30000\"," +
                    "\"cyxx\":"+ "[{\"cyxm\":\"张三\",\"cysfzh\":\"320582198101128831\","+
                    "\"xb\":\"01\",\"nl\":\"12\",\"ldnl\":\"01\"," +
                    "\"jkzk\":\"10\",\"cjlb\":\"69\",\"cjdj\":\"01\"," +
                    "\"whcd\":\"01\",\"sfzx\":\"01\",\"ysqrgx\":\"01\",\"ryzt\":\"01\"},"+
                    "{\"cyxm\":\"李四\",\"cysfzh\":\"320582198101128833\"," +
                    "\"xb\":\"01\",\"nl\":\"12\",\"ldnl\":\"01\",\"jkzk\":\"10\"," +
                    "\"cjlb\":\"69\",\"cjdj\":\"01\",\"whcd\":\"01\",\"sfzx\":\"01\"," +
                    "\"ysqrgx\":\"01\",\"ryzt\":\"01\"}," + "{\"cyxm\":\"王五\",\"cysfzh\":\"320582198101128834\"," +
                    "\"xb\":\"01\",\"nl\":\"12\",\"ldnl\":\"01\",\"jkzk\":\"10\",\"cjlb\":\"69\"," +
                    "\"cjdj\":\"01\",\"whcd\":\"01\",\"sfzx\":\"01\",\"ysqrgx\":\"01\",\"ryzt\":\"01\"}" +
                    "],"+ "\"zmclxx\":"+"[{\"sqrq\":\"2017-01-01\",\"clmc\":\"personal_pic\","+
                    "\"clid\":\"320582198101128831-102.jpg\",\"cllx\":\"102\"}"+
                    "," + "{\"sqrq\":\"2017-01-01\"," +
                    "\"clmc\":\"personal_pic\",\"clid\":\"320582198101128833-102.jpg\"," +
                    "\"cllx\":\"102\"},"+ "{\"sqrq\":\"2017-01-01\"," +
                    "\"clmc\":\"personal_pic\",\"clid\":\"320582198101128834-102.jpg\"," +
                    "\"cllx\":\"102\"}" + "]}]}";
            res.body = new JSONObject(bodyStr);
        } catch (org.json.JSONException exp) {
        }

        return res;
    }


    /* 救助家庭查询
    sqrsfzh : 申请人身份证号
    jzywlx : 救助业务
    jzny : jzny
     */
    public RestResult GetFamily(String sqrsfzh, String jzywlx, String jzny) {

        RestResult res = new RestResult();
        res.statusCode = 202;
        try {
            String bodyStr = "{\"query\":\"success\",\"info\":\"\","+
                    " \"jtid\":\"1\",\"xzqhdm\":\"430000000000\",\"sqrxm\":\"张三\"," +
                    "\"sqrsfzh\":\"320582198101128831\",\"sqrq\":\"2017-01-22\",\"zyzpyy\":\"01\"," +
                    "\"bzrks\":\"2\",\"jtyzsr\":\"20\",\"sfynrfp\":\"01\"," +
                    "\"lxdh\":\"13877638827\",\"jzdz\":\"gaoqinlu\",\"jzywlx\":\"01\"," +
                    "\"jzksny\":\"201701\",\"jzjzny\":\"201703\",\"hybzje\":\"30000\"," +
                    "\"cyxx\":"+ "[{\"cyxm\":\"张三\",\"cysfzh\":\"320582198101128831\","+
                    "\"xb\":\"01\",\"nl\":\"12\",\"ldnl\":\"01\"," +
                    "\"jkzk\":\"10\",\"cjlb\":\"69\",\"cjdj\":\"01\"," +
                    "\"whcd\":\"01\",\"sfzx\":\"01\",\"ysqrgx\":\"01\",\"ryzt\":\"01\"},"+
                    "{\"cyxm\":\"李四\",\"cysfzh\":\"320582198101128833\"," +
                    "\"xb\":\"01\",\"nl\":\"12\",\"ldnl\":\"01\",\"jkzk\":\"10\"," +
                    "\"cjlb\":\"69\",\"cjdj\":\"01\",\"whcd\":\"01\",\"sfzx\":\"01\"," +
                    "\"ysqrgx\":\"01\",\"ryzt\":\"01\"}," + "{\"cyxm\":\"王五\",\"cysfzh\":\"320582198101128834\"," +
                    "\"xb\":\"01\",\"nl\":\"12\",\"ldnl\":\"01\",\"jkzk\":\"10\",\"cjlb\":\"69\"," +
                    "\"cjdj\":\"01\",\"whcd\":\"01\",\"sfzx\":\"01\",\"ysqrgx\":\"01\",\"ryzt\":\"01\"}" +
                    "],"+ "\"zmclxx\":"+"[{\"sqrq\":\"2017-01-01\",\"clmc\":\"personal_pic\","+
                    "\"clid\":\"320582198101128831-102.jpg\",\"cllx\":\"102\"}"+
                    "," + "{\"sqrq\":\"2017-01-01\"," +
                    "\"clmc\":\"personal_pic\",\"clid\":\"320582198101128833-102.jpg\"," +
                    "\"cllx\":\"102\"},"+ "{\"sqrq\":\"2017-01-01\"," +
                    "\"clmc\":\"personal_pic\",\"clid\":\"320582198101128834-102.jpg\"," +
                    "\"cllx\":\"102\"}" + "]}";
            res.body = new JSONObject(bodyStr);
        } catch (org.json.JSONException exp) {
        }

        return res;
    }
    private static String[] sAvatars = {"avatar/001.jpg","avatar/002.jpg","avatar/003.jpg","avatar/004.jpg"};

    /* 获取材料
    uri : 需要获得的东西.(不是完整uri, 不包括太极endpoint)
     */
    public byte[] GetMaterial(String uri) {
        Bitmap bmp;
        if (uri.contains("320582198101128834-")) {
            bmp = BitmapUtils.getAssetImage(sAvatars[0]);
            if (bmp != null) {
                return BitmapUtils.getBitmapByte(bmp);
            }
        } else if (uri.contains("320582198101128833-")) {
            bmp = BitmapUtils.getAssetImage(sAvatars[1]);
            if (bmp != null) {
                return BitmapUtils.getBitmapByte(bmp);
            }
        } else if (uri.contains("320582198101128831-")) {
            bmp = BitmapUtils.getAssetImage(sAvatars[2]);
            if (bmp != null) {
                return BitmapUtils.getBitmapByte(bmp);
            }
        }

        return null;
    }
}
