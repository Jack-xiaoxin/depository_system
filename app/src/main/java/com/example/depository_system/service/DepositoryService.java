package com.example.depository_system.service;

import com.example.depository_system.informs.ChukuActionInform;
import com.example.depository_system.informs.ChukuRecordInform;
import com.example.depository_system.informs.ChukuRecordItemInform;
import com.example.depository_system.informs.DepositoryInform;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DepositoryService {
    ServiceBase serviceBase = new ServiceBase();

    public List<DepositoryInform> getDepostList(
            String depositoryId,
            String name
    ) {
        List<DepositoryInform> depositoryInforms = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        try {
            if (depositoryId != null && !depositoryId.isEmpty()) {
                jsonObject.put("depository_id", depositoryId);
            }
            if (name != null && !name.isEmpty()) {
                jsonObject.put("name", name);
            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        String bodyString = serviceBase.HttpBase("/getDepositList", "POST", jsonObject);
        try {
            JSONObject bodyDict = new JSONObject(bodyString);
            JSONArray dataArray = bodyDict.getJSONArray("data");
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject singleObject = dataArray.getJSONObject(i);
                DepositoryInform depositoryInform = new DepositoryInform();

                depositoryInform.depotId = singleObject.getString("depository_id");
                depositoryInform.depotName = singleObject.getString("name");
                depositoryInform.description = singleObject.getString("description");
                depositoryInform.createdTime = singleObject.getString("created_time");

                depositoryInforms.add(depositoryInform);
            }

            return depositoryInforms;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}