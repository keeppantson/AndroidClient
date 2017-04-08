package com.zgmz.ls.utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

/**
 * Created by mixiang on 3/18/17.
 */

public class TaijiClient {
    private int MAX_FILE_SIZE = 400 * 1024 * 1024; //文件最大允许多少, 400M

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

    /*初始化TaijiClient
    初始化失败将直接报exception
     */
    public void LogIn() throws Exception {
        try {
            RestResult res = DoPost(this.taijiUri + "login", String.format("{\"username\":\"%s\",\"password\":\"%s\",\"imei\":\"%s\"}", this.userName, this.passWord, this.imei));
            if (res.body.getString("login").equals("success")) {
                this.appKey = res.body.getString("appKey");
                this.zoneName = res.body.getString("zoneName");
                this.zoneCode = res.body.getString("zoneCode");
            }
            else {
                throw new Exception(String.format("Unable to login with username:%s password:%s imei:%s, failed to init TaijiClient", this.userName, this.passWord, this.imei));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(String.format("Cannot init TaijiClient with exception:%s", e.toString()));
        }
    }

    /* 行政区划获取接口
    xzqhbm : 行政区划编码
    xzqhsd : xzqhsd
     */
    public RestResult GetZoneInfo(String xzqhbm, int xzqhsd) throws Exception {
        return DoPost(this.taijiUri + "queryZone", String.format("{\"xzqhbm\":\"%s\",\"xzqhsd\":%d}", xzqhbm, xzqhsd));
    }

    /* 救助家庭列表批量查询接口
    xzqhbm : 12位行政区划编码
    jzywlx : 救助业务
    hqsl : 每次获取数量
    hqcs : 获取次数
     */
    public RestResult BatchGetFamily(String xzqhbm, String jzywlx, int hqsl, int hqcs) throws Exception {
        return DoPost(this.taijiUri + "queryFamilyList", String.format("{\"xzqhbm\":\"%s\",\"jzywlx\":\"%s\",\"hqsl\":%d, \"hqcs\":%d}", xzqhbm, jzywlx, hqsl, hqcs));
    }


    /* 救助家庭查询
    sqrsfzh : 申请人身份证号
    jzywlx : 救助业务
    jzny : jzny
     */
    public RestResult GetFamily(String sqrsfzh, String jzywlx, String jzny) throws Exception {
        return DoPost(this.taijiUri + "queryFamilyList", String.format("{\"sqrsfzh\":\"%s\",\"jzywlx\":\"%s\",\"jzny\":\"%s\"}", sqrsfzh, jzywlx, jzny));
    }

    /* 获取材料
    uri : 需要获得的东西.(不是完整uri, 不包括太极endpoint)
     */
    public byte[] GetMaterial(String uri) throws Exception {
        int retry = 3;
        while (retry > 0) {
            try {
                URL url = new URL(uri);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type", "application/json");

                int statusCode = conn.getResponseCode();
                if (statusCode != HttpURLConnection.HTTP_OK) {
                    return null;
                }

                char[] buff;
                buff = new char[MAX_FILE_SIZE];
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        (conn.getInputStream())));
                int read = br.read(buff);

                // TODO: this encode method?
                Charset cs = Charset.forName ("UTF-8");
                CharBuffer cb = CharBuffer.allocate(read);
                cb.put (buff);
                cb.flip();
                ByteBuffer bb = cs.encode(cb);

                return bb.array();
            } catch (Exception e) {
                if (IsRetryableException(e)) {
                    retry--;
                } else {
                    e.printStackTrace();
                    throw e;
                }
            }
        }

        throw new Exception("DoGet Retried for 3 times but all failed");
    }

    private RestResult BuildResultFromConnection(HttpURLConnection conn) throws IOException, JSONException {
        return BuildResultFromConnection(conn, 200);
    }
    
    private RestResult BuildResultFromConnection(HttpURLConnection conn, int buffsize) throws IOException, JSONException {
        RestResult res = new RestResult();
        res.statusCode = conn.getResponseCode();

        char[] buff;
        buff = new char[buffsize];

        InputStream is;
        if (res.statusCode >= 400) {
            is = conn.getErrorStream();
        }
        else {
            is = conn.getInputStream();
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        int read = br.read(buff);
        System.out.println("mx: readbuff size:" + read);
        res.body = new JSONObject(new String(buff));

        // record rpc
        res.rpc = conn.getHeaderField("rpc");

        //mx: debug
        System.out.println("mx: printbuff:" + buff);
        return res;
    }

    private RestResult DoPost(String urlStr, String jsonStr) throws Exception {
        int retry = 3;
        while (retry > 0) {
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
            } catch (Exception e) {
                if (IsRetryableException(e)) {
                    retry--;
                } else {
                    e.printStackTrace();
                    throw e;
                }
            }
        }

        throw new Exception("DoPost Retried for 3 times but all failed");
    }

    private RestResult DoGet(String urlStr) throws Exception {
        int retry = 3;
        while (retry > 0) {
            try {
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type", "application/json");

                RestResult res = BuildResultFromConnection(conn);
                conn.disconnect();
                return res;
            } catch (Exception e) {
                if (IsRetryableException(e)) {
                    retry--;
                } else {
                    e.printStackTrace();
                    throw e;
                }
            }
        }

        throw new Exception("DoGet Retried for 3 times but all failed");
    }

    private boolean IsRetryableException(Exception e) {
        if (e instanceof java.net.ConnectException) {
            return true;
        }
        else {
            return false;
        }
    }
}
