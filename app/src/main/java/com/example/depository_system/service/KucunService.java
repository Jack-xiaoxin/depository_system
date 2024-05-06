package com.example.depository_system.service;

import com.example.depository_system.informs.DepositoryInform;
import com.example.depository_system.informs.KucunInform;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class KucunService {
<<<<<<< HEAD
    ServiceBase serviceBase = new ServiceBase();

    public List<KucunInform> getKucunList(
            String kucunId,
            String materialId
=======

    public static List<KucunInform> getKucunList(
            String kucunId,
            String materialId,
            String depotId
>>>>>>> kc/front_end
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
<<<<<<< HEAD
=======
            if (depotId != null && !depotId.isEmpty()) {
                jsonObject.put("depository_id", depotId);
            }
>>>>>>> kc/front_end

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

<<<<<<< HEAD
        String bodyString = serviceBase.HttpBase("/getStoredInfoList", "POST", jsonObject);
=======
        String bodyString = ServiceBase.HttpBase("/getStoredInfoList", "POST", jsonObject);
>>>>>>> kc/front_end
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
<<<<<<< HEAD
=======
                kucunInform.depotId = singleObject.getString("depository_id");
>>>>>>> kc/front_end

                kucunInforms.add(kucunInform);
            }

            return kucunInforms;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

<<<<<<< HEAD
    public List<KucunInform> getKucunAlarmList() {
        List<KucunInform> kucunInforms = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();

        String bodyString = serviceBase.HttpBase("/getAlarmedInfoList", "POST", jsonObject);
=======
    public static List<KucunInform> getKucunAlarmList() {
        List<KucunInform> kucunInforms = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();

        String bodyString = ServiceBase.HttpBase("/getAlarmedInfoList", "POST", jsonObject);
>>>>>>> kc/front_end
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
<<<<<<< HEAD
}
=======
}
>>>>>>> kc/front_end
