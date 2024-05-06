package com.example.depository_system.service;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ServiceBase {
    private static String baseUrl = "http://117.50.194.115:8090";
    private static OkHttpClient client = new OkHttpClient();

    private static String requestBase(Request request) {
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
            return futureTask.get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String HttpBase(String url, String method, JSONObject jsonObject){
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json;charset=utf-8"), String.valueOf(jsonObject)
        );
        Request request = new Request.Builder()
                .url(baseUrl + url)
                .method(method, body)
                .header("Content-Type", "application/json")
                .build();

        return requestBase(request);
    }

    public static String uploadImage(List<String> imagePaths) {
        // 创建一个请求体的构建器
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);

        // 为每个图片文件添加部分
        for (String imagePath : imagePaths) {
            File file = new File(imagePath);
            RequestBody fileBody = RequestBody.create(MediaType.parse("image/jpeg"), file);
            builder.addPart(
                    Headers.of("Content-Disposition", "form-data; name=\"file\"; filename=\"" + file.getName() + "\""),
                    fileBody
            );
        }

        // 构建请求体
        MultipartBody multipartBody = builder.build();

        // 创建请求对象
        Request request = new Request.Builder()
                .url(baseUrl + "/upload")
                .post(multipartBody)
                .header("Content-Type", "multipart/form-data")
                .build();

        String bodyString = requestBase(request);
        try {
            JSONObject bodyDict = new JSONObject(bodyString);
            return bodyDict.getString("data");
        } catch (
        JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}