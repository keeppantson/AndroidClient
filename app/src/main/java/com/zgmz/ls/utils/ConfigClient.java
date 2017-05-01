package com.zgmz.ls.utils;

import android.os.Environment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static com.zgmz.ls.utils.FileUtils.ensureDownloadLocation;

/**
 * Created by mixiang on 4/17/17.
 */

public class ConfigClient {
    private final String configServer = "115.28.185.19:80";
    public ArrayList<String> servers = new ArrayList<String>();
    public String remoteApkName = null;
    private int currentServerIndex = 0; // current server index
    private static String versionString = "v1";


    public void Init() throws IOException, JSONException {
        RestResult res = GetConfig();
        JSONArray array = res.body.getJSONArray("servers");
        for (int i = 0; i < array.length(); ++i) {
            servers.add(array.getString(i));
        }
        this.remoteApkName = res.body.getString("apk");
    }

    public RestResult GetConfig() throws IOException, JSONException {
        URL url = new URL("http://" + configServer + "/config");

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        RestResult res = BuildResultFromConnection(conn);
        conn.disconnect();
        return res;

    }

    public File DowloadApk(String apkFileName) throws Exception {
        int retry = 6;

        while (retry > 0) {
            try {
                URL url = new URL("http://" + servers.get(this.currentServerIndex) + "/" + versionString + "/apk/" + apkFileName);

                System.out.println("try connect to " + url);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                InputStream is;
                if (conn.getResponseCode() >= 400) {
                    is = conn.getErrorStream();
                    // just retry
                    retry--;
                    this.currentServerIndex = (this.currentServerIndex + 1) % this.servers.size(); // try next server
                    continue;
                }
                else {
                    is = conn.getInputStream();
                }
                ensureDownloadLocation();
                //File apkFile = new File(System.getProperty("java.io.tmpdir") + "/" + apkFileName);
                File apkFile = new File(Environment.getExternalStorageDirectory() + "/download/" + apkFileName);
                System.out.println("mx: download to tmpdir:" + apkFile.getAbsolutePath());
                    apkFile.deleteOnExit();
                apkFile.createNewFile();

                FileOutputStream os = new FileOutputStream(apkFile);

                byte[] buffer = new byte[1024];
                int len1 = 0;
                while ((len1 = is.read(buffer)) != -1) {
                    os.write(buffer, 0, len1);
                }
                os.flush();
                os.close();
                is.close();

                System.out.println("mx: Download complete:" + apkFile.getAbsolutePath() + "/" + apkFile.getName());

                conn.disconnect();
                return apkFile;
            } catch (Exception e) {
                if (IsRetryableException(e)) {
                    retry--;
                    this.currentServerIndex = (this.currentServerIndex + 1) % this.servers.size(); // try next server
                } else {
                    e.printStackTrace();
                    throw e;
                }
            }
        }

        throw new Exception("UpgradeApk: tried to download " + apkFileName + " for 6 times but all failed");
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

        //mx: debug
        System.out.println("mx: printbuff:" + tmp);
        res.body = new JSONObject(tmp);
        return res;
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
