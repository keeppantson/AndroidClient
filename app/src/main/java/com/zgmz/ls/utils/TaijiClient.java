package com.zgmz.ls.utils;

import android.util.Base64;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mixiang on 3/18/17.
 */

public class TaijiClient {
    private String userName = null;
    private String passWord = null;
    private String imei = null;

    private String taijiUri = "http://222.240.178.36:6888/saas_hn/app/interfaceAction.do?act=";
    private String taijiContentUri = "http://222.240.178.36:6888/fileRelease/servlet/FileServlet?appKey=";
    private String appKey = null;
    private String zoneName = null;

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getZoneCode() {
        return zoneCode;
    }

    public void setZoneCode(String zoneCode) {
        this.zoneCode = zoneCode;
    }

    private String zoneCode = null;

    public TaijiClient(String userName, String passWord, String imei) {
        this.userName = userName;
        this.passWord = passWord;
        this.imei = imei;
    }

    private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params)
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    /*初始化TaijiClient
    初始化失败将直接报exception
     */
    public void LogIn() throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(this.passWord.getBytes());
        String pwd = new BigInteger(1, md.digest()).toString(16);
        RestResult res = null;

        int retry = 3;
        while (retry > 0) {
            try {
                String urlStr = this.taijiUri + "login";
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("GET");

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("json", String.format("{\"username\":\"%s\", \"password\":\"%s\", \"imei\":\"%s\"}",
                        this.userName, pwd, this.imei)));

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getQuery(params));
                writer.flush();
                writer.close();
                os.close();

                res = BuildResultFromConnection(conn);
                conn.disconnect();
                break;
            } catch (Exception e) {
                if (IsRetryableException(e)) {
                    retry--;
                } else {
                    e.printStackTrace();
                    throw e;
                }
            }
        }

        if (res.body.getString("login").equals("success")) {
            this.appKey = res.body.getString("appKey");
            this.zoneName = res.body.getString("zoneName");
            this.zoneCode = res.body.getString("zoneCode");
        }
        else {
            throw new Exception(String.format("Unable to login with username:%s password:%s imei:%s, failed to init TaijiClient", this.userName, this.passWord, this.imei));
        }
    }

    /* 行政区划获取接口
    xzqhbm : 行政区划编码
    xzqhsd : xzqhsd
     */
    public RestResult GetZoneInfo(String xzqhbm, int xzqhsd) throws Exception {
        return DoGet(this.taijiUri + "queryZone", String.format("{\"xzqhbm\":\"%s\",\"xzqhsd\":%d}", xzqhbm, xzqhsd));
    }

    /* 救助家庭列表批量查询接口
    xzqhbm : 12位行政区划编码
    jzywlx : 救助业务
    hqsl : 每次获取数量
    hqcs : 获取次数
     */
    public RestResult BatchGetFamily(String xzqhbm, String jzywlx, int hqsl, int hqcs) throws Exception {
        return DoGet(this.taijiUri + "queryFamilyList", String.format("{\"xzqhbm\":\"%s\",\"jzywlx\":\"%s\",\"hqsl\":%d, \"hqcs\":%d}", xzqhbm, jzywlx, hqsl, hqcs));
    }


    /* 救助家庭查询
    sqrsfzh : 申请人身份证号
    jzywlx : 救助业务
    jzny : jzny
     */
    public RestResult GetFamily(String sqrsfzh, String jzywlx, String jzny) throws Exception {
        return DoGet(this.taijiUri + "queryFamilyList", String.format("{\"sqrsfzh\":\"%s\",\"jzywlx\":\"%s\",\"jzny\":\"%s\"}", sqrsfzh, jzywlx, jzny));
    }

    /* 获取材料
    uri : 需要获得的东西.(不是完整uri, 不包括太极endpoint)
     */
    public byte[] GetMaterial(String jia_ting_id, String xzqhbm, String cai_liao_id) throws Exception {
        int retry = 3;
        String jsonStr = String.format("{\"jtid\":\"%s\",\"xzqhdm\":\"%s\",\"clid\":\"%s\"}", jia_ting_id, xzqhbm, cai_liao_id);
        while (retry > 0) {
            try {
                String uri = taijiContentUri;
                String value = URLEncoder.encode(appKey, "UTF-8")
                        + "&imei="+ imei + "&json="
                        + URLEncoder.encode("{\"jtid\":\""+ jia_ting_id +"\",\"xzqhdm\":\""+ xzqhbm +"\",\"clid\":\""+ cai_liao_id +"\"}","UTF-8");
                String urll = uri + value;

                URL url = new URL(urll);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("GET");
                String result;
                if (conn.getResponseCode() == 200) {
                    // 获取响应的输入流对象
                    InputStream is = conn.getInputStream();

                    // 创建字节输出流对象
                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    // 定义读取的长度
                    int len = 0;
                    // 定义缓冲区
                    byte buffer[] = new byte[1024];
                    // 按照缓冲区的大小，循环读取
                    while ((len = is.read(buffer)) != -1) {
                        // 根据读取的长度写入到os对象中
                        os.write(buffer, 0, len);
                    }
                    // 释放资源
                    is.close();
                    os.close();
                    // 返回字符串
                    result = new String(os.toByteArray());
                } else {
                    result = "";
                }
                conn.disconnect();
                JSONObject obj = new JSONObject(result);
                String cont = obj.getString("content");
                return Base64.decode(cont, Base64.DEFAULT);
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
        RestResult res = new RestResult();
        res.statusCode = conn.getResponseCode();

        InputStream is;
        if (res.statusCode >= 400) {
            is = conn.getErrorStream();
        }
        else {
            is = conn.getInputStream();
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        char[] buff;
        buff = new char[1024];

        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        int read = br.read(buff);
        while (read > 0) {
            os.write(new String(buff, 0, read).getBytes());
            read = br.read(buff);
        }

        System.out.println("mx: readbuff size:" + os.size());
        String tmp = new String(os.toByteArray());

        is.close();
        os.close();
        //mx: debug
        System.out.println("mx: printbuff:" + tmp);
        res.body = new JSONObject(tmp);

        // record rpc
        res.rpc = conn.getHeaderField("rpc");
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
                //conn.setRequestProperty("Content-Type", "application/json");

                String input = jsonStr;

                // appkey and imei
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("imei", this.imei));
                params.add(new BasicNameValuePair("appKey", this.appKey));
                params.add(new BasicNameValuePair("json", jsonStr));

                // write
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getQuery(params));
                writer.flush();
                writer.close();
                os.close();

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

    private RestResult DoGet(String urlStr, String jsonStr) throws Exception {
        int retry = 3;
        while (retry > 0) {
            try {
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("GET");
                //conn.setRequestProperty("Content-Type", "application/json");

                // appkey and imei
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("imei", this.imei));
                params.add(new BasicNameValuePair("appKey", this.appKey));
                params.add(new BasicNameValuePair("json", jsonStr));
                // write
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getQuery(params));
                writer.flush();
                writer.close();
                os.close();

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