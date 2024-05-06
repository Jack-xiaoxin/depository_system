package com.example.depository_system.service;

import com.example.depository_system.informs.DepositoryInform;
import com.example.depository_system.informs.UserInform;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserService {
<<<<<<< HEAD
    ServiceBase serviceBase = new ServiceBase();

    public List<UserInform> getUserList(
=======

    public static List<UserInform> getUserList(
>>>>>>> kc/front_end
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

<<<<<<< HEAD
        String bodyString = serviceBase.HttpBase("/getDepositList", "POST", jsonObject);
=======
        String bodyString = ServiceBase.HttpBase("/getUserList", "POST", jsonObject);
>>>>>>> kc/front_end
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
<<<<<<< HEAD

                userInforms.add(userInform);
            }

            return userInforms;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
=======
                userInform.password = singleObject.getString("password");

                userInforms.add(userInform);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return userInforms;
    }
}
>>>>>>> kc/front_end
