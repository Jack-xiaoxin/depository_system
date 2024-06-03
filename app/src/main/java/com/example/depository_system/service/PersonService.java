package com.example.depository_system.service;

import android.app.Person;

import com.example.depository_system.informs.PersonInform;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PersonService {


    public static List<PersonInform> getPersonInfoList(String name) {
        List<PersonInform> personInforms = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        try {
            if(name != null && !name.isEmpty()) {
                jsonObject.put("name", name);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String bodyString = ServiceBase.HttpBase("/queryPersonInfo", "POST", jsonObject);
        try {
            JSONObject bodyDict = new JSONObject(bodyString);
            JSONArray dataArray = bodyDict.getJSONArray("data");
            for(int i = 0; i < dataArray.length(); i++) {
                JSONObject singleObject = dataArray.getJSONObject(i);
                PersonInform personInform = new PersonInform();
                personInform.name = singleObject.getString("name");
                personInforms.add(personInform);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return personInforms;
    }

    public static String insertPersonInfo(String name) {
        JSONObject jsonObject = new JSONObject();
        try {
            if(name != null && !name.isEmpty()) {
                jsonObject.put("name", name);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        String result = null;
        String bodyString = ServiceBase.HttpBase("/insertPersonInfo", "POST", jsonObject);
        try {
            JSONObject bodyDict = new JSONObject(bodyString);
            result = bodyDict.getString("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

}
