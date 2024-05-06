package com.example.depository_system.service;

import com.example.depository_system.informs.DepositoryInform;
import com.example.depository_system.informs.UserInform;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserService {

    public static List<UserInform> getUserList(
            String userId,
            String userName,
            String phoneNumber,
            String category
    ) {
        List<UserInform> userInforms = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        try {
            if (userId != null && !userId.isEmpty()) {
                jsonObject.put("user_id", userId);
            }
            if (userName != null && !userName.isEmpty()) {
                jsonObject.put("user_name", userName);
            }
            if (phoneNumber != null && !phoneNumber.isEmpty()) {
                jsonObject.put("phone_number", phoneNumber);
            }
            if (category != null && !category.isEmpty()) {
                jsonObject.put("category", category);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        String bodyString = ServiceBase.HttpBase("/getUserList", "POST", jsonObject);
        try {
            JSONObject bodyDict = new JSONObject(bodyString);
            JSONArray dataArray = bodyDict.getJSONArray("data");
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject singleObject = dataArray.getJSONObject(i);
                UserInform userInform = new UserInform();

                userInform.userId = singleObject.getString("user_id");
                userInform.userName = singleObject.getString("user_name");
                userInform.phoneNumber = singleObject.getString("phone_number");
                userInform.category = singleObject.getInt("category");
                userInform.password = singleObject.getString("password");

                userInforms.add(userInform);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return userInforms;
    }

    public static List<UserInform> getNewUserList() {
        List<UserInform> userInforms = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();

        String bodyString = ServiceBase.HttpBase("/getNewUserList", "POST", jsonObject);
        try {
            JSONObject bodyDict = new JSONObject(bodyString);
            JSONArray dataArray = bodyDict.getJSONArray("data");
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject singleObject = dataArray.getJSONObject(i);
                UserInform userInform = new UserInform();

                userInform.userId = singleObject.getString("user_id");
                userInform.userName = singleObject.getString("user_name");
                userInform.phoneNumber = singleObject.getString("phone_number");
                userInform.category = singleObject.getInt("category");
                userInform.password = singleObject.getString("password");

                userInforms.add(userInform);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return userInforms;
    }

    public static String userRegister(
            String userName,
            String phoneNumber,
            String password
    ) {
        JSONObject jsonObject = new JSONObject();
        try {
            if (password != null && !password.isEmpty()) {
                jsonObject.put("password", password);
            }
            if (userName != null && !userName.isEmpty()) {
                jsonObject.put("user_name", userName);
            }
            if (phoneNumber != null && !phoneNumber.isEmpty()) {
                jsonObject.put("phone_number", phoneNumber);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        String result = null;
        String bodyString = ServiceBase.HttpBase("/userRegister", "POST", jsonObject);
        try {
            JSONObject bodyDict = new JSONObject(bodyString);
            result = bodyDict.getString("data");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String userInfoModify(
            String userName,
            String password,
            String category
    ) {
        JSONObject jsonObject = new JSONObject();
        try {
            if (password != null && !password.isEmpty()) {
                jsonObject.put("password", password);
            }
            if (userName != null && !userName.isEmpty()) {
                jsonObject.put("user_name", userName);
            }
            if (category != null && !category.isEmpty()) {
                jsonObject.put("category", category);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        String result = null;
        String bodyString = ServiceBase.HttpBase("/userInfoModify", "POST", jsonObject);
        try {
            JSONObject bodyDict = new JSONObject(bodyString);
            result = bodyDict.getString("data");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String userLogin(
            String phoneNumber,
            String password
    ) {
        JSONObject jsonObject = new JSONObject();
        try {
            if (password != null && !password.isEmpty()) {
                jsonObject.put("password", password);
            }
            if (phoneNumber != null && !phoneNumber.isEmpty()) {
                jsonObject.put("username", phoneNumber);
            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        String result = null;
        String bodyString = ServiceBase.HttpBase("/login", "POST", jsonObject);
        try {
            JSONObject bodyDict = new JSONObject(bodyString);
            result = bodyDict.getString("data");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
}