package com.example.depository_system.service;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.depository_system.informs.RukuInform;
import com.example.depository_system.informs.RukuRecordInform;
import com.example.depository_system.informs.RukuRecordItemInform;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class RukuService {

    public static String action(RukuInform rukuInform) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("new_goods", rukuInform.isNew);
            jsonObject.put("goods_id", rukuInform.materialId);
            jsonObject.put("goods_name", rukuInform.materialName);
            jsonObject.put("goods_model", rukuInform.materialModel);
            jsonObject.put("goods_identifier", rukuInform.materialIdentifer);
            jsonObject.put("depository_name", rukuInform.depotName);
            jsonObject.put("depository_id", rukuInform.depotId);
            jsonObject.put("factory_name", rukuInform.factoryName);
            jsonObject.put("receiver", rukuInform.receiver);
            jsonObject.put("checker", rukuInform.acceptor);
            jsonObject.put("project_name", rukuInform.projectName);
            jsonObject.put("goods_number", rukuInform.number);
            jsonObject.put("inbound_identifier", System.currentTimeMillis());
            jsonObject.put("images", rukuInform.images);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        String response = ServiceBase.HttpBase("/inbound", "POST", jsonObject);
        return response;
    }

    public static List<RukuRecordInform> getRukuRecordListByDateOrFactory(String date, String factoryName, String projectName) {
        List<RukuRecordInform> rukuRecordInformList = new ArrayList<RukuRecordInform>();
        JSONObject jsonObject = new JSONObject();
        try {
            if (date != null && !date.isEmpty()) {
                jsonObject.put("date", date);
            }
            if (factoryName != null && !factoryName.isEmpty()) {
                jsonObject.put("factory_name", factoryName);
            }
            if(projectName != null && !projectName.isEmpty()) {
                jsonObject.put("project_name", projectName);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        String bodyString = ServiceBase.HttpBase("/inboundHistory", "POST", jsonObject);
        try {
            JSONObject bodyDict = new JSONObject(bodyString);
            JSONArray dataArray = bodyDict.getJSONArray("data");
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject singleObject = dataArray.getJSONObject(i);
                RukuRecordInform rukuRecordInform = new RukuRecordInform();

                rukuRecordInform.inboundId = singleObject.getString("inbound_id");
                rukuRecordInform.inboundIdentifier = singleObject.getString("inbound_identifier");
                rukuRecordInform.inboundDate = singleObject.getString("inbound_date");
                rukuRecordInform.factoryName = singleObject.getString("factory_name");
                rukuRecordInform.projectId = singleObject.getString("project_id");

                List<RukuRecordItemInform> itemInformList = new ArrayList<>();
                Iterator<String> keys = singleObject.getJSONObject("inbound_record").keys();
                for (Iterator<String> it = keys; it.hasNext(); ) {
                    RukuRecordItemInform itemInform = new RukuRecordItemInform();

                    String key = it.next();
                    itemInform.id = key;
                    JSONObject valueObject = (JSONObject) singleObject.getJSONObject("inbound_record").get(key);

                    itemInform.checker = valueObject.getString("checker");
                    itemInform.receiver = valueObject.getString("receiver");
                    itemInform.materialId = valueObject.getString("goods_id");
                    itemInform.materialName = valueObject.getString("goods_name");
                    itemInform.materialModel = valueObject.getString("goods_model");
                    itemInform.materialIdentifier = valueObject.getString("goods_identifier");
                    itemInform.factoryName = valueObject.getString("factory_name");
                    itemInform.number = valueObject.getInt("goods_number");
                    itemInform.inboundTime = valueObject.getString("inbound_time");
                    itemInform.projectName = valueObject.getString("project_name");
                    itemInform.depositoryId = valueObject.getString("depository_id");

                    JSONArray imageArray = valueObject.getJSONArray("images");
                    List<String> imgList = new ArrayList<>();
                    for (int j = 0; j < imageArray.length(); j++) {
                        imgList.add(imageArray.getString(j));
                    }
                    itemInform.images = imgList;

                    itemInformList.add(itemInform);
                }
                rukuRecordInform.itemList = itemInformList;

                rukuRecordInformList.add(rukuRecordInform);
            }

            return rukuRecordInformList;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}