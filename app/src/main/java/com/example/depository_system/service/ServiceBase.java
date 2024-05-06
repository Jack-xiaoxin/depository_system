package com.example.depository_system.service;

import android.util.Log;

import com.example.depository_system.informs.RukuInform;
import org.json.JSONObject;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ServiceBase {
<<<<<<< HEAD
    public ServiceBase() {
        Log.d("java", "基础类实例化完成。");
    }

    public String HttpBase(String url, String method, JSONObject jsonObject){
=======

    public static String HttpBase(String url, String method, JSONObject jsonObject){
>>>>>>> kc/front_end
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json;charset=utf-8"), String.valueOf(jsonObject)
        );
        Request request = new Request.Builder()
                .url("http://117.50.194.115:8090" + url)
                .method(method, body)
                .header("Content-Type", "application/json")
                .build();

        Callable<String> task = () -> {
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    return responseBody;
                } else {
                    return "Request not successful, response code: " + response.code();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        };

        FutureTask<String> futureTask = new FutureTask<String>(task);
        Thread thread = new Thread(futureTask);
        thread.start();

        try {
            String response = futureTask.get();
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    };
<<<<<<< HEAD
}
=======
}
>>>>>>> kc/front_end
