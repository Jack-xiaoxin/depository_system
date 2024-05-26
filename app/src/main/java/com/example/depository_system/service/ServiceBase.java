package com.example.depository_system.service;

import android.content.ContentResolver;
import android.net.Uri;
import android.util.Log;

import com.example.depository_system.informs.RukuInform;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

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
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String uploadImage(List<Uri> imageUris, ContentResolver contentResolver) {

        if(imageUris == null || imageUris.size() == 0) return "无图片上传";

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);

        for(Uri uri : imageUris) {
            InputStream inputStream;
            try {
                inputStream = contentResolver.openInputStream(uri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return "文件不存在";
            }

            RequestBody requestBody = new RequestBody() {
                @Override
                public MediaType contentType() {
                    return MediaType.parse("image/jpeg"); // 根据实际情况设置媒体类型
                }

                @Override
                public void writeTo(BufferedSink sink) throws IOException {
                    try (Source source = Okio.source(inputStream)) {
                        sink.writeAll(source);
                    }
                }
            };

            builder.addPart(
                    Headers.of("Content-Disposition", "form-data; name=\"file\"; filename=\"" + uri.getPath() + "\""),
                    requestBody
            );
        }

        MultipartBody multipartBody = builder.build();

        Request request = new Request.Builder()
                .url(baseUrl + "/upload") // 替换为你的上传URL
                .post(multipartBody)
                .header("Content-Type", "multipart/form-data")
                .build();

        String bodyString = requestBase(request);
        try {
            JSONObject bodyDict = new JSONObject(bodyString);
            return bodyDict.getString("data");
        } catch (JSONException e) {
            e.printStackTrace();
            return "上传失败";
        }
    }
}