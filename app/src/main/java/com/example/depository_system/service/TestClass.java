package com.example.depository_system.service;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.depository_system.informs.RukuInform;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSink;


public class TestClass {
    // 构造函数，用于显式初始化
    public TestClass() {
        System.out.println("测试类实例化完成。");
    }

    /**
     * 公共方法用于测试，打印传入的字符串内容。
     *
     * @param content 需要打印的字符串内容
     */
    public String test(String content){
        if (content != null && !content.isEmpty()) {
            System.out.println(content);
        } else {
            System.out.println("传入的字符串为空或null。");
        }

        ServiceBase serviceBase = new ServiceBase();

//        OkHttpClient client = new OkHttpClient();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("new_goods", false);
            jsonObject.put("goods_id", 10);
            jsonObject.put("goods_name", "aaa");
            jsonObject.put("goods_model","bbb");
            jsonObject.put("factory_name", "Manabox 2");
            jsonObject.put("receiver", "receiver");
            jsonObject.put("checker", "checker");
            jsonObject.put("project_name", "project_name");
            jsonObject.put("goods_number", 5);
            jsonObject.put("images", "[\"img1.jpg\", \"img2.jpg\", \"img3.jpg\"]");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


        String body = serviceBase.HttpBase("/inbound", "POST", jsonObject);

        return body;
//        RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), String.valueOf(jsonObject));
//        Request request = new Request.Builder()
//                .url("http://117.50.194.115:8090/inbound")
//                .method("POST", body)
//                .header("Content-Type", "application/json")
//                .build();
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try (Response response = client.newCall(request).execute()) {
//                    if (response.isSuccessful()) {
//                        System.out.println("网络输出" + response.body().string());
//                    } else {
//                        System.out.println("Request not successful");
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        thread.start();
//        try {
//            thread.join();
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }

    }

    public int insertBase(String url, String method, JSONObject jsonObject) {
        return 0;
    }

    public int insertRuku(String url, RukuInform inform) {
        // hashmap  => json
        //insertBase()
        return 0;
    }
}
