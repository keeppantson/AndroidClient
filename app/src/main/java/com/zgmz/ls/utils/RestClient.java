package com.zgmz.ls.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import android.content.Intent;
import android.net.Uri;
import android.util.Base64;

public class RestClient {
    private ArrayList<String> servers = new ArrayList<String>();
    private int currentServerIndex = 0; // current server index
    private String userName = null;
    private String passWord = null;
    private String imei = null;

    private static String versionString = "v1";

    /*
    建立一个client
    param@serverUrl : config server的ip+port in String e.g. 127.0.0.1:8080,
    param@userName : 登陆太极使用的用户名
    param@passWord : 登陆太极使用的密码
     */
    public RestClient(String serverUrl, String userName, String passWord, String imei) {
        servers.add(serverUrl);
        this.userName = userName;
        this.passWord = passWord;
        this.imei = imei;
    }

    /*
    初始化client
    从config server拉取可用的server
    初始化失败将直接报exception
     */
    public void Init() throws IOException, JSONException {
        ConfigClient config = new ConfigClient();
        config.Init();
        for (int i = 0; i < config.servers.size(); ++i) {
            servers.add(config.servers.get(i));
        }
    }

    /*
    上传一个低保家庭信息(将写入太极)
    param@family : 低保家庭实例
    ret param@: rest请求返回结果
     */
    public RestResult UploadFamily(String family) throws Exception {
        return DoPost("users", family);
    }

    /*
    上传一个文件
    param@filePath : 文件将要上传到服务器上的路径
    param@fileContent : 文件内容
    ret param@: rest请求返回结果
     */
    public RestResult UploadFile(String filePath, byte[] fileContent, String server, String rpc) throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append("[");

        builder.append("{");
        byte[] encode = Base64.encode(fileContent, Base64.NO_WRAP);
        builder.append(String.format("\"path\" : \"%s\", \"content\" : \"%s\"", filePath, new String(encode, "UTF-8")));
        builder.append("},");

        String request = builder.toString();
        request = request.substring(0, request.length() - 1); // trim last ,
        String input = request + "]";

