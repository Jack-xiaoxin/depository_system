package com.example.depository_system.service;

import com.example.depository_system.informs.DepositoryInform;
import com.example.depository_system.informs.KucunInform;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class KucunService {

    public static List<KucunInform> getKucunList(
            String kucunId,
            String materialId,
            String depotId,
            String projectId
    ) {
        List<KucunInform> kucunInforms = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        try {
            if (kucunId != null && !kucunId.isEmpty()) {
                jsonObject.put("stored_id", kucunId);
            }
            if (materialId != null && !materialId.isEmpty()) {
                jsonObject.put("goods_id", materialId);
            }
            if (depotId != null && !depotId.isEmpty()) {
                jsonObject.put("depository_id", depotId);
            }
            if(projectId != null && !projectId.isEmpty()) {
                jsonObject.put("project_id", projectId);
            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        String bodyString = ServiceBase.HttpBase("/getStoredInfoList", "POST", jsonObject);
        try {
            JSONObject bodyDict = new JSONObject(bodyString);
            JSONArray dataArray = bodyDict.getJSONArray("data");
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject singleObject = dataArray.getJSONObject(i);
                KucunInform kucunInform = new KucunInform();

                kucunInform.kucunId = singleObject.getString("stored_id");
                kucunInform.materialId = singleObject.getString("goods_id");
                kucunInform.kucunNumber = singleObject.getInt("stored_number");
                kucunInform.alarmNumber = singleObject.getInt("alarm_number");
                kucunInform.updateTime = singleObject.getString("update_time");
                kucunInform.depotId = singleObject.getString("depository_id");
                kucunInform.projectId = singleObject.getString("project_id");
                kucunInform.getDepositoryInfo();
                kucunInform.getMaterialInfo();
                kucunInform.getProjectInfo();

                kucunInforms.add(kucunInform);
            }

            return kucunInforms;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<KucunInform> getKucunAlarmList() {
        List<KucunInform> kucunInforms = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();

        String bodyString = ServiceBase.HttpBase("/getAlarmedInfoList", "POST", jsonObject);
        try {
            JSONObject bodyDict = new JSONObject(bodyString);
            JSONArray dataArray = bodyDict.getJSONArray("data");
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject singleObject = dataArray.getJSONObject(i);
                KucunInform kucunInform = new KucunInform();

                kucunInform.kucunId = singleObject.getString("stored_id");
                kucunInform.materialId = singleObject.getString("goods_id");
                kucunInform.kucunNumber = singleObject.getInt("stored_number");
                kucunInform.alarmNumber = singleObject.getInt("alarm_number");
                kucunInform.updateTime = singleObject.getString("update_time");

                kucunInforms.add(kucunInform);
            }

            return kucunInforms;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String updateStoredNumber(
            String kucunId,
            String storedNumber
    ) {
        JSONObject jsonObject = new JSONObject();
        try {
            if (kucunId != null && !kucunId.isEmpty()) {
                jsonObject.put("stored_id", kucunId);
            }
            if (storedNumber != null && !storedNumber.isEmpty()) {
                jsonObject.put("stored_number", storedNumber);
            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        String bodyString = ServiceBase.HttpBase("/updateStoredNumber", "POST", jsonObject);
        try {
            JSONObject bodyDict = new JSONObject(bodyString);
            String res = bodyDict.getString("data");

            return res;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String updateAlarmedNumber(
            String kucunId,
            String alarmedNumber
    ) {
        JSONObject jsonObject = new JSONObject();
        try {
            if (kucunId != null && !kucunId.isEmpty()) {
                jsonObject.put("stored_id", kucunId);
            }
            if (alarmedNumber != null && !alarmedNumber.isEmpty()) {
                jsonObject.put("alarm_number", alarmedNumber);
            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        String bodyString = ServiceBase.HttpBase("/updateAlarmedNumber", "POST", jsonObject);
        try {
            JSONObject bodyDict = new JSONObject(bodyString);
            String res = bodyDict.getString("data");

            return res;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

//    public static String updateAlarmedNumber(
//            String
//    ) {
//
//    }

    public static String deleteStoredInfo( String kucunId) {
        JSONObject jsonObject = new JSONObject();
        try {
            if(kucunId != null && !kucunId.isEmpty()) {
                jsonObject.put("stored_id", kucunId);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        String bodyString = ServiceBase.HttpBase("/deleteStoredInfo", "POST", jsonObject);
        try {
            JSONObject bodyDict = new JSONObject(bodyString);
            String res = bodyDict.getString("data");
            return res;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}