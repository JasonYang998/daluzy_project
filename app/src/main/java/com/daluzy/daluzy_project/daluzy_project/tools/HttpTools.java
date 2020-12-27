package com.daluzy.daluzy_project.daluzy_project.tools;


import com.daluzy.daluzy_project.daluzy_project.contants.Constants;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpTools {
    //OkHttp
    //xUtils
//HttpURLConnection post get put delete
    public static void getData(String path, HttpBackListener backListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                StringBuilder sb = new StringBuilder();
                try {
                    //创建URL
                    URL url = new URL(path);
                    //获取HttpURLConnection的对象
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    //connection 配置超时
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);

                    //connection 配置请求数据格式
                    connection.setRequestProperty("Content-type", "application/x-java-serialized-object");

                    //connection 配置请求方式
                    connection.setRequestMethod("GET");
                    //HttpURLConnection 发起连接
                    connection.connect();

                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStream inputStream = connection.getInputStream();
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                        BufferedReader reader = new BufferedReader(inputStreamReader);

                        String temp;
                        while ((temp = reader.readLine()) != null) {
                            sb.append(temp);
                        }
                        reader.close();
                        backListener.onSuccess(sb.toString(), connection.getResponseCode());
                    } else {
                        backListener.onError(connection.getResponseMessage(), connection.getResponseCode());
                    }
                    connection.disconnect();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public static void postData(String path ,HttpBackListener backListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                StringBuilder sb = new StringBuilder();
                try {
                    //创建URL
                    URL url = new URL(path);
                    //获取HttpURLConnection的对象
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    //connection 配置超时
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.setUseCaches(false);

                    //connection 配置请求方式
                    connection.setRequestMethod("POST");

                    connection.connect();

                    String body = "key=" + Constants.NEWS_APPKEY;
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                    writer.write(body);
                    writer.close();


                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStream inputStream = connection.getInputStream();
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                        BufferedReader reader = new BufferedReader(inputStreamReader);

                        String temp;
                        while ((temp = reader.readLine()) != null) {
                            sb.append(temp);
                        }
                        reader.close();
                        backListener.onSuccess(sb.toString(), connection.getResponseCode());
                    } else {
                        backListener.onError(connection.getResponseMessage(), connection.getResponseCode());
                    }
                    connection.disconnect();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public interface HttpBackListener {
        void onSuccess(String data, int code);

        void onError(String error, int code);
    }

}