        System.out.println("mx: upload json" + input);
        //String tmp = "[{\"content\": \"d2Vxd2UxZXF3ZXdxZXE=\", \"path\": \"/2017-11-12/441502001001/231512198109118873/101/231512198109118873-01.jpg\"}]";
        return DoPost("files", input, server, rpc);
    }
    private RestResult DoPost_content_new(String urlPostfix, String jsonStr, String server, String rpc) throws Exception {
        RestResult result = new RestResult();
        try {
            String url = null;
            if (server == null) {
                url = new String("http://" + servers.get(this.currentServerIndex) + "/" + versionString + "/" + urlPostfix);
            }
            else {
                url = new String("http://" + server + "/" + versionString + "/" + urlPostfix);
            }

            String input = DecorateJson(jsonStr);
            HttpPost httpPost = new HttpPost(url);
            DefaultHttpClient httpClient = new DefaultHttpClient();
            StringEntity s = new StringEntity(input.toString());
            s.setContentEncoding("UTF-8");
            s.setContentType("application/json");
            httpPost.setHeader("rpc", rpc);
            httpPost.setEntity(s);
            HttpResponse res = httpClient.execute(httpPost);

            if(res.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = res.getEntity();
                String charset = EntityUtils.getContentCharSet(entity);
                result.statusCode = 200;
            }
        } catch (Exception e) {
        }
        return result;
    }

    private RestResult DoPost_content(String urlPostfix, String jsonStr, String server, String rpc) throws Exception {
        int retry = 6;
        //jsonStr = "[{\"path\" : \"2016-02-03/430702001001/430702195901091213/103/430702195901091213-103.jpg\", \"content\" : \"/9j/4AAQND//Z\"}]";
        while (retry > 0) {
            try {
                URL url = null;
                if (server == null) {
                    url = new URL("http://" + servers.get(this.currentServerIndex) + "/" + versionString + "/" + urlPostfix);
                }
                else {
                    url = new URL("http://" + server + "/" + versionString + "/" + urlPostfix);
                }

                System.out.println("try connect to " + url);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                //设置该连接允许读取
                conn.setDoOutput(true);
                //设置该连接允许写入
                conn.setDoInput(true);
                //设置不能适用缓存
                conn.setUseCaches(false);
                //设置连接超时时间
                conn.setConnectTimeout(3000);   //设置连接超时时间
                //设置读取时间
                conn.setReadTimeout(4000);   //读取超时
                //设置维持长连接
                conn.setRequestProperty("connection", "Keep-Alive");
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                this.SetHeader(conn);

                if (rpc != null) {
                    conn.setRequestProperty("rpc", rpc);
                }

                String input = DecorateJson(jsonStr);
                System.out.println("mx:before send:" + input);
                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                int start_len = 0;
                int sent_len = input.length();
                byte[] bytes = input.getBytes();
                while (start_len < sent_len) {
                    int len = sent_len - start_len;
                    if (len > 10000)
                        len = 10000;
                    dos.write(bytes, start_len, len);
                    start_len += len;
                }
                dos.flush();
                dos.close();

                RestResult res = BuildResultFromConnection(conn);
                conn.disconnect();

                //record last server
                res.server = servers.get(this.currentServerIndex);

                return res;
            } catch (Exception e) {
                if (IsRetryableException(e)) {
                    retry--;

                    // if server not specified, try next one
                    if (server == null) {
                        this.currentServerIndex = (this.currentServerIndex + 1) % this.servers.size(); // try next server
                    }
                } else {
                    e.printStackTrace();
                    throw e;
                }
            }
        }

        throw new Exception("retry for 6 times but all failed");
    }
    /*
    查询上传进度
    param@requestId : UploadFamily返回结果中,后端提供的本次上传唯一标示
    ret param@: rest请求返回结果
     */
    public RestResult GetProgress(String requestId, String server, String rpc) {
        try {
            URL url = new URL("http://" + server + "/" + versionString + "/" + "progress/" + requestId);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("rpc", rpc);
            SetHeader(conn);

            RestResult res = BuildResultFromConnection(conn);
            conn.disconnect();
            return res;
        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private RestResult BuildResultFromConnection(HttpURLConnection conn) throws IOException, JSONException {
        return BuildResultFromConnection(conn, 200);
    }

    private RestResult BuildResultFromConnection(HttpURLConnection conn, int buffsize) throws IOException, JSONException {
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

        //mx: debug
        System.out.println("mx: printbuff:" + tmp);
        res.body = new JSONObject(tmp);

        // record rpc
        res.rpc = conn.getHeaderField("rpc");
        return res;
    }

    private void SetHeader(HttpURLConnection conn) {
        conn.setRequestProperty("Username", this.userName);
        conn.setRequestProperty("Password", this.passWord);
        conn.setRequestProperty("Imei", this.imei);
    }

    private String DecorateJson(String jsonStr) {
        // because our backend only understand "request": {<body>}, so we change
        return String.format("{\"request\": %s}", jsonStr);
    }

    private RestResult DoPost(String urlPostfix, String jsonStr) throws Exception {
        return DoPost(urlPostfix, jsonStr, null, null);
    }


    private RestResult DoPost(String urlPostfix, String jsonStr, String server, String rpc) throws Exception {
        int retry = 6;
        // TODO: Error Fakeing
        //jsonStr = "[{\"path\" : \"2016-02-03/430702001001/430702195901091213/103/430702195901091213-103.jpg\", \"content\" : \"/9j/4AAQND//Z\"}]";
        while (retry > 0) {
            try {
                URL url = null;
                if (server == null) {
                    url = new URL("http://" + servers.get(this.currentServerIndex) + "/" + versionString + "/" + urlPostfix);
                }
                else {
                    url = new URL("http://" + server + "/" + versionString + "/" + urlPostfix);
                }

                System.out.println("try connect to " + url);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                this.SetHeader(conn);

                if (rpc != null) {
                    conn.setRequestProperty("rpc", rpc);
                }

                String input = DecorateJson(jsonStr);
                OutputStream os = conn.getOutputStream();
                os.write(input.getBytes());
                os.flush();

                RestResult res = BuildResultFromConnection(conn);
                conn.disconnect();

                //record last server
                res.server = servers.get(this.currentServerIndex);

                return res;
            } catch (Exception e) {
                if (IsRetryableException(e)) {
                    retry--;

                    // if server not specified, try next one
                    if (server == null) {
                        this.currentServerIndex = (this.currentServerIndex + 1) % this.servers.size(); // try next server
                    }
                } else {
                    e.printStackTrace();
                    throw e;
                }
            }
        }

        throw new Exception("retry for 6 times but all failed");
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
