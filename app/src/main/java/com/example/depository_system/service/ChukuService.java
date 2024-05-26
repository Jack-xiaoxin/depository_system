package com.example.depository_system.service;

import com.example.depository_system.DataManagement;
import com.example.depository_system.informs.ChukuActionInform;
import com.example.depository_system.informs.ChukuRecordInform;
import com.example.depository_system.informs.ChukuRecordItemInform;
import com.example.depository_system.informs.DepositoryInform;
import com.example.depository_system.informs.RukuRecordInform;
import com.example.depository_system.informs.RukuRecordItemInform;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class ChukuService {

    public static String action(ChukuActionInform chukuInform) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("depository_id", chukuInform.depotId);
            jsonObject.put("goods_id", chukuInform.materialId);
            jsonObject.put("applier", chukuInform.applier);
            jsonObject.put("goods_identifier", chukuInform.materialIdentifier);
            jsonObject.put("apply_department_name", chukuInform.applyDepartmentName);
            jsonObject.put("apply_project_name", chukuInform.applyProjectName);
            jsonObject.put("director", chukuInform.director);
            jsonObject.put("goods_number", chukuInform.number);
            jsonObject.put("outbound_identifier", System.currentTimeMillis());
            jsonObject.put("images", chukuInform.images);
            jsonObject.put("time", chukuInform.time);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        String response = ServiceBase.HttpBase("/outbound", "POST", jsonObject);
        return response;
    }

    public static List<ChukuRecordInform> getChukuRecordList(
            String outboundDate,
            String applyDepartmentName,
            String applyProjectName,
            String director
    ) {
        List<ChukuRecordInform> chukuRecordInformList = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        try {
            if (outboundDate != null && !outboundDate.isEmpty()) {
                jsonObject.put("outbound_date", outboundDate);
            }
            if (applyDepartmentName != null && !applyDepartmentName.isEmpty()) {
                jsonObject.put("apply_department_name", applyDepartmentName);
            }
            if (applyProjectName != null && !applyProjectName.isEmpty()) {
                jsonObject.put("apply_project_name", applyProjectName);
            }
            if (director != null && !director.isEmpty()) {
                jsonObject.put("director", director);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        String bodyString = ServiceBase.HttpBase("/outboundHistory", "POST", jsonObject);
        try {
            JSONObject bodyDict = new JSONObject(bodyString);
            JSONArray dataArray = bodyDict.getJSONArray("data");
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject singleObject = dataArray.getJSONObject(i);
                ChukuRecordInform chukuRecordInform = new ChukuRecordInform();

                chukuRecordInform.outboundId = singleObject.getString("outbound_id");
                chukuRecordInform.outboundIdentifier = singleObject.getString("outbound_identifier");
                chukuRecordInform.applyProjectName = singleObject.getString("apply_project_name");
                chukuRecordInform.applyDepartmentName = singleObject.getString("apply_department_name");
                chukuRecordInform.director = singleObject.getString("director");
                chukuRecordInform.outboundDate = singleObject.getString("outbound_date");

                List<ChukuRecordItemInform> itemInformList = new ArrayList<>();
                Iterator<String> keys = singleObject.getJSONObject("outbound_record").keys();
                for (Iterator<String> it = keys; it.hasNext(); ) {
                    ChukuRecordItemInform itemInform = new ChukuRecordItemInform();

                    String key = it.next();
                    itemInform.id = key;
                    JSONObject valueObject = (JSONObject) singleObject.getJSONObject("outbound_record").get(key);

                    itemInform.applier = valueObject.getString("applier");
                    itemInform.materialId = valueObject.getString("goods_id");
                    itemInform.materialName = valueObject.getString("goods_name");
                    itemInform.materialModel = valueObject.getString("goods_model");
                    itemInform.factoryName = valueObject.getString("factory_name");
                    itemInform.number = valueObject.getInt("goods_number");
                    itemInform.materialIdentifier = valueObject.getString("goods_identifier");
                    itemInform.outboundTime = valueObject.getString("outbound_time");
                    itemInform.departmentName = valueObject.getString("apply_department_name");
                    itemInform.projectMajor = valueObject.getString("director");
                    itemInform.projectName = valueObject.getString("apply_project_name");
                    itemInform.depository_id = valueObject.getString("depository_id");
                    for(DepositoryInform depositoryInform : DataManagement.depositoryInforms) {
                        if(depositoryInform.depotId.equals(itemInform.depository_id)) {
                            itemInform.depositoryName = depositoryInform.depotName;
                        }
                    }

                    JSONArray imageArray = valueObject.getJSONArray("images");
                    List<String> imgList = new ArrayList<>();
                    for (int j = 0; j < imageArray.length(); j++) {
                        imgList.add(imageArray.getString(j));
                    }
                    itemInform.images = imgList;

                    itemInformList.add(itemInform);
                }
                chukuRecordInform.itemList = itemInformList;

                chukuRecordInformList.add(chukuRecordInform);
            }

            return chukuRecordInformList;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return chukuRecordInformList;
    }
}