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
import java.util.ArrayList;
import android.util.Base64;

public class RestClient {
    private ArrayList<String> servers = new ArrayList<String>();
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
        //mx: TODO contact config server to retrieve available servers
        servers.add(serverUrl);
        //
        this.userName = userName;
        this.passWord = passWord;
        this.imei = imei;
    }

    /*
    上传一个低保家庭信息(将写入太极)
    param@family : 低保家庭实例
    ret param@: rest请求返回结果
     */
    public RestResult UploadFamily(String family) {
        try {
            //mx: TODO
            URL url = new URL("http://" + servers.get(0) + "/" + versionString + "/" + "users");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            SetHeader(conn);

            String input = family;

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

    /*
    上传一个文件
    param@filePath : 文件将要上传到服务器上的路径
    param@fileContent : 文件内容
    ret param@: rest请求返回结果
     */
    public RestResult UploadFile(ArrayList<String> filePath, ArrayList<byte[]> fileContent) {
        try {
            //mx: TODO
            URL url = new URL("http://" + servers.get(0) + "/" + versionString + "/" + "files");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            SetHeader(conn);

            //assemble content
            if (filePath.size() != fileContent.size()) {
                throw new Exception("filePath number doesn't match with fileContent number");
            }
            if (filePath.isEmpty()) {
                throw new Exception("filePath and fileContent are both empty");
            }

            StringBuilder builder = new StringBuilder();
            builder.append("{files:[");
            for (int i = 0; i < filePath.size(); ++i) {
                builder.append("{");
                byte[] encode = Base64.encode(fileContent.get(i), Base64.DEFAULT);
                builder.append(String.format("\"path\" : \"%s\", \"content\" : \"%s\"", filePath.get(i), new String(encode, "UTF-8")));
                builder.append("},");
            }
            String request = builder.toString();
            request = request.substring(0, request.length() - 1); // trim last ,
            String input = request + "]}";

            System.out.println("mx: file string is:" + input);

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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    /*
    查询上传进度
    param@requestId : UploadFamily返回结果中,后端提供的本次上传唯一标示
    ret param@: rest请求返回结果
     */
    public RestResult GetProgress(int requestId) {
        try {
            //mx: TODO
            URL url = new URL("http://" + servers.get(0) + "/" + versionString + "/" + "progress/" + Integer.toString(requestId));

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
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

    public RestResult GetConfig() {
        try {
            //mx: TODO
            URL url = new URL("http://" + servers.get(0) + "/config");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

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

    private void SetHeader(HttpURLConnection conn) {
        conn.setRequestProperty("Username", this.userName);
        conn.setRequestProperty("Password", this.passWord);
        conn.setRequestProperty("Imei", this.imei);
    }
}
