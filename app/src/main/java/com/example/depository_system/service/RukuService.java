package com.example.depository_system.service;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.depository_system.informs.RukuInform;
import com.example.depository_system.informs.RukuRecordInform;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class RukuService {
    private ServiceBase serviceBase = new ServiceBase();

    public String action(RukuInform rukuInform) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("new_goods", rukuInform.isNew);
            jsonObject.put("goods_id", rukuInform.materialIdentifer);
            jsonObject.put("goods_name", rukuInform.materialName);
            jsonObject.put("goods_model", rukuInform.materialModel);
            jsonObject.put("depository_name", rukuInform.depotName);
            jsonObject.put("factory_name", rukuInform.factoryName);
            jsonObject.put("receiver", rukuInform.receiver);
            jsonObject.put("checker", rukuInform.acceptor);
            jsonObject.put("project_name", rukuInform.projectName);
            jsonObject.put("goods_number", rukuInform.number);
            jsonObject.put("images", rukuInform.images);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        String response = serviceBase.HttpBase("/inbound", "POST", jsonObject);
        return response;
    }

    public List<RukuRecordInform> getRukuRecordListByDateOrFactory(String date, String factoryName) {
        List<RukuRecordInform> rukuRecordInformList = new ArrayList<RukuRecordInform>();
        JSONObject jsonObject = new JSONObject();
        try {
            if (date != null && !date.isEmpty()) {
                jsonObject.put("date", date);
            }
            if (factoryName != null && !factoryName.isEmpty()) {
                jsonObject.put("factory_name", factoryName);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        String bodyString = serviceBase.HttpBase("/inboundHistory", "POST", jsonObject);
        try {
            JSONObject bodyDict = new JSONObject(bodyString);
            System.out.println(bodyDict);
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
