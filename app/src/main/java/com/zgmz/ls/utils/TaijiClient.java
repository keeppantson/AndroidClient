package com.zgmz.ls.utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by mixiang on 3/18/17.
 */

public class TaijiClient {
    private String userName = null;
    private String passWord = null;
    private String imei = null;

    private String taijiUri = "http://222.240.178.36:6888/saas_hn/app/interfaceAction.do?act=";
    private String appKey = null;
    private String zoneName = null;
    private String zoneCode = null;

    public TaijiClient(String userName, String passWord, String imei) {
        this.userName = userName;
        this.passWord = passWord;
        this.imei = imei;
    }

    public Boolean LogIn() {
        RestResult res = DoPost(this.taijiUri + "login", String.format("{\"username\":\"%s\",\"password\":\"%s\",\"imei\":\"%s\"}", this.userName, this.passWord, this.imei));
        try {
            if (res.body.getString("login").equals("success")) {
                this.appKey = res.body.getString("appKey");
                this.zoneName = res.body.getString("zoneName");
                this.zoneCode = res.body.getString("zoneCode");
                return true;
            }
            else {
                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    /* 行政区划获取接口
    xzqhbm : 行政区划编码
    xzqhsd : xzqhsd
     */
    public RestResult GetZoneInfo(String xzqhbm, int xzqhsd) {
        return DoPost(this.taijiUri + "queryZone", String.format("{\"xzqhbm\":\"%s\",\"xzqhsd\":%d}", xzqhbm, xzqhsd));
    }

    /* 救助家庭列表批量查询接口
    xzqhbm : 12位行政区划编码
    jzywlx : 救助业务
    hqsl : 每次获取数量
    hqcs : 获取次数
     */
    public RestResult BatchGetFamily(String xzqhbm, String jzywlx, int hqsl, int hqcs) {
        return DoPost(this.taijiUri + "queryFamilyList", String.format("{\"xzqhbm\":\"%s\",\"jzywlx\":\"%s\",\"hqsl\":%d, \"hqcs\":%d}", xzqhbm, jzywlx, hqsl, hqcs));
    }


    /* 救助家庭查询
    sqrsfzh : 申请人身份证号
    jzywlx : 救助业务
    jzny : jzny
     */
    public RestResult GetFamily(String sqrsfzh, String jzywlx, String jzny) {
        return DoPost(this.taijiUri + "queryFamilyList", String.format("{\"sqrsfzh\":\"%s\",\"jzywlx\":\"%s\",\"jzny\":\"%s\"}", sqrsfzh, jzywlx, jzny));
    }

    /* 获取材料
    uri : 需要获得的东西.(不是完整uri, 不包括太极endpoint)
     */
    public byte[] GetMaterial(String uri) {
        return null;
    }

    private RestResult BuildResultFromConnection(HttpURLConnection conn) throws IOException, JSONException {
        return BuildResultFromConnection(conn, 200);
    }

    private RestResult BuildResultFromConnection(HttpURLConnection conn, int buffsize) throws IOException, JSONException {
        RestResult res = new RestResult();
        res.statusCode = conn.getResponseCode();
        char[] buff;
        buff = new char[buffsize];
        BufferedReader br = new BufferedReader(new InputStreamReader(
                (conn.getInputStream())));
        int read = br.read(buff);
        res.body = new JSONObject(new String(buff));

        //mx: debug
        System.out.println("mx: printbuff:" + buff);
        return res;
    }

    private RestResult DoPost(String urlStr, String jsonStr) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");

            String input = jsonStr;

            OutputStream os = conn.getOutputStream();
            os.write(input.getBytes());
            os.flush();

            RestResult res = BuildResultFromConnection(conn);
            conn.disconnect();
            return res;
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
