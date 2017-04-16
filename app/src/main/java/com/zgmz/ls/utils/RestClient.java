package com.zgmz.ls.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
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
        RestResult res = GetConfig();
        JSONArray array = res.body.getJSONArray("servers");
        for (int i = 0; i < array.length(); ++i) {
            servers.add(array.getString(i));
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
        byte[] encode = Base64.encode(fileContent, Base64.DEFAULT);
        builder.append(String.format("\"path\" : \"%s\", \"content\" : \"%s\"", filePath, new String(encode, "UTF-8")));
        builder.append("},");

        String request = builder.toString();
        request = request.substring(0, request.length() - 1); // trim last ,
        String input = request + "]";

        System.out.println("mx: upload json" + input);
        //String tmp = "[{\"content\": \"d2Vxd2UxZXF3ZXdxZXE=\", \"path\": \"/2017-11-12/441502001001/231512198109118873/101/231512198109118873-01.jpg\"}]";
        return DoPost("files", input, server, rpc);
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

    public RestResult GetConfig() throws IOException, JSONException {
        //mx: TODO
        URL url = new URL("http://" + servers.get(0) + "/config");

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        RestResult res = BuildResultFromConnection(conn);
        conn.disconnect();
        return res;

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
                System.out.println("mx:before send:" + input);

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
