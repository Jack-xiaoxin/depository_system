package com.example.depository_system.service;
import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class TestClass {
    // 构造函数，用于显式初始化
    public TestClass() {
        System.out.println("类的实例化完成。");
    }

    /**
     * 公共方法用于测试，打印传入的字符串内容。
     *
     * @param content 需要打印的字符串内容
     */
    public void test(String content) {
        if (content != null && !content.isEmpty()) {
            System.out.println(content);
        } else {
            System.out.println("传入的字符串为空或null。");
        }

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://baidu.com")
                .build();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try (Response response = client.newCall(request).execute()) {
                    if (response.isSuccessful()) {
                        System.out.println("网络输出" + response.body().string());
                    } else {
                        System.out.println("Request not successful");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
